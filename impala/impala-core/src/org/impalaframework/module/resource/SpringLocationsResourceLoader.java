package org.impalaframework.module.resource;

import org.impalaframework.module.definition.ModuleDefinition;
import org.springframework.core.io.Resource;

public interface SpringLocationsResourceLoader {
	Resource[] getSpringLocations(ModuleDefinition moduleDefinition);
}
