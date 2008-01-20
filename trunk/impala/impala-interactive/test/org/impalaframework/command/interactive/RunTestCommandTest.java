package org.impalaframework.command.interactive;

import org.impalaframework.command.framework.CommandState;
import org.impalaframework.command.framework.GlobalCommandState;
import org.impalaframework.exception.NoServiceException;
import org.impalaframework.testrun.DynamicContextHolder;

import junit.framework.TestCase;

public class RunTestCommandTest extends TestCase {

	private RunTestCommand runTestCommand;

	private CommandState commandState;

	private RerunTestCommand rerunTestCommand;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		runTestCommand = new RunTestCommand();
		rerunTestCommand = new RerunTestCommand();
		commandState = new CommandState();
		GlobalCommandState.getInstance().reset();
		DynamicContextHolder.clear();
		System.clearProperty("impala.root.projects");
	}

	public final void testNotSet() {
		assertFalse(runTestCommand.execute(commandState));
		assertFalse(rerunTestCommand.execute(commandState));
	}

	public final void testNoModuleDefinition() {
		GlobalCommandState.getInstance().addValue(CommandStateConstants.TEST_CLASS, Test1.class);
		try {
			runTestCommand.execute(commandState);
			fail();
		}
		catch (NoServiceException e) {
		}
	}

	public final void testWithModuleDefinition() {
		System.setProperty("impala.root.projects", "impala-core");

		//no test method set, so this returns false
		assertFalse(rerunTestCommand.execute(commandState));
		
		GlobalCommandState.getInstance().addValue(CommandStateConstants.TEST_CLASS, Test1.class);
		DynamicContextHolder.init(new Test1());
		assertTrue(runTestCommand.execute(commandState));

		//returns true, because test name set
		assertTrue(rerunTestCommand.execute(commandState));
	}

}
