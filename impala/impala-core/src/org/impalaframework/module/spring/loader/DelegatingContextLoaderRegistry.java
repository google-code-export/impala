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

package org.impalaframework.module.spring.loader;

import java.util.HashMap;
import java.util.Map;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.spring.DelegatingContextLoader;
import org.impalaframework.util.ObjectMapUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * Holds a mapping of module types to {@link DelegatingContextLoader} instances, where module types are determined using the
 * call {@link ModuleDefinition#getType()}.
 *  
 * @author Phil Zoio
 */
public class DelegatingContextLoaderRegistry implements InitializingBean {

	private Map<String, DelegatingContextLoader> delegatingLoaders = new HashMap<String, DelegatingContextLoader>();

	private Map<String, DelegatingContextLoader> extraDelegatingLoaders = new HashMap<String, DelegatingContextLoader>();

	public void afterPropertiesSet() throws Exception {
		ObjectMapUtils.maybeOverwriteToLowerCase(delegatingLoaders, extraDelegatingLoaders, "Extra delegating context loader");
	}
	
	public void setDelegatingLoader(String type, DelegatingContextLoader moduleLoader) {
		Assert.notNull(type, "type cannot be null");
		delegatingLoaders.put(type.toLowerCase(), moduleLoader);
	}

	public DelegatingContextLoader getDelegatingLoader(String type) {
		Assert.notNull(type, "type cannot be null");
		return delegatingLoaders.get(type.toLowerCase());
	}

	public boolean hasDelegatingLoader(String type) {
		Assert.notNull(type, "type cannot be null");
		return (delegatingLoaders.get(type.toLowerCase()) != null);
	}

	public void setDelegatingLoaders(Map<String, DelegatingContextLoader> delegatingLoaders) {
		this.delegatingLoaders.clear();
		ObjectMapUtils.putToLowerCase(this.delegatingLoaders, delegatingLoaders, "Delegating context loader");
	}

	public void setExtraDelegatingLoaders(
			Map<String, DelegatingContextLoader> extraDelegatingLoaders) {
		this.extraDelegatingLoaders = extraDelegatingLoaders;
	}
	
}
