package org.impalaframework.module.loader;


import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.resolver.ClassLocationResolver;
import org.springframework.core.io.Resource;

/**
 * @author Phil Zoio
 */
public class ManualReloadingRootModuleLoader extends RootModuleLoader implements ModuleLoader {

	public ManualReloadingRootModuleLoader(ClassLocationResolver classLocationResolver) {
		super(classLocationResolver);
	}

	@Override
	public Resource[] getClassLocations(ModuleDefinition moduleDefinition) {
		return new Resource[0];
	}

}
