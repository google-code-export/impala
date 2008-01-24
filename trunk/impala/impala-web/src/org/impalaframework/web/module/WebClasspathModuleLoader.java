package org.impalaframework.web.module;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.ServletContext;

import org.impalaframework.module.loader.ModuleLoader;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.impalaframework.spring.resource.ClassPathResourceLoader;
import org.impalaframework.spring.resource.ResourceLoader;

public class WebClasspathModuleLoader extends WebRootModuleLoader implements ModuleLoader {

	public WebClasspathModuleLoader(ModuleLocationResolver moduleLocationResolver) {
		super(moduleLocationResolver);
	}
	
	public WebClasspathModuleLoader(ModuleLocationResolver moduleLocationResolver, ServletContext servletContext) {
		super(moduleLocationResolver, servletContext);
	}

	@Override
	protected Collection<ResourceLoader> getSpringLocationResourceLoaders() {
		Collection<ResourceLoader> resourceLoaders = new ArrayList<ResourceLoader>();
		resourceLoaders.add(new ClassPathResourceLoader());
		return resourceLoaders;
	}

}
