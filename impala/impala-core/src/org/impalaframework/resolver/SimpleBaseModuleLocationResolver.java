package org.impalaframework.resolver;

import java.util.Arrays;
import java.util.List;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

public abstract class SimpleBaseModuleLocationResolver extends AbstractModuleLocationResolver {

	
	private String[] rootProjects;
	
	private String workspaceRoot;
	
	public void init() {
		Assert.notNull(rootProjects, "rootProjects cannot be null");
		Assert.notNull(workspaceRoot, "workspaceRoot cannot be null");
		Assert.isTrue(rootProjects.length > 0, "rootProjects cannot be empty");
	}
	
	public List<String> getRootProjects() {
		return Arrays.asList(rootProjects);
	}

	@Override
	protected String getWorkspaceRoot() {
		return workspaceRoot;
	}

	public void setRootProjectString(String rootProjects) {
		this.rootProjects = StringUtils.tokenizeToStringArray(rootProjects, " ,");
	}

	public void setWorkspaceRoot(String workspaceRoot) {
		this.workspaceRoot = workspaceRoot;
	}

}
