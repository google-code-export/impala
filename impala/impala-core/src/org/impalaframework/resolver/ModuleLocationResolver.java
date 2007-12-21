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

package org.impalaframework.resolver;

import java.io.File;

public interface ModuleLocationResolver {

	/**
	 * Returns the name of the parent project
	 */
	public String getParentProject();
	
	/**
	 * Returns the directory locations for test classes for a parent project
	 */
	public File[] getPluginTestClassLocations(String projectName);

	/**
	 * Returns the directory locations for plugin classes
	 */
	public File[] getApplicationModuleClassLocations(String plugin);

	/**
	 * Returns the directory location for Spring context files for a particular plugin
	 */
	public File getApplicationModuleSpringLocation(String plugin);

}
