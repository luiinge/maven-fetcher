/*
  @author Luis Iñesta Gelabert -  luiinge@gmail.com
 */
package iti.commons.maven.fetcher;


import java.util.stream.Collectors;
import maven.fetcher.*;
import org.assertj.core.api.Assertions;
import org.junit.*;
import org.slf4j.*;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Properties;


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
    public void fetchArtifactWithDependencies() {
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
    public void fetchArtifactWithExcludedDependencies() {
        var result = new MavenFetcher()
            .localRepositoryPath(localRepo.toString())
            .clearRemoteRepositories()
            .addRemoteRepository("mock", mockRepo, 0)
            .logger(LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME))
            .fetchArtifacts(
                new MavenFetchRequest("junit:junit:4.12")
                    .scopes("compile")
                    .excludingArtifacts("org.hamcrest:hamcrest-core")
            );
        assertJUnit4_12IsFetchedWithoutDependencies(result);
    }


    @Test
    public void fetchLatestVersionIfVersionNotSpecified() {
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


    @Test
    public void fetcherCanBeConfiguredViaProperties() {
        Properties properties = new Properties();
        properties.setProperty(MavenFetcherProperties.LOCAL_REPOSITORY, localRepo.toString());
        properties.setProperty(MavenFetcherProperties.USE_DEFAULT_REMOTE_REPOSITORY,"false");
        properties.setProperty(MavenFetcherProperties.REMOTE_REPOSITORIES,"mock="+mockRepo);
        var result = new MavenFetcher()
            .config(properties)
            .logger(LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME))
            .fetchArtifacts(
                new MavenFetchRequest("junit:junit:4.12").scopes("compile")
            );
        assertJUnit4_12IsFetched(result);
    }


    @Test
    public void malformedPropertiesThrowError() {
        Assertions.assertThatCode(() -> {
            Properties properties = new Properties();
            properties.setProperty(MavenFetcherProperties.REMOTE_REPOSITORIES,"mock:file://repository");
            new MavenFetcher().config(properties);
        }).hasMessage("Invalid value for property 'remoteRepositories' : Wrong repository format 'mock:file://repository' ; expected <repo_id>=<repo_url>");
    }


   @Test
   public void attemptToFetchANonExistingArtifact() {
       var result = new MavenFetcher()
           .localRepositoryPath(localRepo.toString())
           .clearRemoteRepositories()
           .addRemoteRepository("mock", mockRepo, 0)
           .logger(LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME))
           .fetchArtifacts(
               new MavenFetchRequest("a:b:1.0").scopes("compile")
           );
       Assertions.assertThat(result.allArtifacts()).isEmpty();
       Assertions.assertThat(result.hasErrors()).isTrue();
       Assertions.assertThat(result.errors().findAny().map(Exception::getMessage).orElseThrow())
           .isEqualTo("Could not fetch artifact b-1.0.jar");

   }


    void assertJUnit4_12IsFetched(MavenFetchResult result) {
        Assertions.assertThat(result.allArtifacts())
            .anyMatch(artifact->artifact.coordinates().equals("junit:junit:4.12"))
            .anyMatch(artifact->artifact.path().getFileName().toString().equals("junit-4.12.jar"))
            .anyMatch(artifact->artifact.toString().equals(junitArtifactToString()))
            .allMatch(artifact->artifact.path().toFile().exists())
        ;
    }


    void assertJUnit4_12IsFetchedWithoutDependencies(MavenFetchResult result) {
        Assertions.assertThat(result.allArtifacts())
            .anyMatch(artifact->artifact.coordinates().equals("junit:junit:4.12"))
            .anyMatch(artifact->artifact.path().getFileName().toString().equals("junit-4.12.jar"))
            .anyMatch(artifact->artifact.toString().equals(junitArtifactWithoutDependenciesToString()))
            .allMatch(artifact->artifact.path().toFile().exists())
        ;
    }


    private String junitArtifactToString() {
         return
         "|- junit:junit:4.12  ["+localRepo+"/junit/junit/4.12/junit-4.12.jar]\n"+
         "   |- org.hamcrest:hamcrest-core:1.3  ["+localRepo+"/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar]\n"
        ;
    }

    private String junitArtifactWithoutDependenciesToString() {
        return "|- junit:junit:4.12  ["+localRepo+"/junit/junit/4.12/junit-4.12.jar]\n";
    }

}
