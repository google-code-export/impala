package org.impalaframework.command.interactive;

import junit.framework.TestCase;

import org.impalaframework.command.framework.CommandPropertyValue;
import org.impalaframework.command.framework.CommandState;
import org.impalaframework.command.framework.GlobalCommandState;
import org.impalaframework.exception.NoServiceException;
import org.impalaframework.testrun.DynamicContextHolder;

public class ReloadModuleCommandTest extends TestCase {

	private static ReloadModuleCommand command;

	private CommandState commandState;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		DynamicContextHolder.clear();
		GlobalCommandState.getInstance().reset();
		command = new ReloadModuleCommand();
		commandState = new CommandState();
		commandState.addProperty(ReloadModuleCommand.MODULE_NAME, new CommandPropertyValue(Test1.plugin1));
	}

	public final void testExtractText1() {
		command.extractText(new String[] { "str1", "str2" }, commandState);
		assertEquals("str1", commandState.getGlobalStateHolder().getProperty(ReloadModuleCommand.MODULE_NAME)
				.getValue());
	}

	public final void testExtractText2() {
		command.extractText(new String[] { " str1 ", "str2" }, commandState);
		assertEquals("str1", commandState.getGlobalStateHolder().getProperty(ReloadModuleCommand.MODULE_NAME)
				.getValue());
	}

	public void testReloadNoService() {
		try {
			command.execute(commandState);
			fail();
		}
		catch (NoServiceException e) {
		}
	}

	public void testReload() throws Exception {
		doReloadTest();
	}
	
	public void testReloadLike() throws Exception {
		commandState.addProperty(ReloadModuleCommand.MODULE_NAME, new CommandPropertyValue("impala-sample-dynamic-plugin1"));
		doReloadTest();
	}

	private void doReloadTest() {
		GlobalCommandState.getInstance().addValue(CommandStateConstants.MODULE_DEFINITION_SOURCE, new Test1());
		assertTrue(new InitContextCommand().execute(null));
		
		command.execute(commandState);

		CommandPropertyValue value = commandState.getProperties().get(ReloadModuleCommand.ACTUAL_MODULE_RELOADED);
		assertEquals(Test1.plugin1, value.getValue());
	}

}
