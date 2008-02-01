package org.impalaframework.command.interactive;

import junit.framework.TestCase;

import org.impalaframework.command.framework.CommandInfo;
import org.impalaframework.command.framework.CommandLineInputCapturer;
import org.impalaframework.command.framework.CommandState;
import org.impalaframework.command.framework.GlobalCommandState;
import org.impalaframework.facade.DynamicContextHolder;

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
		fromClassNameCommand = new LoadDefinitionFromClassNameCommand(null);
		commandState = new CommandState();
		setInputCapturer();
		GlobalCommandState.getInstance().addValue(CommandStateConstants.DIRECTORY_NAME, "impala-interactive");
	}

	public final void testNotSetFromClassCommand() {
		assertFalse(fromClassCommand.execute(commandState));
		GlobalCommandState.getInstance().addValue(CommandStateConstants.TEST_CLASS_NAME, Test1.class.getName());
		assertFalse(fromClassCommand.execute(commandState));
	}

	public final void testNotSetFromClassNameCommand() {
		assertTrue(fromClassNameCommand.execute(commandState));
		assertEquals(Test1.class, GlobalCommandState.getInstance().getValue(CommandStateConstants.TEST_CLASS));
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

	private void setInputCapturer() {
		commandState.setInputCapturer(new CommandLineInputCapturer() {
			@Override
			public String capture(CommandInfo info) {
				if (info.getPropertyName().equals("class")) {
					return Test1.class.getName();
				}
				return super.capture(info);
			}
		});
	}

}
