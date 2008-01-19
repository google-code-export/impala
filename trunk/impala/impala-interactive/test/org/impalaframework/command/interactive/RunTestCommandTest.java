package org.impalaframework.command.interactive;

import org.impalaframework.command.framework.CommandState;
import org.impalaframework.command.framework.GlobalCommandState;
import org.impalaframework.exception.NoServiceException;
import org.impalaframework.testrun.DynamicContextHolder;

import junit.framework.TestCase;

public class RunTestCommandTest extends TestCase {

	private RunTestCommand command;

	private CommandState commandState;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		command = new RunTestCommand();
		commandState = new CommandState();
		GlobalCommandState.getInstance().reset();
		DynamicContextHolder.clear();
		System.clearProperty("impala.parent.project");
	}

	public final void testNotSet() {
		assertFalse(command.execute(commandState));
	}


	public final void testNoModuleDefinition() {
		GlobalCommandState.getInstance().addValue(CommandStateConstants.TEST_CLASS, Test1.class);
		try {
			command.execute(commandState);
			fail();
		}
		catch (NoServiceException e) {
		}
	}


	public final void testWithModuleDefinition() {
		System.setProperty("impala.parent.project", "impala-core");

		GlobalCommandState.getInstance().addValue(CommandStateConstants.TEST_CLASS, Test1.class);
		DynamicContextHolder.init(new Test1());
		assertTrue(command.execute(commandState));
	}

}
