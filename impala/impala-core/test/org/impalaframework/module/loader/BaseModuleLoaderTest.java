package org.impalaframework.module.loader;

import java.util.List;

import junit.framework.TestCase;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.impalaframework.module.definition.SimpleRootModuleDefinition;
import org.impalaframework.module.loader.ApplicationModuleLoader;
import org.impalaframework.module.loader.BaseModuleLoader;
import org.impalaframework.resolver.PropertyModuleLocationResolver;
import org.impalaframework.spring.plugin.ModuleDefinitionPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;

public class BaseModuleLoaderTest extends TestCase {
	public void testNewBeanDefinitionReader() throws Exception {
		BaseModuleLoader loader = new ApplicationModuleLoader(new PropertyModuleLocationResolver());
		GenericApplicationContext context = new GenericApplicationContext();
		XmlBeanDefinitionReader reader = loader.newBeanDefinitionReader(context, new SimpleModuleDefinition("pluginName"));
		assertSame(context.getBeanFactory(), reader.getBeanFactory());
	}

	@SuppressWarnings("unchecked")
	public void testNewApplicationContext() throws Exception {
		BaseModuleLoader loader = new BaseModuleLoader() {

			public Resource[] getClassLocations(ModuleDefinition moduleDefinition) {
				return null;
			}

			public Resource[] getSpringConfigResources(ModuleDefinition moduleDefinition, ClassLoader classLoader) {
				return null;
			}

			public ClassLoader newClassLoader(ModuleDefinition moduleDefinition, ApplicationContext parent) {
				return null;
			}
		};
		
		GenericApplicationContext parentContext = new GenericApplicationContext();
		SimpleRootModuleDefinition parentSpec = new SimpleRootModuleDefinition("context.xml");
		ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
		GenericApplicationContext context = loader.newApplicationContext(parentContext, parentSpec, classLoader);
		
		DefaultListableBeanFactory beanFactory = context.getDefaultListableBeanFactory();
		List<BeanPostProcessor> beanPostProcessors = beanFactory.getBeanPostProcessors();
		
		boolean hasPostProcessor = false;
		for (BeanPostProcessor processor : beanPostProcessors) {
			if (processor instanceof ModuleDefinitionPostProcessor) {
				hasPostProcessor = true;
			}
		}
		assertTrue(hasPostProcessor);
	}
}
