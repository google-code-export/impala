package net.java.impala.command.impl;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

public class ContextSpecAwareClassFilterTest extends TestCase {

	public final void testAccept() throws IOException {
		
		final ContextSpecAwareClassFilter filter = new ContextSpecAwareClassFilter();
		
		try {
			filter.accept(new File("test/net/java/impala/command/impl/SpecAwareClass.class"));
			fail(IllegalStateException.class.getName());
		}
		catch (IllegalStateException e) {
			assertEquals("root canonical path not set", e.getMessage());
		}
		
		filter.setRootPath(new File("test"));

		//directory is true
		assertTrue(filter.accept(new File("test/net")));
		
		//should find this one
		assertTrue(filter.accept(new File("test/net/java/impala/command/impl/SpecAwareClass.class")));

		//won't work for interface
		assertFalse(filter.accept(new File("test/net/java/impala/command/impl/SpecAwareInterface.class")));
		assertFalse(filter.accept(new File("test/net/java/impala/command/impl/AbstractSpecAwareClass.class")));
		
		assertFalse(filter.accept(new File("test/net/java/impala/command/impl/SpecAwareClass.java")));
		assertFalse(filter.accept(new File("nonexistentfile.class")));
		assertFalse(filter.accept(new File("test/net/java/impala/command/impl/ContextSpecAwareClassFilterTest.java")));
	}

}
