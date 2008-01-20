package org.impalaframework.command.basic;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class FileFilterHandlerTest extends TestCase {
	
	public void testFiltering() throws Exception {
		FileFilterHandler handler = new FileFilterHandler();
		final ArrayList<File> files = new ArrayList<File>();
		files.add(new File("../impala"));
		files.add(new File("../impala-core"));
		files.add(new File("../impala-interactive"));
		files.add(new File("../impala-web"));
		final List<File> result = handler.filter(files, new FileFilter() {
			int count;
			public boolean accept(File pathname) {
				count++;
				return count % 2 == 0;
			}
		});
		
		assertEquals(2, result.size());
	}
}
