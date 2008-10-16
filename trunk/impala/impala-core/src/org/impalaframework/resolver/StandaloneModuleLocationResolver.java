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
import java.util.Properties;

import org.impalaframework.constants.LocationConstants;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

/**
 * Implementation of {@link ModuleLocationResolver} designed for use in standalone environments. Used in interactive test runner.
 * Module classes and tests both by default go in the <i>bin</i> directory. These can be overridden, either as system property
 * or wired in via {@link Properties} in constructor. Relevant property names are:
 * {@link LocationConstants#MODULE_CLASS_DIR_PROPERTY} and  {@link LocationConstants#MODULE_TEST_DIR_PROPERTY}
 * @author Phil Zoio
 */
public class StandaloneModuleLocationResolver extends BaseModuleLocationResolver {

	public StandaloneModuleLocationResolver() {
		super();
		init();
	}

	public StandaloneModuleLocationResolver(Properties properties) {
		super(properties);
		init();
	}
	
	protected void init() {
		super.init();

		// the module directory which is expected to contain classes
		mergeProperty(LocationConstants.MODULE_CLASS_DIR_PROPERTY, "bin", null);

		// the parent directory in which tests are expected to be found
		mergeProperty(LocationConstants.MODULE_TEST_DIR_PROPERTY, "bin", null);
	}

	public List<Resource> getModuleTestClassLocations(String moduleName) {
		String classDir = StringUtils.cleanPath(getProperty(LocationConstants.MODULE_TEST_DIR_PROPERTY));
		return getResources(moduleName, classDir);
	}

	public List<Resource> getApplicationModuleClassLocations(String moduleName) {
		String classDir = getProperty(LocationConstants.MODULE_CLASS_DIR_PROPERTY);
		return getResources(moduleName, classDir);
	}
	
	@Override
	public Resource getRootDirectory() {
		Resource rootDirectory = super.getRootDirectory();
		if (rootDirectory != null) {
			return rootDirectory;
		}
		// note that if workspace root is not specified, then parent directory
		// is used
		return new FileSystemResource("../");
	}

}
