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

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;

/**
 * @author mtodorov
 */
public class PropertyValueInjectionTest
{


    @Test
    public void testInjection()
            throws IOException
    {
        PropertyHolder holder = new PropertyHolder();

        PropertyValueInjector.inject(holder);

        assertNotNull("Failed to inject property 'jdbc.username'!", holder.getUsername());
        assertNotNull("Failed to inject property 'jdbc.password'!", holder.getPassword());

        System.out.println("Username: " + holder.getUsername());
        System.out.println("Password: " + holder.getPassword());
    }

    @PropertiesResources(resources = { "META-INF/properties/jdbc.properties" })
    private class PropertyHolder
    {
        @PropertyValue(key = "jdbc.username")
        String username;

        @PropertyValue(key = "jdbc.password")
        String password;

        @PropertyValue(key = "")
        String blah;


        private PropertyHolder()
        {
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

}
