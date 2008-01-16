package org.impalaframework.command.interactive;

import org.impalaframework.command.framework.CommandState;
import org.impalaframework.command.framework.GlobalCommandState;
import org.impalaframework.testrun.DynamicContextHolder;

import junit.framework.TestCase;

public class LoadDefinitionFromClassCommandTest extends TestCase {

	private LoadDefinitionFromClassCommand fromClassCommand;
	private LoadDefinitionFromClassNameCommand fromClassNameCommand;
	private CommandState commandState;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		DynamicContextHolder.clear();
		GlobalCommandState.getInstance().reset();
		fromClassCommand = new LoadDefinitionFromClassCommand();
		fromClassNameCommand = new LoadDefinitionFromClassNameCommand();
		commandState = new CommandState();
	}
	
	public final void testNotSetFromClassCommand() {
		assertFalse(fromClassCommand.execute(commandState));
		GlobalCommandState.getInstance().addValue(CommandStateConstants.TEST_CLASS_NAME, Test1.class.getName());
		assertFalse(fromClassCommand.execute(commandState));
	}
	
	public final void testNotSetFromClassNameCommand() {
		assertFalse(fromClassNameCommand.execute(commandState));
		GlobalCommandState.getInstance().addValue(CommandStateConstants.TEST_CLASS, Test1.class);
		assertFalse(fromClassNameCommand.execute(commandState));
	}
	
	public final void testSetFromClassCommand() {
		GlobalCommandState.getInstance().addValue(CommandStateConstants.TEST_CLASS, Test1.class);
		assertTrue(fromClassCommand.execute(commandState));
		GlobalCommandState.getInstance().getValue(CommandStateConstants.TEST_CLASS_NAME);
	}
	
	public final void testSetFromClassNameCommand() {
		GlobalCommandState.getInstance().addValue(CommandStateConstants.TEST_CLASS_NAME, Test1.class.getName());
		assertTrue(fromClassNameCommand.execute(commandState));
		GlobalCommandState.getInstance().getValue(CommandStateConstants.TEST_CLASS);
	}

}
