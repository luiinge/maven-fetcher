/**
 * @author Luis Iñesta Gelabert - linesta@iti.es | luiinge@gmail.com
 */
package iti.commons.maven.fetcher;


import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.TimeUnit;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import maven.fetcher.MavenFetchRequest;
import maven.fetcher.MavenFetcher;


public class TestMavenFetcher {

    private static FileVisitor<Path> deleteFileTree = new SimpleFileVisitor<Path>() {

        @Override
        public FileVisitResult visitFile(Path arg0, BasicFileAttributes arg1) throws IOException {
            Files.delete(arg0);
            return FileVisitResult.CONTINUE;
        }


        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            Files.delete(dir);
            return FileVisitResult.CONTINUE;
        }
    };


    @Test
    public void testFetcher() throws Exception {
        Path localRepo = Paths.get("target/mvn-repo");
        if (localRepo.toFile().exists()) {
            Files.walkFileTree(localRepo, deleteFileTree);
        }
        var mockRepo = Path.of("src","test","resources","mock_maven_repo").toAbsolutePath().toUri().toString();
        var result = new MavenFetcher()
            .localRepositoryPath(localRepo)
            .addRemoteRepository("mock", mockRepo, 0)
            .logger(LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME))
            .fetchArtifacts(
                new MavenFetchRequest("junit:junit:4.12").scopes("compile")
            )
        .get(5,TimeUnit.SECONDS);
        Assertions.assertThat(result.allArtifacts()).allSatisfy(artifact->artifact.path().toFile().exists());
    }

}
