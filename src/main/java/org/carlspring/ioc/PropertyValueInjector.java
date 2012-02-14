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

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author mtodorov
 */
public class PropertyValueInjector
{

    /**
     * A list of cached properties.
     */
    static Map<String, Properties> cachedProperties = new LinkedHashMap<String, Properties>();


    public static void inject(Object target)
            throws IOException
    {
        Class clazz = target.getClass();

        loadPropertyResources(clazz);

        injectProperties(target, clazz);
    }

    private static void injectProperties(Object target,
                                         Class clazz)
            throws IOException
    {
        for (Field field : clazz.getDeclaredFields())
        {
            PropertyValue propertyValue = field.getAnnotation(PropertyValue.class);

            if (propertyValue != null)
            {
                String key = propertyValue.key();

                if (key.trim().equals(""))
                {
                    // Put a warning here

                    System.out.println("WARN: Ignoring @PropertyValue annotation on field '" + field.getName() +
                                       "' in class '" +clazz.getName() + "' as it has an empty key.");

                    continue;
                }

                String value;
                if (propertyValue.resource().trim().equals(""))
                {
                    value = getMergedProperties().getProperty(key);
                }
                else
                {
                    value = getValue(key, propertyValue.resource());
                }

                setField(field, target, value);
            }
        }
    }

    private static void loadPropertyResources(Class clazz)
            throws IOException
    {
        Annotation[] annotations = clazz.getAnnotations();

        for (Annotation annotation : annotations)
        {
            if (annotation instanceof PropertiesResources)
            {
                loadProperties(((PropertiesResources) annotation).resources());
            }
        }
    }

    public static Properties getMergedProperties()
    {
        Properties merged = new Properties();

        for (String key : cachedProperties.keySet())
        {
            Properties properties = cachedProperties.get(key);

            for (Object o : properties.keySet())
            {
                String propertyKey = (String) o;
                merged.put(propertyKey, properties.getProperty(propertyKey));
            }
        }

        return merged;
    }

    public static String getValue(String key,
                                  String resource)
            throws IOException
    {
        final Properties properties = getProperties(resource);

        return properties.getProperty(key);
    }

    public static Properties getProperties(String resource)
            throws IOException
    {
        Properties properties;
        if (!cachedProperties.containsKey(resource))
        {
            loadProperties(resource);
        }

        properties = cachedProperties.get(resource);

        return properties;
    }

    private static void loadProperties(String[] resources)
            throws IOException
    {
        for (String resource : resources)
        {
            loadProperties(resource);
        }
    }

    private static void loadProperties(String resource)
            throws IOException
    {
        if (!cachedProperties.containsKey(resource))
        {
            Properties properties = new Properties();
            InputStream is = null;
            try
            {
                is = PropertyValueInjector.class.getClassLoader().getResourceAsStream(resource);
                properties.load(is);
            }
            finally
            {
                if (is != null)
                {
                    is.close();
                }
            }

            cachedProperties.put(resource, properties);
        }
    }

    private static void setField(Field field,
                                 Object target,
                                 Object value)
    {
        if (!Modifier.isPublic(field.getModifiers()))
        {
            field.setAccessible(true);
        }

        try
        {
            field.set(target, value);
        }
        catch (IllegalAccessException iae)
        {
            throw new IllegalArgumentException("Could not set field " + field, iae);
        }
    }

}
