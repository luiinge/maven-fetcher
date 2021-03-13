/**
 * @author Luis Iñesta Gelabert -  luiinge@gmail.com
 */
package maven.fetcher.internal;


import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.collection.CollectResult;
import org.eclipse.aether.collection.DependencyCollectionContext;
import org.eclipse.aether.collection.DependencyCollectionException;
import org.eclipse.aether.collection.DependencySelector;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.graph.DependencyNode;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.ArtifactRequest;
import org.eclipse.aether.resolution.ArtifactResolutionException;
import org.eclipse.aether.resolution.MetadataRequest;
import org.eclipse.aether.resolution.VersionRangeRequest;
import org.eclipse.aether.resolution.VersionRequest;
import org.eclipse.aether.resolution.VersionResolutionException;
import org.eclipse.aether.transfer.ArtifactNotFoundException;
import org.slf4j.Logger;

import maven.fetcher.MavenFetchRequest;
import maven.fetcher.MavenFetchResult;


public class MavenDependencyFetcher implements DependencySelector {

    private final DefaultRepositorySystemSession session;
    private final Collection<String> scopes;
    private final boolean retrieveOptionals;
    private final List<Dependency> dependencies;
    private final List<RemoteRepository> remoteRepositories;
    private final RepositorySystem system;
    private final Logger logger;

    private Set<String> retrievedArtifacts;


    public MavenDependencyFetcher(
        RepositorySystem system,
        List<RemoteRepository> remoteRepositories,
        DefaultRepositorySystemSession session,
        MavenFetchRequest fetchRequest,
        Logger logger
    ) {
        this.system = system;
        this.remoteRepositories = remoteRepositories;
        this.session = session.setDependencySelector(this);
        this.scopes = fetchRequest.scopes();
        this.retrieveOptionals = fetchRequest.isRetrievingOptionals();
        this.dependencies = fetchRequest.artifacts().stream()
            .map(this::artifactFromCoordinates)                        
            .map(artifact -> new Dependency(artifact, null))
            .collect(Collectors.toList());
        this.logger = logger;
    }


    public MavenFetchResult fetch() throws DependencyCollectionException {
        if (logger.isInfoEnabled()) {
            logger.info("Using the following repositories:");
            for (var remoteRepository : remoteRepositories) {
                logger.info("- {repository} [{uri}]", remoteRepository.getId(), remoteRepository.getUrl());
            }
        }
        this.retrievedArtifacts = new HashSet<>();
        CollectRequest request = new CollectRequest(dependencies, null, remoteRepositories);
        CollectResult result = system.collectDependencies(session, request);
        retrieveDependency(result.getRoot());
        return new MavenFetchResultImpl(result, session);
    }


    private DefaultArtifact artifactFromCoordinates (String coordinates) {
        try {
            return new DefaultArtifact(coordinates);
        } catch (IllegalArgumentException e) {
            // if version is not supplied, try to use the latest
            var parts = coordinates.split(":");
            if (parts.length == 2) {
                try {
                    var groupId = parts[0];
                    var artifactId = parts[1];
                    var versionRequest = new VersionRequest()
                        .setArtifact(new DefaultArtifact(groupId, artifactId, "jar", "LATEST"))
                        .setRepositories(this.remoteRepositories);                    
                    var version = system.resolveVersion(session, versionRequest).getVersion();
                    return new DefaultArtifact(groupId, artifactId, "jar", version);
                } catch (VersionResolutionException ex) {
                    throw new IllegalArgumentException("Cannot resolve artifact version: "+ex.getMessage(), ex);
                }
            }
            throw e;
        }
    }

    private void retrieveDependency(DependencyNode node) {
        if (node.getArtifact() != null) {
            try {
                ArtifactRequest request = new ArtifactRequest(
                    node.getArtifact(), remoteRepositories, null
                );
                system.resolveArtifact(session, request);
            } catch (ArtifactResolutionException e) {
                if (!(e.getCause() instanceof ArtifactNotFoundException)) {
                    logger.debug("<caused by>", e);
                }
            }
        }
        for (DependencyNode child : node.getChildren()) {
            retrieveDependency(child);
        }
    }


    @Override
    public boolean selectDependency(Dependency dependency) {
        String artifactKey = key(dependency.getArtifact());
        if (this.retrievedArtifacts.contains(artifactKey) ||
            (dependency.isOptional() && !retrieveOptionals) ||
            (!dependency.getScope().isEmpty() && !scopes.contains(dependency.getScope()))
        ) {
            return false;
        }
        this.retrievedArtifacts.add(artifactKey);
        return true;
    }


    @Override
    public DependencySelector deriveChildSelector(DependencyCollectionContext context) {
        return this;
    }


    private static String key(Artifact artifact) {
        return artifact.getGroupId() + ":" + artifact.getArtifactId() + ":" + artifact.getVersion();
    }

}
