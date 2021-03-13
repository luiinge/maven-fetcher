/**
 * @author Luis Iñesta Gelabert -  luiinge@gmail.com
 */
package iti.commons.maven.fetcher;


import java.io.File;
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
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import maven.fetcher.MavenFetchRequest;
import maven.fetcher.MavenFetchResult;
import maven.fetcher.MavenFetcher;


public class TestMavenFetcher {

    private final String mockRepo = Path.of("src","test","resources","mock_maven_repo")
        .toAbsolutePath()
        .toUri()
        .toString();

    private Path localRepo;
        
    @Before
    public void prepareLocalRepo() throws IOException {
        localRepo = Files.createTempDirectory("test");
    }

    @After 
    public void cleanLocalRepo() throws IOException {
        Files.walkFileTree(localRepo, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) 
            throws IOException {
                Files.delete(path);
                return FileVisitResult.CONTINUE;
            }
        });
    }


    
    @Test
    public void fetchArtifactWithDependencies() throws Exception {        
        var result = new MavenFetcher()
            .localRepositoryPath(localRepo.toString())
            .clearRemoteRepositories()
            .addRemoteRepository("mock", mockRepo, 0)
            .logger(LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME))
            .fetchArtifacts(
                new MavenFetchRequest("junit:junit:4.12").scopes("compile")
            );
        assertJUnit4_12IsFetched(result);
    }


    @Test
    public void fetchLastestVersionIfVersionNotSpecified() throws Exception {
        var mockRepo = Path.of("src","test","resources","mock_maven_repo").toAbsolutePath().toUri().toString();
        var result = new MavenFetcher()
            .localRepositoryPath(localRepo.toString())
            .clearRemoteRepositories()
            .addRemoteRepository("mock", mockRepo, 0)
            .logger(LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME))
            .fetchArtifacts(
                new MavenFetchRequest("junit:junit").scopes("compile")
            );
        assertJUnit4_12IsFetched(result);
    }



    void assertJUnit4_12IsFetched(MavenFetchResult result) throws Exception {
        Assertions.assertThat(result.allArtifacts())
            .anyMatch(artifact->artifact.coordinates().equals("junit:junit:4.12"))
            .anyMatch(artifact->artifact.path().getFileName().toString().equals("junit-4.12.jar"))
            .allMatch(artifact->artifact.path().toFile().exists())
            ;
 
    }



    
}
