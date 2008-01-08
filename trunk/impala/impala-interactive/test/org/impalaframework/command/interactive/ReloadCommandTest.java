package org.impalaframework.command.interactive;

import org.impalaframework.command.CommandDefinition;
import org.impalaframework.command.GlobalCommandState;

import junit.framework.TestCase;

public class ReloadCommandTest extends TestCase {

	private ReloadCommand command;

	@Override
	protected void setUp() throws Exception {
		GlobalCommandState.getInstance().reset();
		command = new ReloadCommand();
	}
	
	public final void testExecute() {
		assertFalse(command.execute(null));
	}

	public final void testGetCommandDefinition() {
		CommandDefinition commandDefinition = command.getCommandDefinition();
		assertTrue(commandDefinition.getCommandInfos().isEmpty());
	}

}
