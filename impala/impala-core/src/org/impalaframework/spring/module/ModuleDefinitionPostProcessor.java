package org.impalaframework.spring.module;

import org.impalaframework.module.definition.ModuleDefinition;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.Assert;

public class ModuleDefinitionPostProcessor implements BeanPostProcessor {

	private final ModuleDefinition moduleDefinition;

	public ModuleDefinitionPostProcessor(ModuleDefinition moduleDefinition) {
		Assert.notNull(moduleDefinition);
		this.moduleDefinition = moduleDefinition;
	}

	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof ModuleDefinitionAware) {
			ModuleDefinitionAware psa = (ModuleDefinitionAware) bean;
			psa.setModuleDefinition(moduleDefinition);
		}
		return bean;
	}

	public Object postProcessAfterInitialization(Object bean, String name) {
		return bean;
	}

}