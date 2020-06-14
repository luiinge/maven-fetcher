package org.apache.maven.repository.legacy.metadata;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.repository.metadata.RepositoryMetadataStoreException;

/**
 * Contains metadata about an artifact, and methods to retrieve/store it from an artifact repository.
 *
 * @author <a href="mailto:brett@apache.org">Brett Porter</a>
 * @todo merge with artifactmetadatasource
 * @todo retrieval exception not appropriate for store
 */
public interface ArtifactMetadata
{
    /** Whether this metadata should be stored alongside the artifact. */
    boolean storedInArtifactVersionDirectory();

    /** Whether this metadata should be stored alongside the group. */
    boolean storedInGroupDirectory();

    String getGroupId();

    String getArtifactId();

    String getBaseVersion();

    Object getKey();

    /**
     * Get the filename of this metadata on the local repository.
     *
     * @param repository the remote repository it came from
     * @return the filename
     */
    String getLocalFilename( ArtifactRepository repository );

    /**
     * Get the filename of this metadata on the remote repository.
     *
     * @return the filename
     */
    String getRemoteFilename();

    /**
     * Merge a new metadata set into this piece of metadata.
     *
     * @param metadata the new metadata
     * @todo this should only be needed on the repository metadata
     */
    void merge( ArtifactMetadata metadata );

    /**
     * Store the metadata in the local repository.
     *
     * @param localRepository  the local repository
     * @param remoteRepository the remote repository it came from
     * @todo this should only be needed on the repository metadata
     */
    void storeInLocalRepository( ArtifactRepository localRepository,
                                 ArtifactRepository remoteRepository )
        throws RepositoryMetadataStoreException;

    String extendedToString();
}
