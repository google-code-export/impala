package org.impalaframework.module.loader;


import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.springframework.core.io.Resource;

/**
 * @author Phil Zoio
 */
public class ManualReloadingRootModuleLoader extends RootModuleLoader implements ModuleLoader {

	public ManualReloadingRootModuleLoader(ModuleLocationResolver moduleLocationResolver) {
		super(moduleLocationResolver);
	}

	@Override
	public Resource[] getClassLocations(ModuleDefinition moduleDefinition) {
		return new Resource[0];
	}

}
