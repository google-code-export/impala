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

package org.impalaframework.bootstrap;

import java.util.List;

import org.impalaframework.config.BooleanPropertyValue;
import org.impalaframework.config.PropertySource;
import org.impalaframework.config.StringPropertyValue;
import org.impalaframework.exception.ConfigurationException;

public class SimpleContextLocationResolver implements ContextLocationResolver {

	public void addContextLocations(List<String> contextLocations, PropertySource propertySource) {
		
		contextLocations.add("META-INF/impala-bootstrap.xml");
		
		//add context associated with class loader type
		addModuleType(contextLocations, propertySource);
		
		//add context indicating parent class loader first
		addParentClassLoaderFirst(contextLocations, propertySource);
		
	}

	protected void addParentClassLoaderFirst(List<String> contextLocations,
			PropertySource propertySource) {
		BooleanPropertyValue parentClassLoaderFirst = new BooleanPropertyValue(propertySource, "parentClassLoaderFirst", false);
		if (parentClassLoaderFirst.getValue()) {
			contextLocations.add("META-INF/impala-parent-loader-bootstrap.xml");
		}
	}

	protected void addModuleType(List<String> contextLocations,
			PropertySource propertySource) {
		//check the classloader type
		StringPropertyValue classLoaderType = new StringPropertyValue(propertySource, "moduleType", "hierarchical");
		
		final String value = classLoaderType.getValue();
		if ("shared".equalsIgnoreCase(value)) {
			contextLocations.add("META-INF/impala-shared-loader-bootstrap.xml");
		} else if ("graph".equalsIgnoreCase(value)) {
			contextLocations.add("META-INF/impala-graph-bootstrap.xml");
		} else if ("hierarchical".equalsIgnoreCase(value)) {
			//nothing to do here
		} else {
			throw new ConfigurationException("Invalid value for property 'moduleType': " + value);
		}
	}

}
