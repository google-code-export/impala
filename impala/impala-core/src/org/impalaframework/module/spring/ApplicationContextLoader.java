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

package org.impalaframework.module.spring;


import org.impalaframework.module.DelegatingContextLoader;
import org.impalaframework.module.ModuleLoader;
import org.impalaframework.module.definition.ModuleDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * {@link ApplicationContextLoader} is responsible for the overall task of
 * loading a module-specific ApplicationContext given a {@link ModuleDefinition}
 * and parent {@link ApplicationContext}. Unlike {@link ModuleLoader} and
 * {@link DelegatingContextLoader}, {@link ApplicationContextLoader} is not
 * module type-specific. Instead, it has a broader responsibility. The default
 * implementation, {@link org.impalaframework.module.spring.loader.DefaultApplicationContextLoader} uses the appropriate
 * type specific instance of {@link ModuleLoader} or
 * {@link DelegatingContextLoader} to perform the actual module load.
 * 
 * @see ModuleLoader
 * @see DelegatingContextLoader
 * @see org.impalaframework.module.spring.loader.DefaultApplicationContextLoader
 * @author Phil Zoio
 */
public interface ApplicationContextLoader {
	
	ConfigurableApplicationContext loadContext(ModuleDefinition definition, ApplicationContext parent);
	
}