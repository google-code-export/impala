package org.impalaframework.module.loader;

import org.impalaframework.module.spec.PluginSpec;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

public interface DelegatingContextLoader {

	ConfigurableApplicationContext loadApplicationContext(ApplicationContext parent, PluginSpec plugin);

}
