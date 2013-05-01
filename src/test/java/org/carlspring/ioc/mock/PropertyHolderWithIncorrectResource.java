package org.carlspring.ioc.mock;

import org.carlspring.ioc.PropertiesResources;
import org.carlspring.ioc.PropertyValue;

/**
 * @author mtodorov
 */
@PropertiesResources(resources = {"META-INF/properties/incorrect.properties"})
public class PropertyHolderWithIncorrectResource
{

    // Let's have a private field in the parent class.
    @PropertyValue(key = "jdbc.password")
    private String password;


    public PropertyHolderWithIncorrectResource()
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

}

