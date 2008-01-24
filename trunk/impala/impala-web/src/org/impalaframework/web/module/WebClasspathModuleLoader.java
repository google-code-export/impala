package org.impalaframework.web.module;

import javax.servlet.ServletContext;

import org.impalaframework.module.loader.ModuleLoader;
import org.impalaframework.resolver.ModuleLocationResolver;

public class WebClasspathModuleLoader extends BaseWebModuleLoader implements ModuleLoader {

	public WebClasspathModuleLoader(ModuleLocationResolver moduleLocationResolver) {
		super(moduleLocationResolver);
	}
	
	public WebClasspathModuleLoader(ModuleLocationResolver moduleLocationResolver, ServletContext servletContext) {
		super(moduleLocationResolver, servletContext);
	}

}
