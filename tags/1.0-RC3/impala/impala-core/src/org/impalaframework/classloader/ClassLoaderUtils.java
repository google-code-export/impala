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

package org.impalaframework.classloader;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.springframework.util.ClassUtils;

/**
 * Class with static utility methods involving {@link ClassLoader}s.
 * 
 * @author Phil Zoio
 */
public abstract class ClassLoaderUtils {
    
    public static List<String> getClassHierarchyNames(String className) {
        
        List<String> classNames = new LinkedList<String>();
        ClassLoader defaultClassLoader = ClassUtils.getDefaultClassLoader();
        
        try {
            Class<?> loadClass = defaultClassLoader.loadClass(className);
            classNames.add(loadClass.getName());
            findSuperClass(classNames, loadClass);
            return classNames;
        }
        catch (ClassNotFoundException e) {
            return Collections.emptyList();
        }
    }

    private static void findSuperClass(List<String> classNames, Class<?> loadClass) {
        Class<?> superClass = loadClass.getSuperclass();
        if (superClass != null) {
            classNames.add(superClass.getName());
            findSuperClass(classNames, superClass);
        }
    }
    
    /**
     * Returns true if a class loaded by the parent class loader is visible to the child class loader
     */
    public static boolean isVisibleFrom(ClassLoader parent, ClassLoader child) {
        if (parent == child) return true;
        
        if (child instanceof ModularClassLoader) {
            ModularClassLoader modularChild = (ModularClassLoader) child;
            return modularChild.hasVisibilityOf(parent);
        }
        
        return false;
    }
}
