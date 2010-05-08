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

package org.impalaframework.module.spring.transition;

import static org.easymock.EasyMock.aryEq;
import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import junit.framework.TestCase;

import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.impalaframework.module.holder.DefaultModuleStateHolder;
import org.impalaframework.module.holder.SharedModuleDefinitionSources;
import org.impalaframework.module.loader.ModuleLoaderRegistry;
import org.impalaframework.module.spi.Application;
import org.impalaframework.module.spi.ModuleStateHolder;
import org.impalaframework.module.spi.TestApplicationManager;
import org.impalaframework.spring.module.DefaultSpringRuntimeModule;
import org.impalaframework.spring.module.SpringModuleLoader;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;

public class AddLocationsTransitionProcessorTest extends TestCase {

	private ModuleStateHolder moduleStateHolder;

	private ConfigurableApplicationContext context;

	private SpringModuleLoader moduleLoader;

	private BeanDefinitionReader beanDefinitionReader;

    private Application application;

	public final void testProcess() {
		ModuleLoaderRegistry registry = new ModuleLoaderRegistry();

		AddLocationsTransitionProcessor processor = new AddLocationsTransitionProcessor(registry);
		RootModuleDefinition originalSpec = SharedModuleDefinitionSources.newTest1().getModuleDefinition();
		RootModuleDefinition newSpec = SharedModuleDefinitionSources.newTest1a().getModuleDefinition();
		ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
		
		moduleStateHolder = createMock(DefaultModuleStateHolder.class);
		context = createMock(ConfigurableApplicationContext.class);
		moduleLoader = createMock(SpringModuleLoader.class);
		beanDefinitionReader = createMock(BeanDefinitionReader.class);
        application = TestApplicationManager.newApplicationManager(null, moduleStateHolder, null).getCurrentApplication();

		registry.addItem(originalSpec.getType(), moduleLoader);

		Resource[] resources1 = new Resource[]{ new FileSystemResource("r1")};
		Resource[] resources2 = new Resource[]{ new FileSystemResource("r1"), new FileSystemResource("r2")};
		Resource[] resources3 = new Resource[]{ new FileSystemResource("r2")};

		expect(moduleStateHolder.getRootModule()).andReturn(new DefaultSpringRuntimeModule(new SimpleModuleDefinition("newlocation"), context));
		expect(moduleStateHolder.getRootModuleDefinition()).andReturn(originalSpec);
		expect(moduleLoader.newBeanDefinitionReader("id", context, newSpec)).andReturn(beanDefinitionReader);
		expect(context.getClassLoader()).andReturn(classLoader);
		expect(moduleLoader.getSpringConfigResources("id", originalSpec, classLoader)).andReturn(resources1);
		expect(moduleLoader.getSpringConfigResources("id", newSpec, classLoader)).andReturn(resources2);
		expect(beanDefinitionReader.loadBeanDefinitions(aryEq(resources3))).andReturn(0);

		replayMocks();
		processor.process(application, newSpec, newSpec);
		verifyMock();
	}

	private void verifyMock() {
		verify(moduleStateHolder);
		verify(context);
		verify(moduleLoader);
		verify(beanDefinitionReader);
	}

	private void replayMocks() {
		replay(moduleStateHolder);
		replay(context);
		replay(moduleLoader);
		replay(beanDefinitionReader);
	}

}
