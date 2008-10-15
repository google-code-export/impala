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

package org.impalaframework.resolver;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

/**
 * Extension of {@link SimpleBaseModuleLocationResolver} whose class and test directories 
 * are wired in through depedency injection.
 * @author Phil Zoio
 */
public class SimpleModuleLocationResolver extends SimpleBaseModuleLocationResolver {

	private String moduleClassDirectory;
	private String moduleTestDirectory;
	
	public void init() {
		super.init();
		Assert.notNull(moduleClassDirectory, "moduleClassDirectory cannot be null");
		Assert.notNull(moduleTestDirectory, "moduleTestDirectory cannot be null");
	}

	public List<Resource> getApplicationModuleClassLocations(String moduleName) {
		return getResources(moduleName, moduleClassDirectory);
	}
	
	public List<Resource> getModuleTestClassLocations(String moduleName) {
		return getResources(moduleName, moduleTestDirectory);
	}

	public void setModuleTestDirectory(String moduleTestDirectory) {
		this.moduleTestDirectory = moduleTestDirectory;
	}

	public void setModuleClassDirectory(String moduleClassDirectory) {
		this.moduleClassDirectory = moduleClassDirectory;
	}

}
