package org.carlspring.ioc.mock;

import org.carlspring.ioc.PropertiesResources;
import org.carlspring.ioc.PropertyValue;

/**
 * @author mtodorov
 */
@PropertiesResources(resources = { "META-INF/properties/nosuch.properties" })
public class PropertyHolderWithMissingResource
{

    // Let's have a private field in the parent class.
    @PropertyValue(key = "jdbc.password")
    private String password;

    // Verify defaults is still injected when resource missing
    @PropertyValue(defaultValue = "anotatedDefault")
    protected String fromDefault;

    // Verify omitted defaults do not clear bean property value
    @PropertyValue(key = "retain.value")
    protected String retainValue = "retained";

    public PropertyHolderWithMissingResource()
    {
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getFromDefault()
    {
        return fromDefault;
    }

    public void setFromDefault(String fromDefault)
    {
        this.fromDefault = fromDefault;
    }

    public String getRetainValue()
    {
        return retainValue;
    }

    public void setRetainValue(String retainValue)
    {
        this.retainValue = retainValue;
    }

}
