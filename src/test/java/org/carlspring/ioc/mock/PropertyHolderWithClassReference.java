package org.carlspring.ioc.mock;

/**
 * @author mtodorov
 */
public class PropertyHolderWithClassReference extends AbstractPropertyHolder
{

    @Override
    public Object getImplementation()
    {
        return getClass();
    }

}
