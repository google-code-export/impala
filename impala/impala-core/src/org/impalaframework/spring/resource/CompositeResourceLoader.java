package org.impalaframework.spring.resource;

import java.util.Collection;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/**
 * Implements resource loader according to the composite pattern. The Composite
 * resource loader will return the first resource which exists using the
 * available set of resource loaders
 * @author Phil Zoio
 */
public class CompositeResourceLoader implements ResourceLoader {

	private Collection<ResourceLoader> resourceLoaders;

	private ClassLoader classLoader;

	public CompositeResourceLoader(Collection<ResourceLoader> resourceLoaders) {
		super();
		Assert.notNull(resourceLoaders);
		this.resourceLoaders = resourceLoaders;
		this.classLoader = ClassUtils.getDefaultClassLoader();
	}

	public CompositeResourceLoader(Collection<ResourceLoader> resourceLoaders, ClassLoader classLoader) {
		super();
		Assert.notNull(resourceLoaders);
		Assert.notNull(classLoader);
		this.resourceLoaders = resourceLoaders;
		this.classLoader = classLoader;
	}

	/**
	 * Returns the class loader. Note that the ClassLoader for the composites is
	 * ignoredF
	 */
	public ClassLoader getClassLoader() {
		return classLoader;
	}

	public Resource getResource(String location) {
		Resource resource = null;
		for (ResourceLoader resourceLoader : resourceLoaders) {
			resource = resourceLoader.getResource(location);
			if (resource != null && resource.exists()) {
				return resource;
			}
		}
		return null;
	}

}
