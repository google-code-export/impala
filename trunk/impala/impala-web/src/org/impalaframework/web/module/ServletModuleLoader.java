package org.impalaframework.web.module;

import java.util.ArrayList;
import java.util.Collection;

import org.impalaframework.resolver.ModuleLocationResolver;
import org.impalaframework.spring.resource.ClassPathResourceLoader;
import org.impalaframework.spring.resource.ResourceLoader;

public class ServletModuleLoader extends WebRootModuleLoader {

	public ServletModuleLoader(ModuleLocationResolver moduleLocationResolver) {
		super(moduleLocationResolver);
	}
	
	@Override
	protected Collection<ResourceLoader> getSpringLocationResourceLoaders() {
		Collection<ResourceLoader> resourceLoaders = new ArrayList<ResourceLoader>();
		resourceLoaders.add(new ClassPathResourceLoader());
		return resourceLoaders;
	}

}
