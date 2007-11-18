package org.impalaframework.plugin.loader;

import java.util.List;

import junit.framework.TestCase;

import org.impalaframework.plugin.spec.PluginSpec;
import org.impalaframework.plugin.spec.SimpleParentSpec;
import org.impalaframework.plugin.spec.SimplePluginSpec;
import org.impalaframework.resolver.PropertyClassLocationResolver;
import org.impalaframework.spring.plugin.PluginMetadataPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;

public class BasePluginLoaderTest extends TestCase {
	public void testNewBeanDefinitionReader() throws Exception {
		BasePluginLoader loader = new ApplicationPluginLoader(new PropertyClassLocationResolver());
		GenericApplicationContext context = new GenericApplicationContext();
		XmlBeanDefinitionReader reader = loader.newBeanDefinitionReader(context, new SimplePluginSpec("pluginName"));
		assertSame(context.getBeanFactory(), reader.getBeanFactory());
	}

	@SuppressWarnings("unchecked")
	public void testNewApplicationContext() throws Exception {
		BasePluginLoader loader = new BasePluginLoader() {

			public Resource[] getClassLocations(PluginSpec pluginSpec) {
				return null;
			}

			public Resource[] getSpringConfigResources(PluginSpec pluginSpec, ClassLoader classLoader) {
				return null;
			}

			public ClassLoader newClassLoader(PluginSpec pluginSpec, ApplicationContext parent) {
				return null;
			}
		};
		
		GenericApplicationContext parentContext = new GenericApplicationContext();
		SimpleParentSpec parentSpec = new SimpleParentSpec("context.xml");
		ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
		GenericApplicationContext context = loader.newApplicationContext(parentContext, parentSpec, classLoader);
		
		DefaultListableBeanFactory beanFactory = context.getDefaultListableBeanFactory();
		List<BeanPostProcessor> beanPostProcessors = beanFactory.getBeanPostProcessors();
		
		boolean hasPluginSpecPostProcessor = false;
		for (BeanPostProcessor processor : beanPostProcessors) {
			if (processor instanceof PluginMetadataPostProcessor) {
				hasPluginSpecPostProcessor = true;
			}
		}
		assertTrue(hasPluginSpecPostProcessor);
	}
}
