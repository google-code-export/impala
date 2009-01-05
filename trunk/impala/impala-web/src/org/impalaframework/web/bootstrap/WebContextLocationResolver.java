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

package org.impalaframework.web.bootstrap;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.bootstrap.SimpleContextLocationResolver;
import org.impalaframework.config.BooleanPropertyValue;
import org.impalaframework.config.PropertySource;

public class WebContextLocationResolver extends SimpleContextLocationResolver {

	private static Log logger = LogFactory.getLog(WebContextLocationResolver.class);
	
	@Override
	public boolean addContextLocations(List<String> contextLocations, PropertySource propertySource) {
		if (!super.addContextLocations(contextLocations, propertySource)) {
		
			addJarModuleLocation(contextLocations, propertySource);
			addAutoReloadListener(contextLocations, propertySource);
			
			BooleanPropertyValue servletContextPartitioned = new BooleanPropertyValue(propertySource, WebBootstrapProperties.PARTITIONED_SERVLET_CONTEXT, false);
			BooleanPropertyValue sessionModuleProtected = new BooleanPropertyValue(propertySource, WebBootstrapProperties.SESSION_MODULE_PROTECTION, true);
			logger.info("Value for '" + WebBootstrapProperties.PARTITIONED_SERVLET_CONTEXT + "': " + servletContextPartitioned.getValue());
			logger.info("Value for '" + WebBootstrapProperties.SESSION_MODULE_PROTECTION + "': " + sessionModuleProtected.getValue());
			
			return false;
		} else {
			return true;
		}
	}

	protected void addJarModuleLocation(List<String> contextLocations, PropertySource propertySource) {
		BooleanPropertyValue embeddedMode = new BooleanPropertyValue(propertySource, WebBootstrapProperties.EMBEDDED_MODE, false);
		logger.info("Value for '" + WebBootstrapProperties.EMBEDDED_MODE + "': " + embeddedMode.getValue());
		
		if (!embeddedMode.getValue()) {
			contextLocations.add("META-INF/impala-web-jar-module-bootstrap.xml");
		}
	}

	protected void addAutoReloadListener(List<String> contextLocations, PropertySource propertySource) {
		BooleanPropertyValue autoReloadModules = new BooleanPropertyValue(propertySource, WebBootstrapProperties.AUTO_RELOAD_MODULES, false);
		logger.info("Value for '" + WebBootstrapProperties.AUTO_RELOAD_MODULES + "': " + autoReloadModules.getValue());
		
		if (autoReloadModules.getValue()) {
			contextLocations.add("META-INF/impala-web-listener-bootstrap.xml");
		}
	}

	protected void addDefaultLocations(List<String> contextLocations) {
		contextLocations.add("META-INF/impala-bootstrap.xml");
		contextLocations.add("META-INF/impala-web-bootstrap.xml");
	}

}
