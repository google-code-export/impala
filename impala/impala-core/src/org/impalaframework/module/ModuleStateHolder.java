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

package org.impalaframework.module;

import java.util.Map;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * The main job of the `ModuleStateHolder` is to maintain information on the
 * state of modules loaded. This includes references to the
 * {@link ApplicationContext}s which back the modules, as well as to currently
 * loaded {@link ModuleDefinition}s. It also exposes a few convenience methods
 * for querying the state of the module hierarchy.
 * 
 * @author Phil Zoio
 */
public interface ModuleStateHolder extends ModuleDefinitionSource {
	
	//FIXME add methods for flagging application context as under transition, in order to offer extra concurrency protection

	void processTransitions(TransitionSet transitions);

	ConfigurableApplicationContext getRootModuleContext();

	ConfigurableApplicationContext getModule(String name);

	RootModuleDefinition getRootModuleDefinition();

	RootModuleDefinition cloneRootModuleDefinition();

	boolean hasModule(String name);

	boolean hasRootModuleDefinition();

	Map<String, ConfigurableApplicationContext> getModuleContexts();

	void putModule(String name, ConfigurableApplicationContext context);

	ConfigurableApplicationContext removeModule(String name);

}