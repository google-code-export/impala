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

package org.impalaframework.spring.module.loader;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.impalaframework.exception.NoServiceException;
import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.definition.ModuleTypes;
import org.impalaframework.module.definition.SimpleRootModuleDefinition;
import org.impalaframework.module.loader.ModuleLoaderRegistry;
import org.impalaframework.module.spi.ModuleLoader;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.impalaframework.resolver.StandaloneModuleLocationResolver;
import org.impalaframework.spring.module.DelegatingContextLoader;
import org.impalaframework.spring.module.loader.ApplicationModuleLoader;
import org.impalaframework.spring.module.loader.DelegatingContextLoaderRegistry;
import org.impalaframework.spring.module.loader.RootModuleLoader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author Phil Zoio
 */
public class ModuleLoaderRegistryTest extends TestCase {
	
	private String rootModuleName = "project1";

	private ModuleLoaderRegistry moduleLoaderRegistry;
	private DelegatingContextLoaderRegistry delegatingContextLoaderRegistry;
	
	@Override
	protected void setUp() throws Exception {
		moduleLoaderRegistry = new ModuleLoaderRegistry();
		delegatingContextLoaderRegistry = new DelegatingContextLoaderRegistry();
	}
	public void testNoModuleLoader() {
		try {
			moduleLoaderRegistry.getModuleLoader("unknowntype");
		}
		catch (NoServiceException e) {
			assertEquals("No " + ModuleLoader.class.getName() + 
					" instance available for module definition type unknowntype", e.getMessage());
		}
	}
	
	public void testGetModuleLoader() {
		ModuleLocationResolver resolver = new StandaloneModuleLocationResolver();
		RootModuleLoader rootModuleLoader = new RootModuleLoader();
		rootModuleLoader.setModuleLocationResolver(resolver);
		moduleLoaderRegistry.setModuleLoader(ModuleTypes.ROOT, rootModuleLoader);
		ApplicationModuleLoader applicationModuleLoader = new ApplicationModuleLoader();
		applicationModuleLoader.setModuleLocationResolver(resolver);
		moduleLoaderRegistry.setModuleLoader(ModuleTypes.APPLICATION, applicationModuleLoader);

		ModuleDefinition p = new SimpleRootModuleDefinition(rootModuleName, new String[] { "parent-context.xml" });
		assertTrue(moduleLoaderRegistry.getModuleLoader(p.getType()) instanceof RootModuleLoader);

		DelegatingContextLoader delegatingLoader = new DelegatingContextLoader() {
			public ConfigurableApplicationContext loadApplicationContext(ApplicationContext parent,
					ModuleDefinition definition) {
				return null;
			}
		};
		delegatingContextLoaderRegistry.setDelegatingLoader("sometype", delegatingLoader);
		assertSame(delegatingLoader, delegatingContextLoaderRegistry.getDelegatingLoader("sometype"));
	}
	
	public void testSetModuleLoaders() {
		Map<String, ModuleLoader> moduleLoaders = setModuleLoaders();
		
		assertEquals(2, moduleLoaders.size());

		Map<String,DelegatingContextLoader> delegatingLoaders = new HashMap<String, DelegatingContextLoader>();
		DelegatingContextLoader delegatingLoader = new DelegatingContextLoader() {
			public ConfigurableApplicationContext loadApplicationContext(ApplicationContext parent,
					ModuleDefinition definition) {
				return null;
			}
		};
		delegatingLoaders.put("key", delegatingLoader);
		delegatingContextLoaderRegistry.setDelegatingLoaders(delegatingLoaders);

		assertEquals(1, delegatingLoaders.size());		
	}
	
	public void testExtraLoaders() throws Exception {
		setModuleLoaders();
		final ModuleLoader extraModuleLoader = EasyMock.createMock(ModuleLoader.class);
		final DelegatingContextLoader extraDelegatingLoader = EasyMock.createMock(DelegatingContextLoader.class);
		
		Map<String, ModuleLoader> extraModuleLoaders = new HashMap<String, ModuleLoader>();
		extraModuleLoaders.put(ModuleTypes.APPLICATION, extraModuleLoader);
		extraModuleLoaders.put("another", extraModuleLoader);
		moduleLoaderRegistry.setExtraModuleLoaders(extraModuleLoaders);
		
		Map<String, DelegatingContextLoader> extraDelegatingLoaders = new HashMap<String, DelegatingContextLoader>();
		extraDelegatingLoaders.put(ModuleTypes.APPLICATION, extraDelegatingLoader);
		extraDelegatingLoaders.put("another", extraDelegatingLoader);
		delegatingContextLoaderRegistry.setExtraDelegatingLoaders(extraDelegatingLoaders);
		
		moduleLoaderRegistry.afterPropertiesSet();
		delegatingContextLoaderRegistry.afterPropertiesSet();
		
		assertSame(extraModuleLoader, moduleLoaderRegistry.getModuleLoader(ModuleTypes.APPLICATION));
		assertSame(extraModuleLoader, moduleLoaderRegistry.getModuleLoader("another"));
		assertSame(extraDelegatingLoader, delegatingContextLoaderRegistry.getDelegatingLoader(ModuleTypes.APPLICATION));
		assertSame(extraDelegatingLoader, delegatingContextLoaderRegistry.getDelegatingLoader("another"));
		
		assertFalse(moduleLoaderRegistry.getModuleLoader(ModuleTypes.ROOT) == extraModuleLoader);
		assertFalse(delegatingContextLoaderRegistry.getDelegatingLoader(ModuleTypes.ROOT) == extraDelegatingLoader);
	}
	
	
	private Map<String, ModuleLoader> setModuleLoaders() {
		ModuleLocationResolver resolver = new StandaloneModuleLocationResolver();
		Map<String,ModuleLoader> moduleLoaders = new HashMap<String, ModuleLoader>();
		RootModuleLoader rootModuleLoader = new RootModuleLoader();
		rootModuleLoader.setModuleLocationResolver(resolver);
		moduleLoaders.put(ModuleTypes.ROOT, rootModuleLoader);
		ApplicationModuleLoader applicationModuleLoader = new ApplicationModuleLoader();
		applicationModuleLoader.setModuleLocationResolver(resolver);
		moduleLoaders.put(ModuleTypes.APPLICATION, applicationModuleLoader);
		moduleLoaderRegistry.setModuleLoaders(moduleLoaders);
		return moduleLoaders;
	}
}
