package org.impalaframework.command.basic;

import junit.framework.TestCase;

import org.impalaframework.command.basic.ClassFindCommand.ClassFindFileRecurseHandler;

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
