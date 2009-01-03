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

package org.impalaframework.web.bootstrap;

/**
 * Configuration properties which are specific to running Impala in a web environment.
 * @author Phil Zoio
 */
public interface WebBootstrapProperties {

	/**
	 * Whether modules should be automatically reloaded if the file/directory
	 * containing the modules has modified. If true, a background thread will
	 * monitor module files/folders and automatically reload modules when
	 * changes are detected. Default is <b>false</b>.
	 */
	String AUTO_RELOAD_MODULES = "auto.reload.modules";
	
	/**
	 * Whether modules should be multi-module aware.
	 */
	// Issue 142 - need to tighten up on this concept:
	// - split out the mechanism for protecting session from module reloads
	// - split out the mechanism for partitioning servlet context
	// - make both more easily configurable without need for additional Spring config files
	String WEB_MULTI_MODULE = "web.multi.module";
	
	/**
	 * This property must be set to true if you want to run a web application
	 * embedded within the IDE, without any specific packaging required.
	 * Suitable for a fast develop/deploy/test cycle. The alternative involves
	 * creating a war file with modules placed in jars files in
	 * <i>WEB-INF/modules</i>. This is the default, and is the valid production
	 * setting.
	 */
	String EMBEDDED_MODE = "embedded.mode";

}
