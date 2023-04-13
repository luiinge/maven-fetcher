/**
 * @author Luis Iñesta Gelabert -  luiinge@gmail.com
 */
package maven.fetcher;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.stream.Stream;

/**
 * This class contains data regarding a fetched artifact, such as its coordinates and
 * physical path in the local repository.
 *
 */
public class FetchedArtifact {

    private final String groupId;
    private final String artifactId;
    private final String version;
    private final Path path;
    private final List<FetchedArtifact> dependencies;


    public FetchedArtifact(
        String groupId,
        String artifactId,
        String version,
        Path path,
        List<FetchedArtifact> dependencies
    ) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.path = path;
        this.dependencies = dependencies;
    }


    public FetchedArtifact(String coordinates, FetchedArtifact... dependencies) {
        this(coordinates.split(":")[0],coordinates.split(":")[1],coordinates.split(":")[2],null,List.of(dependencies));
    }

    /**
     * The group id
     */
    public String groupId() {
        return groupId;
    }


    /**
     * The artifact id
     */
    public String artifactId() {
        return artifactId;
    }


    /**
     * The version of the artifact
     */
    public String version() {
        return version;
    }


    /**
     * Full coordinates text, in form of {@literal <groupId>:<artifactId>:<version>}
     */
    public String coordinates() {
        return groupId() + ":" + artifactId() + ":" + version();
    }


    /**
     * The fetched artifacts that are direct dependencies of this artifact
     */
    public Stream<FetchedArtifact> dependencies() {
        return dependencies.stream();
    }


    /**
     * The fetched artifacts that are direct or inherited dependencies of this artifact
     */
    public Stream<FetchedArtifact> allDepedencies() {
        return Stream.concat(
            dependencies(),
            dependencies().flatMap(FetchedArtifact::allDepedencies)
        );
    }


    /**
     * The path of the physical file in the local repository
     */
    public Path path() {
        return path;
    }


    @Override
    public String toString() {
        return toString(0, new StringBuilder()).toString();
    }


    private StringBuilder toString(int level, StringBuilder string) {
        for (int i = 0; i < level; i++) {
            string.append("   ");
        }
        string
            .append("|- ")
            .append(coordinates())
            .append("  [")
            .append(path)
            .append("]")
            .append("\n");
        for (FetchedArtifact child : dependencies) {
            child.toString(level + 1, string);
        }
        return string;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FetchedArtifact that = (FetchedArtifact) o;
        return Objects.equals(groupId, that.groupId) && Objects.equals(artifactId, that.artifactId) && Objects.equals(version, that.version) && Objects.equals(dependencies, that.dependencies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, artifactId, version, dependencies);
    }
}