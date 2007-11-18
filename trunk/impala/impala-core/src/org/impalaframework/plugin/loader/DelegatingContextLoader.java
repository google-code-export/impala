package org.impalaframework.plugin.loader;

import org.impalaframework.plugin.spec.PluginSpec;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

public interface DelegatingContextLoader {

	ConfigurableApplicationContext loadApplicationContext(ApplicationContext parent, PluginSpec plugin);

}
