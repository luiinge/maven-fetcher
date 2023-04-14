Changelog
===============================================================================


All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog][1],
and this project adheres to [Semantic Versioning][2].

[1.7.0]
-------------------------------------------------------------------------------
**Release date:** 2023/04/14

### Modified
- Maven resolver included as shaded copy instead of module dependencies
due to incompatibilities with the Java module system

[1.6.0]
-------------------------------------------------------------------------------
**Release date:** 2023/04/13

### Modified
- Updated and fixed dependencies


[1.5.1]
-------------------------------------------------------------------------------
**Release date:** 2022/09/17

### Fixed
- Issue when parsing repositories with non-alphabetical symbols


[1.5.0]
-------------------------------------------------------------------------------
**Release date:** 2022/09/11

### Added
- Capability of passing credentials to remote repositories

### Fixed
- Property `useDefaultRemoteRepository` was inverted (https://github.com/luiinge/maven-fetcher/issues/13)


[1.4.0]
-------------------------------------------------------------------------------
**Release date:** 2022/07/30

### Added
- Method `MavenFetchResult.errors` returning a stream with the exceptions
occurred during a fetch operation

### Fixed
- Method `MavenFetchResult.hasErrors` returned negated value
- Typos in some messages
- Already fetched artifacts do not show 'downloaded' log messages

### Modified
- Now when any artifact could not be fetched, it will be registered
as an error in the fetch result.


[1.3.0]
-------------------------------------------------------------------------------
**Release date:** 2021/12/01

### Added
- Feature: add exclusions to a fetch request; excluded artifacts
will not be fetched.

### Modified
- Parameters of `MavenFetchRequest` are stored as unmodifiable copies,
so modifying original parameter source would not affect the request.
- Bumped version of `org.apache.maven.resolver` to `1.7.2`
### Removed
- Unused dependency on `maven-aether-provider`

[1.2.2]
-------------------------------------------------------------------------------
**Release date:** 2021/11/28

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