package org.impalaframework.spring.resource;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ClassUtils;

public class ClassPathResourceLoader implements ResourceLoader {

	private ClassLoader classLoader;

	public ClassPathResourceLoader(ClassLoader classLoader) {
		super();
		this.classLoader = classLoader;
	}

	public ClassPathResourceLoader() {
		super();
		this.classLoader = ClassUtils.getDefaultClassLoader();
	}

	public Resource getResource(String location) {
		return new ClassPathResource(location, getClassLoader());
	}

	public ClassLoader getClassLoader() {
		return classLoader;
	}
	
	
}
