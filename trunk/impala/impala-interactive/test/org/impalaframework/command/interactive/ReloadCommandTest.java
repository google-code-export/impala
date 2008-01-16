package org.impalaframework.command.interactive;

import org.impalaframework.command.framework.CommandDefinition;
import org.impalaframework.command.framework.GlobalCommandState;
import org.impalaframework.exception.NoServiceException;

import junit.framework.TestCase;

public class ReloadCommandTest extends TestCase {

	private ReloadCommand command;

	@Override
	protected void setUp() throws Exception {
		GlobalCommandState.getInstance().reset();
		command = new ReloadCommand();
	}
	
	public final void testExecute() {
		//no module definition loaded, so this won't work
		assertFalse(command.execute(null));
		
		//now load up the module definition properly
		GlobalCommandState.getInstance().addValue(CommandStateConstants.MODULE_DEFINITION_SOURCE, new Test1());
		
		//this will cause NoServiceException, because DynamicContextHolder.init() has not been called
		try {
			command.execute(null);
		}
		catch (NoServiceException e) {
			e.printStackTrace();
			assertEquals("The application has not been initialised. Has DynamicContextHolder.init(ModuleDefinitionSource) been called?", e.getMessage());
		}
		
		assertTrue(new InitContextCommand().execute(null));
		
		assertTrue(command.execute(null));
	}

	public final void testGetCommandDefinition() {
		CommandDefinition commandDefinition = command.getCommandDefinition();
		assertTrue(commandDefinition.getCommandInfos().isEmpty());
	}

}
