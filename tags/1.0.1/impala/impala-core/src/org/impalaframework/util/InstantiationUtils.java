/*
 * Copyright 2007-2010 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.impalaframework.util;

import java.lang.reflect.Constructor;

import org.impalaframework.exception.ExecutionException;
import org.impalaframework.exception.InvalidStateException;
import org.springframework.util.ClassUtils;

public class InstantiationUtils {

    @SuppressWarnings("unchecked")
    public static <T extends Object> T instantiate(String className) {
        return (T) instantiate(className, ClassUtils.getDefaultClassLoader());
    }   
    
    public static Object instantiate(String className, ClassLoader classLoader) {
        Class<?> clazz = null;
        try {
            clazz = org.springframework.util.ClassUtils.forName(className, classLoader);
        }
        catch (ClassNotFoundException e) {
            throw new ExecutionException("Unable to find class of type '" + className + "'");
        }

        Object o = null;
        try {
            
            Constructor<?> constructor = ReflectionUtils.findConstructor(clazz, new Class[0]);
            if (constructor == null) {
                throw new InvalidStateException("Cannot instantiate class '" + clazz + "' as it has no no-args constructor");
            }
            
            ReflectionUtils.makeAccessible(constructor);
            
            o = constructor.newInstance();
            return o;
        }
        catch (InvalidStateException e) {
            throw e;
        }
        catch (ClassCastException e) {
            String message = "Created object '" + o + "' is an instance of " + o.getClass().getName();
            throw new ExecutionException(message, e);
        }
        catch (Exception e) {
            String message = "Error instantiating class of type '" + className + "': " + e.getMessage();
            throw new ExecutionException(message, e);
        }
    }

}
