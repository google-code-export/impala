package org.impalaframework.command.impl;

import org.impalaframework.command.impl.ClassFindCommand;
import org.impalaframework.command.impl.ClassFindCommand.ClassFindFileRecurseHandler;

import junit.framework.TestCase;

public class ClassFindFileRecurseHandlerTest extends TestCase {
	
	public void testGetPackageSegment() throws Exception {
		ClassFindCommand cmd = new ClassFindCommand();
		ClassFindFileRecurseHandler handler = cmd.new ClassFindFileRecurseHandler("ClassFindCommand");
		assertEquals("ClassFindCommand", handler.getClassSegment());
		assertEquals(null, handler.getPackageSegment());
		
		handler = cmd.new ClassFindFileRecurseHandler("net.java.ClassFindCommand");
		assertEquals("ClassFindCommand", handler.getClassSegment());
		assertEquals("net.java", handler.getPackageSegment());
		
		handler = cmd.new ClassFindFileRecurseHandler("net.java.");
		assertEquals("", handler.getClassSegment());
		assertEquals("net.java", handler.getPackageSegment());
	}

	
}
