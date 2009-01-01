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

package org.impalaframework.jmx.bootstrap;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.bootstrap.ContextLocationResolver;
import org.impalaframework.config.BooleanPropertyValue;
import org.impalaframework.config.PropertySource;

public class JMXContextLocationResolver implements ContextLocationResolver {

	private static Log logger = LogFactory.getLog(JMXContextLocationResolver.class);
	
	public boolean addContextLocations(List<String> contextLocations,
			PropertySource propertySource) {

		//add jmx operartions
		addJmxOperations(contextLocations, propertySource);
		
		//whether to add mx4j adaptor
		addMx4jAdaptorContext(contextLocations, propertySource);
		
		return false;
	}

	void addMx4jAdaptorContext(List<String> contextLocations,
			PropertySource propertySource) {
		
		BooleanPropertyValue exposeMx4jAdaptor = new BooleanPropertyValue(propertySource, JMXBootstrapProperties.EXPOSE_MX4J_ADAPTOR, false);

		if (exposeMx4jAdaptor.getValue()) {
			
			if (contextLocations.contains("META-INF/impala-jmx-bootstrap.xml")) {

				boolean mx4jPresent = isMX4JPresent();

				if (mx4jPresent) {
					contextLocations.add("META-INF/impala-jmx-adaptor-bootstrap.xml");
				} else {
					logger.warn("'exposeMx4jAdaptor' set to true MX4J classes not present. Not exposing Mx4j adaptor");
				}
			} else {
				logger.warn("'exposeMx4jAdaptor' set to true but 'exposeJmxOperations' set to false. Not exposing Mx4j adaptor");
			}
		}
	}
	
	protected void addJmxOperations(List<String> contextLocations,
			PropertySource propertySource) {
		BooleanPropertyValue exposeJmx = new BooleanPropertyValue(propertySource, JMXBootstrapProperties.EXPOSE_JMX_OPERATIONS, true);
		if (exposeJmx.getValue()) {
			contextLocations.add("META-INF/impala-jmx-bootstrap.xml");
		}
	}

	boolean isMX4JPresent() {
		boolean mx4jPresent = false;
		
		//Check for presence of MX4J packages
		try {
			Class.forName("mx4j.tools.adaptor.http.XSLTProcessor");
			mx4jPresent = true;
		} catch (ClassNotFoundException e) {
			mx4jPresent = false;
		}
		return mx4jPresent;
	}

}
