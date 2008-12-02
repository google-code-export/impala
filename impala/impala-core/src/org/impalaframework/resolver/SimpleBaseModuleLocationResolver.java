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

import org.impalaframework.exception.InvalidStateException;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

/**
 * {@link ModuleLocationResolver} implementation whose {@link #workspaceRoot} property is directly wired in.
 * @author Phil Zoio
 */
public abstract class SimpleBaseModuleLocationResolver extends AbstractModuleLocationResolver {
	
	private String workspaceRoot;
	
	public void init() {
		Assert.notNull(workspaceRoot, "workspaceRoot cannot be null");
	}

	@Override
	protected String getWorkspaceRoot() {
		return workspaceRoot;
	}

	public void setWorkspaceRoot(String workspaceRoot) {
		this.workspaceRoot = workspaceRoot;
	}

	protected void checkResources(List<Resource> resources, String moduleName,
			String moduleVersion, String rootDirectoryPath) {
		if (resources.isEmpty()) {
			throw new InvalidStateException("Unable to find any resources in workspace file '" 
					+ rootDirectoryPath
					+ "', module name '" + moduleName
					+ "', module version '" + moduleVersion + "'");
		}
	}

}
