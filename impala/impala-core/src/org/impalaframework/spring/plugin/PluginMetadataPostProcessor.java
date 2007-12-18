package org.impalaframework.spring.plugin;

import org.impalaframework.module.definition.ModuleDefinition;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.Assert;

public class PluginMetadataPostProcessor implements BeanPostProcessor {

	private final ModuleDefinition moduleDefinition;

	public PluginMetadataPostProcessor(ModuleDefinition moduleDefinition) {
		Assert.notNull(moduleDefinition);
		this.moduleDefinition = moduleDefinition;
	}

	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof PluginSpecAware) {
			PluginSpecAware psa = (PluginSpecAware) bean;
			psa.setPluginSpec(moduleDefinition);
		}
		return bean;
	}

	public Object postProcessAfterInitialization(Object bean, String name) {
		return bean;
	}

}