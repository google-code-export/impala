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

import org.impalaframework.bootstrap.ConfigurationSettings;
import org.impalaframework.bootstrap.SimpleContextLocationResolver;
import org.impalaframework.config.BooleanPropertyValue;
import org.impalaframework.config.PropertySource;

public class WebContextLocationResolver extends SimpleContextLocationResolver {
	
	@Override
	public boolean addContextLocations(ConfigurationSettings configSettings, PropertySource propertySource) {
		if (!super.addContextLocations(configSettings, propertySource)) {
		
			addJarModuleLocation(configSettings, propertySource);
			addAutoReloadListener(configSettings, propertySource);
			
			BooleanPropertyValue servletContextPartitioned = new BooleanPropertyValue(propertySource, WebBootstrapProperties.PARTITIONED_SERVLET_CONTEXT, false);
			BooleanPropertyValue sessionModuleProtected = new BooleanPropertyValue(propertySource, WebBootstrapProperties.SESSION_MODULE_PROTECTION, true);
			BooleanPropertyValue preserveSessionOnReloadFailure = new BooleanPropertyValue(propertySource, WebBootstrapProperties.PRESERVE_SESSION_ON_RELOAD_FAILURE, true);

			configSettings.addProperty(WebBootstrapProperties.PARTITIONED_SERVLET_CONTEXT, servletContextPartitioned);
			configSettings.addProperty(WebBootstrapProperties.SESSION_MODULE_PROTECTION, sessionModuleProtected);
			configSettings.addProperty(WebBootstrapProperties.PRESERVE_SESSION_ON_RELOAD_FAILURE, preserveSessionOnReloadFailure);
			
			return false;
		} else {
			return true;
		}
	}

	protected void addJarModuleLocation(ConfigurationSettings configSettings, PropertySource propertySource) {
		BooleanPropertyValue embeddedMode = new BooleanPropertyValue(propertySource, WebBootstrapProperties.EMBEDDED_MODE, false);
		configSettings.addProperty(WebBootstrapProperties.EMBEDDED_MODE, embeddedMode);
		
		if (!embeddedMode.getValue()) {
			configSettings.add("META-INF/impala-web-jar-module-bootstrap.xml");
		}
	}

	protected void addAutoReloadListener(ConfigurationSettings configSettings, PropertySource propertySource) {
		BooleanPropertyValue autoReloadModules = new BooleanPropertyValue(propertySource, WebBootstrapProperties.AUTO_RELOAD_MODULES, false);
		configSettings.addProperty(WebBootstrapProperties.AUTO_RELOAD_MODULES, autoReloadModules);
		
		if (autoReloadModules.getValue()) {
			configSettings.add("META-INF/impala-web-listener-bootstrap.xml");
		}
	}

	protected void addDefaultLocations(ConfigurationSettings configSettings) {
		configSettings.add("META-INF/impala-bootstrap.xml");
		configSettings.add("META-INF/impala-web-bootstrap.xml");
	}

}
