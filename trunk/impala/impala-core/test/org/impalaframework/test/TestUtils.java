package org.impalaframework.test;

import java.io.File;

import org.impalaframework.resolver.LocationConstants;
import org.impalaframework.util.PathUtils;

import junit.framework.TestCase;

public class TestUtils extends TestCase {

	public static String getCompileRoot() {
		String workspaceRoot = System.getProperty("compile." + LocationConstants.WORKSPACE_ROOT_PROPERTY);
		if (workspaceRoot != null) {
			return workspaceRoot;
		}
		return "../";		
	}
	
	public static String getCompileDirectory(String projectName) {
		String prefix = getWorkspaceRoot();
		String suffix = projectName + "/bin";
		return PathUtils.getPath(prefix, suffix);
	}

	public static File getCompileFile(String projectName) {
		return new File(getCompileDirectory(projectName));
	}
	
	public static String getWorkspaceRoot() {
		String workspaceRoot = System.getProperty("test." + LocationConstants.WORKSPACE_ROOT_PROPERTY);
		if (workspaceRoot != null) {
			return workspaceRoot;
		}
		return "../";
	}
	
	public static String getWorkspacePath(String relativePath) {
		String prefix = getWorkspaceRoot();
		String suffix = relativePath;
		return PathUtils.getPath(prefix, suffix);
	}
	
	public static File getWorkspaceFile(String relativePath) {
		return new File(getWorkspacePath(relativePath));
	}
	
}
