package net.java.impala.spring.plugin;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author Phil Zoio
 */
public abstract class BasePluginLoader implements PluginLoader {
	
	public XmlBeanDefinitionReader newBeanDefinitionReader(ConfigurableApplicationContext context, PluginSpec plugin) {
		final ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();

		if (!(beanFactory instanceof BeanDefinitionRegistry)) {
			throw new IllegalStateException(BeanFactory.class.getSimpleName() + " is not an instance of "
					+ BeanDefinitionRegistry.class.getSimpleName());
		}

		return new XmlBeanDefinitionReader((BeanDefinitionRegistry) context);
	}

	public void afterRefresh(ConfigurableApplicationContext context, BeanDefinitionReader reader, PluginSpec plugin) {
	}
}
