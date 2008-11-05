/*
 * Copyright 2007-2008 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.impalaframework.facade;

import java.util.List;

import org.impalaframework.exception.InvalidBeanTypeException;
import org.impalaframework.exception.NoServiceException;
import org.impalaframework.module.ModuleStateHolder;
import org.impalaframework.module.definition.ConstructedModuleDefinitionSource;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.operation.ModuleOperation;
import org.impalaframework.module.operation.ModuleOperationConstants;
import org.impalaframework.module.operation.ModuleOperationInput;
import org.impalaframework.startup.ClassPathApplicationContextStarter;
import org.impalaframework.startup.ContextStarter;
import org.impalaframework.util.ObjectUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.Assert;

/**
 * Abstract base implementation of <code>InternalOperationsFacade</code>.
 * Subclasses must provide implementation of abstract method
 * <code>getBootstrapContextLocations()</code>, which contains the bean
 * definitions for the bootstrap <code>ApplicationContext</code> used to
 * initialise Impala.
 * @author Phil Zoio
 * @see #getBootstrapContextLocations()
 */
public abstract class BaseOperationsFacade implements InternalOperationsFacade {

	private ModuleStateHolder moduleStateHolder = null;

	private ModuleManagementFacade facade;

	/*
	 * **************************** initialising operations
	 * **************************
	 */
	
	public BaseOperationsFacade(ModuleManagementFacade facade) {
		//FIXME test
		this.facade = facade;
		this.moduleStateHolder = facade.getModuleStateHolder();
	}

	public BaseOperationsFacade() {
		super();
		init();
	}

	protected abstract List<String> getBootstrapContextLocations();

	protected void init() {
		List<String> locations = getBootstrapContextLocations();

		ContextStarter contextStarter = getContextStarter();
		ApplicationContext applicationContext = contextStarter.startContext(locations);

		facade = ObjectUtils.cast(applicationContext.getBean("moduleManagementFacade"),
				ModuleManagementFacade.class);
		moduleStateHolder = facade.getModuleStateHolder();
	}

	protected ContextStarter getContextStarter() {
		return new ClassPathApplicationContextStarter();
	}

	public void init(ModuleDefinitionSource source) {
		ModuleOperation operation = facade.getModuleOperationRegistry().getOperation(
				ModuleOperationConstants.IncrementalUpdateRootModuleOperation);
		operation.execute(new ModuleOperationInput(source, null, null));
	}

	/*
	 * **************************** modifying operations
	 * **************************
	 */

	public boolean reload(String moduleName) {
		ModuleOperation operation = facade.getModuleOperationRegistry().getOperation(
				ModuleOperationConstants.ReloadNamedModuleOperation);
		ModuleOperationInput moduleOperationInput = new ModuleOperationInput(null, null, moduleName);
		return operation.execute(moduleOperationInput).isSuccess();
	}

	public String reloadLike(String moduleName) {
		String like = findLike(moduleName);
		if (like != null) {
			reload(like);
		}
		return like;
	}

	public void reloadRootModule() {
		RootModuleDefinition rootModuleDefinition = getModuleStateHolder().getRootModuleDefinition();
		ModuleOperation operation = facade.getModuleOperationRegistry().getOperation(
				ModuleOperationConstants.CloseRootModuleOperation);
		operation.execute(null);
		ConstructedModuleDefinitionSource newModuleDefinitionSource = new ConstructedModuleDefinitionSource(
				rootModuleDefinition);

		ModuleOperationInput input = new ModuleOperationInput(newModuleDefinitionSource, null, null);
		operation = facade.getModuleOperationRegistry().getOperation(
				ModuleOperationConstants.UpdateRootModuleOperation);
		operation.execute(input);
	}

	public void unloadRootModule() {
		ModuleOperation operation = facade.getModuleOperationRegistry().getOperation(
				ModuleOperationConstants.CloseRootModuleOperation);
		operation.execute(null);
	}

	public boolean remove(String moduleName) {
		ModuleOperation operation = facade.getModuleOperationRegistry().getOperation(
				ModuleOperationConstants.RemoveModuleOperation);
		ModuleOperationInput moduleOperationInput = new ModuleOperationInput(null, null, moduleName);
		return operation.execute(moduleOperationInput).isSuccess();
	}

	public void addModule(final ModuleDefinition moduleDefinition) {
		ModuleOperation operation = facade.getModuleOperationRegistry().getOperation(
				ModuleOperationConstants.AddModuleOperation);
		ModuleOperationInput moduleOperationInput = new ModuleOperationInput(null, moduleDefinition, null);
		operation.execute(moduleOperationInput);
	}

	/* **************************** getters ************************** */

	public boolean hasModule(String moduleName) {
		RootModuleDefinition rootModuleDefinition = getModuleStateHolder().getRootModuleDefinition();
		return (rootModuleDefinition.findChildDefinition(moduleName, true) != null);
	}

	public String findLike(String moduleName) {
		RootModuleDefinition rootModuleDefinition = getModuleStateHolder().getRootModuleDefinition();
		ModuleDefinition definition = rootModuleDefinition.findChildDefinition(moduleName, false);
		if (definition != null) {
			return definition.getName();
		}
		return null;
	}

	public ApplicationContext getRootContext() {
		ConfigurableApplicationContext context = internalGet();
		if (context == null) {
			throw new NoServiceException("No root application has been loaded");
		}
		return context;
	}

	public ApplicationContext getModuleContext(String moduleName) {
		ApplicationContext context = getModuleStateHolder().getModule(moduleName);
		if (context == null) {
			throw new NoServiceException("No application context could be found for module " + moduleName);
		}
		return context;
	}

	@SuppressWarnings("unchecked")
	public <T extends Object> T getBean(String beanName, Class<T> t) {
		ApplicationContext context = getRootContext();

		Object bean = getBean(beanName, t, context);
		return (T) bean;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Object> T getModuleBean(String moduleName, String beanName, Class<T> t) {
		ApplicationContext context = getModuleContext(moduleName);
		return (T) getBean(beanName, t, context);
	}

	public RootModuleDefinition getRootModuleDefinition() {
		return getModuleStateHolder().getRootModuleDefinition();
	}

	/*
	 * ******************* InternalOperationsFacade methods
	 * **************************
	 */

	public ApplicationContext getModule(String moduleName) {
		return getModuleStateHolder().getModule(moduleName);
	}
	
	public ModuleManagementFacade getModuleManagementFacade() {
		if (facade == null) {
			throw new IllegalStateException("Operations Facade not initialised");
		}
		return facade;
	}

	/* **************************** private methods ************************** */

	private <T> Object getBean(String beanName, Class<T> requiredType, ApplicationContext context) {
		Assert.notNull(requiredType);
		
		Object bean = null;
		try {
			bean = context.getBean(beanName);
			
			// Check if required type matches the type of the actual bean instance.
			if (!requiredType.isAssignableFrom(bean.getClass())) {
				throw new InvalidBeanTypeException(beanName, requiredType, bean.getClass());
			}
			
		}
		catch (BeansException e) {
			e.printStackTrace();
			throw e;
		}
		return bean;
	}

	protected ModuleStateHolder getModuleStateHolder() {
		return moduleStateHolder;
	}

	/* **************************** private methods ************************** */

	private ConfigurableApplicationContext internalGet() {
		ModuleStateHolder stateHolder = getModuleStateHolder();
		return stateHolder.getRootModuleContext();
	}

}
