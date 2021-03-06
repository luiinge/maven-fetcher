/**
 * @author Luis Iñesta Gelabert - linesta@iti.es | luiinge@gmail.com
 */
package maven.fetcher;


import java.util.stream.Stream;

/**
 * This interface exposes the results of a fetch operation
 */
public interface MavenFetchResult {

    /** @return A new stream with the fetched artifacts requested */
    Stream<FetchedArtifact> artifacts();

    /** @return A new stream with all fetched artifacts, includind dependencies */
    Stream<FetchedArtifact> allArtifacts();

    /** @return <tt>true</tt> if any error has ocurred during the fetching */
    boolean hasErrors();


}
