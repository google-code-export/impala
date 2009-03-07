package org.impalaframework.build.ant;

import java.io.File;

import junit.framework.TestCase;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;

public class MavenPublishTaskTest extends TestCase {

	private MavenPublishTask task;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		task = new MavenPublishTask();
		task.setProject(new Project());
		task.setSourceDir(new File("nonexistent"));
		task.setArtifacts("impala-core,impala-build,impala-osgi");
		task.setOrganisation("org.impalaframework");
		task.setSourceDir(new File("../impala-repository/dist"));
		task.setDestDir(new File("../maven/repo"));
	}
	
	public void testExecute() throws Exception {
		task.execute();
	}
	
	public void testCheckArgs() throws Exception {
		task = new MavenPublishTask();
		expectFailure("'sourceDir' cannot be null");
		task.setSourceDir(new File("nonexistent"));
		expectFailure("'artifacts' cannot be null");
		task.setArtifacts("art1,art2");
		expectFailure("'organisation' cannot be null");
		task.setOrganisation("org");
		expectFailure("'destDir' cannot be null");
		task.setDestDir(new File("nonexistent"));
		expectFailure("The source directory 'nonexistent' does not exist");
		task.setSourceDir(new File("../impala-build/build.xml"));
		expectFailure("'sourceDir' is not a directory");
		task.setSourceDir(new File("./"));
		task.checkArgs();		
	}
	
	public void testGetFiles() throws Exception {
		final File[] files = task.getFiles();		
		for (File file : files) {
			System.out.println(file.getName());
		}
		assertTrue(files.length > 0);
	}
	
	public void testGetDescriptions() throws Exception {
		final File[] files = task.getFiles();		
		final ArtifactDescription[] ads = task.getArtifactOutput(files);
		for (ArtifactDescription artifactDescription : ads) {
			System.out.println(artifactDescription);
		}
	}
	
	public void testGetOrganisationDirectory() throws Exception {
		final File organisationDirectory = task.getOrganisationDirectory();
		assertEquals(new File("../maven/repo/org/impalaframework"), organisationDirectory);
	}

	private void expectFailure(String expectedMessage) {
		try {
			task.checkArgs();
			fail();
		} catch (BuildException e) {
			assertEquals(expectedMessage, e.getMessage());
		}
	}
	
}
