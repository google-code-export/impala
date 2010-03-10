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

package org.impalaframework.spring.module.loader;

import java.io.File;

import junit.framework.TestCase;

import org.impalaframework.classloader.CustomClassLoaderFactory;
import org.impalaframework.classloader.ModuleClassLoader;
import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.ModuleDefinitionSource;
import org.impalaframework.module.source.SimpleModuleDefinitionSource;
import org.impalaframework.resolver.StandaloneModuleLocationResolver;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * @author Phil Zoio
 */
public class ApplicationModuleLoaderTest extends TestCase {

    private static final String plugin1 = "sample-module1";

    private static final String plugin2 = "sample-module2";
    
    private static final String rootProjectName = "impala-core";

    private ApplicationModuleLoader moduleLoader;

    private ModuleDefinitionSource source;

    private ModuleDefinition p2;

    public void setUp() {
        StandaloneModuleLocationResolver locationResolver = new StandaloneModuleLocationResolver();
        moduleLoader = new ApplicationModuleLoader();
        CustomClassLoaderFactory classLoaderFactory = new CustomClassLoaderFactory();
        classLoaderFactory.setModuleLocationResolver(locationResolver);

        source = new SimpleModuleDefinitionSource(rootProjectName, new String[] { "parentTestContext.xml" }, new String[] { plugin1, plugin2 });
        p2 = source.getModuleDefinition().getChildModuleDefinition(plugin2);
    }

    public void testGetSpringLocations() {
        File classLocation = new File("../sample-module2/bin");
        ModuleClassLoader classLoader = new ModuleClassLoader(new File[]{classLocation});
        
        final Resource[] springConfigResources = moduleLoader.getSpringConfigResources("id", p2, classLoader);
        assertEquals(1, springConfigResources.length);
        assertEquals(ClassPathResource.class, springConfigResources[0].getClass());
        assertEquals("class path resource [sample-module2-context.xml]", springConfigResources[0].getDescription());
    }

}
