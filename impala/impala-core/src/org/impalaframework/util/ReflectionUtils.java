/*
 * Copyright 2007-2008 the original author or authors.
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
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.impalaframework.exception.ExecutionException;
import org.springframework.util.Assert;

/**
 * Reflection-related utility methods.
 * 
 * @author Phil Zoio
 */
public class ReflectionUtils {
    
    /**
     * Gets field value for named field from supplied object, returning it as
     * instance of supplied type denoted by <code>clazz</code>.
     */
    public static <T extends Object> T getFieldValue(Object object, String fieldName, Class<T> clazz) {
        
        Assert.notNull(object, "object cannot be null");
        
        try {
            Class<? extends Object> objectClass = object.getClass();
            Field declaredField = null;
            
            while (objectClass != null && declaredField == null) {
                try {
                    declaredField = objectClass.getDeclaredField(fieldName);
                }
                catch (NoSuchFieldException e) {
                }
                objectClass = objectClass.getSuperclass();
            }
            
            if (declaredField != null) {
                return getFieldValue(object, declaredField, clazz);
            } 
            else {
                throw new ExecutionException(object.getClass().getName() + " does not appear to contain field '" + fieldName + "'");
            }
        }
        catch (ExecutionException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ExecutionException(e.getMessage(), e);
        }
    }

    /**
     * Gets the field value for a particular object using reflection
     * @param object the object to extract the field value from
     * @param declaredField the {@link Field} instance
     * @param clazz the type of the field
     * @return a field value, or null
     */
    public static <T> T getFieldValue(Object object, 
            Field declaredField,
            Class<T> clazz) {
        
        makeAccessible(declaredField);
        Object value = null;
        try {
            value = declaredField.get(object);
        }
        catch (Exception e) {
            throw new ExecutionException(e.getMessage(), e);
        }
        return ObjectUtils.cast(value, clazz);
    }
    
    /**
     * Finds the constructor corresponding with the given parameter types
     * @param c the class to check
     * @param parameterTypes the parameters to check
     * @return a constructor, if one can be found
     */
    public static Constructor<?> findConstructor(Class<?> c, Class<?>[] parameterTypes) {
        try {
            final Constructor<?> constructor = c.getDeclaredConstructor(parameterTypes);
            return constructor;
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
    
    /**
     * Invokes the supplied constructor to instantiate an object
     * @param constructor the constructor to invoke
     * @param args arguments to supply to the constructor
     * @param makeAccessible if true then allows private constructor to be invoked
     * @return the instantiated object
     */
    public static Object invokeConstructor(final Constructor<?> constructor, Object[] args, boolean makeAccessible) {
        try {
            makeAccessible(constructor);
            return constructor.newInstance(args);
        } catch (Exception e) {
            return rethrowConstructorException(e, constructor, args);
        }
    }

    static void makeAccessible(final Constructor<?> constructor) {
        if (!constructor.isAccessible()) {
            AccessController.doPrivileged(new PrivilegedAction<Object>(){
                
                public Object run() {
                    constructor.setAccessible(true);
                    return null;
                }
            });
        }
    }
    
    static void makeAccessible(final Field field) {
        if (!field.isAccessible()) {
            AccessController.doPrivileged(new PrivilegedAction<Object>(){
                
                public Object run() {
                    field.setAccessible(true);
                    return null;
                }
            });
        }
    }
    
    /**
     * Returns the interfaces of an object's class as a {@link Class} array.
     */
    public static Class<?>[] findInterfaces(Object o) {
        
        List<Class<?>> interfaceList = findInterfaceList(o);    
        return interfaceList.toArray(new Class<?>[0]);
    }

    /**
     * Returns the interfaces of an object's class as a {@link List} of {@link Class} objects
     */
    public static List<Class<?>> findInterfaceList(Object o) {
        Assert.notNull(o);
        List<Class<?>> interfaceList = new ArrayList<Class<?>>();
        
        Class<?> c = o.getClass();
        
        while (c != null) {
            Class<?>[] interfaces = c.getInterfaces();
            interfaceList.addAll(Arrays.asList(interfaces));
            c = c.getSuperclass();
        }
        return interfaceList;
    }
    
    /**
     * Returns whether a Class implements a particular interface. Does this in a way that
     * will not introduce any hard Class dependencies.
     */
    public static boolean implementsInterface(Class<?> clazz, String interfaceName) {
        
        Assert.notNull(clazz);
        Assert.notNull(interfaceName);

        Class<?> c = clazz;
        
        while (c != null) {
            Class<?>[] interfaces = c.getInterfaces();
            if (interfaces != null) {
                for (Class<?> iface : interfaces) {
                    if (interfaceName.equals(iface.getName())) {
                        return true;
                    }
                }
            }
            c = c.getSuperclass();
        }
        
        return false;
    }
    
    /**
     * Returns whether a Class is a subclass of named class. Does this in a way that
     * will not introduce any hard Class dependencies.
     */
    public static boolean isSubclass(Class<?> clazz, String parentClassName) {
        
        Assert.notNull(clazz);
        Assert.notNull(parentClassName);

        Class<?> c = clazz;

        while (c != null) {
            Class<?> superClass = c.getSuperclass();
            
            if (superClass != null && parentClassName.equals(superClass.getName())) {
                return true;
            }
            c = superClass;
        }
        
        return false;
    }
    
    /**
     * Invokes the named method on the target
     * @param target the target of the method invocation
     * @param methodName the name of the method
     * @param args the arguments to the method call
     * @return the result of the method call
     */
    public static Object invokeMethod(Object target, String methodName, Object... args) {

        Class<?>[] paramTypes = new Class[args.length];
        for (int i = 0; i < paramTypes.length; i++) {
            paramTypes[i] = args[i].getClass();
        }

        Method findMethod = findMethod(target.getClass(), methodName, paramTypes);
        if (findMethod == null) {
            throw new UnsupportedOperationException("No method compatible with method: " + methodName + ", args: "
                    + Arrays.toString(args));
        }

        return invokeMethod(findMethod, target, args);
    }

    /**
     * Invokes the method on the target object
     * @param method the method to invoke
     * @param target the target object to invoke the method on
     * @param args the arguments to the method call
     * @return the result of the method call
     */
    public static Object invokeMethod(final Method method, Object target, Object... args) {
        try {
            if (!method.isAccessible()) {
                AccessController.doPrivileged(new PrivilegedAction<Object>(){
                    
                    public Object run() {
                        method.setAccessible(true);
                        return null;
                    }
                });
            }
            return method.invoke(target, args);
        }
        catch (InvocationTargetException e) {
            if (e.getCause() instanceof RuntimeException) {
                throw (RuntimeException) e.getCause();
            }
            throw rethrowMethodException(e, method.getName(), args);
        }
        catch (Exception e) {
            throw rethrowMethodException(e, method.getName(), args);
        }
    }

    /**
     * Returns the named methods with the corresponding parameter types
     * @param clazz the class from which to find the method
     * @param name the name of the method
     * @param paramTypes the parameter types for the method call
     * @return the {@link Method} corresponding with the parameters, or null if none is found
     */
    public static Method findMethod(Class<?> clazz, String name, Class<?>[] paramTypes) {
        Assert.notNull(clazz, "Class must not be null");
        Assert.notNull(name, "Method name must not be null");
        Class<?> searchType = clazz;
        while (!Object.class.equals(searchType) && searchType != null) {
            Method[] methods = (searchType.isInterface() ? searchType.getMethods() : searchType.getDeclaredMethods());
            for (int i = 0; i < methods.length; i++) {
                Method method = methods[i];
                if (name.equals(method.getName()) && (paramTypes.length == method.getParameterTypes().length)) {

                    boolean found = true;
                    Class<?>[] methodParameterTypes = method.getParameterTypes();

                    for (int j = 0; j < methodParameterTypes.length; j++) {
                        found = methodParameterTypes[j].isAssignableFrom(paramTypes[j]);
                        if (!found)
                            break;
                    }

                    if (found)
                        return method;
                }
            }
            searchType = searchType.getSuperclass();
        }
        return null;
    }

    private static ExecutionException rethrowMethodException(Exception e, String methodName, Object... args) {
        return new ExecutionException("Unable to execute method: " + methodName + ", args: " + Arrays.toString(args), e);
    }

    private static ExecutionException rethrowConstructorException(Exception e, Constructor<?> constructor, Object... args) {
        return new ExecutionException("Unable to instantiate object using constructor '" + constructor + "', args: " + Arrays.toString(args), e);
    }
}
