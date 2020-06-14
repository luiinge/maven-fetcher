/**
 * @author Luis Iñesta Gelabert - linesta@iti.es | luiinge@gmail.com
 */
package maven.fetcher;


import java.util.Arrays;
import java.util.Collection;



public class MavenFetchRequest {

    private final Collection<String> artifacts;
    private Collection<String> scopes = Arrays.asList("compile", "provided");
    private boolean retrievingOptionals = false;


    public MavenFetchRequest(Collection<String> artifacts) {
        this.artifacts = artifacts;
    }

    public MavenFetchRequest(String... artifacts) {
        this.artifacts = Arrays.asList(artifacts);
    }


    public MavenFetchRequest scopes(String... scopes) {
        this.scopes = Arrays.asList(scopes);
        return this;
    }


    public MavenFetchRequest retrievingOptionals() {
        this.retrievingOptionals = true;
        return this;
    }


    public Collection<String> artifacts() {
        return artifacts;
    }


    public Collection<String> scopes() {
        return scopes;
    }


    public boolean isRetrievingOptionals() {
        return retrievingOptionals;
    }
}
