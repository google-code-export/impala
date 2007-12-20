package org.impalaframework.module.bootstrap;

import org.impalaframework.module.holder.ModuleStateHolder;
import org.impalaframework.module.loader.ApplicationContextLoader;
import org.impalaframework.module.loader.ModuleLoaderRegistry;
import org.impalaframework.module.modification.ModificationExtractorRegistry;
import org.impalaframework.module.transition.TransitionProcessorRegistry;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.Assert;

public class BeanFactoryModuleManagementSource implements BeanFactory, ModuleManagementSource {

	private final ConfigurableApplicationContext applicationContext;

	public BeanFactoryModuleManagementSource(final ConfigurableApplicationContext applicationContext) {
		super();
		Assert.notNull(applicationContext);
		this.applicationContext = applicationContext;
	}
	
	public ApplicationContextLoader getApplicationContextLoader() {
		return (ApplicationContextLoader) getBean("applicationContextLoader", ApplicationContextLoader.class);
	}

	public ModuleLocationResolver getClassLocationResolver() {
		return (ModuleLocationResolver) getBean("classLocationResolver", ModuleLocationResolver.class);
	}

	public ModuleLoaderRegistry getPluginLoaderRegistry() {
		return (ModuleLoaderRegistry) getBean("pluginLoaderRegistry", ModuleLoaderRegistry.class);
	}

	public ModuleStateHolder getModuleStateHolder() {
		return (ModuleStateHolder) getBean("pluginStateManager", ModuleStateHolder.class);
	}

	public ModificationExtractorRegistry getPluginModificationCalculatorRegistry() {
		return (ModificationExtractorRegistry) getBean("pluginModificationCalculatorRegistry",
				ModificationExtractorRegistry.class);
	}

	public TransitionProcessorRegistry getTransitionProcessorRegistry() {
		return (TransitionProcessorRegistry) getBean("transitionProcessorRegistry", TransitionProcessorRegistry.class);
	}

	public boolean containsBean(String name) {
		return applicationContext.containsBean(name);
	}

	public String[] getAliases(String name) {
		return this.applicationContext.getAliases(name);
	}

	public Object getBean(String name) throws BeansException {
		return this.applicationContext.getBean(name);
	}

	public Object getBean(String name, Class requiredType) throws BeansException {
		return this.applicationContext.getBean(name, requiredType);
	}

	public Class getType(String name) throws NoSuchBeanDefinitionException {
		return this.applicationContext.getType(name);
	}

	public boolean isPrototype(String name) throws NoSuchBeanDefinitionException {
		return this.applicationContext.isPrototype(name);
	}

	public boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
		return this.applicationContext.isSingleton(name);
	}

	public boolean isTypeMatch(String name, Class targetType) throws NoSuchBeanDefinitionException {
		return this.applicationContext.isTypeMatch(name, targetType);
	}
	
	public void close() {
		this.applicationContext.close();
	}

}
