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

package org.impalaframework.module.source;

import java.net.URL;
import java.util.List;

import org.impalaframework.classloader.CustomClassLoader;
import org.impalaframework.classloader.ModuleClassLoader;
import org.impalaframework.classloader.NonDelegatingResourceClassLoader;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.impalaframework.util.ResourceUtils;
import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;

public class ModuleResourceUtils {

    public static URL loadModuleResource(ModuleLocationResolver moduleLocationResolver, String moduleName, String resourceName) {
        List<Resource> locations = moduleLocationResolver.getApplicationModuleClassLocations(moduleName);
        CustomClassLoader cl = new ModuleClassLoader(ClassUtils.getDefaultClassLoader(), ResourceUtils.getFiles(locations) );
        NonDelegatingResourceClassLoader ndl = new NonDelegatingResourceClassLoader(cl);
        
        URL resource = ndl.getResource(resourceName);
        return resource;
    }

}
