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

import java.util.Collections;
import java.util.List;

import org.impalaframework.util.PathUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

public class FileModuleResourceFinder implements ModuleResourceFinder {

	private String classDirectory;
	
	public List<Resource> findResources(String workspaceRootPath,
			String moduleName, String moduleVersion) {
		return getResources(workspaceRootPath, moduleName);
	}
	
	protected List<Resource> getResources(String workspaceRootPath, String moduleName) {
		String path = PathUtils.getPath(workspaceRootPath, moduleName);
		path = PathUtils.getPath(path, classDirectory);
		Resource resource = new FileSystemResource(path);
		if (resource.exists())
			return Collections.singletonList(resource);
		else
			return Collections.emptyList();
	}

	public void setClassDirectory(String classDirectory) {
		this.classDirectory = classDirectory;
	}

}
