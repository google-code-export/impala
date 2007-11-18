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

package org.impalaframework.plugin.web;

import javax.servlet.ServletContext;

import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.spec.SimpleParentSpec;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.WebApplicationContext;

@Deprecated
public class MultiServletImpalaContextLoader extends RegistryBasedImpalaContextLoader {

	protected WebApplicationContext createWebApplicationContext(ServletContext servletContext, ApplicationContext parent)
			throws BeansException {

		WebApplicationContext rootWebApplicationContext = null;//super.createWebApplicationContext(servletContext, parent);

		// FIXME when ready, will need to implement this in some
		// way to allow multi-servlet web contexts
		/*
		 * //load the root context for the other web contexts ParentSpec
		 * webappSpec = getWebApplicationSpec(servletContext);
		 * holder.loadParentWebContext(webappSpec);
		 *  // add context holder to servlet context
		 * servletContext.setAttribute(CONTEXT_HOLDER_PARAM, holder);
		 * WebApplicationContext parentContext = holder.getParentRootContext();
		 */
		return rootWebApplicationContext;
	}

	protected ParentSpec getWebApplicationSpec(ServletContext servletContext) {
		String[] locations = null;
		String configLocationString = servletContext.getInitParameter(WEBAPP_LOCATION_PARAM);
		if (configLocationString != null) {
			locations = (StringUtils.tokenizeToStringArray(configLocationString,
					ConfigurableWebApplicationContext.CONFIG_LOCATION_DELIMITERS));
		}
		return new SimpleParentSpec(locations);
	}

}
