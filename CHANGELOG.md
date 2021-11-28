Changelog
===============================================================================


All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog][1],
and this project adheres to [Semantic Versioning][2].

[1.2.2]
-------------------------------------------------------------------------------
**Release date:** 2022/11/28

### Fixed
- Requesting a non-existing artifact resulted in a `FetchedArtifact` pointing to
a non-existing local file. Now such results are omitted in order to avoid confusion.

[1.2.1]
-------------------------------------------------------------------------------
**Release date:** 2021/07/27

### Modified
- Updated versions of all `org.apache.maven.resolver` plugins and `com.google.guava`
  dependency in order to avoid vulnerabilities
  
[1.2.0]
-------------------------------------------------------------------------------
**Release date:** 2021/07/24

### Added
- Method to configure a fetcher from a `Properties` object
  (documented for version 1.1.0 but left out by mistake)

  
[1.1.0]
-------------------------------------------------------------------------------
**Release date:** 2021/05/11

### Modified
- Use of Apache Maven Artifact Resolver instead of Aether components 
  as underlying implementation, as referred in https://github.com/luiinge/maven-fetcher/issues/10

[1.0.0]
-------------------------------------------------------------------------------
**Release date:** 2020/07/04

- Initial release.


[1]: <https://keepachangelog.com>
[2]: <https://semver.org>