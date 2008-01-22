package org.impalaframework.module.loader;

import java.util.List;

import org.impalaframework.classloader.ModuleClassLoader;
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
		List<Resource> classLocations = moduleLocationResolver.getApplicationModuleClassLocations(moduleDefinition
				.getName());
		ModuleClassLoader cl = new ModuleClassLoader(parentClassLoader, ResourceUtils
				.getFiles(classLocations));
		return cl;
	}

	public Resource[] getClassLocations(ModuleDefinition moduleDefinition) {
		List<Resource> locations = moduleLocationResolver.getApplicationModuleClassLocations(moduleDefinition.getName());
		return ResourceUtils.toArray(locations);
	}

}
