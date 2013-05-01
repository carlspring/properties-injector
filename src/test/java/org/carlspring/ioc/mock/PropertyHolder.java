package org.carlspring.ioc.mock;

import org.carlspring.ioc.PropertiesResources;
import org.carlspring.ioc.PropertyValue;

/**
 * @author mtodorov
 */
@PropertiesResources(resources = {"META-INF/properties/jdbc.properties"})
public class PropertyHolder
{

    @PropertyValue(key = "jdbc.username")
    String username;

    // Let's have a private field in the parent class.
    @PropertyValue(key = "jdbc.password")
    private String password;

    @PropertyValue(key = "")
    String blah;


    public PropertyHolder()
    {
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

}
