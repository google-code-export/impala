package org.impalaframework.plugin.spec.transition;

import static org.easymock.EasyMock.aryEq;
import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import junit.framework.TestCase;

import org.impalaframework.plugin.loader.PluginLoader;
import org.impalaframework.plugin.loader.PluginLoaderRegistry;
import org.impalaframework.plugin.spec.ParentSpec;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;

public class AddLocationsTransitionProcessorTest extends TestCase {

	private DefaultPluginStateManager pluginStateManager;

	private ConfigurableApplicationContext context;

	private PluginLoader pluginLoader;

	private BeanDefinitionReader beanDefinitionReader;

	public final void testProcess() {
		PluginLoaderRegistry registry = new PluginLoaderRegistry();

		AddLocationsTransitionProcessor processor = new AddLocationsTransitionProcessor(registry);
		ParentSpec originalSpec = SharedSpecProviders.newTest1().getPluginSpec();
		ParentSpec newSpec = SharedSpecProviders.newTest1a().getPluginSpec();
		ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
		
		pluginStateManager = createMock(DefaultPluginStateManager.class);
		context = createMock(ConfigurableApplicationContext.class);
		pluginLoader = createMock(PluginLoader.class);
		beanDefinitionReader = createMock(BeanDefinitionReader.class);

		registry.setPluginLoader(originalSpec.getType(), pluginLoader);

		Resource[] resources1 = new Resource[]{ new FileSystemResource("r1")};
		Resource[] resources2 = new Resource[]{ new FileSystemResource("r1"), new FileSystemResource("r2")};
		Resource[] resources3 = new Resource[]{ new FileSystemResource("r2")};

		expect(pluginStateManager.getParentContext()).andReturn(context);
		expect(pluginLoader.newBeanDefinitionReader(context, newSpec)).andReturn(beanDefinitionReader);
		expect(context.getClassLoader()).andReturn(classLoader);
		expect(pluginLoader.getSpringConfigResources(originalSpec, classLoader)).andReturn(resources1);
		expect(pluginLoader.getSpringConfigResources(newSpec, classLoader)).andReturn(resources2);
		expect(beanDefinitionReader.loadBeanDefinitions(aryEq(resources3))).andReturn(0);

		replayMocks();
		processor.process(pluginStateManager, originalSpec, newSpec, newSpec);
		verifyMock();
	}

	private void verifyMock() {
		verify(pluginStateManager);
		verify(context);
		verify(pluginLoader);
		verify(beanDefinitionReader);
	}

	private void replayMocks() {
		replay(pluginStateManager);
		replay(context);
		replay(pluginLoader);
		replay(beanDefinitionReader);
	}

}
