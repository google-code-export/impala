package org.impalaframework.module.loader;

import org.impalaframework.module.spec.ModuleDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

public interface DelegatingContextLoader {

	ConfigurableApplicationContext loadApplicationContext(ApplicationContext parent, ModuleDefinition plugin);

}
