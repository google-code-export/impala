package org.impalaframework.testrun;

import org.impalaframework.util.PathUtils;

import junit.framework.TestCase;

public class ImpalaTestRunnerTest extends TestCase {

	public void testRun() {
		String name = PathUtils.getCurrentDirectoryName();
		System.out.println(name);
	}


}
