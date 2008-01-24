package org.impalaframework.spring.resource;

import java.util.Collection;

import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

/**
 * Implements resource loader according to the composite pattern. The Composite
 * resource loader will return the first resource which exists using the
 * available set of resource loaders
 * @author Phil Zoio
 */
public class CompositeResourceLoader implements ResourceLoader {

	private Collection<ResourceLoader> resourceLoaders;

	public CompositeResourceLoader(Collection<ResourceLoader> resourceLoaders) {
		super();
		Assert.notNull(resourceLoaders);
		this.resourceLoaders = resourceLoaders;
	}

	public Resource getResource(String location, ClassLoader classLoader) {
		Resource resource = null;
		for (ResourceLoader resourceLoader : resourceLoaders) {
			resource = resourceLoader.getResource(location, classLoader);
			if (resource != null && resource.exists()) {
				return resource;
			}
		}
		return null;
	}

}
