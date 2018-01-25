package org.carlspring.ioc.mock;

import org.carlspring.ioc.PropertiesResources;
import org.carlspring.ioc.PropertyValue;

/**
 * @author basdewitte
 */
@PropertiesResources(resources = { "app.properties" })
public class PropertyHolderUsingMultipleConfigLocations
{

    @PropertyValue(key = "jdbc.username")
    protected String dbUsername;

    @PropertyValue(key = "app.override")
    protected String appOverride;
    
    
    public String getDbUsername()
    {
        return dbUsername;
    }

    public void setDbUsername(String dbUsername)
    {
        this.dbUsername = dbUsername;
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
