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


import org.springframework.context.ConfigurableApplicationContext;

/**
 * Abstraction for loading a {@link RuntimeModule} from a {@link ModuleDefinition}.
 * The default implementation is Spring based. It will load a {@link org.impalaframework.spring.module.SpringRuntimeModule},
 * which will itself contain a {@link ConfigurableApplicationContext} instance.
 * @author Phil Zoio
 */
public interface ModuleRuntime {
	
	RuntimeModule loadRuntimeModule(ModuleDefinition definition);
	
	public RuntimeModule getRootRuntimeModule();

	public RuntimeModule getRuntimeModule(String moduleName);

	void closeModule(RuntimeModule runtimeModule);
	
}