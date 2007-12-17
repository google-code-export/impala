package org.impalaframework.module.loader;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ClassUtils;

/**
 * @author Phil Zoio
 */
public class ModuleUtils {
	
	public static ClassLoader getParentClassLoader(ApplicationContext parent) {
		ClassLoader parentClassLoader = null;
		if (parent != null) {
			parentClassLoader = parent.getClassLoader();
		}
		if (parentClassLoader == null) {
			parentClassLoader = ClassUtils.getDefaultClassLoader();
		}
		return parentClassLoader;
	}

	public static BeanDefinitionRegistry castToBeanDefinitionRegistry(final ConfigurableListableBeanFactory beanFactory) {
		if (!(beanFactory instanceof BeanDefinitionRegistry)) {
			throw new IllegalStateException(beanFactory.getClass().getName() + " is not an instance of "
					+ BeanDefinitionRegistry.class.getSimpleName());
		}
	
		BeanDefinitionRegistry beanDefinitionRegistry = (BeanDefinitionRegistry) beanFactory;
		return beanDefinitionRegistry;
	}

}
