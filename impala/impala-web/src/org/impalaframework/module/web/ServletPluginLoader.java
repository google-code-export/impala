package org.impalaframework.module.web;

import java.io.File;

import org.impalaframework.module.spec.ModuleDefinition;
import org.impalaframework.resolver.ClassLocationResolver;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

public class ServletPluginLoader extends WebRootPluginLoader {

	public ServletPluginLoader(ClassLocationResolver classLocationResolver) {
		super(classLocationResolver);
	}

	@Override
	public Resource[] getSpringConfigResources(ModuleDefinition moduleDefinition, ClassLoader classLoader) {
		File springLocation = this.getClassLocationResolver().getApplicationPluginSpringLocation(moduleDefinition.getName());
		return new Resource[] { new FileSystemResource(springLocation) };
	}

}
