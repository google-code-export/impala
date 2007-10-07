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

package net.java.impala.spring.web;

import javax.servlet.ServletContext;

import net.java.impala.spring.plugin.SpringContextSpec;
import net.java.impala.spring.plugin.SimplePluginSpec;
import net.java.impala.spring.resolver.DefaultWebContextResourceHelper;
import net.java.impala.spring.resolver.WebClassLocationResolver;
import net.java.impala.spring.resolver.WebPropertyClassLocationResolver;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

public class ImpalaContextLoader extends ContextLoader {

	public static final String PLUGIN_NAMES_PARAM = "pluginNames";

	public static final String CONTEXT_HOLDER_PARAM = WebApplicationContext.class.getName() + ".CONTEXT_HOLDER";

	@Override
	protected WebApplicationContext createWebApplicationContext(ServletContext servletContext, ApplicationContext parent)
			throws BeansException {

		SpringContextSpec pluginSpec = getPluginSpec(servletContext);

		WebClassLocationResolver classLocationResolver = newClassLocationResolver();
		DefaultWebApplicationContextLoader applicationContextLoader = new DefaultWebApplicationContextLoader(new DefaultWebContextResourceHelper(classLocationResolver));
		
		WebDynamicContextHolder holder = new WebDynamicContextHolder(applicationContextLoader);
		holder.loadParentContext(ClassUtils.getDefaultClassLoader(), pluginSpec);

		// add context holder to servlet context
		servletContext.setAttribute(CONTEXT_HOLDER_PARAM, holder);

		WebApplicationContext parentContext = holder.getParentWebApplicationContext();
		return parentContext;
	}

	protected SpringContextSpec getPluginSpec(ServletContext servletContext) {

		// subclasses can override to get PluginSpec more intelligently
		String[] locations = null;
		String configLocationString = servletContext.getInitParameter(CONFIG_LOCATION_PARAM);
		if (configLocationString != null) {
			locations = (StringUtils.tokenizeToStringArray(configLocationString,
					ConfigurableWebApplicationContext.CONFIG_LOCATION_DELIMITERS));
		}

		String[] pluginNames = null;
		String pluginNameString = servletContext.getInitParameter(PLUGIN_NAMES_PARAM);
		if (pluginNameString != null) {
			pluginNames = (StringUtils.tokenizeToStringArray(pluginNameString,
					ConfigurableWebApplicationContext.CONFIG_LOCATION_DELIMITERS));
		}
		SpringContextSpec pluginSpec = new SimplePluginSpec(locations, pluginNames);
		return pluginSpec;
	}

	protected WebClassLocationResolver newClassLocationResolver() {
		WebClassLocationResolver classLocationResolver = new WebPropertyClassLocationResolver();
		return classLocationResolver;
	}

}
