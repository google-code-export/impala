package org.impalaframework.command.basic;

import org.impalaframework.command.framework.CommandPropertyValue;
import org.impalaframework.command.framework.CommandState;
import org.impalaframework.command.framework.GlobalCommandState;
import org.impalaframework.command.interactive.CommandStateConstants;

import junit.framework.TestCase;

public class ChangeDirectoryCommandTest extends TestCase {

	private ChangeDirectoryCommand command;
	private CommandState commandState;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		commandState = new CommandState();
		command = new ChangeDirectoryCommand();
		GlobalCommandState.getInstance().reset();
	}

	public final void testChangeDirectoryCommandNull() {
		try {
			command.execute(commandState);
		}
		catch (IllegalArgumentException e) {
			assertEquals("[Assertion failed] - this argument is required; it must not null", e.getMessage());
		}
		assertNull(GlobalCommandState.getInstance().getValue(CommandStateConstants.DIRECTORY_NAME));
	}

	public final void testChangeDirectoryCommand() {
		commandState.addProperty(ChangeDirectoryCommand.DIRECTORY_NAME, new CommandPropertyValue("impala-core"));

		command.execute(commandState);
		assertEquals("impala-core", GlobalCommandState.getInstance().getValue(CommandStateConstants.DIRECTORY_NAME));
	}
	
	public final void testChangeDirectoryCommandDuffValue() {
		commandState.addProperty(ChangeDirectoryCommand.DIRECTORY_NAME, new CommandPropertyValue("duff-value"));

		command.execute(commandState);
		assertNull(GlobalCommandState.getInstance().getValue(CommandStateConstants.DIRECTORY_NAME));
	}

}
