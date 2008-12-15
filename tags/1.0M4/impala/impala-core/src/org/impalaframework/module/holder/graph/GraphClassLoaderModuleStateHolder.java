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

package org.impalaframework.module.holder.graph;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Extension of {@link GraphModuleStateHolder}, which also handles removal of 
 * module specific class loaders when {@link #removeModule(String)} is called.
 */
public class GraphClassLoaderModuleStateHolder extends GraphModuleStateHolder {

	private static final Log logger = LogFactory.getLog(GraphClassLoaderModuleStateHolder.class);

	private GraphClassLoaderRegistry classLoaderRegistry;
	
	/**
	 * Calls the superclass's {@link #removeModule(String)} method after
	 * removing the named module's {@link ClassLoader} from the
	 * {@link GraphClassLoaderRegistry}
	 */
	@Override
	public ConfigurableApplicationContext removeModule(String moduleName) {
		logger.info("Removing class loader from registry for module: " + moduleName);
		classLoaderRegistry.removeClassLoader(moduleName);
		return super.removeModule(moduleName);
	}

	public void setClassLoaderRegistry(GraphClassLoaderRegistry classLoaderRegistry) {
		this.classLoaderRegistry = classLoaderRegistry;
	}

}
