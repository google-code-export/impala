package org.impalaframework.web.module;

import java.io.File;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

public class ServletModuleLoader extends WebRootModuleLoader {

	public ServletModuleLoader(ModuleLocationResolver moduleLocationResolver) {
		super(moduleLocationResolver);
	}

	@Override
	public Resource[] getSpringConfigResources(ModuleDefinition moduleDefinition, ClassLoader classLoader) {
		File springLocation = this.getClassLocationResolver().getApplicationModuleSpringLocation(moduleDefinition.getName());
		return new Resource[] { new FileSystemResource(springLocation) };
	}

}
