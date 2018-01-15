package org.carlspring.ioc.mock;

import org.carlspring.ioc.PropertiesResources;
import org.carlspring.ioc.PropertyValue;

/**
 * @author basdewitte
 */
@PropertiesResources(resources = { "META-INF/properties/app.properties", "META-INF/properties/appxtd.properties" })
public class PropertyHolderUsingMultipleResouces
{

    @PropertyValue(key = "jdbc.username", resource = "META-INF/properties/public.properties")
    protected String dbPublicUsername;

    @PropertyValue(key = "jdbc.username", resource = "META-INF/properties/private.properties")
    protected String dbPrivateUsername;

    @PropertyValue(key = "jdbc.username")
    protected String dbUsername;

    @PropertyValue(key = "xtd.username")
    protected String xtdUsername;

    @PropertyValue(key = "app.override")
    protected String appOverride;
    
    
    public String getDbPublicUsername()
    {
        return dbPublicUsername;
    }

    public void setDbPublicUsername(String dbPublicUsername)
    {
        this.dbPublicUsername = dbPublicUsername;
    }

    public String getDbPrivateUsername()
    {
        return dbPrivateUsername;
    }

    public void setDbPrivateUsername(String dbPrivateUsername)
    {
        this.dbPrivateUsername = dbPrivateUsername;
    }

    public String getDbUsername()
    {
        return dbUsername;
    }

    public void setDbUsername(String dbUsername)
    {
        this.dbUsername = dbUsername;
    }

    public String getXtdUsername()
    {
        return xtdUsername;
    }

    public void setXtdUsername(String xtdUsername)
    {
        this.xtdUsername = xtdUsername;
    }

    public String getAppOverride()
    {
        return appOverride;
    }

    public void setAppOverride(String appOverride)
    {
        this.appOverride = appOverride;
    }

}
