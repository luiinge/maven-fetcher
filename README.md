Maven Fetcher
================================================================================

This project intention is to offer an easy way to retrieve Maven artifacts from remote repositories
outside a regular Maven life-cycle operation. This feature may be necessary, for example, if you are
building a tool that handles plugins that are provided as Maven artifacts, and you need to download
 and including them in your classpath dynamically.

There is already a library solving this situation: [Aether][1]. However, its API is proven to be a
bit overwhelming for casual clients. The *Maven Fetcher* library is **wrapper** around it exposing
a simpler API that should be enough for basic usage, as well as adding the capability of configure
the fetching process externally using property files.


Usage
-----------------------------------------------------------------------------------------

### Example
A typical use of this library would be a three-step process:
1. Create a new fetcher and configure it if necessary
1. Invoke the method `fetchArtifacts`
1. Check the result and handle the fetched artifacts if necessary

```java
  CompletableFuture<MavenFetchResult> result = new MavenFetcher()
    .localRepositoryPath("/home/linesta/.m2/repository")
    .addRemoteRepository("nexus", "https://nexus:8081/repository/maven-releases")
    .fetchArtifacts( new MavenFetchRequest(
         "junit:junit:4.12",
         "org.apache.commons:commons-lang3:3.9"
       )
       .scopes("compile","provided")
       .retrievingOptionals()       .
    );
    Stream<FetchedArtifact> fetchedArtifacts = result.get().allArtifacts();
```

Notice that the method returns a `CompletableFuture` instead of the direct result. This way,
you can launch a fetch without blocking necessarily your application.

### Dependency

#### Maven
Include the following within the `<dependencies>` section of your `pom.xml` file:
```xml
<dependency>
    <groupId>maven-fetcher</groupId>
    <artifactId>maven-fetcher</artifactId>
    <version>1.0.0</version>
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
### Additional license
Some parts of this library are directly copied from the source code of several Maven packages.
This has been necessary in order to make them compatible with Java Jigsaw module system, but no
alteration has been made. Each of these files are licensed independently under the Apache License
as follows:

```
  Licensed to the Apache Software Foundation (ASF) under one
  or more contributor license agreements.  See the NOTICE file
  distributed with this work for additional information
  regarding copyright ownership.  The ASF licenses this file
  to you under the Apache License, Version 2.0 (the
  "License"); you may not use this file except in compliance
  with the License.  You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing,
  software distributed under the License is distributed on an
  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
  KIND, either express or implied.  See the License for the
  specific language governing permissions and limitations
  under the License.

```
Any other part with no explicit license declared is considered to be under the
MIT License.



Authors
-----------------------------------------------------------------------------------------

- Luis Iñesta Gelabert  |  luiinge@gmail.com


Contributions
-----------------------------------------------------------------------------------------
If you want to contribute to this project, visit the
[Github project](https://github.com/luiinge/maven-fetcher). You can open a new issue / feature
request, or make a pull request to consider. If your contribution is worthing, you will be added
as a contributor in this very page.





[1]: <https://projects.eclipse.org/projects/technology.aether>
