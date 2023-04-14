/*
  @author Luis Iñesta Gelabert -  luiinge@gmail.com
 */
package maven.fetcher.test;


import maven.fetcher.*;
import org.assertj.core.api.Assertions;
import org.junit.*;
import org.slf4j.*;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;


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
            .logger(LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME))
            .fetchArtifacts(
                new MavenFetchRequest("org.apache.maven:maven-artifact:3.9.1").scopes("compile")
            );
        System.out.println(result);
        assertThat(result.artifacts()).containsExactly(
            new FetchedArtifact("org.apache.maven:maven-artifact:3.9.1",
                    new FetchedArtifact("org.codehaus.plexus:plexus-utils:3.5.1"),
                    new FetchedArtifact("org.apache.commons:commons-lang3:3.8.1")
            )
        );

    }


    @Test
    public void fetchArtifactWithExcludedDependencies() {
        var result = new MavenFetcher()
            .localRepositoryPath(localRepo.toString())
            .logger(LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME))
            .fetchArtifacts(
                    new MavenFetchRequest("org.apache.maven:maven-artifact:3.9.1")
                        .scopes("compile")
                        .excludingArtifacts("org.codehaus.plexus:plexus-utils")
            );
        System.out.println(result);
        assertThat(result.artifacts()).containsExactly(
                new FetchedArtifact("org.apache.maven:maven-artifact:3.9.1",
                    new FetchedArtifact("org.apache.commons:commons-lang3:3.8.1")
                )
        );
    }


    @Test
    public void fetchLatestVersionIfVersionNotSpecified() {
        var result = new MavenFetcher()
            .localRepositoryPath(localRepo.toString())
            .logger(LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME))
            .fetchArtifacts(
                    new MavenFetchRequest("org.apache.maven:maven-artifact").scopes("compile")
            );
        System.out.println(result);
        assertThat(result.artifacts())
            .anyMatch(it -> it.groupId().equals("org.apache.maven") && it.artifactId().equals("maven-artifact"));
    }




    @Test
    public void doNotUserDefaultRemoteRepository() {
        Properties withoutDefaultRepo = new Properties();
        withoutDefaultRepo.setProperty(MavenFetcherProperties.USE_DEFAULT_REMOTE_REPOSITORY,"false");
        var fetcher1 = new MavenFetcher().config(withoutDefaultRepo);
        assertThat(fetcher1.remoteRepositories()).isEmpty();

        Properties withDefaultRepo = new Properties();
        withoutDefaultRepo.setProperty(MavenFetcherProperties.USE_DEFAULT_REMOTE_REPOSITORY,"true");
        var fetcher2 = new MavenFetcher().config(withDefaultRepo);
        assertThat(fetcher2.remoteRepositories()).containsExactly(
            "maven-central (https://repo.maven.apache.org/maven2, default, releases+snapshots)"
        );

        var fetcher3 = new MavenFetcher();
        assertThat(fetcher3.remoteRepositories()).containsExactly(
            "maven-central (https://repo.maven.apache.org/maven2, default, releases+snapshots)"
        );
    }



    @Test
    public void repositoryFormats() {
        assertThat(new MavenFetcher().config(properties(
            MavenFetcherProperties.REMOTE_REPOSITORIES,
                "maven-central=https://repo1.maven.org/maven2"
        ))).isNotNull();
        assertThat(new MavenFetcher().config(properties(
                MavenFetcherProperties.REMOTE_REPOSITORIES,
                "maven-central=https://repo1.maven.org/maven2 [user123_@domain:mypass#123@!.]])]"
        ))).isNotNull();
    }



    @Test
    public void malformedPropertiesThrowError() {
        Assertions.assertThatCode(() -> {
            Properties properties = new Properties();
            properties.setProperty(MavenFetcherProperties.REMOTE_REPOSITORIES,"mock:file://repository");
            new MavenFetcher().config(properties);
        }).hasMessage("Invalid value for property 'remoteRepositories' : Invalid repository value 'mock:file://repository' .\n"+
            "Expected formats are 'id=url' and 'id=url [user:pwd]'");
    }


   @Test
   public void attemptToFetchANonExistingArtifact() {
       var result = new MavenFetcher()
           .localRepositoryPath(localRepo.toString())
           .clearRemoteRepositories()
           .addRemoteRepository(new Repository("mock", mockRepo).priority(0))
           .logger(LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME))
           .fetchArtifacts(
               new MavenFetchRequest("a:b:1.0").scopes("compile")
           );
       assertThat(result.allArtifacts()).isEmpty();
       assertThat(result.hasErrors()).isTrue();
       assertThat(result.errors().findAny().map(Exception::getMessage).orElseThrow())
           .isEqualTo("Could not fetch artifact b-1.0.jar");

   }




    private Properties properties(String... pairs) {
        Properties properties = new Properties();
        for (int i = 0; i < pairs.length-1; i+=2) {
            properties.setProperty(pairs[i],pairs[i+1]);
        }
        return properties;
    }

}
