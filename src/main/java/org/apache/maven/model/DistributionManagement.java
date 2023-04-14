// =================== DO NOT EDIT THIS FILE ====================
// Generated by Modello 2.0.0,
// any modifications will be overwritten.
// ==============================================================

package org.apache.maven.model;

/**
 * This elements describes all that pertains to distribution for a
 * project. It is
 *         primarily used for deployment of artifacts and the site
 * produced by the build.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings( "all" )
public class DistributionManagement
    implements java.io.Serializable, Cloneable, InputLocationTracker
{

      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Information needed to deploy the artifacts generated by the
     * project to a
     *             remote repository.
     */
    private DeploymentRepository repository;

    /**
     * 
     *             
     *             Where to deploy snapshots of artifacts to. If
     * not given, it defaults to the
     *             <code>repository</code> element.
     *             
     *           
     */
    private DeploymentRepository snapshotRepository;

    /**
     * Information needed for deploying the web site of the project.
     */
    private Site site;

    /**
     * 
     *             
     *             The URL of the project's download page. If not
     * given users will be
     *             referred to the homepage given by
     * <code>url</code>.
     *             This is given to assist in locating artifacts
     * that are not in the repository due to
     *             licensing restrictions.
     *             
     *           
     */
    private String downloadUrl;

    /**
     * Relocation information of the artifact if it has been moved
     * to a new group ID
     *             and/or artifact ID.
     */
    private Relocation relocation;

    /**
     * 
     *             
     *             Gives the status of this artifact in the remote
     * repository.
     *             This must not be set in your local project, as
     * it is updated by
     *             tools placing it in the reposiory. Valid values
     * are: <code>none</code> (default),
     *             <code>converted</code> (repository manager
     * converted this from an Maven 1 POM),
     *             <code>partner</code>
     *             (directly synced from a partner Maven 2
     * repository), <code>deployed</code> (was deployed from a
     * Maven 2
     *             instance), <code>verified</code> (has been hand
     * verified as correct and final).
     *             
     *           
     */
    private String status;

    /**
     * Field locations.
     */
    private java.util.Map<Object, InputLocation> locations;

    /**
     * Field location.
     */
    private InputLocation location;

    /**
     * Field repositoryLocation.
     */
    private InputLocation repositoryLocation;

    /**
     * Field snapshotRepositoryLocation.
     */
    private InputLocation snapshotRepositoryLocation;

    /**
     * Field siteLocation.
     */
    private InputLocation siteLocation;

    /**
     * Field downloadUrlLocation.
     */
    private InputLocation downloadUrlLocation;

    /**
     * Field relocationLocation.
     */
    private InputLocation relocationLocation;

    /**
     * Field statusLocation.
     */
    private InputLocation statusLocation;


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method clone.
     * 
     * @return DistributionManagement
     */
    public DistributionManagement clone()
    {
        try
        {
            DistributionManagement copy = (DistributionManagement) super.clone();

            if ( this.repository != null )
            {
                copy.repository = (DeploymentRepository) this.repository.clone();
            }

            if ( this.snapshotRepository != null )
            {
                copy.snapshotRepository = (DeploymentRepository) this.snapshotRepository.clone();
            }

            if ( this.site != null )
            {
                copy.site = (Site) this.site.clone();
            }

            if ( this.relocation != null )
            {
                copy.relocation = (Relocation) this.relocation.clone();
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
    } //-- DistributionManagement clone()

    /**
     * Get the URL of the project's download page. If not given
     * users will be
     *             referred to the homepage given by
     * <code>url</code>.
     *             This is given to assist in locating artifacts
     * that are not in the repository due to
     *             licensing restrictions.
     * 
     * @return String
     */
    public String getDownloadUrl()
    {
        return this.downloadUrl;
    } //-- String getDownloadUrl()

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
                case "repository" :
                {
                    return repositoryLocation;
                }
                case "snapshotRepository" :
                {
                    return snapshotRepositoryLocation;
                }
                case "site" :
                {
                    return siteLocation;
                }
                case "downloadUrl" :
                {
                    return downloadUrlLocation;
                }
                case "relocation" :
                {
                    return relocationLocation;
                }
                case "status" :
                {
                    return statusLocation;
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
                case "repository" :
                {
                    repositoryLocation = location;
                    return;
                }
                case "snapshotRepository" :
                {
                    snapshotRepositoryLocation = location;
                    return;
                }
                case "site" :
                {
                    siteLocation = location;
                    return;
                }
                case "downloadUrl" :
                {
                    downloadUrlLocation = location;
                    return;
                }
                case "relocation" :
                {
                    relocationLocation = location;
                    return;
                }
                case "status" :
                {
                    statusLocation = location;
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
     * Get relocation information of the artifact if it has been
     * moved to a new group ID
     *             and/or artifact ID.
     * 
     * @return Relocation
     */
    public Relocation getRelocation()
    {
        return this.relocation;
    } //-- Relocation getRelocation()

    /**
     * Get information needed to deploy the artifacts generated by
     * the project to a
     *             remote repository.
     * 
     * @return DeploymentRepository
     */
    public DeploymentRepository getRepository()
    {
        return this.repository;
    } //-- DeploymentRepository getRepository()

    /**
     * Get information needed for deploying the web site of the
     * project.
     * 
     * @return Site
     */
    public Site getSite()
    {
        return this.site;
    } //-- Site getSite()

    /**
     * Get where to deploy snapshots of artifacts to. If not given,
     * it defaults to the
     *             <code>repository</code> element.
     * 
     * @return DeploymentRepository
     */
    public DeploymentRepository getSnapshotRepository()
    {
        return this.snapshotRepository;
    } //-- DeploymentRepository getSnapshotRepository()

    /**
     * Get gives the status of this artifact in the remote
     * repository.
     *             This must not be set in your local project, as
     * it is updated by
     *             tools placing it in the reposiory. Valid values
     * are: <code>none</code> (default),
     *             <code>converted</code> (repository manager
     * converted this from an Maven 1 POM),
     *             <code>partner</code>
     *             (directly synced from a partner Maven 2
     * repository), <code>deployed</code> (was deployed from a
     * Maven 2
     *             instance), <code>verified</code> (has been hand
     * verified as correct and final).
     * 
     * @return String
     */
    public String getStatus()
    {
        return this.status;
    } //-- String getStatus()

    /**
     * Set the URL of the project's download page. If not given
     * users will be
     *             referred to the homepage given by
     * <code>url</code>.
     *             This is given to assist in locating artifacts
     * that are not in the repository due to
     *             licensing restrictions.
     * 
     * @param downloadUrl a downloadUrl object.
     */
    public void setDownloadUrl( String downloadUrl )
    {
        this.downloadUrl = downloadUrl;
    } //-- void setDownloadUrl( String )

    /**
     * Set relocation information of the artifact if it has been
     * moved to a new group ID
     *             and/or artifact ID.
     * 
     * @param relocation a relocation object.
     */
    public void setRelocation( Relocation relocation )
    {
        this.relocation = relocation;
    } //-- void setRelocation( Relocation )

    /**
     * Set information needed to deploy the artifacts generated by
     * the project to a
     *             remote repository.
     * 
     * @param repository a repository object.
     */
    public void setRepository( DeploymentRepository repository )
    {
        this.repository = repository;
    } //-- void setRepository( DeploymentRepository )

    /**
     * Set information needed for deploying the web site of the
     * project.
     * 
     * @param site a site object.
     */
    public void setSite( Site site )
    {
        this.site = site;
    } //-- void setSite( Site )

    /**
     * Set where to deploy snapshots of artifacts to. If not given,
     * it defaults to the
     *             <code>repository</code> element.
     * 
     * @param snapshotRepository a snapshotRepository object.
     */
    public void setSnapshotRepository( DeploymentRepository snapshotRepository )
    {
        this.snapshotRepository = snapshotRepository;
    } //-- void setSnapshotRepository( DeploymentRepository )

    /**
     * Set gives the status of this artifact in the remote
     * repository.
     *             This must not be set in your local project, as
     * it is updated by
     *             tools placing it in the reposiory. Valid values
     * are: <code>none</code> (default),
     *             <code>converted</code> (repository manager
     * converted this from an Maven 1 POM),
     *             <code>partner</code>
     *             (directly synced from a partner Maven 2
     * repository), <code>deployed</code> (was deployed from a
     * Maven 2
     *             instance), <code>verified</code> (has been hand
     * verified as correct and final).
     * 
     * @param status a status object.
     */
    public void setStatus( String status )
    {
        this.status = status;
    } //-- void setStatus( String )

}
