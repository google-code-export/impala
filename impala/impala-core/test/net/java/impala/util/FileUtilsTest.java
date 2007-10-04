package net.java.impala.util;

import java.io.File;
import java.io.IOException;

import org.springframework.core.io.FileSystemResource;

import junit.framework.TestCase;

public class FileUtilsTest extends TestCase {

	public void testGetClassBytesFromFile() throws IOException {
		assertNotNull(FileUtils.getBytes(new File(".classpath")));
	}

	public void testGetClassBytesFromResource() throws IOException {
		assertNotNull(FileUtils.getBytes(new FileSystemResource(".classpath")));
		assertEquals(FileUtils.getBytes(new File(".classpath")).length, FileUtils.getBytes(new FileSystemResource(
				".classpath")).length);
	}

}
