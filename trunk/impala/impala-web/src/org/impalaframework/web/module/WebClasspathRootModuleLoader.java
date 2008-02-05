package org.impalaframework.web.module;

import javax.servlet.ServletContext;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.loader.ModuleUtils;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.springframework.core.io.Resource;

public class WebClasspathRootModuleLoader extends BaseWebModuleLoader {

	public WebClasspathRootModuleLoader(ModuleLocationResolver moduleLocationResolver) {
		super(moduleLocationResolver);
	}
	
	public WebClasspathRootModuleLoader(ModuleLocationResolver moduleLocationResolver, ServletContext servletContext) {
		super(moduleLocationResolver, servletContext);
	}
	
	@Override
	public Resource[] getClassLocations(ModuleDefinition moduleDefinition) {
		//FIXME test
		return ModuleUtils.getRootClassLocations(getClassLocationResolver());
	}

}
