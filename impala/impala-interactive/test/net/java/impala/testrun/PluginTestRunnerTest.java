package net.java.impala.testrun;

import net.java.impala.util.PathUtils;
import junit.framework.TestCase;

public class PluginTestRunnerTest extends TestCase {

	public void testRun() {
		String name = PathUtils.getCurrentDirectoryName();
		System.out.println(name);
	}


}
