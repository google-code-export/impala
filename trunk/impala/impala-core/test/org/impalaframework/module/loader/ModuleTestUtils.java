package org.impalaframework.module.loader;

import java.util.List;

import junit.framework.Assert;

import org.impalaframework.spring.module.ModuleDefinitionPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;

public class ModuleTestUtils {

	@SuppressWarnings("unchecked")
	public static void checkHasModuleDefinitionPostProcessor(boolean expectPostProcessor,
			ConfigurableApplicationContext context) {
		DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) context.getBeanFactory();
		List<BeanPostProcessor> beanPostProcessors = beanFactory.getBeanPostProcessors();
		
		boolean hasPostProcessor = false;
		for (BeanPostProcessor processor : beanPostProcessors) {
			if (processor instanceof ModuleDefinitionPostProcessor) {
				hasPostProcessor = true;
			}
		}
		Assert.assertTrue(hasPostProcessor == expectPostProcessor);
	}

}
