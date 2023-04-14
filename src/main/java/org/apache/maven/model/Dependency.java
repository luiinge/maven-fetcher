// =================== DO NOT EDIT THIS FILE ====================
// Generated by Modello 2.0.0,
// any modifications will be overwritten.
// ==============================================================

package org.apache.maven.model;

/**
 * 
 *         
 *         The <code>&lt;dependency&gt;</code> element contains
 * information about a dependency
 *         of the project.
 *         
 *       
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings( "all" )
public class Dependency
    implements java.io.Serializable, Cloneable, InputLocationTracker
{

      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * 
     *             
     *             The project group that produced the dependency,
     * e.g.
     *             <code>org.apache.maven</code>.
     *             
     *           
     */
    private String groupId;

    /**
     * 
     *             
     *             The unique id for an artifact produced by the
     * project group, e.g.
     *             <code>maven-artifact</code>.
     *             
     *           
     */
    private String artifactId;

    /**
     * 
     *             
     *             The version of the dependency, e.g.
     * <code>3.2.1</code>. In Maven 2, this can also be
     *             specified as a range of versions.
     *             
     *           
     */
    private String version;

    /**
     * 
     *             
     *             The type of dependency, that will be mapped to a
     * file extension, an optional classifier, and a few other
     * attributes.
     *             Some examples are <code>jar</code>,
     * <code>war</code>, <code>ejb-client</code>
     *             and <code>test-jar</code>: see <a
     * href="../maven-core/artifact-handlers.html">default
     *             artifact handlers</a> for a list. New types can
     * be defined by extensions, so this is not a complete list.
     *             
     *           
     */
    private String type = "jar";

    /**
     * 
     *             
     *             The classifier of the dependency. It is appended
     * to
     *             the filename after the version. This allows:
     *             <ul>
     *             <li>referring to attached artifact, for example
     * <code>sources</code> and <code>javadoc</code>:
     *             see <a
     * href="../maven-core/artifact-handlers.html">default artifact
     * handlers</a> for a list,</li>
     *             <li>distinguishing two artifacts
     *             that belong to the same POM but were built
     * differently.
     *             For example, <code>jdk14</code> and
     * <code>jdk15</code>.</li>
     *             </ul>
     *             
     *           
     */
    private String classifier;

    /**
     * 
     *             
     *             The scope of the dependency -
     * <code>compile</code>, <code>runtime</code>,
     *             <code>test</code>, <code>system</code>, and
     * <code>provided</code>. Used to
     *             calculate the various classpaths used for
     * compilation, testing, and so on.
     *             It also assists in determining which artifacts
     * to include in a distribution of
     *             this project. For more information, see
     *             <a
     * href="https://maven.apache.org/guides/introduction/introduction-to-dependency-mechanism.html">the
     *             dependency mechanism</a>. The default scope is
     * <code>compile</code>.
     *             
     *           
     */
    private String scope;

    /**
     * 
     *             
     *             FOR SYSTEM SCOPE ONLY. Note that use of this
     * property is <b>discouraged</b>
     *             and may be replaced in later versions. This
     * specifies the path on the filesystem
     *             for this dependency.
     *             Requires an absolute path for the value, not
     * relative.
     *             Use a property that gives the machine specific
     * absolute path,
     *             e.g. <code>${java.home}</code>.
     *             
     *           
     */
    private String systemPath;

    /**
     * Field exclusions.
     */
    private java.util.List<Exclusion> exclusions;

    /**
     * 
     *             
     *             Indicates the dependency is optional for use of
     * this library. While the
     *             version of the dependency will be taken into
     * account for dependency calculation if the
     *             library is used elsewhere, it will not be passed
     * on transitively. Note: While the type
     *             of this field is <code>String</code> for
     * technical reasons, the semantic type is actually
     *             <code>Boolean</code>. Default value is
     * <code>false</code>.
     *             
     *           
     */
    private String optional;

    /**
     * Field locations.
     */
    private java.util.Map<Object, InputLocation> locations;

    /**
     * Field location.
     */
    private InputLocation location;

    /**
     * Field groupIdLocation.
     */
    private InputLocation groupIdLocation;

    /**
     * Field artifactIdLocation.
     */
    private InputLocation artifactIdLocation;

    /**
     * Field versionLocation.
     */
    private InputLocation versionLocation;

    /**
     * Field typeLocation.
     */
    private InputLocation typeLocation;

    /**
     * Field classifierLocation.
     */
    private InputLocation classifierLocation;

    /**
     * Field scopeLocation.
     */
    private InputLocation scopeLocation;

    /**
     * Field systemPathLocation.
     */
    private InputLocation systemPathLocation;

    /**
     * Field exclusionsLocation.
     */
    private InputLocation exclusionsLocation;

    /**
     * Field optionalLocation.
     */
    private InputLocation optionalLocation;


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addExclusion.
     * 
     * @param exclusion a exclusion object.
     */
    public void addExclusion( Exclusion exclusion )
    {
        getExclusions().add( exclusion );
    } //-- void addExclusion( Exclusion )

    /**
     * Method clone.
     * 
     * @return Dependency
     */
    public Dependency clone()
    {
        try
        {
            Dependency copy = (Dependency) super.clone();

            if ( this.exclusions != null )
            {
                copy.exclusions = new java.util.ArrayList<Exclusion>();
                for ( Exclusion item : this.exclusions )
                {
                    copy.exclusions.add( ( (Exclusion) item).clone() );
                }
            }

            if ( copy.locations != null )
            {
                copy.locations = new java.util.LinkedHashMap( copy.locations );
            }

            return copy;
        }
        catch ( Exception ex )
        {
            throw (RuntimeException) new UnsupportedOperationException( getClass().getName()
                + " does not support clone()" ).initCause( ex );
        }
    } //-- Dependency clone()

    /**
     * Get the unique id for an artifact produced by the project
     * group, e.g.
     *             <code>maven-artifact</code>.
     * 
     * @return String
     */
    public String getArtifactId()
    {
        return this.artifactId;
    } //-- String getArtifactId()

    /**
     * Get the classifier of the dependency. It is appended to
     *             the filename after the version. This allows:
     *             <ul>
     *             <li>referring to attached artifact, for example
     * <code>sources</code> and <code>javadoc</code>:
     *             see <a
     * href="../maven-core/artifact-handlers.html">default artifact
     * handlers</a> for a list,</li>
     *             <li>distinguishing two artifacts
     *             that belong to the same POM but were built
     * differently.
     *             For example, <code>jdk14</code> and
     * <code>jdk15</code>.</li>
     *             </ul>
     * 
     * @return String
     */
    public String getClassifier()
    {
        return this.classifier;
    } //-- String getClassifier()

    /**
     * Method getExclusions.
     * 
     * @return List
     */
    public java.util.List<Exclusion> getExclusions()
    {
        if ( this.exclusions == null )
        {
            this.exclusions = new java.util.ArrayList<Exclusion>();
        }

        return this.exclusions;
    } //-- java.util.List<Exclusion> getExclusions()

    /**
     * Get the project group that produced the dependency, e.g.
     *             <code>org.apache.maven</code>.
     * 
     * @return String
     */
    public String getGroupId()
    {
        return this.groupId;
    } //-- String getGroupId()

    /**
     * 
     * 
     * @param key a key object.
     * @return InputLocation
     */
    public InputLocation getLocation( Object key )
    {
        if ( key instanceof String )
        {
            switch ( ( String ) key )
            {
                case "" :
                {
                    return this.location;
                }
                case "groupId" :
                {
                    return groupIdLocation;
                }
                case "artifactId" :
                {
                    return artifactIdLocation;
                }
                case "version" :
                {
                    return versionLocation;
                }
                case "type" :
                {
                    return typeLocation;
                }
                case "classifier" :
                {
                    return classifierLocation;
                }
                case "scope" :
                {
                    return scopeLocation;
                }
                case "systemPath" :
                {
                    return systemPathLocation;
                }
                case "exclusions" :
                {
                    return exclusionsLocation;
                }
                case "optional" :
                {
                    return optionalLocation;
                }
                default :
                {
                    return getOtherLocation( key );
                }
                }
            }
            else
            {
                return getOtherLocation( key );
            }
    } //-- InputLocation getLocation( Object )

    /**
     * Get indicates the dependency is optional for use of this
     * library. While the
     *             version of the dependency will be taken into
     * account for dependency calculation if the
     *             library is used elsewhere, it will not be passed
     * on transitively. Note: While the type
     *             of this field is <code>String</code> for
     * technical reasons, the semantic type is actually
     *             <code>Boolean</code>. Default value is
     * <code>false</code>.
     * 
     * @return String
     */
    public String getOptional()
    {
        return this.optional;
    } //-- String getOptional()

    /**
     * 
     * 
     * @param key a key object.
     * @param location a location object.
     */
    public void setLocation( Object key, InputLocation location )
    {
        if ( key instanceof String )
        {
            switch ( ( String ) key )
            {
                case "" :
                {
                    this.location = location;
                    return;
                }
                case "groupId" :
                {
                    groupIdLocation = location;
                    return;
                }
                case "artifactId" :
                {
                    artifactIdLocation = location;
                    return;
                }
                case "version" :
                {
                    versionLocation = location;
                    return;
                }
                case "type" :
                {
                    typeLocation = location;
                    return;
                }
                case "classifier" :
                {
                    classifierLocation = location;
                    return;
                }
                case "scope" :
                {
                    scopeLocation = location;
                    return;
                }
                case "systemPath" :
                {
                    systemPathLocation = location;
                    return;
                }
                case "exclusions" :
                {
                    exclusionsLocation = location;
                    return;
                }
                case "optional" :
                {
                    optionalLocation = location;
                    return;
                }
                default :
                {
                    setOtherLocation( key, location );
                    return;
                }
            }
        }
        else
        {
            setOtherLocation( key, location );
        }
    } //-- void setLocation( Object, InputLocation )

    /**
     * 
     * 
     * @param key a key object.
     * @param location a location object.
     */
    public void setOtherLocation( Object key, InputLocation location )
    {
        if ( location != null )
        {
            if ( this.locations == null )
            {
                this.locations = new java.util.LinkedHashMap<Object, InputLocation>();
            }
            this.locations.put( key, location );
        }
    } //-- void setOtherLocation( Object, InputLocation )

    /**
     * 
     * 
     * @param key a key object.
     * @return InputLocation
     */
    private InputLocation getOtherLocation( Object key )
    {
        return ( locations != null ) ? locations.get( key ) : null;
    } //-- InputLocation getOtherLocation( Object )

    /**
     * Get the scope of the dependency - <code>compile</code>,
     * <code>runtime</code>,
     *             <code>test</code>, <code>system</code>, and
     * <code>provided</code>. Used to
     *             calculate the various classpaths used for
     * compilation, testing, and so on.
     *             It also assists in determining which artifacts
     * to include in a distribution of
     *             this project. For more information, see
     *             <a
     * href="https://maven.apache.org/guides/introduction/introduction-to-dependency-mechanism.html">the
     *             dependency mechanism</a>. The default scope is
     * <code>compile</code>.
     * 
     * @return String
     */
    public String getScope()
    {
        return this.scope;
    } //-- String getScope()

    /**
     * Get fOR SYSTEM SCOPE ONLY. Note that use of this property is
     * <b>discouraged</b>
     *             and may be replaced in later versions. This
     * specifies the path on the filesystem
     *             for this dependency.
     *             Requires an absolute path for the value, not
     * relative.
     *             Use a property that gives the machine specific
     * absolute path,
     *             e.g. <code>${java.home}</code>.
     * 
     * @return String
     */
    public String getSystemPath()
    {
        return this.systemPath;
    } //-- String getSystemPath()

    /**
     * Get the type of dependency, that will be mapped to a file
     * extension, an optional classifier, and a few other
     * attributes.
     *             Some examples are <code>jar</code>,
     * <code>war</code>, <code>ejb-client</code>
     *             and <code>test-jar</code>: see <a
     * href="../maven-core/artifact-handlers.html">default
     *             artifact handlers</a> for a list. New types can
     * be defined by extensions, so this is not a complete list.
     * 
     * @return String
     */
    public String getType()
    {
        return this.type;
    } //-- String getType()

    /**
     * Get the version of the dependency, e.g. <code>3.2.1</code>.
     * In Maven 2, this can also be
     *             specified as a range of versions.
     * 
     * @return String
     */
    public String getVersion()
    {
        return this.version;
    } //-- String getVersion()

    /**
     * Method removeExclusion.
     * 
     * @param exclusion a exclusion object.
     */
    public void removeExclusion( Exclusion exclusion )
    {
        getExclusions().remove( exclusion );
    } //-- void removeExclusion( Exclusion )

    /**
     * Set the unique id for an artifact produced by the project
     * group, e.g.
     *             <code>maven-artifact</code>.
     * 
     * @param artifactId a artifactId object.
     */
    public void setArtifactId( String artifactId )
    {
        this.artifactId = artifactId;
    } //-- void setArtifactId( String )

    /**
     * Set the classifier of the dependency. It is appended to
     *             the filename after the version. This allows:
     *             <ul>
     *             <li>referring to attached artifact, for example
     * <code>sources</code> and <code>javadoc</code>:
     *             see <a
     * href="../maven-core/artifact-handlers.html">default artifact
     * handlers</a> for a list,</li>
     *             <li>distinguishing two artifacts
     *             that belong to the same POM but were built
     * differently.
     *             For example, <code>jdk14</code> and
     * <code>jdk15</code>.</li>
     *             </ul>
     * 
     * @param classifier a classifier object.
     */
    public void setClassifier( String classifier )
    {
        this.classifier = classifier;
    } //-- void setClassifier( String )

    /**
     * Set lists a set of artifacts that should be excluded from
     * this dependency's
     *             artifact list when it comes to calculating
     * transitive dependencies.
     * 
     * @param exclusions a exclusions object.
     */
    public void setExclusions( java.util.List<Exclusion> exclusions )
    {
        this.exclusions = exclusions;
    } //-- void setExclusions( java.util.List )

    /**
     * Set the project group that produced the dependency, e.g.
     *             <code>org.apache.maven</code>.
     * 
     * @param groupId a groupId object.
     */
    public void setGroupId( String groupId )
    {
        this.groupId = groupId;
    } //-- void setGroupId( String )

    /**
     * Set indicates the dependency is optional for use of this
     * library. While the
     *             version of the dependency will be taken into
     * account for dependency calculation if the
     *             library is used elsewhere, it will not be passed
     * on transitively. Note: While the type
     *             of this field is <code>String</code> for
     * technical reasons, the semantic type is actually
     *             <code>Boolean</code>. Default value is
     * <code>false</code>.
     * 
     * @param optional a optional object.
     */
    public void setOptional( String optional )
    {
        this.optional = optional;
    } //-- void setOptional( String )

    /**
     * Set the scope of the dependency - <code>compile</code>,
     * <code>runtime</code>,
     *             <code>test</code>, <code>system</code>, and
     * <code>provided</code>. Used to
     *             calculate the various classpaths used for
     * compilation, testing, and so on.
     *             It also assists in determining which artifacts
     * to include in a distribution of
     *             this project. For more information, see
     *             <a
     * href="https://maven.apache.org/guides/introduction/introduction-to-dependency-mechanism.html">the
     *             dependency mechanism</a>. The default scope is
     * <code>compile</code>.
     * 
     * @param scope a scope object.
     */
    public void setScope( String scope )
    {
        this.scope = scope;
    } //-- void setScope( String )

    /**
     * Set fOR SYSTEM SCOPE ONLY. Note that use of this property is
     * <b>discouraged</b>
     *             and may be replaced in later versions. This
     * specifies the path on the filesystem
     *             for this dependency.
     *             Requires an absolute path for the value, not
     * relative.
     *             Use a property that gives the machine specific
     * absolute path,
     *             e.g. <code>${java.home}</code>.
     * 
     * @param systemPath a systemPath object.
     */
    public void setSystemPath( String systemPath )
    {
        this.systemPath = systemPath;
    } //-- void setSystemPath( String )

    /**
     * Set the type of dependency, that will be mapped to a file
     * extension, an optional classifier, and a few other
     * attributes.
     *             Some examples are <code>jar</code>,
     * <code>war</code>, <code>ejb-client</code>
     *             and <code>test-jar</code>: see <a
     * href="../maven-core/artifact-handlers.html">default
     *             artifact handlers</a> for a list. New types can
     * be defined by extensions, so this is not a complete list.
     * 
     * @param type a type object.
     */
    public void setType( String type )
    {
        this.type = type;
    } //-- void setType( String )

    /**
     * Set the version of the dependency, e.g. <code>3.2.1</code>.
     * In Maven 2, this can also be
     *             specified as a range of versions.
     * 
     * @param version a version object.
     */
    public void setVersion( String version )
    {
        this.version = version;
    } //-- void setVersion( String )

    
            
    public boolean isOptional()
    {
        return ( optional != null ) ? Boolean.parseBoolean( optional ) : false;
    }

    public void setOptional( boolean optional )
    {
        this.optional = String.valueOf( optional );
    }

    /**
     * @see Object#toString()
     */
    public String toString()
    {
        return "Dependency {groupId=" + groupId + ", artifactId=" + artifactId + ", version=" + version + ", type=" + type + "}";
    }
            
          
    
            
    private String managementKey;

    /**
     * @return the management key as <code>groupId:artifactId:type</code>
     */
    public String getManagementKey()
    {
        if ( managementKey == null )
        {
            managementKey = groupId + ":" + artifactId + ":" + type + ( classifier != null ? ":" + classifier : "" );
        }
        return managementKey;
    }

    /**
     * Clears the management key in case one field has been modified.
     */
    public void clearManagementKey()
    {
        managementKey = null;
    }
            
          
}
