package org.impalaframework.plugin.web;

import org.impalaframework.plugin.loader.DelegatingContextLoader;
import org.impalaframework.plugin.spec.PluginSpec;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;

public class WebPlaceholderDelegatingContextLoader implements DelegatingContextLoader {

	public ConfigurableApplicationContext loadApplicationContext(ApplicationContext parent, PluginSpec plugin) {
		//FIXME test
		return new GenericWebApplicationContext();
	}

}
