// =================== DO NOT EDIT THIS FILE ====================
// Generated by Modello 2.0.0,
// any modifications will be overwritten.
// ==============================================================

package org.apache.maven.model;

/**
 * Section for management of default plugin information for use in
 * a group of POMs.
 *       
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings( "all" )
public class PluginManagement
    extends PluginContainer
    implements java.io.Serializable, Cloneable
{

      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method clone.
     * 
     * @return PluginManagement
     */
    public PluginManagement clone()
    {
        try
        {
            PluginManagement copy = (PluginManagement) super.clone();

            return copy;
        }
        catch ( Exception ex )
        {
            throw (RuntimeException) new UnsupportedOperationException( getClass().getName()
                + " does not support clone()" ).initCause( ex );
        }
    } //-- PluginManagement clone()

}
