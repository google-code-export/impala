package org.impalaframework.plugin.web;

import java.io.File;

import org.impalaframework.plugin.spec.PluginSpec;
import org.impalaframework.resolver.ClassLocationResolver;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

public class ServletPluginLoader extends WebRootPluginLoader {

	public ServletPluginLoader(ClassLocationResolver classLocationResolver) {
		super(classLocationResolver);
	}

	@Override
	public Resource[] getSpringConfigResources(PluginSpec pluginSpec, ClassLoader classLoader) {
		//FIXME test
		File springLocation = this.getClassLocationResolver().getApplicationPluginSpringLocation(pluginSpec.getName());
		return new Resource[] { new FileSystemResource(springLocation) };
	}
	
	

}
