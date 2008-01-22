package org.impalaframework.resolver;

import java.io.File;
import java.util.Collections;
import java.util.List;

import org.impalaframework.util.PathUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

public class JarModuleLocationResolver extends BaseModuleLocationResolver {

	public List<Resource> getApplicationModuleClassLocations(String moduleName) {
		// FIXME test

		File workspaceRoot = getRootDirectory();

		// return a classpath resource representing from a jar
		String path = PathUtils.getPath(workspaceRoot.getAbsolutePath(), moduleName + ".jar");
		Resource resource = new FileSystemResource(path);
		List<Resource> singletonList = Collections.singletonList(resource);
		return singletonList;
	}

	protected File getRootDirectory() {
		File rootDirectory = super.getRootDirectory();

		// FIXME check that this exists, unlike PropertyModuleLocationResolver

		return rootDirectory;
	}

	public Resource getApplicationModuleSpringLocation(String moduleName) {
		// this should return the superclass
		return null;
	}

	public List<Resource> getModuleTestClassLocations(String moduleName) {
		// return a classpath resource representing from a jar.
		// not sure what to do with this as this is a test
		return null;
	}

}
