package org.impalaframework.module.bootstrap;

import org.impalaframework.module.holder.ModuleStateHolder;
import org.impalaframework.module.loader.ApplicationContextLoader;
import org.impalaframework.module.loader.ModuleLoaderRegistry;
import org.impalaframework.module.modification.ModificationExtractorRegistry;
import org.impalaframework.module.operation.ModuleOperationRegistry;
import org.impalaframework.module.transition.TransitionProcessorRegistry;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.impalaframework.util.ObjectUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.Assert;

public class DefaultModuleManagementFactory implements BeanFactory, ModuleManagementFactory,
		ApplicationContextAware, InitializingBean {

	private ConfigurableApplicationContext applicationContext;

	private ModuleOperationRegistry moduleOperationRegistry;

	private ApplicationContextLoader applicationContextLoader;

	private ModuleLocationResolver moduleLocationResolver;

	private ModuleLoaderRegistry moduleLoaderRegistry;

	private ModificationExtractorRegistry modificationExtractorRegistry;

	private TransitionProcessorRegistry transitionProcessorRegistry;

	private ModuleStateHolder moduleStateHolder;

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(moduleOperationRegistry, "moduleOperationRegistry cannot be null");
		Assert.notNull(applicationContextLoader, "applicationContextLoader cannot be null");
		Assert.notNull(moduleLocationResolver, "moduleLocationResolver cannot be null");
		Assert.notNull(moduleLoaderRegistry, "moduleLoaderRegistry cannot be null");
		Assert.notNull(modificationExtractorRegistry, "modificationExtractorRegistry cannot be null");
		Assert.notNull(transitionProcessorRegistry, "transitionProcessorRegistry cannot be null");
		Assert.notNull(moduleStateHolder, "moduleStateHolder cannot be null");
	}

	public DefaultModuleManagementFactory() {
		super();
	}

	public ApplicationContextLoader getApplicationContextLoader() {
		return applicationContextLoader;
	}

	public ModuleLocationResolver getModuleLocationResolver() {
		return moduleLocationResolver;
	}

	public ModificationExtractorRegistry getModificationExtractorRegistry() {
		return modificationExtractorRegistry;
	}

	public ModuleOperationRegistry getModuleOperationRegistry() {
		return moduleOperationRegistry;
	}

	public ModuleLoaderRegistry getModuleLoaderRegistry() {
		return moduleLoaderRegistry;
	}

	public TransitionProcessorRegistry getTransitionProcessorRegistry() {
		return transitionProcessorRegistry;
	}

	public ModuleStateHolder getModuleStateHolder() {
		return moduleStateHolder;
	}

	/* *************** Injection setters ************* */

	public void setApplicationContextLoader(ApplicationContextLoader applicationContextLoader) {
		this.applicationContextLoader = applicationContextLoader;
	}

	public void setModuleLocationResolver(ModuleLocationResolver moduleLocationResolver) {
		this.moduleLocationResolver = moduleLocationResolver;
	}

	public void setModificationExtractorRegistry(ModificationExtractorRegistry modificationExtractorRegistry) {
		this.modificationExtractorRegistry = modificationExtractorRegistry;
	}

	public void setModuleOperationRegistry(ModuleOperationRegistry moduleOperationRegistry) {
		this.moduleOperationRegistry = moduleOperationRegistry;
	}

	public void setModuleStateHolder(ModuleStateHolder moduleStateHolder) {
		this.moduleStateHolder = moduleStateHolder;
	}

	public void setModuleLoaderRegistry(ModuleLoaderRegistry moduleLoaderRegistry) {
		this.moduleLoaderRegistry = moduleLoaderRegistry;
	}

	public void setTransitionProcessorRegistry(TransitionProcessorRegistry transitionProcessorRegistry) {
		this.transitionProcessorRegistry = transitionProcessorRegistry;
	}
	
	/* *************** ApplicationContext method implementations ************* */

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

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = ObjectUtils.cast(applicationContext, ConfigurableApplicationContext.class);
	}

}
