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

package org.impalaframework.classloader.graph;

import junit.framework.TestCase;

import org.impalaframework.classloader.ClassLoaderUtils;
import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.module.definition.DependencyManager;
import org.impalaframework.module.holder.graph.GraphClassLoaderFactory;
import org.impalaframework.module.holder.graph.GraphClassLoaderRegistry;
import org.impalaframework.module.source.InternalModuleDefinitionSource;
import org.impalaframework.module.type.TypeReaderRegistry;
import org.impalaframework.module.type.TypeReaderRegistryFactory;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.impalaframework.resolver.StandaloneModuleLocationResolver;
import org.springframework.util.ClassUtils;

public class GraphClassLoaderTest extends TestCase {
    
    private GraphClassLoaderRegistry classLoaderRegistry;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        classLoaderRegistry = new GraphClassLoaderRegistry();
    }

    public void testClassLoader() throws Exception {
        
        TypeReaderRegistry typeReaderRegistry = TypeReaderRegistryFactory.getTypeReaderRegistry();
        ModuleLocationResolver resolver = new StandaloneModuleLocationResolver();
        InternalModuleDefinitionSource source = new InternalModuleDefinitionSource(typeReaderRegistry, resolver, new String[]{"impala-core", "sample-module4", "sample-module6"});

        RootModuleDefinition rootDefinition = source.getModuleDefinition();
        
        DependencyManager dependencyManager = new DependencyManager(rootDefinition);
        GraphClassLoaderFactory factory = new GraphClassLoaderFactory();
        factory.setModuleLocationResolver(resolver);
        
        GraphClassLoader rootClassLoader = factory.newClassLoader(classLoaderRegistry, dependencyManager, rootDefinition);
        
        System.out.println(rootClassLoader);
        String lineSeparator = System.getProperty("line.separator");
        
        assertEquals("Class loader for impala-core" + lineSeparator +
                "Loading first from parent: false" + lineSeparator, rootClassLoader.toString());
        
        ModuleDefinition moduleDefinition6 = rootDefinition.findChildDefinition("sample-module6", true);
        
        GraphClassLoader definition6Loader = factory.newClassLoader(classLoaderRegistry, dependencyManager, moduleDefinition6);
        System.out.println(definition6Loader);
        
        assertEquals(
                "Class loader for sample-module6" + lineSeparator +
                "Loading first from parent: false" + lineSeparator +
                "Delegate class loader: sample-module5,impala-core,sample-module2,sample-module4" + lineSeparator, 
                definition6Loader.toString());
        
        assertTrue(ClassLoaderUtils.isVisibleFrom(rootClassLoader, definition6Loader));
        assertFalse(ClassLoaderUtils.isVisibleFrom(definition6Loader, rootClassLoader));
        
        assertTrue(ClassLoaderUtils.isVisibleFrom(ClassUtils.getDefaultClassLoader(), definition6Loader));
        assertTrue(ClassLoaderUtils.isVisibleFrom(ClassUtils.getDefaultClassLoader(), rootClassLoader));
        
        assertFalse(ClassLoaderUtils.isVisibleFrom(rootClassLoader, ClassUtils.getDefaultClassLoader()));
        assertFalse(ClassLoaderUtils.isVisibleFrom(definition6Loader, ClassUtils.getDefaultClassLoader()));
    }
    
}

