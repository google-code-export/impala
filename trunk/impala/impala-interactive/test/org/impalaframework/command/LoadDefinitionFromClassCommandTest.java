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

package org.impalaframework.command;

import junit.framework.TestCase;

import org.impalaframework.command.framework.CommandInfo;
import org.impalaframework.command.framework.CommandLineInputCapturer;
import org.impalaframework.command.framework.CommandState;
import org.impalaframework.command.framework.GlobalCommandState;
import org.impalaframework.facade.Impala;
import org.impalaframework.resolver.StandaloneModuleLocationResolver;

public class LoadDefinitionFromClassCommandTest extends TestCase {

	private LoadDefinitionFromClassCommand fromClassCommand;

	private LoadDefinitionFromClassNameCommand fromClassNameCommand;

	private CommandState commandState;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		Impala.clear();
		GlobalCommandState.getInstance().reset();
		fromClassCommand = new LoadDefinitionFromClassCommand();
		fromClassNameCommand = new LoadDefinitionFromClassNameCommand(new StandaloneModuleLocationResolver());
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
