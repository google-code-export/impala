package org.impalaframework.spring.resource;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class ClassPathResourceLoader extends PathBasedResourceLoader {

	public ClassPathResourceLoader() {
		super();
	}

	@Override
	protected Resource getResourceForPath(String prefix, String location, ClassLoader classLoader) {
		return new ClassPathResource(prefix + location, classLoader);
	}
	
}
