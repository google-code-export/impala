package org.impalaframework.resolver;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.util.PathUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

public class SimpleJarModuleLocationResolver extends SimpleBaseModuleLocationResolver {
	
	private String applicationVersion;

	public List<Resource> getApplicationModuleClassLocations(String moduleName) {
		Resource workspaceRoot = getRootDirectory();

		String jarName = moduleName;
		
		if (this.applicationVersion != null){
			jarName = jarName + "-" + applicationVersion;
		}
		
		// return a classpath resource representing from a jar
		File file = null;
		try {
			file = workspaceRoot.getFile();
		}
		catch (IOException e) {
			throw new ConfigurationException("Unable to get file for root resource " + workspaceRoot);
		}
		
		String path = PathUtils.getPath(file.getAbsolutePath(), jarName + ".jar");
		Resource resource = new FileSystemResource(path);
		return Collections.singletonList(resource);
	}

	public List<Resource> getModuleTestClassLocations(String moduleName) {
		// not sure what to do with this as this is a test
		throw new UnsupportedOperationException();
	}

	public void setApplicationVersion(String applicationVersion) {
		this.applicationVersion = applicationVersion;
	}

}
