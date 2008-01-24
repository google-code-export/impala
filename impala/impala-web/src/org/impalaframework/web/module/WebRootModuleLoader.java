package org.impalaframework.web.module;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.ServletContext;

import org.impalaframework.resolver.ModuleLocationResolver;
import org.impalaframework.spring.resource.ResourceLoader;
import org.impalaframework.web.resource.ServletContextResourceLoader;
import org.springframework.web.context.ServletContextAware;

public class WebRootModuleLoader extends BaseWebModuleLoader implements ServletContextAware {

	public WebRootModuleLoader(ModuleLocationResolver moduleLocationResolver) {
		super(moduleLocationResolver);
	}
	
	public WebRootModuleLoader(ModuleLocationResolver moduleLocationResolver, ServletContext servletContext) {
		super(moduleLocationResolver, servletContext);
	}

	@Override
	protected Collection<ResourceLoader> getSpringLocationResourceLoaders() {
		Collection<ResourceLoader> resourceLoaders = new ArrayList<ResourceLoader>();
		ServletContextResourceLoader servletContextResourceLoader = new ServletContextResourceLoader();
		servletContextResourceLoader.setServletContext(getServletContext());
		resourceLoaders.add(servletContextResourceLoader);
		return resourceLoaders;
	}

}
