package org.impalaframework.module.transition;

import static org.easymock.EasyMock.aryEq;
import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import junit.framework.TestCase;

import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.loader.ModuleLoader;
import org.impalaframework.module.loader.ModuleLoaderRegistry;
import org.impalaframework.module.transition.AddLocationsTransitionProcessor;
import org.impalaframework.module.transition.DefaultModuleStateManager;
import org.impalaframework.module.transition.ModuleStateManager;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;

public class AddLocationsTransitionProcessorTest extends TestCase {

	private ModuleStateManager moduleStateManager;

	private ConfigurableApplicationContext context;

	private ModuleLoader moduleLoader;

	private BeanDefinitionReader beanDefinitionReader;

	public final void testProcess() {
		ModuleLoaderRegistry registry = new ModuleLoaderRegistry();

		AddLocationsTransitionProcessor processor = new AddLocationsTransitionProcessor(registry);
		RootModuleDefinition originalSpec = SharedModuleDefinitionSources.newTest1().getModuleDefinition();
		RootModuleDefinition newSpec = SharedModuleDefinitionSources.newTest1a().getModuleDefinition();
		ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
		
		moduleStateManager = createMock(DefaultModuleStateManager.class);
		context = createMock(ConfigurableApplicationContext.class);
		moduleLoader = createMock(ModuleLoader.class);
		beanDefinitionReader = createMock(BeanDefinitionReader.class);

		registry.setPluginLoader(originalSpec.getType(), moduleLoader);

		Resource[] resources1 = new Resource[]{ new FileSystemResource("r1")};
		Resource[] resources2 = new Resource[]{ new FileSystemResource("r1"), new FileSystemResource("r2")};
		Resource[] resources3 = new Resource[]{ new FileSystemResource("r2")};

		expect(moduleStateManager.getParentContext()).andReturn(context);
		expect(moduleLoader.newBeanDefinitionReader(context, newSpec)).andReturn(beanDefinitionReader);
		expect(context.getClassLoader()).andReturn(classLoader);
		expect(moduleLoader.getSpringConfigResources(originalSpec, classLoader)).andReturn(resources1);
		expect(moduleLoader.getSpringConfigResources(newSpec, classLoader)).andReturn(resources2);
		expect(beanDefinitionReader.loadBeanDefinitions(aryEq(resources3))).andReturn(0);

		replayMocks();
		processor.process(moduleStateManager, originalSpec, newSpec, newSpec);
		verifyMock();
	}

	private void verifyMock() {
		verify(moduleStateManager);
		verify(context);
		verify(moduleLoader);
		verify(beanDefinitionReader);
	}

	private void replayMocks() {
		replay(moduleStateManager);
		replay(context);
		replay(moduleLoader);
		replay(beanDefinitionReader);
	}

}
