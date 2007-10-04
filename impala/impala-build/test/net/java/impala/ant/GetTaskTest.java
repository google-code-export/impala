package net.java.impala.ant;

import java.io.File;

import junit.framework.TestCase;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;

public class GetTaskTest extends TestCase {

	private File downloadDir;

	private GetTask task;

	public void setUp() {
		File tmpDir = new File(System.getProperty("java.io.tmpdir"));
		downloadDir = new File(tmpDir, "downloads");
		downloadDir.mkdir();

		File file = new File("resources/test-dependencies.txt");
		File toDir = downloadDir;
		String baseUrl = "http://ibiblio.org/pub/packages/maven2/";

		task = new GetTask();
		task.setBaseSourceUrls(baseUrl);
		task.setToDir(toDir);
		task.setDependencies(file);
		task.setProject(new Project());
	}

	public void testDuffDependencies() {
		task.setDependencies(null);
		doFail("");
		
		task.setDependencies(new File("a file that does not exist"));
		task.execute();

		// can't be a directory
		task.setDependencies(new File("src"));
		doFail("");
	}

	public void testDuffToDir() {
		task.setToDir(null);
		doFail("");

		task.setToDir(new File("a file that does not exist"));
		doFail("");

		// can't be a file
		task.setDependencies(new File(".classpath"));
		doFail("");
	}

	public void testDuffBaseUrl() {
		task.setBaseSourceUrls(null);
		doFail("");
	}

	private void doFail(String expected) {
		try {
			task.execute();
			fail();
		}
		catch (BuildException e) {
			assertTrue(e.getMessage().contains(expected));
		}
	}

}
