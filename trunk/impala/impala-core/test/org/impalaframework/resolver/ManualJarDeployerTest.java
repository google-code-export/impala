package org.impalaframework.resolver;

import java.io.File;

import junit.framework.TestCase;

public class ManualJarDeployerTest extends TestCase {

	public void testRun() throws Exception {
		String workspaceRoot = "../wineorder-sample/deploy";
		File file = new File(workspaceRoot);
		assertTrue(file.exists());
		
		System.setProperty(LocationConstants.WORKSPACE_ROOT_PROPERTY, workspaceRoot);
		
	}
	
}
