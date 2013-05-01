package org.carlspring.ioc.mock;

import org.carlspring.ioc.PropertiesResources;
import org.carlspring.ioc.PropertyValue;

/**
 * @author mtodorov
 */
@PropertiesResources(resources = {"META-INF/properties/jdbc.properties"})
public class ClassExtendingAbstractPropertyHolder extends AbstractPropertyHolder
{

    @PropertyValue(key = "jdbc.autocommit")
    boolean autocommit;


    public ClassExtendingAbstractPropertyHolder()
    {
    }

    public boolean isAutocommit()
    {
        return autocommit;
    }

    public void setAutocommit(boolean autocommit)
    {
        this.autocommit = autocommit;
    }

    @Override
    public Object getImplementation()
    {
        return this;
    }

}
