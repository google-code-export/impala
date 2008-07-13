package org.impalaframework.web.type;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.type.ApplicationModuleTypeReader;
import org.impalaframework.module.type.TypeReader;
import org.impalaframework.web.module.WebRootModuleDefinition;

public class WebRootModuleTypeReader extends ApplicationModuleTypeReader implements TypeReader {

	@Override
	protected ModuleDefinition newDefinition(ModuleDefinition parent,
			String moduleName, String[] locationsArray) {
		return new WebRootModuleDefinition(parent, moduleName, locationsArray);
	}

}
