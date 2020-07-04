/**
 * @author Luis Iñesta Gelabert - linesta@iti.es | luiinge@gmail.com
 */
package maven.fetcher;


import java.util.Arrays;
import java.util.Collection;


/**
 * This class defines the fetch request that would be passed to a {@link MavenFetcher} instance.
 */
public class MavenFetchRequest {

    private final Collection<String> artifacts;
    private Collection<String> scopes = Arrays.asList("compile", "provided");
    private boolean retrievingOptionals = false;


    /**
     * Creates a new request asking for the given artifact coordinates
     * @param artifacts The artifacts requested, in form of {@literal <groupId>:<artifactId>:<version>}
     */
    public MavenFetchRequest(Collection<String> artifacts) {
        this.artifacts = artifacts;
    }


    /**
     * Creates a new request asking for the given artifact coordinates
     * @param artifacts The artifacts requested, in form of {@literal <groupId>:<artifactId>:<version>}
     */
    public MavenFetchRequest(String... artifacts) {
        this.artifacts = Arrays.asList(artifacts);
    }



    /**
     * Set the scopes of the dependencies of the requested artifacts
     * @param scopes One or more scopes, that would be <tt>compile</tt>,<tt>provided</tt>,<tt>test</tt>
     * @return The same instance
     */
    public MavenFetchRequest scopes(String... scopes) {
        this.scopes = Arrays.asList(scopes);
        return this;
    }


    /**
     * Instruct this request to fetch also any optional dependency
     * @return The same instance
     */
    public MavenFetchRequest retrievingOptionals() {
        this.retrievingOptionals = true;
        return this;
    }


    /**
     * @return The request artifact coordinates, in form of {@literal <groupId>:<artifactId>:<version>}
     */
    public Collection<String> artifacts() {
        return artifacts;
    }


    /**
     * @return The scope of the requested artifact dependencies
     */
    public Collection<String> scopes() {
        return scopes;
    }


    /**
     * @return Whether optional dependencies would be requested
     */
    public boolean isRetrievingOptionals() {
        return retrievingOptionals;
    }
}
