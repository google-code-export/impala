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
import org.impalaframework.facade.InternalOperationsFacade;
import org.impalaframework.facade.ParentReloadingOperationsFacade;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.springframework.context.ApplicationContext;

public class DynamicContextHolder {

	private static InternalOperationsFacade facade;

	/*
	 * **************************** initialising operations
	 * **************************
	 */

	public static void init(boolean reloadableParent) {
		if (facade == null) {
			if (reloadableParent)
				facade = new ParentReloadingOperationsFacade();
			else
				facade = new DefaultOperationsFacade();
		}
	}

	public static void init(ModuleDefinitionSource source) {
		init(false);
		getFacade().init(source);
	}

	/*
	 * **************************** modifying operations
	 * **************************
	 */

	public static boolean reload(String plugin) {
		return getFacade().reload(plugin);
	}

	public static boolean reload(ModuleDefinitionSource source, String plugin) {
		return getFacade().reload(source, plugin);
	}

	public static String reloadLike(ModuleDefinitionSource source, String plugin) {
		return getFacade().reloadLike(source, plugin);
	}

	public static String reloadLike(String plugin) {
		return getFacade().reloadLike(plugin);
	}

	public static void reloadParent() {
		getFacade().reloadAll();
	}

	public static void unloadParent() {
		getFacade().unloadParent();
	}

	public static boolean remove(String plugin) {
		return getFacade().remove(plugin);
	}

	public static void addPlugin(final ModuleDefinition moduleDefinition) {
		getFacade().addPlugin(moduleDefinition);
	}

	/* **************************** getters ************************** */

	public static boolean hasPlugin(String plugin) {
		return getFacade().hasModule(plugin);
	}

	public static String findLike(ModuleDefinitionSource source, String plugin) {
		return getFacade().findLike(source, plugin);
	}

	public static ApplicationContext get() {
		return getFacade().getRootContext();
	}

	@SuppressWarnings("unchecked")
	public static <T extends Object> T getBean(String beanName, Class<T> t) {
		return getFacade().getBean(beanName, t);
	}

	@SuppressWarnings("unchecked")
	public static <T extends Object> T getPluginBean(String pluginName, String beanName, Class<T> t) {
		return getFacade().getPluginBean(pluginName, beanName, t);
	}

	static ApplicationContext getModule(String pluginName) {
		return getFacade().getModule(pluginName);
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
