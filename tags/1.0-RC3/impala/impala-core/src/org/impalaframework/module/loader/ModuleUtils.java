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

package org.impalaframework.module.loader;

import java.util.ArrayList;
import java.util.List;

import org.impalaframework.exception.ExecutionException;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;

/**
 * @author Phil Zoio
 */
public abstract class ModuleUtils {
    
    public static ClassLoader getParentClassLoader(ApplicationContext parent) {
        ClassLoader parentClassLoader = null;
        if (parent != null) {
            parentClassLoader = parent.getClassLoader();
        }
        if (parentClassLoader == null) {
            parentClassLoader = ClassUtils.getDefaultClassLoader();
        }
        return parentClassLoader;
    }

    public static BeanDefinitionRegistry castToBeanDefinitionRegistry(final ConfigurableListableBeanFactory beanFactory) {
        if (!(beanFactory instanceof BeanDefinitionRegistry)) {
            throw new ExecutionException(beanFactory.getClass().getName() + " is not an instance of "
                    + BeanDefinitionRegistry.class.getSimpleName());
        }
    
        BeanDefinitionRegistry beanDefinitionRegistry = (BeanDefinitionRegistry) beanFactory;
        return beanDefinitionRegistry;
    }

    public static Resource[] getRootClassLocations(ModuleLocationResolver moduleLocationResolver,
            List<String> rootProjects) {
        List<Resource> allLocations = new ArrayList<Resource>(rootProjects.size());
        
        for (String rootProjectName : rootProjects) {
            List<Resource> locations = moduleLocationResolver.getApplicationModuleClassLocations(rootProjectName);
            allLocations.addAll(locations);
        }
        
        return allLocations.toArray(new Resource[allLocations.size()]);
    }


}
