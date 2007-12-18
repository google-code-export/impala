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
import org.impalaframework.module.transition.DefaultPluginStateManager;
import org.impalaframework.module.transition.PluginStateManager;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;

public class AddLocationsTransitionProcessorTest extends TestCase {

	private PluginStateManager pluginStateManager;

	private ConfigurableApplicationContext context;

	private ModuleLoader moduleLoader;

	private BeanDefinitionReader beanDefinitionReader;

	public final void testProcess() {
		ModuleLoaderRegistry registry = new ModuleLoaderRegistry();

		AddLocationsTransitionProcessor processor = new AddLocationsTransitionProcessor(registry);
		RootModuleDefinition originalSpec = SharedSpecProviders.newTest1().getModuleDefintion();
		RootModuleDefinition newSpec = SharedSpecProviders.newTest1a().getModuleDefintion();
		ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
		
		pluginStateManager = createMock(DefaultPluginStateManager.class);
		context = createMock(ConfigurableApplicationContext.class);
		moduleLoader = createMock(ModuleLoader.class);
		beanDefinitionReader = createMock(BeanDefinitionReader.class);

		registry.setPluginLoader(originalSpec.getType(), moduleLoader);

		Resource[] resources1 = new Resource[]{ new FileSystemResource("r1")};
		Resource[] resources2 = new Resource[]{ new FileSystemResource("r1"), new FileSystemResource("r2")};
		Resource[] resources3 = new Resource[]{ new FileSystemResource("r2")};

		expect(pluginStateManager.getParentContext()).andReturn(context);
		expect(moduleLoader.newBeanDefinitionReader(context, newSpec)).andReturn(beanDefinitionReader);
		expect(context.getClassLoader()).andReturn(classLoader);
		expect(moduleLoader.getSpringConfigResources(originalSpec, classLoader)).andReturn(resources1);
		expect(moduleLoader.getSpringConfigResources(newSpec, classLoader)).andReturn(resources2);
		expect(beanDefinitionReader.loadBeanDefinitions(aryEq(resources3))).andReturn(0);

		replayMocks();
		processor.process(pluginStateManager, originalSpec, newSpec, newSpec);
		verifyMock();
	}

	private void verifyMock() {
		verify(pluginStateManager);
		verify(context);
		verify(moduleLoader);
		verify(beanDefinitionReader);
	}

	private void replayMocks() {
		replay(pluginStateManager);
		replay(context);
		replay(moduleLoader);
		replay(beanDefinitionReader);
	}

}
