package org.impalaframework.command.interactive;

import junit.framework.TestCase;

import org.impalaframework.command.framework.CommandState;
import org.impalaframework.command.framework.GlobalCommandState;

public class LoadTestClassContextCommandTest extends TestCase {

	private LoadTestClassContextCommand command;
	private CommandState commandState;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		command = new LoadTestClassContextCommand();
		commandState = new CommandState();
		GlobalCommandState.getInstance().reset();
	}
	
	public final void testNoTestClassSet() {
		assertFalse(command.execute(commandState));
	}
	
	public final void testWithTestClassSet() {
		GlobalCommandState.getInstance().addValue(CommandStateConstants.TEST_CLASS_NAME, Test1.class.getName());
		assertFalse(command.execute(commandState));
		assertNotNull(GlobalCommandState.getInstance().getValue(CommandStateConstants.MODULE_DEFINITION_SOURCE));
		assertNotNull(GlobalCommandState.getInstance().getValue(CommandStateConstants.TEST_CLASS));
	}

}
