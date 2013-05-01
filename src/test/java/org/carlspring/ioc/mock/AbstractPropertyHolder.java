package org.carlspring.ioc.mock;

import org.carlspring.ioc.InjectionException;
import org.carlspring.ioc.PropertiesResources;
import org.carlspring.ioc.PropertyValue;
import org.carlspring.ioc.PropertyValueInjector;

/**
 * @author mtodorov
 */
@PropertiesResources(resources = {"META-INF/properties/jdbc.properties"})
public abstract class AbstractPropertyHolder
{

    @PropertyValue(key = "jdbc.username")
    protected String username;                          // Test protected modifier

    @PropertyValue(key = "jdbc.password")
    private String password;                            // Test private modifier

    @PropertyValue(key = "jdbc.url")
    static String url;                                  // Test static modifier

    // Let's have a private field in the parent class.
    @PropertyValue(key = "jdbc.version")
    String version;                                     // Test package local variable


    public AbstractPropertyHolder()
    {
    }

    public void init()
            throws InjectionException
    {
        PropertyValueInjector.inject(getImplementation());
    }

    public abstract Object getImplementation();

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

    public static String getUrl()
    {
        return url;
    }

    public static void setUrl(String url)
    {
        AbstractPropertyHolder.url = url;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

}
