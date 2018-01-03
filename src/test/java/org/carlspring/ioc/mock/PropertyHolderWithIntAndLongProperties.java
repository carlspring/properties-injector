package org.carlspring.ioc.mock;

import org.carlspring.ioc.PropertiesResources;
import org.carlspring.ioc.PropertyValue;

/**
 * @author basdewitte 
 */

@PropertiesResources(resources = {"META-INF/properties/typed.properties"})
public class PropertyHolderWithIntAndLongProperties {
	
    @PropertyValue(key = "prim.int")
	protected int primInt;

    @PropertyValue(key = "prim.long")
	protected long primLong;

    @PropertyValue(key = "prim.double")
    protected double primDouble;
    
    @PropertyValue(key = "prim.float")
    protected float primFloat; 

    @PropertyValue(key = "prim.bool")
    protected boolean primBool;
    
    @PropertyValue(key = "java.int")
    protected Integer javaInt;

    @PropertyValue(key = "java.long")
	protected Long javaLong;
    
    @PropertyValue(key = "java.double")
    protected Double javaDouble; 

    @PropertyValue(key = "java.float")
    protected Float javaFloat; 

    @PropertyValue(key = "java.bool")
    protected Boolean javaBool;

	public int getPrimInt() {
		return primInt;
	}

	public void setPrimInt(int primInt) {
		this.primInt = primInt;
	}

	public long getPrimLong() {
		return primLong;
	}

	public void setPrimLong(long primLong) {
		this.primLong = primLong;
	}

	public double getPrimDouble() {
		return primDouble;
	}

	public void setPrimDouble(double primDouble) {
		this.primDouble = primDouble;
	}

	public float getPrimFloat() {
		return primFloat;
	}

	public void setPrimFloat(float primFloat) {
		this.primFloat = primFloat;
	}

	public boolean isPrimBool() {
		return primBool;
	}

	public void setPrimBool(boolean primBool) {
		this.primBool = primBool;
	}

	public Integer getJavaInt() {
		return javaInt;
	}

	public void setJavaInt(Integer javaInt) {
		this.javaInt = javaInt;
	}

	public Long getJavaLong() {
		return javaLong;
	}

	public void setJavaLong(Long javaLong) {
		this.javaLong = javaLong;
	}

	public Double getJavaDouble() {
		return javaDouble;
	}

	public void setJavaDouble(Double javaDouble) {
		this.javaDouble = javaDouble;
	}

	public Float getJavaFloat() {
		return javaFloat;
	}

	public void setJavaFloat(Float javaFloat) {
		this.javaFloat = javaFloat;
	}

	public Boolean getJavaBool() {
		return javaBool;
	}

	public void setJavaBool(Boolean javaBool) {
		this.javaBool = javaBool;
	} 
    
    
}
