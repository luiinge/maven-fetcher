// =================== DO NOT EDIT THIS FILE ====================
// Generated by Modello 2.0.0,
// any modifications will be overwritten.
// ==============================================================

package org.apache.maven.artifact.repository.metadata;

/**
 * Mapping information for a single plugin within this group
 * (deprecated).
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings( "all" )
@Deprecated
public class Plugin
    implements java.io.Serializable, Cloneable
{

      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Display name for the plugin.
     */
    private String name;

    /**
     * The plugin invocation prefix (i.e. eclipse for
     * eclipse:eclipse).
     */
    private String prefix;

    /**
     * The plugin artifactId.
     */
    private String artifactId;


      //-----------/
     //- Methods -/
    //-----------/

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

            return copy;
        }
        catch ( Exception ex )
        {
            throw (RuntimeException) new UnsupportedOperationException( getClass().getName()
                + " does not support clone()" ).initCause( ex );
        }
    } //-- Plugin clone()

    /**
     * Get the plugin artifactId.
     * 
     * @return String
     */
    public String getArtifactId()
    {
        return this.artifactId;
    } //-- String getArtifactId()

    /**
     * Get display name for the plugin.
     * 
     * @return String
     */
    public String getName()
    {
        return this.name;
    } //-- String getName()

    /**
     * Get the plugin invocation prefix (i.e. eclipse for
     * eclipse:eclipse).
     * 
     * @return String
     */
    public String getPrefix()
    {
        return this.prefix;
    } //-- String getPrefix()

    /**
     * Set the plugin artifactId.
     * 
     * @param artifactId a artifactId object.
     */
    public void setArtifactId( String artifactId )
    {
        this.artifactId = artifactId;
    } //-- void setArtifactId( String )

    /**
     * Set display name for the plugin.
     * 
     * @param name a name object.
     */
    public void setName( String name )
    {
        this.name = name;
    } //-- void setName( String )

    /**
     * Set the plugin invocation prefix (i.e. eclipse for
     * eclipse:eclipse).
     * 
     * @param prefix a prefix object.
     */
    public void setPrefix( String prefix )
    {
        this.prefix = prefix;
    } //-- void setPrefix( String )

}
