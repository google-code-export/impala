package org.impalaframework.web.module;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class ServletModuleLoader extends WebRootModuleLoader {

	public ServletModuleLoader(ModuleLocationResolver moduleLocationResolver) {
		super(moduleLocationResolver);
	}

	@Override
	public Resource[] getSpringConfigResources(ModuleDefinition moduleDefinition, ClassLoader classLoader) {
		return new Resource[]{new ClassPathResource(moduleDefinition.getName() + "-context.xml")};
	}

}
