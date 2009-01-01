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
import org.impalaframework.util.InstantiationUtils;
import org.springframework.util.StringUtils;

/**
 * Implementation of {@link ContextLocationResolver} with responsiblility for
 * determining which of the context locations to add from those available in the
 * impala-core module. Uses properties from the supplied {@link PropertySource}
 * instance to make these determiniations.
 * 
 * Also responsible for delegating to the JMX
 * {@link org.impalaframework.jmx.bootstrap.JMXContextLocationResolver} if the
 * relevant classes are present on the class path.
 * 
 * @author Phil Zoio
 */
public class SimpleContextLocationResolver implements ContextLocationResolver {

	public boolean addContextLocations(List<String> contextLocations, PropertySource propertySource) {
		
		if (!explicitlySetLocations(contextLocations, propertySource)) {
		
			addDefaultLocations(contextLocations);
			
			//add context associated with class loader type
			addModuleType(contextLocations, propertySource);
			
			//add context indicating parent class loader first
			//FIXME wire in PropertySourceValueFactoryBean, no need for separate XML file
			addParentClassLoaderFirst(contextLocations, propertySource);
			
			maybeAddJmxLocations(contextLocations, propertySource);
		
			explicitlyAddLocations(contextLocations, propertySource);
			
			return false;
		} else {
			return true;
		}
	}
	
	protected boolean explicitlySetLocations(List<String> contextLocations, PropertySource propertySource) {
		boolean added = addNamedLocations(contextLocations, propertySource, CoreBootstrapProperties.ALL_LOCATIONS);
		
		//TODO line left in for backward compatiblity. Remove after 1.0M5
		if (!added) added = addNamedLocations(contextLocations, propertySource, CoreBootstrapProperties.BOOTSTRAP_LOCATIONS);
		return added;
	}

	protected void addDefaultLocations(List<String> contextLocations) {
		contextLocations.add("META-INF/impala-bootstrap.xml");
	}

	protected void addParentClassLoaderFirst(List<String> contextLocations,	PropertySource propertySource) {
		
		BooleanPropertyValue parentClassLoaderFirst = new BooleanPropertyValue(propertySource, CoreBootstrapProperties.PARENT_CLASS_LOADER_FIRST, true);
		if (parentClassLoaderFirst.getValue()) {
			contextLocations.add("META-INF/impala-parent-loader-bootstrap.xml");
		}
	}

	protected void addModuleType(List<String> contextLocations,	PropertySource propertySource) {
		
		//check the classloader type
		//FIXME default set to graph
		StringPropertyValue classLoaderType = new StringPropertyValue(propertySource, CoreBootstrapProperties.MODULE_TYPE, "graph");
		
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

	protected void maybeAddJmxLocations(List<String> contextLocations, PropertySource propertySource) {
		ContextLocationResolver c = null;
		try {
			c = InstantiationUtils.instantiate("org.impalaframework.jmx.bootstrap.JMXContextLocationResolver");
			c.addContextLocations(contextLocations, propertySource);
		} catch (Exception e) {
		}
	}

	public void explicitlyAddLocations(List<String> contextLocations, PropertySource propertySource) {
		addNamedLocations(contextLocations, propertySource, CoreBootstrapProperties.EXTRA_LOCATIONS);
	}

	private boolean addNamedLocations(List<String> contextLocations,
			PropertySource propertySource, final String propertyName) {
		StringPropertyValue allLocations = new StringPropertyValue(propertySource, propertyName, null);
		final String allLocationsValue = allLocations.getValue();
		if (allLocationsValue != null) {
			final String[] allLocationsArray = StringUtils.tokenizeToStringArray(allLocationsValue, " ,");
			final String[] fullNamesArray = getFullNames(allLocationsArray);
			for (String location : fullNamesArray) {
				contextLocations.add(location);
			}
			return true;
		}
		
		return false;
	}
	
	String[] getFullNames(String[] abridgedNames) {
		String[] fullNames = new String[abridgedNames.length];
		
		for (int i = 0; i < abridgedNames.length; i++) {
			if (!abridgedNames[i].endsWith(".xml"))
				fullNames[i] = "META-INF/impala-" + abridgedNames[i] + ".xml";
			else
				fullNames[i] = abridgedNames[i];
		}
		
		return fullNames;
	}

}
