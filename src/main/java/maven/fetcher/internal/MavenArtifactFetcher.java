/*
  @author Luis Iñesta Gelabert -  luiinge@gmail.com
 */
package maven.fetcher.internal;


import java.util.*;
import java.util.stream.Collectors;


import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.collection.*;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.graph.DependencyNode;
import org.eclipse.aether.graph.Exclusion;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.*;
import org.eclipse.aether.transfer.ArtifactNotFoundException;
import org.eclipse.aether.util.graph.selector.AndDependencySelector;
import org.slf4j.Logger;

import maven.fetcher.*;



public class MavenArtifactFetcher implements DependencySelector {

    private final DefaultRepositorySystemSession session;
    private final Collection<String> scopes;
    private final boolean retrieveOptionals;
    private final List<Artifact> artifacts;
    private final List<Exclusion> exclusions;
    private final List<RemoteRepository> remoteRepositories;
    private final RepositorySystem system;
    private final Logger logger;
    private final MavenTransferListener listener;

    private Set<String> retrievedArtifacts;


    public MavenArtifactFetcher(
        RepositorySystem system,
        List<RemoteRepository> remoteRepositories,
        DefaultRepositorySystemSession session,
        MavenFetchRequest fetchRequest,
        MavenTransferListener listener,
        Logger logger
    ) {
        this.system = system;
        this.remoteRepositories = remoteRepositories;
        this.session = session.setDependencySelector(this);
        this.scopes = fetchRequest.scopes();
        this.retrieveOptionals = fetchRequest.isRetrievingOptionals();
        this.exclusions = fetchRequest.excludedArtifacts().stream()
            .map(this::exclusionFromCoordinates)
            .collect(Collectors.toList());
        this.artifacts = fetchRequest.artifacts().stream()
            .map(this::artifactFromCoordinates)                        
            .collect(Collectors.toList());
        this.logger = logger;
        this.listener = listener;
    }


    public MavenFetchResult fetch() throws DependencyCollectionException, ArtifactDescriptorException {
        if (logger.isInfoEnabled()) {
            logger.info("Using the following repositories:");
            for (var remoteRepository : remoteRepositories) {
                if (remoteRepository.getAuthentication() == null) {
                    logger.info("- {repository} [{uri}]", remoteRepository.getId(), remoteRepository.getUrl());
                } else {
                    logger.info("- {repository} [{uri}] (authenticated)", remoteRepository.getId(), remoteRepository.getUrl());
                }
            }
        }
        this.retrievedArtifacts = new HashSet<>();
        List<CollectResult> results = new ArrayList<>();
        for (var artifact : artifacts) {
            results.add(collectResult(artifact));
        }
        return new MavenFetchResultImpl(results, session);
    }



    private CollectResult collectResult(Artifact artifact)
    throws ArtifactDescriptorException,
    DependencyCollectionException
    {
        ArtifactDescriptorRequest descriptorRequest = new ArtifactDescriptorRequest();
        descriptorRequest.setArtifact(artifact);
        descriptorRequest.setRepositories(remoteRepositories);
        ArtifactDescriptorResult descriptorResult = system.readArtifactDescriptor(session, descriptorRequest);

        CollectRequest request = new CollectRequest();
        request.setRootArtifact(descriptorResult.getArtifact());
        request.setDependencies(descriptorResult.getDependencies());
        request.setManagedDependencies(descriptorResult.getManagedDependencies());
        request.setRepositories(remoteRepositories);

        CollectResult result = system.collectDependencies(session, request);
        retrieveDependency(result.getRoot());
        this.listener.failedTransfers()
            .forEach(file -> result.addException(new MavenFetchException("Could not fetch artifact "+file)));
        return result;
    }


    private DefaultArtifact artifactFromCoordinates (String coordinates) {
        try {
            return new DefaultArtifact(coordinates);
        } catch (IllegalArgumentException e) {
            // if version is not supplied, try to use the latest
            var parts = coordinates.split(":");
            if (parts.length == 2) {
                try {
                    var groupId = parts[0];
                    var artifactId = parts[1];
                    var versionRequest = new VersionRequest()
                        .setArtifact(new DefaultArtifact(groupId, artifactId, "jar", "LATEST"))
                        .setRepositories(this.remoteRepositories);                    
                    var version = system.resolveVersion(session, versionRequest).getVersion();
                    return new DefaultArtifact(groupId, artifactId, "jar", version);
                } catch (VersionResolutionException ex) {
                    throw new IllegalArgumentException("Cannot resolve artifact version: "+ex.getMessage(), ex);
                }
            }
            throw e;
        }
    }


    private Exclusion exclusionFromCoordinates (String coordinates) {
        var parts = coordinates.split(":");
        if (parts.length >= 2) {
            var groupId = parts[0];
            var artifactId = parts[1];
            return new Exclusion(groupId, artifactId, "jar", null);
        } else {
            throw new IllegalArgumentException("Invalid exclusion '"+coordinates+"'");
        }
    }



    private void retrieveDependency(DependencyNode node) {
        if (node.getArtifact() != null) {
           if (isExcluded(node.getArtifact())) {
               return;
           }
            try {
                ArtifactRequest request = new ArtifactRequest(
                    node.getArtifact(), remoteRepositories, null
                );
                system.resolveArtifact(session, request);
            } catch (ArtifactResolutionException e) {
                if (!(e.getCause() instanceof ArtifactNotFoundException)) {
                    logger.debug("<caused by>", e);
                }
            }
        }
        for (DependencyNode child : node.getChildren()) {
            retrieveDependency(child);
        }
    }


    private boolean isExcluded(Artifact artifact) {
        for (Exclusion exclusion : exclusions) {
            if (exclusion.getGroupId().equals(artifact.getGroupId()) &&
                exclusion.getArtifactId().equals(artifact.getArtifactId())
            ) {
                return true;
            }
        }
        return false;
    }


    @Override
    public boolean selectDependency(Dependency dependency) {
        String artifactKey = key(dependency.getArtifact());
        if (this.retrievedArtifacts.contains(artifactKey) ||
            (dependency.isOptional() && !retrieveOptionals) ||
            (!dependency.getScope().isEmpty() && !scopes.contains(dependency.getScope()))
        ) {
            return false;
        }
        this.retrievedArtifacts.add(artifactKey);
        return true;
    }


    @Override
    public DependencySelector deriveChildSelector(DependencyCollectionContext context) {
        return this;
    }


    private static String key(Artifact artifact) {
        return artifact.getGroupId() + ":" + artifact.getArtifactId() + ":" + artifact.getVersion();
    }

}
