package org.carlspring.ioc.mock;

import org.carlspring.ioc.PropertiesResources;
import org.carlspring.ioc.PropertyValue;

/**
 * @author basdewitte
 */
@PropertiesResources(resources = { "META-INF/properties/typed.properties" })
public class PropertyHolderWithIntAndLongProperties
{

    @PropertyValue(key = "prim.int")
    protected int primitiveInteger;

    @PropertyValue(key = "prim.long")
    protected long primitiveLong;

    @PropertyValue(key = "prim.double")
    protected double primitiveDouble;

    @PropertyValue(key = "prim.float")
    protected float primitiveFloat;

    @PropertyValue(key = "prim.bool")
    protected boolean primitiveBoolean;

    @PropertyValue(key = "java.int")
    protected Integer javaInteger;

    @PropertyValue(key = "java.long")
    protected Long javaLong;

    @PropertyValue(key = "java.double")
    protected Double javaDouble;

    @PropertyValue(key = "java.float")
    protected Float javaFloat;

    @PropertyValue(key = "java.bool")
    protected Boolean javaBoolean;

    public int getPrimitiveInteger()
    {
        return primitiveInteger;
    }

    public void setPrimitiveInteger(int primitiveInteger)
    {
        this.primitiveInteger = primitiveInteger;
    }

    public long getPrimitiveLong()
    {
        return primitiveLong;
    }

    public void setPrimitiveLong(long primitiveLong)
    {
        this.primitiveLong = primitiveLong;
    }

    public double getPrimitiveDouble()
    {
        return primitiveDouble;
    }

    public void setPrimitiveDouble(double primitiveDouble)
    {
        this.primitiveDouble = primitiveDouble;
    }

    public float getPrimitiveFloat()
    {
        return primitiveFloat;
    }

    public void setPrimitiveFloat(float primitiveFloat)
    {
        this.primitiveFloat = primitiveFloat;
    }

    public boolean isPrimitiveBoolean()
    {
        return primitiveBoolean;
    }

    public void setPrimitiveBoolean(boolean primitiveBoolean)
    {
        this.primitiveBoolean = primitiveBoolean;
    }

    public Integer getJavaInteger()
    {
        return javaInteger;
    }

    public void setJavaInteger(Integer javaInteger)
    {
        this.javaInteger = javaInteger;
    }

    public Long getJavaLong()
    {
        return javaLong;
    }

    public void setJavaLong(Long javaLong)
    {
        this.javaLong = javaLong;
    }

    public Double getJavaDouble()
    {
        return javaDouble;
    }

    public void setJavaDouble(Double javaDouble)
    {
        this.javaDouble = javaDouble;
    }

    public Float getJavaFloat()
    {
        return javaFloat;
    }

    public void setJavaFloat(Float javaFloat)
    {
        this.javaFloat = javaFloat;
    }

    public Boolean getJavaBoolean()
    {
        return javaBoolean;
    }

    public void setJavaBoolean(Boolean javaBoolean)
    {
        this.javaBoolean = javaBoolean;
    }

}
