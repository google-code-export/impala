package org.impalaframework.spring.resource;

import org.springframework.core.io.Resource;

public abstract class PathBasedResourceLoader implements ResourceLoader {
	
	private String prefix;

	public PathBasedResourceLoader() {
		super();
	}

	public Resource getResource(String location, ClassLoader classLoader) {
		String prefix = (this.prefix != null ? this.prefix : "");
		return getResourceForPath(prefix, location, classLoader);
	}
	
	protected abstract Resource getResourceForPath(String prefix, String location, ClassLoader classLoader);


	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
}
