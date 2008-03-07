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

import java.util.Arrays;
import java.util.List;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

public abstract class SimpleBaseModuleLocationResolver extends AbstractModuleLocationResolver {

	private String[] rootProjects;
	
	private String workspaceRoot;
	
	public void init() {
		//FIXME this may not be being called for web classLocationResolver
		Assert.notNull(rootProjects, "rootProjects cannot be null");
		Assert.notNull(workspaceRoot, "workspaceRoot cannot be null");
		Assert.isTrue(rootProjects.length > 0, "rootProjects cannot be empty");
	}
	
	public List<String> getRootProjects() {
		return Arrays.asList(rootProjects);
	}

	@Override
	protected String getWorkspaceRoot() {
		return workspaceRoot;
	}

	public void setRootProjectString(String rootProjects) {
		this.rootProjects = StringUtils.tokenizeToStringArray(rootProjects, " ,");
	}

	public void setWorkspaceRoot(String workspaceRoot) {
		this.workspaceRoot = workspaceRoot;
	}

}
