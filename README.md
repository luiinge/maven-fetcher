Maven Fetcher
================================================================================

![GitHub](https://img.shields.io/github/license/luiinge/maven-fetcher?style=plastic)
![GitHub Workflow Status (branch)](https://img.shields.io/github/workflow/status/luiinge/maven-fetcher/quality%20check/master?style=plastic)
![Maven Central](https://img.shields.io/maven-central/v/io.github.luiinge/maven-fetcher?style=plastic)

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=luiinge_maven-fetcher&metric=alert_status)](https://sonarcloud.io/dashboard?id=luiinge_maven-fetcher)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=luiinge_maven-fetcher&metric=ncloc)](https://sonarcloud.io/dashboard?id=luiinge_maven-fetcher)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=luiinge_maven-fetcher&metric=coverage)](https://sonarcloud.io/dashboard?id=luiinge_maven-fetcher)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=luiinge_maven-fetcher&metric=bugs)](https://sonarcloud.io/dashboard?id=luiinge_maven-fetcher)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=luiinge_maven-fetcher&metric=code_smells)](https://sonarcloud.io/dashboard?id=luiinge_maven-fetcher)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=luiinge_maven-fetcher&metric=duplicated_lines_density)](https://sonarcloud.io/dashboard?id=luiinge_maven-fetcher)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=luiinge_maven-fetcher&metric=sqale_index)](https://sonarcloud.io/dashboard?id=luiinge_maven-maven-fetcher)



This project intention is to offer an easy way to retrieve Maven artifacts from remote repositories
outside a regular Maven life-cycle operation. This feature may be necessary, for example, if you are
building a tool that handles plugins that are provided as Maven artifacts, and you need to download
 and including them in your classpath dynamically.

There is already a library solving this situation: [Aether][1], which was later migrated to 
[Apache Maven Artifact Resolver][2] according this [official announcment](https://projects.eclipse.org/projects/technology.aether/reviews/termination-review). 
However, this API is arguably a bit overwhelming for casual clients. 
The *Maven Fetcher* library is **wrapper** around `Apache Maven Artifact Resolver` exposing
a simpler API that should be enough for basic usage, as well as adding the capability of configure
the fetching process externally using properties.

> **NOTE**  
> Since version `1.1.0` the underlying implementation uses components of `Apache Maven Artifact 
> Resolver` instead of `Aether`.

Usage
-----------------------------------------------------------------------------------------

### Example
A typical use of this library would be a three-step process:
1. Create a new fetcher and configure it if necessary
1. Invoke the method `fetchArtifacts`
1. Check the result and handle the fetched artifacts if necessary

```java
  var result = new MavenFetcher()
    .localRepositoryPath("/home/linesta/.m2/repository")
    .addRemoteRepository("nexus", "https://nexus:8081/repository/maven-releases")
    .fetchArtifacts( new MavenFetchRequest(
         "junit:junit:4.12",
         "org.apache.commons:commons-lang3:3.9"
       )
       .scopes("compile","provided")
       .retrievingOptionals()       .
    );
```

### Dependency

#### Maven
Include the following within the `<dependencies>` section of your `pom.xml` file:
```xml
<dependency>
    <groupId>io.github.luiinge</groupId>
    <artifactId>maven-fetcher</artifactId>
    <version>1.6.0</version>
</dependency>
```


### Configuration

The API offers a set of methods to configure options fluently:

- `logger(logger :Logger)`

  > Set a custom SLF4J Logger instance to log the traces

- `proxyURL(url :string)`

  > Set a proxy URL, if required

- `proxyCredentials(username: string, password: string)`

  > Set the proxy credentials, if required

- `proxyExceptions(exceptions: Collection<String>)`

  > Set a list of URL exceptions for the proxy, if required

- `localRepositoryPath(localRepositoryPath: Path)`

  > Set the path of the Maven local repository folder

- `addRemoteRepository(repositoryId: string, repositoryURL: string)`

  > Add a Maven remote repository from where retrieve artifacts

#### Configuration via properties

Another way to configure the fetcher is load either a `Properties` object or an external  `.properties` file:

- `config(properties: Properties)`

   >  Configure the fetcher using the properties from the passed object


The accepted properties are the following:

| Property                     | Description                                                                                                                                                              |
|------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `useDefaultRemoteRepository` | Whether use the default Maven Central repository (`true` by default)                                                                                                     |
| `remoteRepositories`         | A list of remote repositories separeted with `;` .<br/> The expected format for each repository is either `id=url` or `id=url [user:pwd]` (when credentials are needed). |
| `localRepository`            | The path of the Maven local repository folder                                                                                                                            |
| `proxy.url`                  | A proxy URL, if required                                                                                                                                                 |
| `proxy.username`             | The username for proxy credentials                                                                                                                                       |
| `proxy.password`             | The password for proxy credentials                                                                                                                                       |
| `proxy.exceptions`           | A list of proxy exceptions separated with `;`                                                                                                                            |


Other considerations
-----------------------------------------------------------------------------------------

### Restricting access to remote repositories
By default, the `MavenFetcher` object would check the Maven central repository at 
[https://repo.maven.apache.org/maven2]. However, in some scenarios you may want to 
restrict the access to remote repositories and use only a curated or private list.
To achieve this, simply clear the remote repositories before adding your own.

```java
  var result = new MavenFetcher()
    .localRepositoryPath("/home/linesta/.m2/repository")
    .clearRemoteRepositories()
    .addRemoteRepository("nexus", "https://nexus:8081/repository/maven-releases")
    .fetchArtifacts( ... )
```

### Artifact versions

The artifact request usually include the specific coordinates of the required artifacts 
in form of `<groupId>:<artifactId>:<version>` . However, this library benefits from the 
Maven version range mechanism, being `version` some of the following:
| Range       | Meaning                           |
| ----------- | --------------------------------- |
| `1.0`       | specific version                  |
| `[1.5,)`    | any version greater or equal      |
| `(,1.5]`    | any version less or equal         |
| `[1.2,1.3]` | any version in the given interval |
| none        | latest version available          |

Notice that, if the version is not included (coordinates in form `<groupId>:<artifactId>`),
the latest version available in the remote repositories will be used.




License
-----------------------------------------------------------------------------------------

```
    MIT License

    Copyright (c) 2020 Luis Iñesta Gelabert

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
```


Authors
-----------------------------------------------------------------------------------------

- Luis Iñesta Gelabert  |  luiinge@gmail.com


Contributions
-----------------------------------------------------------------------------------------
If you want to contribute to this project, visit the
[Github project](https://github.com/luiinge/maven-fetcher). You can open a new issue / feature
request, or make a pull request to consider. If your contribution is worthy, you will be added
as a contributor in this very page.





[1]: <https://projects.eclipse.org/projects/technology.aether>
[2]: <https://maven.apache.org/resolver/>
