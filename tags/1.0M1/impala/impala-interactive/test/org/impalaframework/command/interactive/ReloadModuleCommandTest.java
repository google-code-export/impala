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

import org.impalaframework.command.framework.CommandPropertyValue;
import org.impalaframework.command.framework.CommandState;
import org.impalaframework.command.framework.GlobalCommandState;
import org.impalaframework.exception.NoServiceException;
import org.impalaframework.facade.Impala;

public class ReloadModuleCommandTest extends TestCase {

	private static ReloadModuleCommand command;

	private CommandState commandState;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		Impala.clear();
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
		commandState.addProperty(ReloadModuleCommand.MODULE_NAME, new CommandPropertyValue("sample-module1"));
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
