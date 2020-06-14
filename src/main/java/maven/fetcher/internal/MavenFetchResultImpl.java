/**
 * @author Luis Iñesta Gelabert - linesta@iti.es | luiinge@gmail.com
 */
package maven.fetcher.internal;


import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.collection.CollectResult;
import org.eclipse.aether.graph.DependencyNode;
import org.eclipse.aether.repository.LocalRepositoryManager;

import maven.fetcher.FetchedArtifact;
import maven.fetcher.MavenFetchResult;



public class MavenFetchResultImpl implements MavenFetchResult {


    private final CollectResult result;
    private final List<FetchedArtifact> rootArtifacts;


    MavenFetchResultImpl(CollectResult result, DefaultRepositorySystemSession session) {
        this.result = result;
        LocalRepositoryManager localRepositoryManager = session.getLocalRepositoryManager();
        Path repositoryPath = localRepositoryManager.getRepository().getBasedir().toPath();
        this.rootArtifacts = result.getRoot().getChildren().stream()
            .map(node -> collectArtifact(node, localRepositoryManager, repositoryPath))
            .collect(Collectors.toList());
    }


    private FetchedArtifact collectArtifact(
        DependencyNode node,
        LocalRepositoryManager localRepositoryManager,
        Path repositoryPath
    ) {
        Artifact artifact = node.getArtifact();
        return new FetchedArtifact(
            artifact,
            repositoryPath.resolve(localRepositoryManager.getPathForLocalArtifact(artifact)),
            node.getChildren().stream()
                .map(child -> collectArtifact(child, localRepositoryManager, repositoryPath))
                .collect(Collectors.toList())
        );
    }


    public Stream<FetchedArtifact> artifacts() {
        return rootArtifacts.stream();
    }


    public Stream<FetchedArtifact> allArtifacts() {
        return Stream.concat(
            artifacts(),
            artifacts().flatMap(FetchedArtifact::allDepedencies)
        );
    }


    public boolean hasErrors() {
        return result.getExceptions().isEmpty();
    }


    @Override
    public String toString() {
        return artifacts().map(FetchedArtifact::toString).collect(Collectors.joining("\n"));
    }

}
