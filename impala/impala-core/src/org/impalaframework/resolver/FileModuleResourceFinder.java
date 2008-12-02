package org.impalaframework.resolver;

import java.util.Collections;
import java.util.List;

import org.impalaframework.util.PathUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

public class FileModuleResourceFinder implements ModuleResourceFinder {

	private String classDirectory;
	
	public List<Resource> findResources(String workspaceRootPath,
			String moduleName, String moduleVersion) {
		return getResources(workspaceRootPath, moduleName);
	}
	
	protected List<Resource> getResources(String workspaceRootPath, String moduleName) {
		String path = PathUtils.getPath(workspaceRootPath, moduleName);
		path = PathUtils.getPath(path, classDirectory);
		Resource resource = new FileSystemResource(path);
		if (resource.exists())
			return Collections.singletonList(resource);
		else
			return Collections.emptyList();
	}

	public void setClassDirectory(String classDirectory) {
		this.classDirectory = classDirectory;
	}

}
