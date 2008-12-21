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

import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

/**
 * <p>
 * <code>ModuleLoader</code> defines common operations shared by module loader
 * implementations regardless of the runtime (Spring, etc.). Modules based on
 * Spring will implement the sub-interface
 * {@link org.impalaframework.spring.module.SpringModuleLoader}
 * 
 * @see org.impalaframework.spring.module.DelegatingContextLoader
 * @see org.impalaframework.spring.module.SpringModuleLoader
 * @author Phil Zoio
 */
public interface ModuleLoader {
	
	/**
	 * returns a new class loader for the module
	 */
	ClassLoader newClassLoader(ModuleDefinition moduleDefinition, ApplicationContext parent);
	
	/**
	 * Return an array of {@link Resource} instances which represent the locations from which module classes and resources are to be loaded
	 */
	Resource[] getClassLocations(ModuleDefinition moduleDefinition);
	
}
