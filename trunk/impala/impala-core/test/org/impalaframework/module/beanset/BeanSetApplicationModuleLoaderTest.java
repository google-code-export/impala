package org.impalaframework.module.beanset;

import java.util.Arrays;

import junit.framework.TestCase;

import org.impalaframework.module.definition.BeansetModuleDefinition;
import org.impalaframework.module.definition.SimpleBeansetModuleDefinition;
import org.impalaframework.module.loader.BeansetApplicationModuleLoader;
import org.impalaframework.resolver.StandaloneModuleLocationResolver;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;

public class BeanSetApplicationModuleLoaderTest extends TestCase {

	private static final String plugin4 = "impala-sample-dynamic-plugin4";

	private ConfigurableApplicationContext parent;

	private ConfigurableApplicationContext child;

	private StandaloneModuleLocationResolver locationResolver;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		locationResolver = new StandaloneModuleLocationResolver();
	}

	public final void testInitialModuleDefinition() {
		BeansetModuleDefinition definition = new SimpleBeansetModuleDefinition(plugin4);
		loadChild(definition);
		System.out.println(Arrays.toString(child.getBeanDefinitionNames()));
		assertTrue(child.containsBean("bean1"));
		assertTrue(child.containsBean("importedBean1"));
		assertTrue(child.containsBean("importedBean2"));
	}
	
	public final void testModifiedModuleDefinition() {
		BeansetModuleDefinition definition = new SimpleBeansetModuleDefinition(plugin4, "alternative: myImports");
		loadChild(definition);
		System.out.println(Arrays.toString(child.getBeanDefinitionNames()));
		assertTrue(child.containsBean("bean1"));
		assertTrue(child.containsBean("importedBean1"));
		assertFalse(child.containsBean("importedBean2"));
	}
	
	public final void testNewBeanDefinitionReader() {
		BeansetModuleDefinition definition = new SimpleBeansetModuleDefinition(plugin4);
		BeansetApplicationModuleLoader loader = new BeansetApplicationModuleLoader(locationResolver);
	
		XmlBeanDefinitionReader reader = loader.newBeanDefinitionReader(new GenericApplicationContext(), definition);
		int definitions = reader.loadBeanDefinitions(new ClassPathResource("parentTestContext.xml"));
		assertTrue(definitions > 0);
	}

	private void loadChild(BeansetModuleDefinition definition) {
		parent = new ClassPathXmlApplicationContext("parentTestContext.xml");
		BeansetApplicationModuleLoader moduleLoader = new BeansetApplicationModuleLoader(locationResolver);
		ClassLoader classLoader = moduleLoader.newClassLoader(definition,
						parent);
		child = moduleLoader.newApplicationContext(parent, definition, classLoader);
		XmlBeanDefinitionReader xmlReader = moduleLoader.newBeanDefinitionReader(child, definition);
		xmlReader.setBeanClassLoader(classLoader);
		xmlReader.loadBeanDefinitions(moduleLoader.getSpringConfigResources(definition, classLoader));
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
