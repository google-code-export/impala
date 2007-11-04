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

package org.impalaframework.spring.util;


import org.impalaframework.spring.monitor.PluginMonitor;
import org.impalaframework.spring.plugin.ApplicationContextSet;
import org.impalaframework.spring.plugin.PluginSpec;
import org.springframework.context.ApplicationContext;

/**
 * @author Phil Zoio
 */
public interface ApplicationContextLoader {

	//FIXME could make setSpringContextHolder available as a field
	//in ApplicationContextLoader. Then it will be possible to register 
	//bean post processors which can can call SpringContextHolder
	//Problem: don't want plugins to be able to load/unload themselves
	
	void loadParentContext(ApplicationContextSet appSet, PluginSpec pluginSpec);

	void addApplicationPlugin(ApplicationContextSet appSet, PluginSpec plugin, ApplicationContext parent);
	
	PluginMonitor getPluginMonitor();

}