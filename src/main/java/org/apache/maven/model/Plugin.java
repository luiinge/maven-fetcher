// =================== DO NOT EDIT THIS FILE ====================
// Generated by Modello 2.0.0,
// any modifications will be overwritten.
// ==============================================================

package org.apache.maven.model;

/**
 * 
 *         
 *         The <code>&lt;plugin&gt;</code> element contains
 * informations required for a plugin.
 *         
 *       
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings( "all" )
public class Plugin
    extends ConfigurationContainer
    implements java.io.Serializable, Cloneable
{

      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The group ID of the plugin in the repository.
     */
    private String groupId = "org.apache.maven.plugins";

    /**
     * The artifact ID of the plugin in the repository.
     */
    private String artifactId;

    /**
     * The version (or valid range of versions) of the plugin to be
     * used.
     */
    private String version;

    /**
     * 
     *             
     *             Whether to load Maven extensions (such as
     * packaging and type handlers) from
     *             this plugin. For performance reasons, this
     * should only be enabled when necessary. Note: While the type
     *             of this field is <code>String</code> for
     * technical reasons, the semantic type is actually
     *             <code>Boolean</code>. Default value is
     * <code>false</code>.
     *             
     *           
     */
    private String extensions;

    /**
     * Field executions.
     */
    private java.util.List<PluginExecution> executions;

    /**
     * Field dependencies.
     */
    private java.util.List<Dependency> dependencies;

    /**
     * 
     *             
     *             <b>Deprecated</b>. Unused by Maven.
     *             
     *           
     */
    private Object goals;


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addDependency.
     * 
     * @param dependency a dependency object.
     */
    public void addDependency( Dependency dependency )
    {
        getDependencies().add( dependency );
    } //-- void addDependency( Dependency )

    /**
     * Method addExecution.
     * 
     * @param pluginExecution a pluginExecution object.
     */
    public void addExecution( PluginExecution pluginExecution )
    {
        getExecutions().add( pluginExecution );
    } //-- void addExecution( PluginExecution )

    /**
     * Method clone.
     * 
     * @return Plugin
     */
    public Plugin clone()
    {
        try
        {
            Plugin copy = (Plugin) super.clone();

            if ( this.executions != null )
            {
                copy.executions = new java.util.ArrayList<PluginExecution>();
                for ( PluginExecution item : this.executions )
                {
                    copy.executions.add( ( (PluginExecution) item).clone() );
                }
            }

            if ( this.dependencies != null )
            {
                copy.dependencies = new java.util.ArrayList<Dependency>();
                for ( Dependency item : this.dependencies )
                {
                    copy.dependencies.add( ( (Dependency) item).clone() );
                }
            }

            if ( this.goals != null )
            {
                copy.goals = new org.codehaus.plexus.util.xml.Xpp3Dom( (org.codehaus.plexus.util.xml.Xpp3Dom) this.goals );
            }

            return copy;
        }
        catch ( Exception ex )
        {
            throw (RuntimeException) new UnsupportedOperationException( getClass().getName()
                + " does not support clone()" ).initCause( ex );
        }
    } //-- Plugin clone()

    /**
     * Get the artifact ID of the plugin in the repository.
     * 
     * @return String
     */
    public String getArtifactId()
    {
        return this.artifactId;
    } //-- String getArtifactId()

    /**
     * Method getDependencies.
     * 
     * @return List
     */
    public java.util.List<Dependency> getDependencies()
    {
        if ( this.dependencies == null )
        {
            this.dependencies = new java.util.ArrayList<Dependency>();
        }

        return this.dependencies;
    } //-- java.util.List<Dependency> getDependencies()

    /**
     * Method getExecutions.
     * 
     * @return List
     */
    public java.util.List<PluginExecution> getExecutions()
    {
        if ( this.executions == null )
        {
            this.executions = new java.util.ArrayList<PluginExecution>();
        }

        return this.executions;
    } //-- java.util.List<PluginExecution> getExecutions()

    /**
     * Get whether to load Maven extensions (such as packaging and
     * type handlers) from
     *             this plugin. For performance reasons, this
     * should only be enabled when necessary. Note: While the type
     *             of this field is <code>String</code> for
     * technical reasons, the semantic type is actually
     *             <code>Boolean</code>. Default value is
     * <code>false</code>.
     * 
     * @return String
     */
    public String getExtensions()
    {
        return this.extensions;
    } //-- String getExtensions()

    /**
     * Get <b>Deprecated</b>. Unused by Maven.
     * 
     * @return Object
     */
    public Object getGoals()
    {
        return this.goals;
    } //-- Object getGoals()

    /**
     * Get the group ID of the plugin in the repository.
     * 
     * @return String
     */
    public String getGroupId()
    {
        return this.groupId;
    } //-- String getGroupId()

    /**
     * Get the version (or valid range of versions) of the plugin
     * to be used.
     * 
     * @return String
     */
    public String getVersion()
    {
        return this.version;
    } //-- String getVersion()

    /**
     * Method removeDependency.
     * 
     * @param dependency a dependency object.
     */
    public void removeDependency( Dependency dependency )
    {
        getDependencies().remove( dependency );
    } //-- void removeDependency( Dependency )

    /**
     * Method removeExecution.
     * 
     * @param pluginExecution a pluginExecution object.
     */
    public void removeExecution( PluginExecution pluginExecution )
    {
        getExecutions().remove( pluginExecution );
    } //-- void removeExecution( PluginExecution )

    /**
     * Set the artifact ID of the plugin in the repository.
     * 
     * @param artifactId a artifactId object.
     */
    public void setArtifactId( String artifactId )
    {
        this.artifactId = artifactId;
    } //-- void setArtifactId( String )

    /**
     * Set additional dependencies that this project needs to
     * introduce to the plugin's
     *             classloader.
     * 
     * @param dependencies a dependencies object.
     */
    public void setDependencies( java.util.List<Dependency> dependencies )
    {
        this.dependencies = dependencies;
    } //-- void setDependencies( java.util.List )

    /**
     * Set multiple specifications of a set of goals to execute
     * during the build
     *             lifecycle, each having (possibly) a different
     * configuration.
     * 
     * @param executions a executions object.
     */
    public void setExecutions( java.util.List<PluginExecution> executions )
    {
        this.executions = executions;
    } //-- void setExecutions( java.util.List )

    /**
     * Set whether to load Maven extensions (such as packaging and
     * type handlers) from
     *             this plugin. For performance reasons, this
     * should only be enabled when necessary. Note: While the type
     *             of this field is <code>String</code> for
     * technical reasons, the semantic type is actually
     *             <code>Boolean</code>. Default value is
     * <code>false</code>.
     * 
     * @param extensions a extensions object.
     */
    public void setExtensions( String extensions )
    {
        this.extensions = extensions;
    } //-- void setExtensions( String )

    /**
     * Set <b>Deprecated</b>. Unused by Maven.
     * 
     * @param goals a goals object.
     */
    public void setGoals( Object goals )
    {
        this.goals = goals;
    } //-- void setGoals( Object )

    /**
     * Set the group ID of the plugin in the repository.
     * 
     * @param groupId a groupId object.
     */
    public void setGroupId( String groupId )
    {
        this.groupId = groupId;
    } //-- void setGroupId( String )

    /**
     * Set the version (or valid range of versions) of the plugin
     * to be used.
     * 
     * @param version a version object.
     */
    public void setVersion( String version )
    {
        this.version = version;
    } //-- void setVersion( String )

    
            
    public boolean isExtensions()
    {
        return ( extensions != null ) ? Boolean.parseBoolean( extensions ) : false;
    }

    public void setExtensions( boolean extensions )
    {
        this.extensions = String.valueOf( extensions );
    }

    private java.util.Map<String, PluginExecution> executionMap = null;

    /**
     * Reset the <code>executionMap</code> field to <code>null</code>
     */
    public void flushExecutionMap()
    {
        this.executionMap = null;
    }

    /**
     * @return a Map of executions field with <code>PluginExecution#getId()</code> as key
     * @see PluginExecution#getId()
     */
    public java.util.Map<String, PluginExecution> getExecutionsAsMap()
    {
        if ( executionMap == null )
        {
            executionMap = new java.util.LinkedHashMap<String, PluginExecution>();
            if ( getExecutions() != null )
            {
                for ( java.util.Iterator<PluginExecution> i = getExecutions().iterator(); i.hasNext(); )
                {
                    PluginExecution exec = (PluginExecution) i.next();

                    if ( executionMap.containsKey( exec.getId() ) )
                    {
                        throw new IllegalStateException( "You cannot have two plugin executions with the same (or missing) <id/> elements.\nOffending execution\n\nId: \'" + exec.getId() + "\'\nPlugin:\'" + getKey() + "\'\n\n" );
                    }

                    executionMap.put( exec.getId(), exec );
                }
            }
        }

        return executionMap;
    }

    /**
     * Gets the identifier of the plugin.
     *
     * @return The plugin id in the form {@code <groupId>:<artifactId>:<version>}, never {@code null}.
     */
    public String getId()
    {
        StringBuilder id = new StringBuilder( 128 );

        id.append( ( getGroupId() == null ) ? "[unknown-group-id]" : getGroupId() );
        id.append( ":" );
        id.append( ( getArtifactId() == null ) ? "[unknown-artifact-id]" : getArtifactId() );
        id.append( ":" );
        id.append( ( getVersion() == null ) ? "[unknown-version]" : getVersion() );

        return id.toString();
    }

    /**
     * @return the key of the plugin, ie <code>groupId:artifactId</code>
     */
    public String getKey()
    {
        return constructKey( groupId, artifactId );
    }

    /**
     * @param groupId The group ID of the plugin in the repository
     * @param artifactId The artifact ID of the reporting plugin in the repository
     * @return the key of the plugin, ie <code>groupId:artifactId</code>
     */
    public static String constructKey( String groupId, String artifactId )
    {
        return groupId + ":" + artifactId;
    }

    /**
     * @see Object#equals(Object)
     */
    public boolean equals( Object other )
    {
        if ( other instanceof Plugin )
        {
            Plugin otherPlugin = (Plugin) other;

            return getKey().equals( otherPlugin.getKey() );
        }

        return false;
    }

    /**
     * @see Object#hashCode()
     */
    public int hashCode()
    {
        return getKey().hashCode();
    }

    /**
     * @see Object#toString()
     */
    public String toString()
    {
        return "Plugin [" + getKey() + "]";
    }
            
          
}
