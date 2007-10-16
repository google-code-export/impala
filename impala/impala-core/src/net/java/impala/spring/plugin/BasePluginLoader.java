package net.java.impala.spring.plugin;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ConfigurableApplicationContext;

public abstract class BasePluginLoader implements PluginLoader {
	public XmlBeanDefinitionReader newBeanDefinitionReader(ConfigurableApplicationContext context) {
		final ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();

 		//FIXME add test
		if (!(beanFactory instanceof BeanDefinitionRegistry)) {
			throw new IllegalStateException(BeanFactory.class.getSimpleName() + " is not an instance of "
					+ BeanDefinitionRegistry.class.getSimpleName());
		}

		return new XmlBeanDefinitionReader((BeanDefinitionRegistry) context);
	}
}
