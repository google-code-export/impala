package org.impalaframework.module.beanset;

import java.util.Arrays;

import junit.framework.TestCase;

import org.impalaframework.module.loader.BeansetApplicationPluginLoader;
import org.impalaframework.module.spec.BeansetModuleDefinition;
import org.impalaframework.module.spec.SimpleBeansetModuleDefinition;
import org.impalaframework.resolver.PropertyClassLocationResolver;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;

public class BeansetApplicationPluginLoaderTest extends TestCase {

	private static final String plugin4 = "impala-sample-dynamic-plugin4";

	private ConfigurableApplicationContext parent;

	private ConfigurableApplicationContext child;

	public final void testInitialPluginSpec() {
		BeansetModuleDefinition pluginSpec = new SimpleBeansetModuleDefinition(plugin4);
		loadChild(pluginSpec);
		System.out.println(Arrays.toString(child.getBeanDefinitionNames()));
		assertTrue(child.containsBean("bean1"));
		assertTrue(child.containsBean("importedBean1"));
		assertTrue(child.containsBean("importedBean2"));
	}
	
	public final void testModifiedPluginSpec() {
		BeansetModuleDefinition pluginSpec = new SimpleBeansetModuleDefinition(plugin4, "alternative: myImports");
		loadChild(pluginSpec);
		System.out.println(Arrays.toString(child.getBeanDefinitionNames()));
		assertTrue(child.containsBean("bean1"));
		assertTrue(child.containsBean("importedBean1"));
		assertFalse(child.containsBean("importedBean2"));
	}
	
	public final void testNewBeanDefinitionReader() {
		BeansetModuleDefinition pluginSpec = new SimpleBeansetModuleDefinition(plugin4);
		BeansetApplicationPluginLoader loader = new BeansetApplicationPluginLoader(new PropertyClassLocationResolver());
	
		XmlBeanDefinitionReader reader = loader.newBeanDefinitionReader(new GenericApplicationContext(), pluginSpec);
		int definitions = reader.loadBeanDefinitions(new ClassPathResource("parentTestContext.xml"));
		assertTrue(definitions > 0);
	}

	private void loadChild(BeansetModuleDefinition pluginSpec) {
		PropertyClassLocationResolver locationResolver = new PropertyClassLocationResolver();
		parent = new ClassPathXmlApplicationContext("parentTestContext.xml");
		BeansetApplicationPluginLoader pluginLoader = new BeansetApplicationPluginLoader(locationResolver);
		ClassLoader classLoader = pluginLoader.newClassLoader(pluginSpec,
						parent);
		child = pluginLoader.newApplicationContext(parent, pluginSpec, classLoader);
		XmlBeanDefinitionReader xmlReader = pluginLoader.newBeanDefinitionReader(child, pluginSpec);
		xmlReader.setBeanClassLoader(classLoader);
		xmlReader.loadBeanDefinitions(pluginLoader.getSpringConfigResources(pluginSpec, classLoader));
		child.refresh();
	}

	public void tearDown() {
		try {
			child.close();
			parent.close();
		} catch (Exception e) {			
		}
	}
}
