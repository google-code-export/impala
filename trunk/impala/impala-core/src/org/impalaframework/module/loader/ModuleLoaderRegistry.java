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

package org.impalaframework.module.loader;

import java.util.HashMap;
import java.util.Map;

import org.impalaframework.exception.NoServiceException;
import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.spi.ModuleLoader;
import org.impalaframework.module.spi.Registry;
import org.impalaframework.util.ObjectMapUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * Holds a mapping of {@link ModuleLoader} instances to module types, as
 * determined using the {@link ModuleDefinition#getType()} call.
 * 
 * @author Phil Zoio
 */
public class ModuleLoaderRegistry implements InitializingBean, Registry<ModuleLoader> {
	
	private Map<String, ModuleLoader> moduleLoaders = new HashMap<String, ModuleLoader>();
	
	private Map<String, ModuleLoader> extraModuleLoaders = new HashMap<String, ModuleLoader>();

	public void afterPropertiesSet() throws Exception {
		ObjectMapUtils.maybeOverwriteToLowerCase(moduleLoaders, extraModuleLoaders, "Extra module loader");
	}

	public ModuleLoader getModuleLoader(String type) {
		return getModuleLoader(type, true);
	}

	public ModuleLoader getModuleLoader(String type, boolean failIfNotFound) {
		Assert.notNull(type, "type cannot be null");
		ModuleLoader moduleLoader = moduleLoaders.get(type.toLowerCase());

		if (failIfNotFound) {
			if (moduleLoader == null) {
				throw new NoServiceException("No " + ModuleLoader.class.getName()
						+ " instance available for module definition type " + type);
			}
		}

		return moduleLoader;
	}

	public void addItem(String type, ModuleLoader moduleLoader) {
		Assert.notNull(type, "type cannot be null");
		moduleLoaders.put(type.toLowerCase(), moduleLoader);
	}

	public boolean hasModuleLoader(String type) {
		Assert.notNull(type, "type cannot be null");
		return (moduleLoaders.get(type.toLowerCase()) != null);
	}
	
	public void setModuleLoaders(Map<String, ModuleLoader> moduleLoaders) {
		this.moduleLoaders.clear();
		ObjectMapUtils.putToLowerCase(this.moduleLoaders, moduleLoaders, "Module loader");
	}

	public void setExtraModuleLoaders(Map<String, ModuleLoader> extraModuleLoaders) {
		this.extraModuleLoaders = extraModuleLoaders;
	}
	
}
