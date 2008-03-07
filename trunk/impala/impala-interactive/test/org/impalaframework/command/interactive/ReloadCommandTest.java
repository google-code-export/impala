/*
 * Copyright 2007-2008 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.impalaframework.command.interactive;

import junit.framework.TestCase;

import org.impalaframework.command.framework.CommandDefinition;
import org.impalaframework.command.framework.GlobalCommandState;
import org.impalaframework.exception.NoServiceException;

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
