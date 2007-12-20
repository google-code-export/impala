package org.impalaframework.web.loader;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.loader.DelegatingContextLoader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;

public class WebPlaceholderDelegatingContextLoader implements DelegatingContextLoader {

	public ConfigurableApplicationContext loadApplicationContext(ApplicationContext parent, ModuleDefinition plugin) {
		GenericWebApplicationContext context = new GenericWebApplicationContext();
		context.refresh();
		return context;
	}

}
