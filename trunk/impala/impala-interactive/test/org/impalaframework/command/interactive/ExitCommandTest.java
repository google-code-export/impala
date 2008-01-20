package org.impalaframework.command.interactive;

import junit.framework.TestCase;

import org.impalaframework.command.framework.TerminatedApplicationException;

public class ExitCommandTest extends TestCase {

	public final void testExecute() {
		ExitCommand exit = new ExitCommand();
		try {
			exit.execute(null);
			fail();
		}
		catch (TerminatedApplicationException e) {
		}
	}

}
