package org.impalaframework.web.type;

import java.util.Properties;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.type.TypeReader;
import org.impalaframework.web.module.WebPlaceholderModuleDefinition;
import org.w3c.dom.Element;

public class WebPlaceholderTypeReader implements TypeReader {

	public ModuleDefinition readModuleDefinition(ModuleDefinition parent,
			String moduleName, Properties properties) {
		return new WebPlaceholderModuleDefinition(parent, moduleName);
	}

	public ModuleDefinition readModuleDefinition(ModuleDefinition parent,
			String moduleName, Element definitionElement) {
		return new WebPlaceholderModuleDefinition(parent, moduleName);
	}

	public Properties readModuleDefinitionProperties(ModuleDefinition parent,
			String moduleName, Element definitionElement) {
		return null;
	}

}
