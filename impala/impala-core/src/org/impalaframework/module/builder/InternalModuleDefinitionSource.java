package org.impalaframework.module.builder;

import java.net.URL;
import java.util.List;

import org.impalaframework.classloader.CustomClassLoader;
import org.impalaframework.classloader.ModuleClassLoader;
import org.impalaframework.classloader.NonDelegatingResourceClassLoader;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.impalaframework.util.ResourceUtils;
import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;

/**
 * Implementation of <code>ModuleDefinitionSource</code> which relies on the
 * presence of a "module.properties" file in the root of the module classpath
 * @author Phil Zoio
 */
public class InternalModuleDefinitionSource implements ModuleDefinitionSource {

	private String[] moduleNames;

	private boolean loadDependendentModules;
	
	//TODO need to figure out where this is to come from
	private ModuleLocationResolver moduleLocationResolver;

	public InternalModuleDefinitionSource(ModuleLocationResolver resolver, String[] moduleNames) {
		this(resolver, moduleNames, true);
	}

	public InternalModuleDefinitionSource(ModuleLocationResolver resolver, String[] moduleNames, boolean loadDependendentModules) {
		super();
		this.moduleLocationResolver = resolver;
		this.moduleNames = moduleNames;
		this.loadDependendentModules = loadDependendentModules;
	}

	public RootModuleDefinition getModuleDefinition() {
		
		for (String moduleName : moduleNames) {
			getResourceForModule(moduleName, "module.properties");
		}
		
		return null;
	}

	URL getResourceForModule(String moduleName, String resourceName) {
		List<Resource> locations = moduleLocationResolver.getApplicationModuleClassLocations(moduleName);
		CustomClassLoader cl = new ModuleClassLoader(ClassUtils.getDefaultClassLoader(), ResourceUtils.getFiles(locations) );
		NonDelegatingResourceClassLoader ndl = new NonDelegatingResourceClassLoader(cl);
		
		return ndl.getResource(resourceName);
	}

}
