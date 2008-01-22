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
import java.util.List;

import org.springframework.core.io.Resource;

public interface ModuleLocationResolver {

	/**
	 * Returns the root directory for modules for the current application
	 */
	public File getRootDirectory();
	
	/**
	 * Returns the namse of the root project
	 */
	public List<String> getRootProjects();
	
	/**
	 * Returns the directory locations for test classes for a parent project
	 */
	public List<Resource> getModuleTestClassLocations(String moduleName);

	/**
	 * Returns the directory locations for module classes
	 */
	public List<Resource> getApplicationModuleClassLocations(String moduleName);

}
