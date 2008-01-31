package org.impalaframework.resolver;

import java.io.File;
import java.util.Collections;
import java.util.List;

import org.impalaframework.util.PathUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

public class SimpleJarModuleLocationResolver extends SimpleBaseModuleLocationResolver {

	public List<Resource> getApplicationModuleClassLocations(String moduleName) {
		File workspaceRoot = getRootDirectory();

		String jarName = moduleName;
		String applicationVersion = System.getProperty(LocationConstants.APPLICATION_VERSION);
		if (applicationVersion != null){
			jarName = jarName + "-" + applicationVersion;
		}
		
		// return a classpath resource representing from a jar
		String path = PathUtils.getPath(workspaceRoot.getAbsolutePath(), jarName + ".jar");
		Resource resource = new FileSystemResource(path);
		return Collections.singletonList(resource);
	}

	public List<Resource> getModuleTestClassLocations(String moduleName) {
		// not sure what to do with this as this is a test
		throw new UnsupportedOperationException();
	}

}
