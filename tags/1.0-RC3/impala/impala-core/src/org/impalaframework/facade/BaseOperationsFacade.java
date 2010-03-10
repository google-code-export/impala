/*
 * Copyright 2007-2010 the original author or authors.
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
import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.ModuleDefinitionSource;
import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.module.RuntimeModule;
import org.impalaframework.module.definition.ConstructedModuleDefinitionSource;
import org.impalaframework.module.operation.ModuleOperation;
import org.impalaframework.module.operation.ModuleOperationConstants;
import org.impalaframework.module.operation.ModuleOperationInput;
import org.impalaframework.module.operation.ModuleOperationResult;
import org.impalaframework.module.spi.ApplicationManager;
import org.impalaframework.module.spi.ModuleStateHolder;
import org.impalaframework.startup.ClassPathApplicationContextStarter;
import org.impalaframework.startup.ContextStarter;
import org.impalaframework.util.ObjectUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

/**
 * Abstract base implementation of <code>InternalOperationsFacade</code>.
 * Subclasses must provide implementation of abstract method
 * <code>getBootstrapContextLocations()</code>, which contains the bean
 * definitions for the bootstrap <code>ApplicationContext</code> used to
 * initialize Impala.
 * 
 * @see #getBootstrapContextLocations()
 * @author Phil Zoio
 */
public abstract class BaseOperationsFacade implements InternalOperationsFacade {
    
    private ApplicationManager applicationManager;

    private ModuleManagementFacade facade;

    /*
     * **************************** initialising operations * **************************
     */
    
    public BaseOperationsFacade(ModuleManagementFacade facade) {
        Assert.notNull(facade, "facade cannot be null");
        this.facade = facade;
        this.applicationManager = facade.getApplicationManager();
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

        this.facade = ObjectUtils.cast(applicationContext.getBean("moduleManagementFacade"),
                ModuleManagementFacade.class);
        this.applicationManager = facade.getApplicationManager();
    }

    protected ContextStarter getContextStarter() {
        return new ClassPathApplicationContextStarter();
    }

    public void init(ModuleDefinitionSource source) {
        ModuleOperation operation = facade.getModuleOperationRegistry().getOperation(
                ModuleOperationConstants.IncrementalUpdateRootModuleOperation);
        ModuleOperationInput moduleOperationInput = new ModuleOperationInput(source, null, null);
        execute(operation, moduleOperationInput);
    }

    /*
     * **************************** modifying operations * **************************
     */

    /**
     * Attempts to reload named module. Returns true if module is found and is successfully reloaded
     */
    public boolean reloadModule(String moduleName) {
        ModuleOperation operation = facade.getModuleOperationRegistry().getOperation(
                ModuleOperationConstants.ReloadNamedModuleOperation);
        ModuleOperationInput moduleOperationInput = new ModuleOperationInput(null, null, moduleName);
        ModuleOperationResult result = execute(operation, moduleOperationInput);
        return result.isSuccess();
    }

    /**
     * Reload module with name which contains that of supplied module name. Returns name of actual module reloaded.
     */
    public String reloadModuleLike(String moduleName) {
        String like = findModuleNameLike(moduleName);
        if (like != null) {
            reloadModule(like);
        }
        return like;
    }

    /**
     * Attempts to reload the root module
     */
    public void reloadRootModule() {
        RootModuleDefinition rootModuleDefinition = getModuleStateHolder().getRootModuleDefinition();
        ModuleOperation operation = facade.getModuleOperationRegistry().getOperation(
                ModuleOperationConstants.CloseRootModuleOperation);
        execute(operation, null);
        ConstructedModuleDefinitionSource newModuleDefinitionSource = new ConstructedModuleDefinitionSource(
                rootModuleDefinition);

        ModuleOperationInput input = new ModuleOperationInput(newModuleDefinitionSource, null, null);
        operation = facade.getModuleOperationRegistry().getOperation(
                ModuleOperationConstants.UpdateRootModuleOperation);
        execute(operation, input);
    }
    
    /**
     * Attempts to repair any modules which are in an error state
     */
    public void repairModules() {
        RootModuleDefinition rootModuleDefinition = getModuleStateHolder().getRootModuleDefinition();
        ModuleOperation operation = facade.getModuleOperationRegistry().getOperation(
                ModuleOperationConstants.RepairModuleOperation);
        ConstructedModuleDefinitionSource newModuleDefinitionSource = new ConstructedModuleDefinitionSource(
                rootModuleDefinition);

        ModuleOperationInput input = new ModuleOperationInput(newModuleDefinitionSource, null, null);
        execute(operation, input);
    }

    /**
     * Unloads all modules, starting from the root module
     */
    public void unloadRootModule() {
        ModuleOperation operation = facade.getModuleOperationRegistry().getOperation(
                ModuleOperationConstants.CloseRootModuleOperation);
        execute(operation, null);
    }

    /**
     * Unloads and removes the named module
     */
    public boolean removeModule(String moduleName) {
        ModuleOperation operation = facade.getModuleOperationRegistry().getOperation(
                ModuleOperationConstants.RemoveModuleOperation);
        ModuleOperationInput moduleOperationInput = new ModuleOperationInput(null, null, moduleName);
        return execute(operation, moduleOperationInput).isSuccess();
    }

    /**
     * Adds the module as determined by the supplied module definition
     */
    public void addModule(final ModuleDefinition moduleDefinition) {
        ModuleOperation operation = facade.getModuleOperationRegistry().getOperation(
                ModuleOperationConstants.AddModuleOperation);
        ModuleOperationInput moduleOperationInput = new ModuleOperationInput(null, moduleDefinition, null);
        execute(operation, moduleOperationInput);
    }

    /* **************************** getters ************************** */

    /**
     * Returns true of a module definition is present for the named module
     */
    public boolean hasModule(String moduleName) {
        RootModuleDefinition rootModuleDefinition = getModuleStateHolder().getRootModuleDefinition();
        return (rootModuleDefinition.findChildDefinition(moduleName, true) != null);
    }

    /**
     * Returns the name of the module which matches the supplied module name, that is, the
     * first module found whose name contains that of the supplied module name
     */
    public String findModuleNameLike(String moduleName) {
        RootModuleDefinition rootModuleDefinition = getModuleStateHolder().getRootModuleDefinition();
        ModuleDefinition definition = rootModuleDefinition.findChildDefinition(moduleName, false);
        if (definition != null) {
            return definition.getName();
        }
        return null;
    }

    /**
     * Gets the {@link RuntimeModule} associated with the root module
     */
    public RuntimeModule getRootRuntimeModule() {
        RuntimeModule runtimeModule = getModuleStateHolder().getRootModule();
        if (runtimeModule == null) {
            throw new NoServiceException("No root application has been loaded");
        }
        return runtimeModule;
    }

    @SuppressWarnings("unchecked")
    public <T extends Object> T getBean(String beanName, Class<T> t) {
        
        RuntimeModule runtimeModule = getRootRuntimeModule();
        return (T) checkBeanType(runtimeModule, beanName, t);
    }
    
    @SuppressWarnings("unchecked")
    public <T extends Object> T getModuleBean(String moduleName, String beanName, Class<T> t) {
        
        RuntimeModule runtimeModule = getRuntimeModule(moduleName);
        return (T) checkBeanType(runtimeModule, beanName, t);
    }

    public RootModuleDefinition getRootModuleDefinition() {
        return getModuleStateHolder().getRootModuleDefinition();
    }

    /*
     * ******************* InternalOperationsFacade methods * **************************
     */

    public RuntimeModule getRuntimeModule(String moduleName) {
        final RuntimeModule runtimeModule = getModuleStateHolder().getModule(moduleName);
        
        if (runtimeModule == null) {
            throw new NoServiceException("No module named '" + moduleName + "' has been loaded");
        }
        
        return runtimeModule;
    }
    
    public ModuleManagementFacade getModuleManagementFacade() {
        if (facade == null) {
            throw new IllegalStateException("Operations Facade not initialised. Has Impala been initialized?");
        }
        return facade;
    }
    
    /* **************************** protected methods ************************** */

    protected ModuleStateHolder getModuleStateHolder() {
        
        if (applicationManager == null) {
            throw new NoServiceException("Module state holder not present. Has Impala been initialized?");
        }
        return applicationManager.getCurrentApplication().getModuleStateHolder();
    }
    
    protected final ModuleOperationResult execute(ModuleOperation operation, ModuleOperationInput moduleOperationInput) {
        return operation.execute(applicationManager.getCurrentApplication(), moduleOperationInput);
    }

    /* **************************** private methods ************************** */


    private <T> Object checkBeanType(RuntimeModule runtimeModule, String beanName, Class<T> requiredType) {
        Assert.notNull(requiredType);
        Assert.notNull(runtimeModule);
        Assert.notNull(beanName);
        Object bean = runtimeModule.getBean(beanName);
        
        try {
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
}
