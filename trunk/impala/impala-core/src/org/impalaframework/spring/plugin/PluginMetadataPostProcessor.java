package org.impalaframework.spring.plugin;

import org.impalaframework.module.spec.PluginSpec;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.Assert;

public class PluginMetadataPostProcessor implements BeanPostProcessor {

	private final PluginSpec pluginSpec;

	public PluginMetadataPostProcessor(PluginSpec pluginSpec) {
		Assert.notNull(pluginSpec);
		this.pluginSpec = pluginSpec;
	}

	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof PluginSpecAware) {
			PluginSpecAware psa = (PluginSpecAware) bean;
			psa.setPluginSpec(pluginSpec);
		}
		return bean;
	}

	public Object postProcessAfterInitialization(Object bean, String name) {
		return bean;
	}

}