package org.impalaframework.module.loader;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ClassUtils;


/**
 * @author Phil Zoio
 */
public class SystemRootModuleLoader extends RootModuleLoader {

	public SystemRootModuleLoader(ModuleLocationResolver moduleLocationResolver) {
		super(moduleLocationResolver);
	}

	@Override
	public ClassLoader newClassLoader(ModuleDefinition moduleDefinition, ApplicationContext parent) {
		return ClassUtils.getDefaultClassLoader();
	}
	
}
