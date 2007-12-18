package org.impalaframework.module.web;

import java.io.File;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

public class ServletPluginLoader extends WebRootPluginLoader {

	public ServletPluginLoader(ModuleLocationResolver moduleLocationResolver) {
		super(moduleLocationResolver);
	}

	@Override
	public Resource[] getSpringConfigResources(ModuleDefinition moduleDefinition, ClassLoader classLoader) {
		File springLocation = this.getClassLocationResolver().getApplicationPluginSpringLocation(moduleDefinition.getName());
		return new Resource[] { new FileSystemResource(springLocation) };
	}

}
