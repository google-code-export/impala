package org.impalaframework.module.type;

import java.util.Properties;

import org.impalaframework.module.definition.ModuleDefinition;
import org.w3c.dom.Element;

public interface TypeReader {

	ModuleDefinition readModuleDefinition(ModuleDefinition parent, String moduleName, Properties properties);

	ModuleDefinition readModuleDefinition(ModuleDefinition parent, String moduleName, Element definitionElement);
	
	void readModuleDefinitionProperties(Properties properties, String moduleName, Element definitionElement);
	
}
