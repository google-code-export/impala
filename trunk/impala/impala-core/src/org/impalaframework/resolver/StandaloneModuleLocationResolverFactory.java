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

import java.util.Properties;

import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.util.PropertyUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

/**
 * @author Phil Zoio
 */
public class StandaloneModuleLocationResolverFactory implements ModuleLocationResolverFactory {

	static String EXECUTION_PROPERTIES_FILE_PATH = "impala.execution.file.path";

	static String EXECUTION_PROPERTIES_FILE_NAME = "impala.execution.file.name";

	public ModuleLocationResolver getClassLocationResolver() {

		// check for system property for property file as an absolute location
		String filePath = System.getProperty(EXECUTION_PROPERTIES_FILE_PATH);

		if (filePath != null) {
			FileSystemResource r = new FileSystemResource(filePath);
			if (!r.exists()) {
				throw new ConfigurationException("System property '" + EXECUTION_PROPERTIES_FILE_PATH
						+ "' points to location which does not exist: " + filePath);
			}

			return load(r);
		}
		else {

			String fileName = System.getProperty(EXECUTION_PROPERTIES_FILE_NAME);

			if (fileName != null) {
				ClassPathResource r = new ClassPathResource(fileName);
				if (!r.exists()) {
					throw new ConfigurationException("System property '" + EXECUTION_PROPERTIES_FILE_NAME
							+ "' points to classpath location which could not be found: " + fileName);
				}
				return load(r);
			}
		}
		
		ClassPathResource defaultResource = new ClassPathResource("execution-locations.properties");
		if (defaultResource.exists()) {
			return load(defaultResource);
		}
		
		StandaloneModuleLocationResolver resolver = new StandaloneModuleLocationResolver();
		return resolver;
	}

	private ModuleLocationResolver load(Resource r) {
		Properties props = PropertyUtils.loadProperties(r);
		return new StandaloneModuleLocationResolver(props);
	}
}
