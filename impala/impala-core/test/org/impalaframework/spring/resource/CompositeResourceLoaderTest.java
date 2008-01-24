package org.impalaframework.spring.resource;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;

public class CompositeResourceLoaderTest extends TestCase {

	private List<ResourceLoader> resourceLoaders;
	private CompositeResourceLoader resourceLoader;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		resourceLoaders = new ArrayList<ResourceLoader>();
		resourceLoaders.add(new ClassPathResourceLoader());
		resourceLoaders.add(new FileSystemResourceLoader());
		resourceLoader = new CompositeResourceLoader(resourceLoaders);
	}
	
	public final void testGetResourceOnFileSystem() {
		Resource resource = resourceLoader.getResource("../impala-core/files/MyTestClass.jar", ClassUtils.getDefaultClassLoader());
		assertTrue(resource instanceof FileSystemResource);
	}
	
	public final void testGetResourceOnClassPath() {
		Resource resource = resourceLoader.getResource("beanset.properties", ClassUtils.getDefaultClassLoader());
		assertTrue(resource instanceof ClassPathResource);
	}
}
