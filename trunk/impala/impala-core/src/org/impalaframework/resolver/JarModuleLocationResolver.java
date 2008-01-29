package org.impalaframework.resolver;

import java.io.File;
import java.util.Collections;
import java.util.List;

import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.util.PathUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

public class JarModuleLocationResolver extends BaseModuleLocationResolver {

	public List<Resource> getApplicationModuleClassLocations(String moduleName) {
		File workspaceRoot = getRootDirectory();

		// return a classpath resource representing from a jar
		String path = PathUtils.getPath(workspaceRoot.getAbsolutePath(), moduleName + ".jar");
		Resource resource = new FileSystemResource(path);
		return Collections.singletonList(resource);
	}

	@Override
	protected File getRootDirectory() {
		File rootDirectory = super.getRootDirectory();

		if (rootDirectory == null) {
			throw new ConfigurationException("Workspace root not set. Have you set the property '"
					+ LocationConstants.WORKSPACE_ROOT_PROPERTY + "'?");
		}

		return rootDirectory;
	}

	public List<Resource> getModuleTestClassLocations(String moduleName) {
		// not sure what to do with this as this is a test
		throw new UnsupportedOperationException();
	}

}
