// =================== DO NOT EDIT THIS FILE ====================
// Generated by Modello 2.0.0,
// any modifications will be overwritten.
// ==============================================================

package org.apache.maven.artifact.repository.metadata;

/**
 * Snapshot data for the last artifact corresponding to the
 * SNAPSHOT base version.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings( "all" )
public class Snapshot
    implements java.io.Serializable, Cloneable
{

      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The timestamp when this version was deployed. The timestamp
     * is expressed using UTC in the format yyyyMMdd.HHmmss.
     */
    private String timestamp;

    /**
     * The incremental build number.
     */
    private int buildNumber = 0;

    /**
     * Whether to use a local copy instead (with filename that
     * includes the base version).
     */
    private boolean localCopy = false;


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method clone.
     * 
     * @return Snapshot
     */
    public Snapshot clone()
    {
        try
        {
            Snapshot copy = (Snapshot) super.clone();

            return copy;
        }
        catch ( Exception ex )
        {
            throw (RuntimeException) new UnsupportedOperationException( getClass().getName()
                + " does not support clone()" ).initCause( ex );
        }
    } //-- Snapshot clone()

    /**
     * Get the incremental build number.
     * 
     * @return int
     */
    public int getBuildNumber()
    {
        return this.buildNumber;
    } //-- int getBuildNumber()

    /**
     * Get the timestamp when this version was deployed. The
     * timestamp is expressed using UTC in the format
     * yyyyMMdd.HHmmss.
     * 
     * @return String
     */
    public String getTimestamp()
    {
        return this.timestamp;
    } //-- String getTimestamp()

    /**
     * Get whether to use a local copy instead (with filename that
     * includes the base version).
     * 
     * @return boolean
     */
    public boolean isLocalCopy()
    {
        return this.localCopy;
    } //-- boolean isLocalCopy()

    /**
     * Set the incremental build number.
     * 
     * @param buildNumber a buildNumber object.
     */
    public void setBuildNumber( int buildNumber )
    {
        this.buildNumber = buildNumber;
    } //-- void setBuildNumber( int )

    /**
     * Set whether to use a local copy instead (with filename that
     * includes the base version).
     * 
     * @param localCopy a localCopy object.
     */
    public void setLocalCopy( boolean localCopy )
    {
        this.localCopy = localCopy;
    } //-- void setLocalCopy( boolean )

    /**
     * Set the timestamp when this version was deployed. The
     * timestamp is expressed using UTC in the format
     * yyyyMMdd.HHmmss.
     * 
     * @param timestamp a timestamp object.
     */
    public void setTimestamp( String timestamp )
    {
        this.timestamp = timestamp;
    } //-- void setTimestamp( String )

}
