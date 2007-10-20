package net.java.impala.command.impl;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

public class ContextSpecAwareClassFilterTest extends TestCase {

	public final void testAccept() throws IOException {
		
		String canonicalRootPath = new File("test").getCanonicalPath();
		final ContextSpecAwareClassFilter filter = new ContextSpecAwareClassFilter(canonicalRootPath);
		assertTrue(filter.accept(new File("test/net/java/impala/command/impl/SpecAwareClass.class")));
		assertFalse(filter.accept(new File("test/net/java/impala/command/impl/SpecAwareClass.java")));
		assertFalse(filter.accept(new File("nonexistentfile.class")));
		assertFalse(filter.accept(new File("test/net/java/impala/command/impl/ContextSpecAwareClassFilterTest.java")));
	}

}
