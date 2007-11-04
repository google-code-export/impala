package org.impalaframework.plugin.beanset;

import java.util.Map;
import java.util.Properties;
import java.util.Set;


import org.impalaframework.classloader.FileSystemClassLoader;
import org.impalaframework.classloader.NonDelegatingResourceClassLoader;
import org.impalaframework.plugin.loader.ApplicationPluginLoader;
import org.impalaframework.plugin.spec.PluginSpec;
import org.impalaframework.plugin.util.PluginUtils;
import org.impalaframework.resolver.ClassLocationResolver;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.xml.BeanDefinitionDocumentReader;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ConfigurableApplicationContext;

public class BeansetApplicationPluginLoader extends ApplicationPluginLoader {

	public BeansetApplicationPluginLoader(ClassLocationResolver classLocationResolver) {
		super(classLocationResolver);
	}

	@Override
	public XmlBeanDefinitionReader newBeanDefinitionReader(ConfigurableApplicationContext context, PluginSpec pluginSpec) {
		if (pluginSpec instanceof BeansetPluginSpec) {
			BeansetPluginSpec beanSetSpec = (BeansetPluginSpec) pluginSpec;
			Map<String, Set<String>> overrides = beanSetSpec.getOverrides();

			ClassLoader classLoader = context.getClassLoader();
			if (classLoader instanceof FileSystemClassLoader) {
				classLoader = new NonDelegatingResourceClassLoader((FileSystemClassLoader) classLoader);
			}

			Properties properties = new BeanSetPropertiesReader().readBeanSetSpec(classLoader, overrides);			
			
			final ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
			final DebuggingImportingBeanDefinitionDocumentReader documentReader = new DebuggingImportingBeanDefinitionDocumentReader(properties);

			XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(PluginUtils.castToBeanDefinitionRegistry(beanFactory)){
				protected BeanDefinitionDocumentReader createBeanDefinitionDocumentReader() {
					return documentReader;
				}
			};
			return xmlReader;
		}
		else {
			return super.newBeanDefinitionReader(context, pluginSpec);
		}
	}
	
}
