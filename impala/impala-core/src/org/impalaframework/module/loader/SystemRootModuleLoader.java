package org.impalaframework.module.loader;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.resolver.ClassLocationResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ClassUtils;


/**
 * @author Phil Zoio
 */
public class SystemRootModuleLoader extends RootModuleLoader implements ModuleLoader {

	public SystemRootModuleLoader(ClassLocationResolver classLocationResolver) {
		super(classLocationResolver);
	}

	@Override
	public ClassLoader newClassLoader(ModuleDefinition moduleDefinition, ApplicationContext parent) {
		return ClassUtils.getDefaultClassLoader();
	}
	
}
