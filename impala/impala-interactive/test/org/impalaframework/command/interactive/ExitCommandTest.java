package org.impalaframework.command.interactive;

import org.impalaframework.command.framework.TerminatedApplicationException;

import junit.framework.TestCase;

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
