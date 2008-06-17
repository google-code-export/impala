package org.impalaframework.web.type;

import java.util.Properties;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.type.ApplicationModuleTypeReader;
import org.impalaframework.module.type.TypeReader;
import org.impalaframework.web.module.ServletModuleDefinition;

public class ServletModuleTypeReader extends ApplicationModuleTypeReader implements TypeReader {

	public ModuleDefinition readModuleDefinition(ModuleDefinition parent,
			String moduleName, Properties properties) {
		//FIXME implement
		return null;
	}

	@Override
	protected ModuleDefinition newDefinition(ModuleDefinition parent,
			String moduleName, String[] locationsArray) {
		return new ServletModuleDefinition(parent, moduleName, locationsArray);
	}

}
