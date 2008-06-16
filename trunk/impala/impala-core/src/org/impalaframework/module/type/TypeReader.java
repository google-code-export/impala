package org.impalaframework.module.type;

import java.util.Properties;

import org.impalaframework.module.definition.ModuleDefinition;

public interface TypeReader {

	ModuleDefinition readModuleDefinition(ModuleDefinition parent, String moduleName, Properties properties);
	
}
