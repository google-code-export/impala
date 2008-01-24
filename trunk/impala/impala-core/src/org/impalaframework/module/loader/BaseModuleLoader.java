package org.impalaframework.module.loader;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.resource.ModuleLocationsResourceLoader;
import org.impalaframework.spring.module.ModuleDefinitionPostProcessor;
import org.impalaframework.spring.resource.ClassPathResourceLoader;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

/**
 * @author Phil Zoio
 */
public abstract class BaseModuleLoader implements ModuleLoader {
	
	public GenericApplicationContext newApplicationContext(ApplicationContext parent, ModuleDefinition definition, ClassLoader classLoader) {
		Assert.notNull(classLoader, "classloader cannot be null");
		
		DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
		beanFactory.setBeanClassLoader(classLoader);
		beanFactory.addBeanPostProcessor(new ModuleDefinitionPostProcessor(definition));

		// create the application context, and set the class loader
		GenericApplicationContext context = new GenericApplicationContext(beanFactory, parent);
		context.setClassLoader(classLoader);
		return context;
	}

	public Resource[] getSpringConfigResources(ModuleDefinition moduleDefinition, ClassLoader classLoader) {
		//FIXME externalize
		ModuleLocationsResourceLoader loader = new ModuleLocationsResourceLoader();
		loader.setResourceLoader(new ClassPathResourceLoader());
		
		return loader.getSpringLocations(moduleDefinition, classLoader);
	}
	
	public XmlBeanDefinitionReader newBeanDefinitionReader(ConfigurableApplicationContext context, ModuleDefinition definition) {
		final ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
		return new XmlBeanDefinitionReader(ModuleUtils.castToBeanDefinitionRegistry(beanFactory));
	}

	public void afterRefresh(ConfigurableApplicationContext context, ModuleDefinition definition) {
	}
}
