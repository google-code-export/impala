package org.impalaframework.spring.resource;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class PathBasedResourceLoader implements ResourceLoader {
	
	private String prefix;

	public PathBasedResourceLoader() {
		super();
	}

	public Resource getResource(String location, ClassLoader classLoader) {
		String prefix = (this.prefix != null ? this.prefix : "");
		return getResourceForPath(prefix, location, classLoader);
	}

	protected Resource getResourceForPath(String prefix, String location, ClassLoader classLoader) {
		return new ClassPathResource(prefix + location, classLoader);
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
}
