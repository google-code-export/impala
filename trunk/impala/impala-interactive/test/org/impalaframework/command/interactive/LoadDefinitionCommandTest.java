package org.impalaframework.command.interactive;

import junit.framework.TestCase;

import org.impalaframework.command.framework.CommandState;
import org.impalaframework.command.framework.GlobalCommandState;
import org.impalaframework.facade.DynamicContextHolder;

public class LoadDefinitionCommandTest extends TestCase {

	private LoadDefinitionFromClassCommand command;

	private CommandState commandState;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		DynamicContextHolder.clear();
		GlobalCommandState.getInstance().reset();
		command = new LoadDefinitionFromClassCommand();
		commandState = new CommandState();
	}

	public void testClassNotSet() {
		assertFalse(command.execute(commandState));
	}
	
	public void testClassIsSet() {
		GlobalCommandState.getInstance().addValue(CommandStateConstants.TEST_CLASS, Test1.class);
		assertTrue(command.execute(commandState));
		assertNotNull(GlobalCommandState.getInstance().getValue(CommandStateConstants.TEST_CLASS_NAME));
	}

}
