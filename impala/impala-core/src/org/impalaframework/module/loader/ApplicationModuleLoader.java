package org.impalaframework.module.loader;

import java.io.File;

import org.impalaframework.classloader.FileSystemModuleClassLoader;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.impalaframework.util.ResourceUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

/**
 * @author Phil Zoio
 */
public class ApplicationModuleLoader extends BaseModuleLoader implements ModuleLoader {

	private ModuleLocationResolver moduleLocationResolver;

	public ApplicationModuleLoader(ModuleLocationResolver moduleLocationResolver) {
		super();
		Assert.notNull("moduleLocationResolver cannot be null");
		this.moduleLocationResolver = moduleLocationResolver;
	}

	@Override
	public GenericApplicationContext newApplicationContext(ApplicationContext parent, ModuleDefinition moduleDefinition, ClassLoader classLoader) {
		Assert.notNull(parent, "parent cannot be null");
		return super.newApplicationContext(parent, moduleDefinition, classLoader);
	}

	public ClassLoader newClassLoader(ModuleDefinition moduleDefinition, ApplicationContext parent) {
		ClassLoader parentClassLoader = ModuleUtils.getParentClassLoader(parent);
		File[] classLocations = moduleLocationResolver.getApplicationPluginClassLocations(moduleDefinition.getName());
		FileSystemModuleClassLoader cl = new FileSystemModuleClassLoader(parentClassLoader, classLocations);
		return cl;
	}

	public Resource[] getClassLocations(ModuleDefinition moduleDefinition) {
		File[] classLocations = moduleLocationResolver.getApplicationPluginClassLocations(moduleDefinition.getName());
		return ResourceUtils.getResources(classLocations);
	}

	public Resource[] getSpringConfigResources(ModuleDefinition moduleDefinition, ClassLoader classLoader) {
		File springLocation = this.moduleLocationResolver.getApplicationPluginSpringLocation(moduleDefinition.getName());
		return new Resource[] { new FileSystemResource(springLocation) };
	}

}
