package org.carlspring.ioc.mock;

import org.carlspring.ioc.InjectionException;
import org.carlspring.ioc.PropertyValue;
import org.carlspring.ioc.PropertyValueInjector;

/**
 * @author mtodorov
 */
public class PropertyHolderWithoutPropertiesResource
{

    @PropertyValue(key = "jdbc.username")
    String username;

    // Let's have a private field in the parent class.
    @PropertyValue(key = "jdbc.password")
    private String password;


    public PropertyHolderWithoutPropertiesResource()
    {
    }

    public void init()
            throws InjectionException
    {
        PropertyValueInjector.inject(this);
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
