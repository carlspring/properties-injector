package org.carlspring.ioc;

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

import org.carlspring.ioc.mock.*;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

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
        System.out.println("Testing class with incorrect resource...");

        System.getProperties().setProperty("jdbc.password", "mypassw0rd");

        PropertyHolderWithIncorrectResource holder = new PropertyHolderWithIncorrectResource();

        PropertyValueInjector.inject(holder);

        assertNull("Should have failed to inject property 'jdbc.password'!", holder.getPassword());
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

}
