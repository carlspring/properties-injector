package org.carlspring.ioc.mock;

import org.carlspring.ioc.PropertyValue;

/**
 * @author mtodorov
 */
public class ExtendedPropertyHolder
        extends PropertyHolder
{

    @PropertyValue(key = "jdbc.url")
    private String url;


    public ExtendedPropertyHolder()
    {
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

}

