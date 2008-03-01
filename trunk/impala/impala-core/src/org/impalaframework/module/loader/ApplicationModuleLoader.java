package org.impalaframework.module.loader;

import java.io.File;
import java.util.List;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.impalaframework.util.ResourceUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

/**
 * @author Phil Zoio
 */
public class ApplicationModuleLoader extends BaseModuleLoader {

	private ModuleLocationResolver moduleLocationResolver;

	public ApplicationModuleLoader(ModuleLocationResolver moduleLocationResolver) {
		super();
		Assert.notNull("moduleLocationResolver cannot be null");
		this.moduleLocationResolver = moduleLocationResolver;
	}

	@Override
	public GenericApplicationContext newApplicationContext(ApplicationContext parent,
			ModuleDefinition moduleDefinition, ClassLoader classLoader) {
		Assert.notNull(parent, "parent cannot be null");
		return super.newApplicationContext(parent, moduleDefinition, classLoader);
	}

	public ClassLoader newClassLoader(ModuleDefinition moduleDefinition, ApplicationContext parent) {
		ClassLoader parentClassLoader = ModuleUtils.getParentClassLoader(parent);
		String name = moduleDefinition.getName();
		List<Resource> classLocations = moduleLocationResolver.getApplicationModuleClassLocations(name);
		File[] files = ResourceUtils.getFiles(classLocations);
		
		return getClassLoaderFactory().newClassLoader(parentClassLoader, files);
	}

	public Resource[] getClassLocations(ModuleDefinition moduleDefinition) {
		List<Resource> locations = moduleLocationResolver.getApplicationModuleClassLocations(moduleDefinition.getName());
		return ResourceUtils.toArray(locations);
	}
	
}
