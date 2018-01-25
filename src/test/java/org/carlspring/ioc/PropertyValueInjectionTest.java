package org.carlspring.ioc;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * Copyright 2012 Martin Todorov.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import org.carlspring.ioc.mock.ClassExtendingAbstractPropertyHolder;
import org.carlspring.ioc.mock.ExtendedPropertyHolder;
import org.carlspring.ioc.mock.PropertyHolder;
import org.carlspring.ioc.mock.PropertyHolderUsingMultipleConfigLocations;
import org.carlspring.ioc.mock.PropertyHolderUsingMultipleResouces;
import org.carlspring.ioc.mock.PropertyHolderWithClassReference;
import org.carlspring.ioc.mock.PropertyHolderWithIntAndLongProperties;
import org.carlspring.ioc.mock.PropertyHolderWithMissingResource;
import org.carlspring.ioc.mock.PropertyHolderWithoutPropertiesResource;
import org.junit.Before;
import org.junit.Test;

/**
 * @author mtodorov
 */
public class PropertyValueInjectionTest
{

    @Before
    public void setUp()
            throws Exception
    {
        PropertyValueInjector.resourceDoesNotExist = false;
    }

    @Test
    public void testInjectionNoParents()
            throws InjectionException
    {
        System.out.println("Testing class without inheritance...");

        PropertyHolder holder = new PropertyHolder();

        PropertyValueInjector.inject(holder);

        assertNotNull("Failed to inject property 'jdbc.username'!", holder.getUsername());
        assertNotNull("Failed to inject property 'jdbc.password'!", holder.getPassword());

        System.out.println("Username: " + holder.getUsername());
        System.out.println("Password: " + holder.getPassword());
    }

    @Test
    public void testInjectionFromParents()
            throws InjectionException
    {
        System.out.println("Testing class with inheritance...");

        ExtendedPropertyHolder holder = new ExtendedPropertyHolder();

        PropertyValueInjector.inject(holder);

        assertNotNull("Failed to inject property 'jdbc.username'!", holder.getUsername());
        assertNotNull("Failed to inject property 'jdbc.password'!", holder.getPassword());
        assertNotNull("Failed to inject property 'jdbc.url'!", holder.getUrl());

        System.out.println("Username: " + holder.getUsername());
        System.out.println("Password: " + holder.getPassword());
        System.out.println("URL:      " + holder.getUrl());
    }

    @Test
    public void testInjectionOverrideFromSystemProperties()
            throws InjectionException
    {
        System.out.println("Testing class with inheritance...");

        System.getProperties().setProperty("jdbc.password", "mypassw0rd");

        ExtendedPropertyHolder holder = new ExtendedPropertyHolder();

        PropertyValueInjector.inject(holder);

        assertNotNull("Failed to inject property 'jdbc.username'!", holder.getUsername());
        assertNotNull("Failed to inject property 'jdbc.password'!", holder.getPassword());
        assertNotNull("Failed to inject property 'jdbc.url'!", holder.getUrl());
        assertEquals("Failed to override property with system value!", "mypassw0rd", holder.getPassword());

        System.out.println("Username: " + holder.getUsername());
        System.out.println("Password: " + holder.getPassword());
        System.out.println("URL:      " + holder.getUrl());
    }

    @Test
    public void testInjectionWithIncorrectResource()
            throws InjectionException
    {
        System.out.println("Testing class with missing resource...");

        System.getProperties().setProperty("jdbc.password", "mypassw0rd");

        PropertyHolderWithMissingResource holder = new PropertyHolderWithMissingResource();

        PropertyValueInjector.inject(holder);

        assertNotNull("Should have injected system property value for property 'jdbc.password'!", holder.getPassword());
        assertNotNull("Should have injected default value for bean property 'fromDefault'!", holder.getFromDefault());
        assertEquals("Should have retained value for bean property 'retainValue'!", "retained", holder.getRetainValue());
    }

    @Test
    public void testInjectionWithClassExtendingAbstractClass()
            throws InjectionException
    {
        System.out.println("Testing class extending abstract class...");

        ClassExtendingAbstractPropertyHolder holder = new ClassExtendingAbstractPropertyHolder();
        holder.init();

        assertNotNull("Failed to inject property 'jdbc.username'!", holder.getUsername());
        assertNotNull("Failed to inject property 'jdbc.password'!", holder.getPassword());
        assertNotNull("Failed to inject property 'jdbc.url'!", holder.getUrl());
        assertNotNull("Failed to inject property 'jdbc.version'!", holder.getVersion());
        assertNotNull("Failed to inject property 'jdbc.autocommit'!", holder.isAutocommit());

        System.out.println("Username:    " + holder.getUsername());
        System.out.println("Password:    " + holder.getPassword());
        System.out.println("URL:         " + holder.getUrl());
        System.out.println("Version:     " + holder.getVersion());
        System.out.println("Auto-commit: " + holder.isAutocommit());
    }

    @Test
    public void testInjectionWithClassExtendingAbstractClassAndPassingClassReference()
            throws InjectionException
    {
        System.out.println("Testing class extending abstract class by passing a class reference...");

        PropertyHolderWithClassReference holder = new PropertyHolderWithClassReference();

        try
        {
            holder.init();

            fail("Incorrect usage of method should have thrown an error.");
        }
        catch (InjectionException e)
        {
            // Expected
            System.out.println("Failed as expected.");
        }
    }

    @Test
    public void testInjectionForPropertyHolderWithoutPropertiesResource()
            throws InjectionException
    {
        System.out.println("Testing property injection for a class that doesn't have a @PropertiesResource annotation...");

        PropertyHolderWithoutPropertiesResource holder = new PropertyHolderWithoutPropertiesResource();

        System.setProperty("jdbc.username", "admin");
        System.setProperty("jdbc.password", "password");

        holder.init();

        System.getProperties().remove("jdbc.username");
        System.getProperties().remove("jdbc.password");

        assertEquals("Failed to inject property based on system property!", "admin", holder.getUsername());
        assertEquals("Failed to inject property based on system property!", "password", holder.getPassword());

        System.out.println("jdbc.username: " + holder.getUsername());
        System.out.println("jdbc.password: " + holder.getPassword());
    }

    @Test
    public void testDefaultValueInjection()
            throws InjectionException
    {
        PropertyHolder holder = new PropertyHolder();
        
        PropertyValueInjector.inject(holder);

        assertEquals("Failed to fallback to defaultValue!", "postgresql", holder.getDialect());
    }

    @Test
    public void testInjectionMultiTyped()
            throws InjectionException
    {
        System.out.println("Testing class with integer and long properties...");

        PropertyHolderWithIntAndLongProperties holder = new PropertyHolderWithIntAndLongProperties();
        
        PropertyValueInjector.inject(holder);

        assertEquals("Failed to inject property 'prim.int'!", (int) 1, holder.getPrimitiveInteger());
        assertEquals("Failed to inject property 'java.int'!", new Integer(2), holder.getJavaInteger());

        assertEquals("Failed to inject property 'prim.long'!", (long) 3, holder.getPrimitiveLong());
        assertEquals("Failed to inject property 'java.long'!", new Long(4), holder.getJavaLong());

        assertEquals("Failed to inject property 'prim.double'!", (double) 5.5, holder.getPrimitiveDouble(), 0);
        assertEquals("Failed to inject property 'java.double'!", new Double(6.6), holder.getJavaDouble(), 0);

        assertEquals("Failed to inject property 'prim.bool'!", (boolean) true, holder.isPrimitiveBoolean());
        assertEquals("Failed to inject property 'java.bool'!", new Boolean(true), holder.isPrimitiveBoolean());
    }

    @Test
    public void testInjectionForPropertyHolderUsingMultipleResources()
            throws InjectionException
    {
        System.out.println("Testing injecting properties from multiple resources...");
        
        PropertyHolderUsingMultipleResouces holder = new PropertyHolderUsingMultipleResouces();
        
        PropertyValueInjector.inject(holder);

        assertEquals("Failed to correctly inject value for bean property 'dbUsername", "dbUsername", holder.getDbUsername());
        assertEquals("Failed to correctly inject value for bean property 'dbPublicUsername", "dbPublicUsername", holder.getDbPublicUsername());
        assertEquals("Failed to correctly inject value for bean property 'dbPrivateUsername", "dbPrivateUsername", holder.getDbPrivateUsername());
        assertEquals("Failed to correctly inject value for bean property 'xtdUsername", "xtdUsername", holder.getXtdUsername());
        assertEquals("Failed to correctly inject value for bean property 'appOverride", "overridden", holder.getAppOverride());
    }
    
    @Test
    public void testInjectionForPropertyHolderUsingMultipleConfigLocations()
            throws InjectionException, IOException, URISyntaxException
    {
        final String resource = "/conf/app.properties";

        System.out.println("Testing injecting properties from resources at multiple locations...");

        // get path to an external location such as /opt/myapp/conf
        // as this is inconvenient for this unit test use class path 
        // directory 'other' to simulate the external location
        
        URL url = getClass().getClassLoader().getResource("other" + resource);
        String path = Paths.get(url.toURI()).toString();
        String configDirectory = path.substring(0, path.length() - resource.length());

        // set system variable to the config location
        System.setProperty("INJECTOR_HOME", configDirectory);

        // read resource from class path first, then override with values from external location 
        List<String> configLocations = Arrays.asList("classpath:META-INF/properties/", "${INJECTOR_HOME}/conf/");

        // inject
        PropertyHolderUsingMultipleConfigLocations holder = new PropertyHolderUsingMultipleConfigLocations();
        
        PropertyValueInjector.inject(holder, configLocations);
        
        assertEquals("Failed to correctly inject value for bean property 'dbUsername", "dbUsername", holder.getDbUsername());
        assertEquals("Failed to correctly inject value for bean property 'appOverride", "externalizedResourceValue", holder.getAppOverride());
    }
    
}
