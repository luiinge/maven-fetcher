/**
 * @author Luis Iñesta Gelabert -  luiinge@gmail.com
 */
package maven.fetcher.internal;


import java.nio.file.*;
import java.util.*;
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
        this.rootArtifacts = result.getRoot()
            .getChildren()
            .stream()
            .map(node -> collectArtifact(node, localRepositoryManager, repositoryPath))
            .flatMap(Optional::stream)
            .collect(Collectors.toList());
    }


    private Optional<FetchedArtifact> collectArtifact(
        DependencyNode node,
        LocalRepositoryManager localRepositoryManager,
        Path repositoryPath
    ) {
        Artifact artifact = node.getArtifact();
        Path localPath = repositoryPath.resolve(
            localRepositoryManager.getPathForLocalArtifact(artifact)
        );
        // the existence of the dependency node does not imply the artifact exists
        if (!Files.exists(localPath)) {
            return Optional.empty();
        }
        List<FetchedArtifact> dependencies = node.getChildren().stream()
            .map(child -> collectArtifact(child, localRepositoryManager, repositoryPath))
            .flatMap(Optional::stream)
            .collect(Collectors.toList());

        return Optional.of(new FetchedArtifact(
            artifact.getGroupId(),
            artifact.getArtifactId(),
            artifact.getVersion(),
            localPath,
            dependencies
        ));
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
