/**
 * @author Luis Iñesta Gelabert - linesta@iti.es | luiinge@gmail.com
 */
package maven.fetcher;


import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.apache.maven.repository.MavenRepositorySystemUtils;
import org.apache.maven.repository.internal.DefaultVersionResolver;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.collection.DependencyCollectionException;
import org.eclipse.aether.connector.basic.BasicRepositoryConnectorFactory;
import org.eclipse.aether.impl.DefaultServiceLocator;
import org.eclipse.aether.impl.DefaultServiceLocator.ErrorHandler;
import org.eclipse.aether.impl.VersionResolver;
import org.eclipse.aether.repository.Authentication;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.Proxy;
import org.eclipse.aether.repository.ProxySelector;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.spi.connector.RepositoryConnectorFactory;
import org.eclipse.aether.spi.connector.transport.TransporterFactory;
import org.eclipse.aether.transport.file.FileTransporterFactory;
import org.eclipse.aether.transport.http.HttpTransporterFactory;
import org.eclipse.aether.util.repository.AuthenticationBuilder;
import org.eclipse.aether.util.repository.DefaultProxySelector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import maven.fetcher.internal.MavenDependencyFetcher;
import maven.fetcher.internal.MavenTransferLogger;



/**
 * This class allows to fetch Maven artifacts from one or several remote repositories
 */
public class MavenFetcher {


    private final List<RemoteRepository> remoteRepositories = new ArrayList<>(List.of(
        createRemoteRepository("maven-central",  "https://repo.maven.apache.org/maven2")
    ));

    private RepositorySystem system;
    private LocalRepository localRepository;
    private String proxyURL;
    private String proxyUsername;
    private String proxyPassword;
    private List<String> proxyExceptions;
    private Logger logger = LoggerFactory.getLogger(MavenFetcher.class);



    /**
     * Set the logger for this object
     */
    public MavenFetcher logger(Logger logger) {
        this.logger = logger;
        return this;
    }


    /**
     * Set the URL for the net proxy
     */
    public MavenFetcher proxyURL(String url) throws MalformedURLException {
        checkURL(url);
        checkNonNull(url);
        this.proxyURL = url;
        return this;
    }


    /**
     * Set the credentials for the next proxy
     */
    public MavenFetcher proxyCredentials(String username, String password) {
        checkNonNull(username, password);
        this.proxyUsername = username;
        this.proxyPassword = password;
        return this;
    }


    /**
     * Set exceptions for the next proxy
     */
    public MavenFetcher proxyExceptions(Collection<String> exceptions) {
        checkNonNull(exceptions);
        this.proxyExceptions = new ArrayList<>(exceptions);
        return this;
    }


    /**
     * Set the local repository path
     */
    public MavenFetcher localRepositoryPath(String localRepositoryPath) {
        this.localRepository = new LocalRepository(localRepositoryPath);
        return this;
    }


    /**
     * Set the local repository path
     */
    public MavenFetcher localRepositoryPath(Path localRepositoryPath) {
        if (localRepositoryPath == null) {
            throw new IllegalArgumentException("Local repository path cannot be null");
        }
        return localRepositoryPath(localRepositoryPath.toString());
    }


    /**
     * Add a remote repository
     */
    public MavenFetcher addRemoteRepository(String id, String url) {
        this.remoteRepositories.add(createRemoteRepository(id, url));
        return this;
    }


    /**
     * Add a remote repository with the given priority (being 0 the highest)
     */
    public MavenFetcher addRemoteRepository(String id, String url, int priority) {
        this.remoteRepositories.add(priority,createRemoteRepository(id, url));
        return this;
    }



    /**
     * Retrieve the specified artifacts and their dependencies from the remote
     * repositories. Since this operation could be time-consuming, this method returns a
     * {@link CompletableFuture} immediately instead of blocking the execution.
     *
     * @param request The artifact fetching request
     */
    public CompletableFuture<MavenFetchResult> fetchArtifacts(MavenFetchRequest request) {
        return new CompletableFuture<MavenFetchResult>().completeAsync(
            ()->doFetchArtifacts(request)
        );
    }


    private MavenFetchResult doFetchArtifacts(MavenFetchRequest request) {
        try {
            if (remoteRepositories.isEmpty()) {
                throw new IllegalArgumentException("Remote repositories not specified");
            }
            MavenFetchResult result = new MavenDependencyFetcher(
                system(),
                remoteRepositories,
                newSession(),
                request,
                logger
            )
            .fetch();
            if (!result.hasErrors()) {
                logger.warn("Some dependencies were not fetched!");
            }
            return result;
        } catch (DependencyCollectionException e) {
            throw new MavenFetchException(e);
        }
    }




    private RepositorySystem system() {
        if (system == null) {
            system = newRepositorySystem(MavenRepositorySystemUtils.newServiceLocator());
            if (system == null) {
                throw new NullPointerException("Cannot instantiate system");
            }
        }
        return system;
    }



    private DefaultRepositorySystemSession newSession() {
        DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();
        session
            .setLocalRepositoryManager(system().newLocalRepositoryManager(session, localRepository));
        session.setTransferListener(new MavenTransferLogger(logger));
        proxy().ifPresent(session::setProxySelector);
        return session;
    }


    private Optional<ProxySelector> proxy() {
        if (proxyURL == null) {
            return Optional.empty();
        }
        URL url;
        try {
            url = new URL(proxyURL);
        } catch (MalformedURLException e) {
            // should never reach this point, URL was checked when set
            throw new MavenFetchException(e);
        }
        int port = url.getPort() < 0 ? 8080 : url.getPort();
        Authentication authentication = null;
        if (proxyUsername != null) {
            authentication = new AuthenticationBuilder().addUsername(proxyUsername)
                .addPassword(proxyPassword).build();
        }
        Proxy proxy = new Proxy(url.getProtocol(), url.getHost(), port, authentication);
        return Optional.of(
            new DefaultProxySelector()
                .add(proxy, proxyExceptions == null ? "" : proxyExceptions.stream().collect(Collectors.joining("|")))
        );
    }



    private RepositorySystem newRepositorySystem(DefaultServiceLocator locator) {
        locator.setErrorHandler(new ErrorHandler() {
            @Override
            public void serviceCreationFailed(Class<?> type, Class<?> impl, Throwable exception) {
                logger.error("Cannot create instance of {} for service {}",impl,type,exception);
            }
        });
        locator.addService(VersionResolver.class, DefaultVersionResolver.class);
        locator.addService(RepositoryConnectorFactory.class, BasicRepositoryConnectorFactory.class);
        locator.addService(TransporterFactory.class, FileTransporterFactory.class);
        locator.addService(TransporterFactory.class, HttpTransporterFactory.class);
        return locator.getService(RepositorySystem.class);
    }



    private static RemoteRepository createRemoteRepository(String id, String url) {
        return new RemoteRepository.Builder(id, "default", url).build();
    }


    private static void checkNonNull(Object... objects) {
        for (Object object : objects) {
            Objects.requireNonNull(object);
        }
    }


    private static void checkNonNull(Collection<? extends Object> collection) {
        Objects.requireNonNull(collection);
        for (Object object : collection) {
            Objects.requireNonNull(object);
        }
    }


    private static void checkURL(String url) throws MalformedURLException {
        new URL(url);
    }



}
