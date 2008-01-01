/*
 * Copyright 2007 the original author or authors.
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

package org.impalaframework.testrun;

import org.impalaframework.exception.NoServiceException;
import org.impalaframework.facade.DefaultOperationsFacade;
import org.impalaframework.facade.FacadeConstants;
import org.impalaframework.facade.InternalOperationsFacade;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.util.InstantiationUtils;
import org.springframework.context.ApplicationContext;

public class DynamicContextHolder {

	private static InternalOperationsFacade facade;

	/*
	 * **************************** initialising operations
	 * **************************
	 */

	public static void init() {
		String facadeClassName = System.getProperty(FacadeConstants.FACADE_CLASS_NAME);
		
		if (facadeClassName == null) {
			facadeClassName = DefaultOperationsFacade.class.getName();
		}
		
		if (facade == null) {
			facade = InstantiationUtils.instantiate(facadeClassName);
		}
	}

	public static void init(ModuleDefinitionSource source) {
		init();
		getFacade().init(source);
	}

	/*
	 * **************************** modifying operations
	 * **************************
	 */

	public static boolean reload(String moduleName) {
		return getFacade().reload(moduleName);
	}

	public static String reloadLike(String moduleName) {
		return getFacade().reloadLike(moduleName);
	}

	public static void reloadParent() {
		getFacade().reloadAll();
	}

	public static void unloadParent() {
		getFacade().unloadParent();
	}

	public static boolean remove(String moduleName) {
		return getFacade().remove(moduleName);
	}

	public static void addModule(final ModuleDefinition moduleDefinition) {
		getFacade().addPlugin(moduleDefinition);
	}

	/* **************************** read-only methods ************************** */

	public static boolean hasModule(String moduleName) {
		return getFacade().hasModule(moduleName);
	}

	public static String findLike(ModuleDefinitionSource source, String moduleName) {
		return getFacade().findLike(source, moduleName);
	}

	public static ApplicationContext get() {
		return getFacade().getRootContext();
	}

	@SuppressWarnings("unchecked")
	public static <T extends Object> T getBean(String beanName, Class<T> type) {
		return getFacade().getBean(beanName, type);
	}

	@SuppressWarnings("unchecked")
	public static <T extends Object> T getPluginBean(String moduleName, String beanName, Class<T> typye) {
		return getFacade().getPluginBean(moduleName, beanName, typye);
	}

	static ApplicationContext getModule(String moduleName) {
		return getFacade().getModule(moduleName);
	}

	public static RootModuleDefinition getRootModuleDefinition() {
		return getFacade().getRootModuleDefinition();
	}

	private static InternalOperationsFacade getFacade() {
		if (facade == null) {
			throw new NoServiceException("The application has not been initialised. Has init(boolean) been called?");
		}
		return facade;
	}
	
}
