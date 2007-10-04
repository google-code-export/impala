package net.java.impala.ant;

import java.io.File;
import java.util.List;

import org.apache.tools.ant.BuildException;

import junit.framework.TestCase;

public class AntForeachTaskTest extends TestCase {

	private AntForeachTask task;

	public void testGetSubdirectories() {
		task = new AntForeachTask();
		File root = new File("../");
		task.setDir(root);
		task.setProjects("impala,impala-core");
		List<File> subdirectories = task.getSubdirectories();
		assertEquals(2, subdirectories.size());
		assertTrue(subdirectories.contains(new File(root, "impala")));
	}

	public void testCheckArgs() {
		task = new AntForeachTask();
		doFail("'values' property not specified");
		task.setProjects("value1,value2");
		doFail("'dir' property not specified");
		task.setDir(new File("a file that does not exit"));
		doFail("'dir' does not exist");
		task.setDir(new File(".classpath"));
		doFail("'dir' must be a directory");
	}
	
	private void doFail(String expected) {
		try {
			task.checkArgs();
			fail();
		}
		catch (BuildException e) {
			System.out.println("Message: " + e.getMessage());
			assertTrue(e.getMessage().contains(expected));
		}
	}

}
