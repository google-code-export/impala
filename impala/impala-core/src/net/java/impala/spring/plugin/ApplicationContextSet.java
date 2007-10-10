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

package net.java.impala.spring.plugin;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.Assert;

public class ApplicationContextSet {

	private ConfigurableApplicationContext context;

	private Map<String, ConfigurableApplicationContext> pluginContext = new ConcurrentHashMap<String, ConfigurableApplicationContext>();

	public ApplicationContextSet(ConfigurableApplicationContext context) {
		super();
		Assert.notNull(context);
		this.context = context;
	}

	public ConfigurableApplicationContext getContext() {
		return context;
	}

	public Map<String, ConfigurableApplicationContext> getPluginContext() {
		return pluginContext;
	}

}
