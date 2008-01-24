package org.impalaframework.spring.resource;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ClassUtils;

public class ClassPathResourceLoader implements ResourceLoader {

	private ClassLoader classLoader;
	
	private String prefix;

	public ClassPathResourceLoader() {
		super();
		this.classLoader = ClassUtils.getDefaultClassLoader();
	}
	
	public ClassPathResourceLoader(ClassLoader classLoader) {
		super();
		this.classLoader = classLoader;
	}

	public Resource getResource(String location) {
		String prefix = (this.prefix != null ? this.prefix : "");
		return new ClassPathResource(prefix + location, getClassLoader());
	}

	public ClassLoader getClassLoader() {
		return classLoader;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	
}
