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

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.exception.InvalidStateException;
import org.impalaframework.util.PathUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

/**
 * {@link ModuleLocationResolver} suitable for using when deploying modules as individual Jar files
 * @author Phil Zoio
 */
public class SimpleJarModuleLocationResolver extends SimpleBaseModuleLocationResolver {
	
	private String applicationVersion;
	
	private ModuleResourceFinder resourceFinder = new JarModuleResourceFinder();

	public SimpleJarModuleLocationResolver() {
		super();
	}

	public List<Resource> getApplicationModuleClassLocations(String moduleName) {
		Resource workspaceRoot = getRootDirectory();
		
		// return a classpath resource representing from a jar
		File workspaceRootFile = null;
		try {
			workspaceRootFile = workspaceRoot.getFile();
		}
		catch (IOException e) {
			throw new ConfigurationException("Unable to get file for root resource " + workspaceRoot);
		}
		
		String moduleVersion = this.applicationVersion;

		List<Resource> resources = resourceFinder.findJarResources(workspaceRootFile, moduleName, moduleVersion);
		
		if (resources.isEmpty()) {
			throw new InvalidStateException("Unable to find any resources in workspace file '" 
					+ PathUtils.getAbsolutePath(workspaceRootFile)
					+ "', module name '" + moduleName
					+ "', module version '" + moduleVersion + "'");
		}
		
		return resources;
	}

	public List<Resource> getModuleTestClassLocations(String moduleName) {
		// not sure what to do with this as this is a test
		throw new UnsupportedOperationException();
	}

	public void setApplicationVersion(String applicationVersion) {
		this.applicationVersion = applicationVersion;
	}

}

class JarModuleResourceFinder implements ModuleResourceFinder {
	
	public List<Resource> findJarResources(
			File workspaceRootFile,
			String moduleName, 
			String moduleVersion) {
		
		String jarName = moduleName;
		String jarWithVersionName = null;
		
		if (moduleVersion != null){
			jarWithVersionName = jarName + "-" + moduleVersion;
		}
		
		Resource resource = null;
		
		if (jarWithVersionName != null)
			resource = findJarFile(workspaceRootFile, jarWithVersionName);
		if (resource == null) {
			resource = findJarFile(workspaceRootFile, jarName);
		}
		
		if (resource == null) {
			return Collections.emptyList();
		}
		return Collections.singletonList(resource);
	}

	private Resource findJarFile(File workspaceRootFile, String jarName) {
		String path = PathUtils.getPath(workspaceRootFile.getAbsolutePath(), jarName + ".jar");
		Resource resource = new FileSystemResource(path);
		
		if (resource.exists()) {
			return resource;
		} else {
			return null;
		}
	}
}
