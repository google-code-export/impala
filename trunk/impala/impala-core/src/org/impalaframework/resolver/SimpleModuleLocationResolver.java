package org.impalaframework.resolver;

import java.util.Arrays;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

public class SimpleModuleLocationResolver extends AbstractModuleLocationResolver {

	private String moduleClassDirectory;
	
	private String moduleTestDirectory;
	
	private String[] rootProjects;
	
	private String workspaceRoot;
	
	public void init() {
		Assert.notNull(moduleClassDirectory, "moduleClassDirectory cannot be null");
		Assert.notNull(moduleTestDirectory, "moduleTestDirectory cannot be null");
		Assert.notNull(rootProjects, "rootProjects cannot be null");
		Assert.notNull(workspaceRoot, "workspaceRoot cannot be null");
		Assert.isTrue(rootProjects.length > 0, "rootProjects cannot be empty");
	}
	
	public List<String> getRootProjects() {
		return Arrays.asList(rootProjects);
	}
	
	public List<Resource> getModuleTestClassLocations(String moduleName) {
		String classDir = moduleTestDirectory;
		return getResources(moduleName, classDir);
	}

	public List<Resource> getApplicationModuleClassLocations(String moduleName) {
		String classDir = moduleClassDirectory;
		return getResources(moduleName, classDir);
	}

	@Override
	protected String getWorkspaceRoot() {
		return workspaceRoot;
	}

	public void setModuleClassDirectory(String moduleClassDirectory) {
		this.moduleClassDirectory = moduleClassDirectory;
	}

	public void setModuleTestDirectory(String moduleTestDirectory) {
		this.moduleTestDirectory = moduleTestDirectory;
	}

	public void setRootProjectString(String rootProjects) {
		this.rootProjects = StringUtils.tokenizeToStringArray(rootProjects, " ,");
	}

	public void setWorkspaceRoot(String workspaceRoot) {
		this.workspaceRoot = workspaceRoot;
	}

}
