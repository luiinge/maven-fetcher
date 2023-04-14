// =================== DO NOT EDIT THIS FILE ====================
// Generated by Modello 2.0.0,
// any modifications will be overwritten.
// ==============================================================

package org.apache.maven.model;

/**
 * 
 *         
 *         The <code>&lt;parent&gt;</code> element contains
 * information required to locate the parent project from which
 *         this project will inherit from.
 *         <strong>Note:</strong> The children of this element are
 * not interpolated and must be given as literal values.
 *         
 *       
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings( "all" )
public class Parent
    implements java.io.Serializable, Cloneable, InputLocationTracker
{

      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The group id of the parent project to inherit from.
     */
    private String groupId;

    /**
     * The artifact id of the parent project to inherit from.
     */
    private String artifactId;

    /**
     * The version of the parent project to inherit.
     */
    private String version;

    /**
     * 
     *             
     *             The relative path of the parent
     * <code>pom.xml</code> file within the check out.
     *             If not specified, it defaults to
     * <code>../pom.xml</code>.
     *             Maven looks for the parent POM first in this
     * location on
     *             the filesystem, then the local repository, and
     * lastly in the remote repo.
     *             <code>relativePath</code> allows you to select a
     * different location,
     *             for example when your structure is flat, or
     * deeper without an intermediate parent POM.
     *             However, the group ID, artifact ID and version
     * are still required,
     *             and must match the file in the location given or
     * it will revert to the repository for the POM.
     *             This feature is only for enhancing the
     * development in a local checkout of that project.
     *             Set the value to an empty string in case you
     * want to disable the feature and always resolve
     *             the parent POM from the repositories.
     *             
     *           
     */
    private String relativePath = "../pom.xml";

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
     * Field relativePathLocation.
     */
    private InputLocation relativePathLocation;


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method clone.
     * 
     * @return Parent
     */
    public Parent clone()
    {
        try
        {
            Parent copy = (Parent) super.clone();

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
    } //-- Parent clone()

    /**
     * Get the artifact id of the parent project to inherit from.
     * 
     * @return String
     */
    public String getArtifactId()
    {
        return this.artifactId;
    } //-- String getArtifactId()

    /**
     * Get the group id of the parent project to inherit from.
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
                case "relativePath" :
                {
                    return relativePathLocation;
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
                case "relativePath" :
                {
                    relativePathLocation = location;
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
     * Get the relative path of the parent <code>pom.xml</code>
     * file within the check out.
     *             If not specified, it defaults to
     * <code>../pom.xml</code>.
     *             Maven looks for the parent POM first in this
     * location on
     *             the filesystem, then the local repository, and
     * lastly in the remote repo.
     *             <code>relativePath</code> allows you to select a
     * different location,
     *             for example when your structure is flat, or
     * deeper without an intermediate parent POM.
     *             However, the group ID, artifact ID and version
     * are still required,
     *             and must match the file in the location given or
     * it will revert to the repository for the POM.
     *             This feature is only for enhancing the
     * development in a local checkout of that project.
     *             Set the value to an empty string in case you
     * want to disable the feature and always resolve
     *             the parent POM from the repositories.
     * 
     * @return String
     */
    public String getRelativePath()
    {
        return this.relativePath;
    } //-- String getRelativePath()

    /**
     * Get the version of the parent project to inherit.
     * 
     * @return String
     */
    public String getVersion()
    {
        return this.version;
    } //-- String getVersion()

    /**
     * Set the artifact id of the parent project to inherit from.
     * 
     * @param artifactId a artifactId object.
     */
    public void setArtifactId( String artifactId )
    {
        this.artifactId = artifactId;
    } //-- void setArtifactId( String )

    /**
     * Set the group id of the parent project to inherit from.
     * 
     * @param groupId a groupId object.
     */
    public void setGroupId( String groupId )
    {
        this.groupId = groupId;
    } //-- void setGroupId( String )

    /**
     * Set the relative path of the parent <code>pom.xml</code>
     * file within the check out.
     *             If not specified, it defaults to
     * <code>../pom.xml</code>.
     *             Maven looks for the parent POM first in this
     * location on
     *             the filesystem, then the local repository, and
     * lastly in the remote repo.
     *             <code>relativePath</code> allows you to select a
     * different location,
     *             for example when your structure is flat, or
     * deeper without an intermediate parent POM.
     *             However, the group ID, artifact ID and version
     * are still required,
     *             and must match the file in the location given or
     * it will revert to the repository for the POM.
     *             This feature is only for enhancing the
     * development in a local checkout of that project.
     *             Set the value to an empty string in case you
     * want to disable the feature and always resolve
     *             the parent POM from the repositories.
     * 
     * @param relativePath a relativePath object.
     */
    public void setRelativePath( String relativePath )
    {
        this.relativePath = relativePath;
    } //-- void setRelativePath( String )

    /**
     * Set the version of the parent project to inherit.
     * 
     * @param version a version object.
     */
    public void setVersion( String version )
    {
        this.version = version;
    } //-- void setVersion( String )

    
            
    /**
     * @return the id as <code>groupId:artifactId:version</code>
     */
    public String getId()
    {
        StringBuilder id = new StringBuilder( 64 );

        id.append( getGroupId() );
        id.append( ":" );
        id.append( getArtifactId() );
        id.append( ":" );
        id.append( "pom" );
        id.append( ":" );
        id.append( getVersion() );

        return id.toString();
    }

    @Override
    public String toString()
    {
        return getId();
    }
            
          
}
