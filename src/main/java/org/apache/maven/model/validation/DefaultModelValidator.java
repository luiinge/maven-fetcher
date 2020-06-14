package org.apache.maven.model.validation;

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

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.maven.model.Activation;
import org.apache.maven.model.ActivationFile;
import org.apache.maven.model.Build;
import org.apache.maven.model.BuildBase;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.model.DistributionManagement;
import org.apache.maven.model.Exclusion;
import org.apache.maven.model.InputLocation;
import org.apache.maven.model.InputLocationTracker;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.apache.maven.model.PluginManagement;
import org.apache.maven.model.Profile;
import org.apache.maven.model.ReportPlugin;
import org.apache.maven.model.Reporting;
import org.apache.maven.model.Repository;
import org.apache.maven.model.Resource;
import org.apache.maven.model.building.ModelBuildingRequest;
import org.apache.maven.model.building.ModelProblem.Severity;
import org.apache.maven.model.building.ModelProblem.Version;
import org.apache.maven.model.building.ModelProblemCollector;
import org.apache.maven.model.building.ModelProblemCollectorRequest;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.util.StringUtils;

/**
 * @author <a href="mailto:trygvis@inamo.no">Trygve Laugst&oslash;l</a>
 */
@Component( role = ModelValidator.class )
public class DefaultModelValidator
    implements ModelValidator
{

    private static final Pattern ID_REGEX = Pattern.compile( "[A-Za-z0-9_\\-.]+" );

    private static final Pattern ID_WITH_WILDCARDS_REGEX = Pattern.compile( "[A-Za-z0-9_\\-.?*]+" );

    private static final String ILLEGAL_FS_CHARS = "\\/:\"<>|?*";

    private static final String ILLEGAL_VERSION_CHARS = ILLEGAL_FS_CHARS;

    private static final String ILLEGAL_REPO_ID_CHARS = ILLEGAL_FS_CHARS;

    @Override
    public void validateRawModel( Model m, ModelBuildingRequest request, ModelProblemCollector problems )
    {
        Parent parent = m.getParent();
        if ( parent != null )
        {
            validateStringNotEmpty( "parent.groupId", problems, Severity.FATAL, Version.BASE, parent.getGroupId(),
                                    parent );

            validateStringNotEmpty( "parent.artifactId", problems, Severity.FATAL, Version.BASE,
                                    parent.getArtifactId(), parent );

            validateStringNotEmpty( "parent.version", problems, Severity.FATAL, Version.BASE, parent.getVersion(),
                                    parent );

            if ( equals( parent.getGroupId(), m.getGroupId() )
                && equals( parent.getArtifactId(), m.getArtifactId() ) )
            {
                addViolation( problems, Severity.FATAL, Version.BASE, "parent.artifactId", null, "must be changed"
                    + ", the parent element cannot have the same groupId:artifactId as the project.", parent );
            }
        }

        if ( request.getValidationLevel() >= ModelBuildingRequest.VALIDATION_LEVEL_MAVEN_2_0 )
        {
            Severity errOn30 = getSeverity( request, ModelBuildingRequest.VALIDATION_LEVEL_MAVEN_3_0 );

            validateEnum( "modelVersion", problems, Severity.ERROR, Version.V20, m.getModelVersion(), null, m,
                          "4.0.0" );

            validateStringNoExpression( "groupId", problems, Severity.WARNING, Version.V20, m.getGroupId(), m );
            if ( parent == null )
            {
                validateStringNotEmpty( "groupId", problems, Severity.FATAL, Version.V20, m.getGroupId(), m );
            }

            validateStringNoExpression( "artifactId", problems, Severity.WARNING, Version.V20, m.getArtifactId(), m );
            validateStringNotEmpty( "artifactId", problems, Severity.FATAL, Version.V20, m.getArtifactId(), m );

            validateVersionNoExpression( "version", problems, Severity.WARNING, Version.V20, m.getVersion(), m );
            if ( parent == null )
            {
                validateStringNotEmpty( "version", problems, Severity.FATAL, Version.V20, m.getVersion(), m );
            }

            validate20RawDependencies( problems, m.getDependencies(), "dependencies.dependency", request );

            if ( m.getDependencyManagement() != null )
            {
                validate20RawDependencies( problems, m.getDependencyManagement().getDependencies(),
                                           "dependencyManagement.dependencies.dependency", request );
            }

            validateRawRepositories( problems, m.getRepositories(), "repositories.repository", request );

            validateRawRepositories( problems, m.getPluginRepositories(), "pluginRepositories.pluginRepository",
                                     request );

            Build build = m.getBuild();
            if ( build != null )
            {
                validate20RawPlugins( problems, build.getPlugins(), "build.plugins.plugin", request );

                PluginManagement mngt = build.getPluginManagement();
                if ( mngt != null )
                {
                    validate20RawPlugins( problems, mngt.getPlugins(), "build.pluginManagement.plugins.plugin",
                                          request );
                }
            }

            Set<String> profileIds = new HashSet<>();

            for ( Profile profile : m.getProfiles() )
            {
                String prefix = "profiles.profile[" + profile.getId() + "]";

                if ( !profileIds.add( profile.getId() ) )
                {
                    addViolation( problems, errOn30, Version.V20, "profiles.profile.id", null,
                                  "must be unique but found duplicate profile with id " + profile.getId(), profile );
                }

                validate30RawProfileActivation( problems, profile.getActivation(), profile.getId(), prefix
                    + ".activation", request );

                validate20RawDependencies( problems, profile.getDependencies(), prefix + ".dependencies.dependency",
                                         request );

                if ( profile.getDependencyManagement() != null )
                {
                    validate20RawDependencies( problems, profile.getDependencyManagement().getDependencies(), prefix
                        + ".dependencyManagement.dependencies.dependency", request );
                }

                validateRawRepositories( problems, profile.getRepositories(), prefix + ".repositories.repository",
                                      request );

                validateRawRepositories( problems, profile.getPluginRepositories(), prefix
                    + ".pluginRepositories.pluginRepository", request );

                BuildBase buildBase = profile.getBuild();
                if ( buildBase != null )
                {
                    validate20RawPlugins( problems, buildBase.getPlugins(), prefix + ".plugins.plugin", request );

                    PluginManagement mngt = buildBase.getPluginManagement();
                    if ( mngt != null )
                    {
                        validate20RawPlugins( problems, mngt.getPlugins(), prefix + ".pluginManagement.plugins.plugin",
                                            request );
                    }
                }
            }
        }
    }

    private void validate30RawProfileActivation( ModelProblemCollector problems, Activation activation,
                                                 String sourceHint, String prefix, ModelBuildingRequest request )
    {
        if ( activation == null )
        {
            return;
        }

        ActivationFile file = activation.getFile();

        if ( file != null )
        {
            String path;
            boolean missing;

            if ( StringUtils.isNotEmpty( file.getExists() ) )
            {
                path = file.getExists();
                missing = false;
            }
            else if ( StringUtils.isNotEmpty( file.getMissing() ) )
            {
                path = file.getMissing();
                missing = true;
            }
            else
            {
                return;
            }

            if ( path.contains( "${project.basedir}" ) )
            {
                addViolation( problems,
                              Severity.WARNING,
                              Version.V30,
                              prefix + ( missing ? ".file.missing" : ".file.exists" ),
                              null,
                              "Failed to interpolate file location " + path + " for profile " + sourceHint
                                  + ": ${project.basedir} expression not supported during profile activation, "
                                  + "use ${basedir} instead",
                              file.getLocation( missing ? "missing" : "exists" ) );
            }
            else if ( hasProjectExpression( path ) )
            {
                addViolation( problems,
                              Severity.WARNING,
                              Version.V30,
                              prefix + ( missing ? ".file.missing" : ".file.exists" ),
                              null,
                              "Failed to interpolate file location "
                                  + path
                                  + " for profile "
                                  + sourceHint
                                  + ": ${project.*} expressions are not supported during profile activation",
                              file.getLocation( missing ? "missing" : "exists" ) );
            }
        }
    }

    private void validate20RawPlugins( ModelProblemCollector problems, List<Plugin> plugins, String prefix,
                                     ModelBuildingRequest request )
    {
        Severity errOn31 = getSeverity( request, ModelBuildingRequest.VALIDATION_LEVEL_MAVEN_3_1 );

        Map<String, Plugin> index = new HashMap<>();

        for ( Plugin plugin : plugins )
        {
            String key = plugin.getKey();

            Plugin existing = index.get( key );

            if ( existing != null )
            {
                addViolation( problems, errOn31, Version.V20, prefix + ".(groupId:artifactId)", null,
                              "must be unique but found duplicate declaration of plugin " + key, plugin );
            }
            else
            {
                index.put( key, plugin );
            }

            Set<String> executionIds = new HashSet<>();

            for ( PluginExecution exec : plugin.getExecutions() )
            {
                if ( !executionIds.add( exec.getId() ) )
                {
                    addViolation( problems, Severity.ERROR, Version.V20, prefix + "[" + plugin.getKey()
                        + "].executions.execution.id", null, "must be unique but found duplicate execution with id "
                        + exec.getId(), exec );
                }
            }
        }
    }

    @Override
    public void validateEffectiveModel( Model m, ModelBuildingRequest request, ModelProblemCollector problems )
    {
        validateStringNotEmpty( "modelVersion", problems, Severity.ERROR, Version.BASE, m.getModelVersion(), m );

        validateId( "groupId", problems, m.getGroupId(), m );

        validateId( "artifactId", problems, m.getArtifactId(), m );

        validateStringNotEmpty( "packaging", problems, Severity.ERROR, Version.BASE, m.getPackaging(), m );

        if ( !m.getModules().isEmpty() )
        {
            if ( !"pom".equals( m.getPackaging() ) )
            {
                addViolation( problems, Severity.ERROR, Version.BASE, "packaging", null,
                              "with value '" + m.getPackaging() + "' is invalid. Aggregator projects "
                                  + "require 'pom' as packaging.", m );
            }

            for ( int i = 0, n = m.getModules().size(); i < n; i++ )
            {
                String module = m.getModules().get( i );
                if ( StringUtils.isBlank( module ) )
                {
                    addViolation( problems, Severity.ERROR, Version.BASE, "modules.module[" + i + "]", null,
                                  "has been specified without a path to the project directory.",
                                  m.getLocation( "modules" ) );
                }
            }
        }

        validateStringNotEmpty( "version", problems, Severity.ERROR, Version.BASE, m.getVersion(), m );

        Severity errOn30 = getSeverity( request, ModelBuildingRequest.VALIDATION_LEVEL_MAVEN_3_0 );

        validateEffectiveDependencies( problems, m.getDependencies(), false, request );

        DependencyManagement mgmt = m.getDependencyManagement();
        if ( mgmt != null )
        {
            validateEffectiveDependencies( problems, mgmt.getDependencies(), true, request );
        }

        if ( request.getValidationLevel() >= ModelBuildingRequest.VALIDATION_LEVEL_MAVEN_2_0 )
        {
            Set<String> modules = new HashSet<>();
            for ( int i = 0, n = m.getModules().size(); i < n; i++ )
            {
                String module = m.getModules().get( i );
                if ( !modules.add( module ) )
                {
                    addViolation( problems, Severity.ERROR, Version.V20, "modules.module[" + i + "]", null,
                                  "specifies duplicate child module " + module, m.getLocation( "modules" ) );
                }
            }

            Severity errOn31 = getSeverity( request, ModelBuildingRequest.VALIDATION_LEVEL_MAVEN_3_1 );

            validateBannedCharacters( "version", problems, errOn31, Version.V20, m.getVersion(), null, m,
                                      ILLEGAL_VERSION_CHARS );
            validate20ProperSnapshotVersion( "version", problems, errOn31, Version.V20, m.getVersion(), null, m );

            Build build = m.getBuild();
            if ( build != null )
            {
                for ( Plugin p : build.getPlugins() )
                {
                    validateStringNotEmpty( "build.plugins.plugin.artifactId", problems, Severity.ERROR, Version.V20,
                                            p.getArtifactId(), p );

                    validateStringNotEmpty( "build.plugins.plugin.groupId", problems, Severity.ERROR, Version.V20,
                                            p.getGroupId(), p );

                    validate20PluginVersion( "build.plugins.plugin.version", problems, p.getVersion(), p.getKey(), p,
                                             request );

                    validateBoolean( "build.plugins.plugin.inherited", problems, errOn30, Version.V20,
                                     p.getInherited(), p.getKey(), p );

                    validateBoolean( "build.plugins.plugin.extensions", problems, errOn30, Version.V20,
                                     p.getExtensions(), p.getKey(), p );

                    validate20EffectivePluginDependencies( problems, p, request );
                }

                validate20RawResources( problems, build.getResources(), "build.resources.resource", request );

                validate20RawResources( problems, build.getTestResources(), "build.testResources.testResource",
                                        request );
            }

            Reporting reporting = m.getReporting();
            if ( reporting != null )
            {
                for ( ReportPlugin p : reporting.getPlugins() )
                {
                    validateStringNotEmpty( "reporting.plugins.plugin.artifactId", problems, Severity.ERROR,
                                            Version.V20, p.getArtifactId(), p );

                    validateStringNotEmpty( "reporting.plugins.plugin.groupId", problems, Severity.ERROR, Version.V20,
                                            p.getGroupId(), p );
                }
            }

            for ( Repository repository : m.getRepositories() )
            {
                validate20EffectiveRepository( problems, repository, "repositories.repository", request );
            }

            for ( Repository repository : m.getPluginRepositories() )
            {
                validate20EffectiveRepository( problems, repository, "pluginRepositories.pluginRepository", request );
            }

            DistributionManagement distMgmt = m.getDistributionManagement();
            if ( distMgmt != null )
            {
                if ( distMgmt.getStatus() != null )
                {
                    addViolation( problems, Severity.ERROR, Version.V20, "distributionManagement.status", null,
                                  "must not be specified.", distMgmt );
                }

                validate20EffectiveRepository( problems, distMgmt.getRepository(), "distributionManagement.repository",
                                               request );
                validate20EffectiveRepository( problems, distMgmt.getSnapshotRepository(),
                                    "distributionManagement.snapshotRepository", request );
            }
        }
    }

    private void validate20RawDependencies( ModelProblemCollector problems, List<Dependency> dependencies,
                                            String prefix, ModelBuildingRequest request )
    {
        Severity errOn30 = getSeverity( request, ModelBuildingRequest.VALIDATION_LEVEL_MAVEN_3_0 );
        Severity errOn31 = getSeverity( request, ModelBuildingRequest.VALIDATION_LEVEL_MAVEN_3_1 );

        Map<String, Dependency> index = new HashMap<>();

        for ( Dependency dependency : dependencies )
        {
            String key = dependency.getManagementKey();

            if ( "import".equals( dependency.getScope() ) )
            {
                if ( !"pom".equals( dependency.getType() ) )
                {
                    addViolation( problems, Severity.WARNING, Version.V20, prefix + ".type", key,
                                  "must be 'pom' to import the managed dependencies.", dependency );
                }
                else if ( StringUtils.isNotEmpty( dependency.getClassifier() ) )
                {
                    addViolation( problems, errOn30, Version.V20, prefix + ".classifier", key,
                                  "must be empty, imported POM cannot have a classifier.", dependency );
                }
            }
            else if ( "system".equals( dependency.getScope() ) )
            {
                String sysPath = dependency.getSystemPath();
                if ( StringUtils.isNotEmpty( sysPath ) )
                {
                    if ( !hasExpression( sysPath ) )
                    {
                        addViolation( problems, Severity.WARNING, Version.V20, prefix + ".systemPath", key,
                                      "should use a variable instead of a hard-coded path " + sysPath, dependency );
                    }
                    else if ( sysPath.contains( "${basedir}" ) || sysPath.contains( "${project.basedir}" ) )
                    {
                        addViolation( problems, Severity.WARNING, Version.V20, prefix + ".systemPath", key,
                                      "should not point at files within the project directory, " + sysPath
                                          + " will be unresolvable by dependent projects", dependency );
                    }
                }
            }

            Dependency existing = index.get( key );

            if ( existing != null )
            {
                String msg;
                if ( equals( existing.getVersion(), dependency.getVersion() ) )
                {
                    msg =
                        "duplicate declaration of version "
                            + StringUtils.defaultString( dependency.getVersion(), "(?)" );
                }
                else
                {
                    msg =
                        "version " + StringUtils.defaultString( existing.getVersion(), "(?)" ) + " vs "
                            + StringUtils.defaultString( dependency.getVersion(), "(?)" );
                }

                addViolation( problems, errOn31, Version.V20, prefix + ".(groupId:artifactId:type:classifier)", null,
                              "must be unique: " + key + " -> " + msg, dependency );
            }
            else
            {
                index.put( key, dependency );
            }
        }
    }

    private void validateEffectiveDependencies( ModelProblemCollector problems, List<Dependency> dependencies,
                                                boolean management, ModelBuildingRequest request )
    {
        Severity errOn30 = getSeverity( request, ModelBuildingRequest.VALIDATION_LEVEL_MAVEN_3_0 );

        String prefix = management ? "dependencyManagement.dependencies.dependency." : "dependencies.dependency.";

        for ( Dependency d : dependencies )
        {
            validateEffectiveDependency( problems, d, management, prefix, request );

            if ( request.getValidationLevel() >= ModelBuildingRequest.VALIDATION_LEVEL_MAVEN_2_0 )
            {
                validateBoolean( prefix + "optional", problems, errOn30, Version.V20, d.getOptional(),
                                 d.getManagementKey(), d );

                if ( !management )
                {
                    validateVersion( prefix + "version", problems, errOn30, Version.V20, d.getVersion(),
                                     d.getManagementKey(), d );

                    /*
                     * TODO: Extensions like Flex Mojos use custom scopes like "merged", "internal", "external", etc.
                     * In order to don't break backward-compat with those, only warn but don't error out.
                     */
                    validateEnum( prefix + "scope", problems, Severity.WARNING, Version.V20, d.getScope(),
                                  d.getManagementKey(), d, "provided", "compile", "runtime", "test", "system" );
                }
            }
        }
    }

    private void validate20EffectivePluginDependencies( ModelProblemCollector problems, Plugin plugin,
                                                        ModelBuildingRequest request )
    {
        List<Dependency> dependencies = plugin.getDependencies();

        if ( !dependencies.isEmpty() )
        {
            String prefix = "build.plugins.plugin[" + plugin.getKey() + "].dependencies.dependency.";

            Severity errOn30 = getSeverity( request, ModelBuildingRequest.VALIDATION_LEVEL_MAVEN_3_0 );

            for ( Dependency d : dependencies )
            {
                validateEffectiveDependency( problems, d, false, prefix, request );

                validateVersion( prefix + "version", problems, errOn30, Version.BASE, d.getVersion(),
                                 d.getManagementKey(), d );

                validateEnum( prefix + "scope", problems, errOn30, Version.BASE, d.getScope(), d.getManagementKey(), d,
                              "compile", "runtime", "system" );
            }
        }
    }

    private void validateEffectiveDependency( ModelProblemCollector problems, Dependency d, boolean management,
                                              String prefix, ModelBuildingRequest request )
    {
        validateId( prefix + "artifactId", problems, Severity.ERROR, Version.BASE, d.getArtifactId(),
                    d.getManagementKey(), d );

        validateId( prefix + "groupId", problems, Severity.ERROR, Version.BASE, d.getGroupId(), d.getManagementKey(),
                    d );

        if ( !management )
        {
            validateStringNotEmpty( prefix + "type", problems, Severity.ERROR, Version.BASE, d.getType(),
                                    d.getManagementKey(), d );

            validateDependencyVersion( problems, d, prefix );
        }

        if ( "system".equals( d.getScope() ) )
        {
            String systemPath = d.getSystemPath();

            if ( StringUtils.isEmpty( systemPath ) )
            {
                addViolation( problems, Severity.ERROR, Version.BASE, prefix + "systemPath", d.getManagementKey(),
                              "is missing.", d );
            }
            else
            {
                File sysFile = new File( systemPath );
                if ( !sysFile.isAbsolute() )
                {
                    addViolation( problems, Severity.ERROR, Version.BASE, prefix + "systemPath", d.getManagementKey(),
                                  "must specify an absolute path but is " + systemPath, d );
                }
                else if ( !sysFile.isFile() )
                {
                    String msg = "refers to a non-existing file " + sysFile.getAbsolutePath();
                    systemPath = systemPath.replace( '/', File.separatorChar ).replace( '\\', File.separatorChar );
                    String jdkHome =
                        request.getSystemProperties().getProperty( "java.home", "" ) + File.separator + "..";
                    if ( systemPath.startsWith( jdkHome ) )
                    {
                        msg += ". Please verify that you run Maven using a JDK and not just a JRE.";
                    }
                    addViolation( problems, Severity.WARNING, Version.BASE, prefix + "systemPath",
                                  d.getManagementKey(), msg, d );
                }
            }
        }
        else if ( StringUtils.isNotEmpty( d.getSystemPath() ) )
        {
            addViolation( problems, Severity.ERROR, Version.BASE, prefix + "systemPath", d.getManagementKey(),
                          "must be omitted." + " This field may only be specified for a dependency with system scope.",
                          d );
        }

        if ( request.getValidationLevel() >= ModelBuildingRequest.VALIDATION_LEVEL_MAVEN_2_0 )
        {
            for ( Exclusion exclusion : d.getExclusions() )
            {
                if ( request.getValidationLevel() < ModelBuildingRequest.VALIDATION_LEVEL_MAVEN_3_0 )
                {
                    validateId( prefix + "exclusions.exclusion.groupId", problems, Severity.WARNING, Version.V20,
                                exclusion.getGroupId(), d.getManagementKey(), exclusion );

                    validateId( prefix + "exclusions.exclusion.artifactId", problems, Severity.WARNING, Version.V20,
                                exclusion.getArtifactId(), d.getManagementKey(), exclusion );
                }
                else
                {
                    validateIdWithWildcards( prefix + "exclusions.exclusion.groupId", problems, Severity.WARNING,
                                             Version.V30, exclusion.getGroupId(), d.getManagementKey(), exclusion );

                    validateIdWithWildcards( prefix + "exclusions.exclusion.artifactId", problems, Severity.WARNING,
                                             Version.V30, exclusion.getArtifactId(), d.getManagementKey(), exclusion );
                }
            }
        }
    }

    /**
     * @since 3.2.4 
     */
    protected void validateDependencyVersion( ModelProblemCollector problems, Dependency d, String prefix )
    {
        validateStringNotEmpty( prefix + "version", problems, Severity.ERROR, Version.BASE, d.getVersion(),
                                d.getManagementKey(), d );
    }

    private void validateRawRepositories( ModelProblemCollector problems, List<Repository> repositories, String prefix,
                                       ModelBuildingRequest request )
    {
        Map<String, Repository> index = new HashMap<>();

        for ( Repository repository : repositories )
        {
            validateStringNotEmpty( prefix + ".id", problems, Severity.ERROR, Version.V20, repository.getId(),
                                    repository );

            validateStringNotEmpty( prefix + "[" + repository.getId() + "].url", problems, Severity.ERROR, Version.V20,
                                    repository.getUrl(), repository );

            String key = repository.getId();

            Repository existing = index.get( key );

            if ( existing != null )
            {
                Severity errOn30 = getSeverity( request, ModelBuildingRequest.VALIDATION_LEVEL_MAVEN_3_0 );

                addViolation( problems, errOn30, Version.V20, prefix + ".id", null,
                              "must be unique: " + repository.getId() + " -> " + existing.getUrl() + " vs "
                                  + repository.getUrl(), repository );
            }
            else
            {
                index.put( key, repository );
            }
        }
    }

    private void validate20EffectiveRepository( ModelProblemCollector problems, Repository repository, String prefix,
                                     ModelBuildingRequest request )
    {
        if ( repository != null )
        {
            Severity errOn31 = getSeverity( request, ModelBuildingRequest.VALIDATION_LEVEL_MAVEN_3_1 );

            validateBannedCharacters( prefix + ".id", problems, errOn31, Version.V20, repository.getId(), null,
                                      repository, ILLEGAL_REPO_ID_CHARS );

            if ( "local".equals( repository.getId() ) )
            {
                addViolation( problems, errOn31, Version.V20, prefix + ".id", null, "must not be 'local'"
                    + ", this identifier is reserved for the local repository"
                    + ", using it for other repositories will corrupt your repository metadata.", repository );
            }

            if ( "legacy".equals( repository.getLayout() ) )
            {
                addViolation( problems, Severity.WARNING, Version.V20, prefix + ".layout", repository.getId(),
                              "uses the unsupported value 'legacy', artifact resolution might fail.", repository );
            }
        }
    }

    private void validate20RawResources( ModelProblemCollector problems, List<Resource> resources, String prefix,
                                    ModelBuildingRequest request )
    {
        Severity errOn30 = getSeverity( request, ModelBuildingRequest.VALIDATION_LEVEL_MAVEN_3_0 );

        for ( Resource resource : resources )
        {
            validateStringNotEmpty( prefix + ".directory", problems, Severity.ERROR, Version.V20,
                                    resource.getDirectory(), resource );

            validateBoolean( prefix + ".filtering", problems, errOn30, Version.V20, resource.getFiltering(),
                             resource.getDirectory(), resource );
        }
    }

    // ----------------------------------------------------------------------
    // Field validation
    // ----------------------------------------------------------------------

    private boolean validateId( String fieldName, ModelProblemCollector problems, String id,
                                InputLocationTracker tracker )
    {
        return validateId( fieldName, problems, Severity.ERROR, Version.BASE, id, null, tracker );
    }

    private boolean validateId( String fieldName, ModelProblemCollector problems, Severity severity, Version version,
                                String id, String sourceHint, InputLocationTracker tracker )
    {
        if ( !validateStringNotEmpty( fieldName, problems, severity, version, id, sourceHint, tracker ) )
        {
            return false;
        }
        else
        {
            boolean match = ID_REGEX.matcher( id ).matches();
            if ( !match )
            {
                addViolation( problems, severity, version, fieldName, sourceHint, "with value '" + id
                    + "' does not match a valid id pattern.", tracker );
            }
            return match;
        }
    }

    private boolean validateIdWithWildcards( String fieldName, ModelProblemCollector problems, Severity severity,
                                             Version version, String id, String sourceHint,
                                             InputLocationTracker tracker )
    {
        if ( !validateStringNotEmpty( fieldName, problems, severity, version, id, sourceHint, tracker ) )
        {
            return false;
        }
        else
        {
            boolean match = ID_WITH_WILDCARDS_REGEX.matcher( id ).matches();
            if ( !match )
            {
                addViolation( problems, severity, version, fieldName, sourceHint, "with value '" + id
                    + "' does not match a valid id pattern.", tracker );
            }
            return match;
        }
    }


    private boolean validateStringNoExpression( String fieldName, ModelProblemCollector problems, Severity severity,
                                                Version version, String string, InputLocationTracker tracker )
    {
        if ( !hasExpression( string ) )
        {
            return true;
        }

        addViolation( problems, severity, version, fieldName, null, "contains an expression but should be a constant.",
                      tracker );

        return false;
    }

    private boolean validateVersionNoExpression( String fieldName, ModelProblemCollector problems, Severity severity,
                                                 Version version, String string, InputLocationTracker tracker )
    {

        if ( !hasExpression( string ) )
        {
            return true;
        }

        //
        // Acceptable versions for continuous delivery
        //
        // changelist
        // revision
        // sha1
        //
        if ( string.trim().contains( "${changelist}" ) || string.trim().contains( "${revision}" )
            || string.trim().contains( "${sha1}" ) )
        {
            return true;
        }

        addViolation( problems, severity, version, fieldName, null, "contains an expression but should be a constant.",
                      tracker );

        return false;
    }

    private boolean hasExpression( String value )
    {
        return value != null && value.contains( "${" );
    }

    private boolean hasProjectExpression( String value )
    {
        return value != null && value.contains( "${project." );
    }

    private boolean validateStringNotEmpty( String fieldName, ModelProblemCollector problems, Severity severity,
                                            Version version, String string, InputLocationTracker tracker )
    {
        return validateStringNotEmpty( fieldName, problems, severity, version, string, null, tracker );
    }

    /**
     * Asserts:
     * <p/>
     * <ul>
     * <li><code>string != null</code>
     * <li><code>string.length > 0</code>
     * </ul>
     */
    private boolean validateStringNotEmpty( String fieldName, ModelProblemCollector problems, Severity severity,
                                            Version version, String string, String sourceHint,
                                            InputLocationTracker tracker )
    {
        if ( !validateNotNull( fieldName, problems, severity, version, string, sourceHint, tracker ) )
        {
            return false;
        }

        if ( string.length() > 0 )
        {
            return true;
        }

        addViolation( problems, severity, version, fieldName, sourceHint, "is missing.", tracker );

        return false;
    }

    /**
     * Asserts:
     * <p/>
     * <ul>
     * <li><code>string != null</code>
     * </ul>
     */
    private boolean validateNotNull( String fieldName, ModelProblemCollector problems, Severity severity,
                                     Version version, Object object, String sourceHint, InputLocationTracker tracker )
    {
        if ( object != null )
        {
            return true;
        }

        addViolation( problems, severity, version, fieldName, sourceHint, "is missing.", tracker );

        return false;
    }

    private boolean validateBoolean( String fieldName, ModelProblemCollector problems, Severity severity,
                                     Version version, String string, String sourceHint, InputLocationTracker tracker )
    {
        if ( string == null || string.length() <= 0 )
        {
            return true;
        }

        if ( "true".equalsIgnoreCase( string ) || "false".equalsIgnoreCase( string ) )
        {
            return true;
        }

        addViolation( problems, severity, version, fieldName, sourceHint, "must be 'true' or 'false' but is '" + string
            + "'.", tracker );

        return false;
    }

    private boolean validateEnum( String fieldName, ModelProblemCollector problems, Severity severity, Version version,
                                  String string, String sourceHint, InputLocationTracker tracker,
                                  String... validValues )
    {
        if ( string == null || string.length() <= 0 )
        {
            return true;
        }

        List<String> values = Arrays.asList( validValues );

        if ( values.contains( string ) )
        {
            return true;
        }

        addViolation( problems, severity, version, fieldName, sourceHint, "must be one of " + values + " but is '"
            + string + "'.", tracker );

        return false;
    }

    private boolean validateBannedCharacters( String fieldName, ModelProblemCollector problems, Severity severity,
                                              Version version, String string, String sourceHint,
                                              InputLocationTracker tracker, String banned )
    {
        if ( string != null )
        {
            for ( int i = string.length() - 1; i >= 0; i-- )
            {
                if ( banned.indexOf( string.charAt( i ) ) >= 0 )
                {
                    addViolation( problems, severity, version, fieldName, sourceHint,
                                  "must not contain any of these characters " + banned + " but found "
                                      + string.charAt( i ), tracker );
                    return false;
                }
            }
        }

        return true;
    }

    private boolean validateVersion( String fieldName, ModelProblemCollector problems, Severity severity,
                                     Version version, String string, String sourceHint, InputLocationTracker tracker )
    {
        if ( string == null || string.length() <= 0 )
        {
            return true;
        }

        if ( hasExpression( string ) )
        {
            addViolation( problems, severity, version, fieldName, sourceHint,
                          "must be a valid version but is '" + string + "'.", tracker );
            return false;
        }

        return validateBannedCharacters( fieldName, problems, severity, version, string, sourceHint, tracker,
                                         ILLEGAL_VERSION_CHARS );

    }

    private boolean validate20ProperSnapshotVersion( String fieldName, ModelProblemCollector problems,
                                                     Severity severity, Version version, String string,
                                                     String sourceHint, InputLocationTracker tracker )
    {
        if ( string == null || string.length() <= 0 )
        {
            return true;
        }

        if ( string.endsWith( "SNAPSHOT" ) && !string.endsWith( "-SNAPSHOT" ) )
        {
            addViolation( problems, severity, version, fieldName, sourceHint,
                          "uses an unsupported snapshot version format, should be '*-SNAPSHOT' instead.", tracker );
            return false;
        }

        return true;
    }

    private boolean validate20PluginVersion( String fieldName, ModelProblemCollector problems, String string,
                                             String sourceHint, InputLocationTracker tracker,
                                             ModelBuildingRequest request )
    {
        if ( string == null )
        {
            // NOTE: The check for missing plugin versions is handled directly by the model builder
            return true;
        }

        Severity errOn30 = getSeverity( request, ModelBuildingRequest.VALIDATION_LEVEL_MAVEN_3_0 );

        if ( !validateVersion( fieldName, problems, errOn30, Version.V20, string, sourceHint, tracker ) )
        {
            return false;
        }

        if ( string.length() <= 0 || "RELEASE".equals( string ) || "LATEST".equals( string ) )
        {
            addViolation( problems, errOn30, Version.V20, fieldName, sourceHint, "must be a valid version but is '"
                + string + "'.", tracker );
            return false;
        }

        return true;
    }

    private static void addViolation( ModelProblemCollector problems, Severity severity, Version version,
                                      String fieldName, String sourceHint, String message,
                                      InputLocationTracker tracker )
    {
        StringBuilder buffer = new StringBuilder( 256 );
        buffer.append( '\'' ).append( fieldName ).append( '\'' );

        if ( sourceHint != null )
        {
            buffer.append( " for " ).append( sourceHint );
        }

        buffer.append( ' ' ).append( message );

        problems.add( new ModelProblemCollectorRequest( severity, version )
            .setMessage( buffer.toString() ).setLocation( getLocation( fieldName, tracker ) ) );
    }

    private static InputLocation getLocation( String fieldName, InputLocationTracker tracker )
    {
        InputLocation location = null;

        if ( tracker != null )
        {
            if ( fieldName != null )
            {
                Object key = fieldName;

                int idx = fieldName.lastIndexOf( '.' );
                if ( idx >= 0 )
                {
                    fieldName = fieldName.substring( idx + 1 );
                    key = fieldName;
                }

                if ( fieldName.endsWith( "]" ) )
                {
                    key = fieldName.substring( fieldName.lastIndexOf( '[' ) + 1, fieldName.length() - 1 );
                    try
                    {
                        key = Integer.valueOf( key.toString() );
                    }
                    catch ( NumberFormatException e )
                    {
                        // use key as is
                    }
                }

                location = tracker.getLocation( key );
            }

            if ( location == null )
            {
                location = tracker.getLocation( "" );
            }
        }

        return location;
    }

    private static boolean equals( String s1, String s2 )
    {
        return StringUtils.clean( s1 ).equals( StringUtils.clean( s2 ) );
    }

    private static Severity getSeverity( ModelBuildingRequest request, int errorThreshold )
    {
        return getSeverity( request.getValidationLevel(), errorThreshold );
    }

    private static Severity getSeverity( int validationLevel, int errorThreshold )
    {
        if ( validationLevel < errorThreshold )
        {
            return Severity.WARNING;
        }
        else
        {
            return Severity.ERROR;
        }
    }

}
