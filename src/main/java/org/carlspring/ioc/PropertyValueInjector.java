package org.carlspring.ioc;

import java.io.FileInputStream;

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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.text.StrSubstitutor;

/**
 * @author mtodorov
 */
public class PropertyValueInjector
{
    static String CLASSPATH_IDENTIFIER_NAME = "classpath:";

    /**
     * Cached Properties per resourcename.
     */
    static Map<String, Properties> cachedProperties = new LinkedHashMap<String, Properties>();
    
    
    /**
     * Cached properties defined though annotation at class level.
     */
    static Properties classLevelProperties = new Properties();

    /**
     * Paths to look for property files in given order, overriding duplicates if any.    
     */
    static List<String> configLocations = null;

	static boolean resourceDoesNotExist;

    public static void inject(Object target)
            throws InjectionException
    {
        List<String> locations = Arrays.asList(CLASSPATH_IDENTIFIER_NAME);
        inject(target, locations); 
    }

    public static void inject(Object target, 
            List<String> locations)
            throws InjectionException
   
    {
        try
        {
            if (target instanceof Class)
            {
                throw new InjectionException("Incorrect parameter for injection. You should not use a class " +
                                             "representation, but rather -- the instance of the actual object.");
            }

            configLocations = expandConfigLocations(locations);
            
            Class clazz = target.getClass();
            cacheAllReferencedResources(clazz);
            
            injectProperties(target, clazz);
        }
        catch (IOException | IllegalAccessException e)
        {
            throw new InjectionException(e);
        }
    }

    private static void injectProperties(Object target,
                                         Class clazz)
            throws IOException, IllegalAccessException
    {
        final List<Field> fields = new ArrayList<Field>();

        getAllFields(fields, clazz);

        for (Field field : fields)
        {
            PropertyValue propertyValue = field.getAnnotation(PropertyValue.class);

            if (propertyValue != null)
            {
                String key = propertyValue.key();

                if (key.trim().equals(""))
                {
                    // use bean property name if property resource key omitted
                    // in annotation
                    key = field.getName();
                }

                String value;
                if (System.getProperty(key) != null)
                {
                    value = System.getProperty(key);
                }
                else
                {
                    value = getValue(key, propertyValue.resource());
                }

                if (value == null || value.trim().equals(""))
                {
                    value = propertyValue.defaultValue();

                    if ((value == null || value.trim().equals("")))
                    {
                        if (target != null)
                        {
                            // retain initialized bean property value if default
                            // value is omitted in annotation
                            value = null;
                        }
                    }
                }

                // inject value
                if (value != null)
                {
                    // Add this check, as some fields might already have a defined value and
                    // if there is no resolved property at this point, it would break things.
                    setField(field, target, value);
                }
            }
        }
    }

    public static List<Field> getAllFields(List<Field> fields,
                                           Class<?> clazz)
    {
        Collections.addAll(fields, clazz.getDeclaredFields());

        if (clazz.getSuperclass() != null)
        {
            fields = getAllFields(fields, clazz.getSuperclass());
        }

        return fields;
    }

    private static void cacheAllReferencedResources(Class clazz)
            throws IOException
    {
        // class level 
        final List<Annotation> annotations = new ArrayList<Annotation>();

        getAllAnnotations(annotations, clazz);

        for (Annotation annotation : annotations)
        {
            if (annotation instanceof PropertiesResources)
            {
                String[] resources = ((PropertiesResources) annotation).resources();
                for (String resource : resources)
                {
                    Properties properties = cachePropertyResource(resource);
                    cacheClassLevelProperties(properties);
                }
            }
        }

        // bean property level 
        final List<Field> fields = new ArrayList<Field>();

        getAllFields(fields, clazz);

        for (Field field : fields)
        {
            PropertyValue propertyValue = field.getAnnotation(PropertyValue.class);

            if (propertyValue != null)
            {
                cachePropertyResource(propertyValue.resource());
            }
        }
    }

    public static List<Annotation> getAllAnnotations(List<Annotation> annotations,
                                                     Class<?> clazz)
    {
        Collections.addAll(annotations, clazz.getAnnotations());

        if (clazz.getSuperclass() != null)
        {
            annotations = getAllAnnotations(annotations, clazz.getSuperclass());
        }

        return annotations;
    }

    private static void cacheClassLevelProperties(Properties properties)
    {
        for (Object o : properties.keySet())
        {
            String propertyKey = (String) o;
            classLevelProperties.setProperty(propertyKey, properties.getProperty(propertyKey)); 
        }
    }
    
    public static String getValue(String key,
                                  String resource)
            throws IOException
    {
        String value = null;
        
        if (resource.trim().equals(""))
        {
            value = classLevelProperties.getProperty(key);
        }
        else
        {
            Properties properties = cachedProperties.get(resource);
            
            if(properties != null)
            {
                value = properties.getProperty(key); 
            }
        }

        return value;
    }


    private static Properties cachePropertyResource(String resource)
            throws IOException
    {
        Properties properties = new Properties();
        
        if (!cachedProperties.containsKey(resource))
        {
            InputStream is = null;
            
            for (String location : configLocations)
            {
                try
                {
                    if (location.startsWith(CLASSPATH_IDENTIFIER_NAME))
                    {
                        String path = Paths.get(location.substring(CLASSPATH_IDENTIFIER_NAME.length()), resource).toString();
                        is = PropertyValueInjector.class.getClassLoader().getResourceAsStream(path);
                    }
                    else
                    {
                        Path path = Paths.get(location, resource); 
                        is = new FileInputStream(path.toFile());
                    }

                    properties.load(is);
                }
                catch (NullPointerException | IOException  ex)
                {
                    resourceDoesNotExist = true;
                }
                finally
                {
                    if (is != null)
                    {
                        is.close();
                    }
                }
            }            

            cachedProperties.put(resource, properties);
        }
        
        return properties;
    }

    
    private static void setField(Field field,
                                 Object target,
                                 Object value)
            throws IllegalAccessException
    {


        if (!Modifier.isPublic(field.getModifiers()))
        {
            field.setAccessible(true);
        }

        try
        {
            Class<?> targetType = field.getType();
            Object convertedValue = value;

            if (targetType.equals(Integer.class) || targetType.equals(Integer.TYPE))
            {
                convertedValue = Integer.parseInt(value.toString());
            }
            else if (targetType.equals(Long.class) || targetType.equals(Long.TYPE))
            {
                convertedValue = Long.parseLong(value.toString());
            }
            else if (targetType.equals(Float.class) || targetType.equals(Float.TYPE))
            {
                convertedValue = Float.parseFloat(value.toString());
            }
            else if (targetType.equals(Double.class) || targetType.equals(Double.TYPE))
            {
                convertedValue = Double.parseDouble(value.toString());
            }
            else if (targetType.equals(Boolean.class) || targetType.equals(Boolean.TYPE))
            {
                convertedValue = Boolean.parseBoolean(value.toString());
            }
            else if (targetType.equals(Character.class) || targetType.equals(Character.TYPE))
            {
                convertedValue = value.toString().charAt(0);
            }

            field.set(target, convertedValue);
        }
        catch (IllegalAccessException iae)
        {
            throw new IllegalArgumentException("Could not set field " + field, iae);        
        }
        catch (NumberFormatException iae)
        {
            throw new IllegalArgumentException("Could not set field " + field, iae);
        }
    }

    public boolean resourceDoesNotExist()
    {
        return resourceDoesNotExist;
    }

    private static List<String> expandConfigLocations(List<String> locations)
    {
        List<String> configLocations = new ArrayList<>();
        
        for (String location : locations)
        {
            String configLocation = StrSubstitutor.replaceSystemProperties(location);
            configLocations.add(configLocation);            
        }

        return configLocations;
    }
}
