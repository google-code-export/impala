/*
 * Copyright 2007 the original author or authors.
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

package org.impalaframework.testrun;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.impalaframework.command.Command;
import org.impalaframework.command.CommandLineInputCapturer;
import org.impalaframework.command.CommandState;
import org.impalaframework.command.interactive.ExitCommand;
import org.impalaframework.command.interactive.InteractiveTestCommand;
import org.impalaframework.command.interactive.ReloadCommand;
import org.impalaframework.facade.FacadeConstants;
import org.impalaframework.facade.ParentReloadingOperationsFacade;
import org.impalaframework.module.definition.ModuleDefinitionSource;

public class InteractiveTestRunner {

	public static void main(String[] args) {
		run();
	}

	public static void run() {
		new InteractiveTestRunner().start(null);
	}

	public static void run(Class<? extends ModuleDefinitionSource> sourceClass) {
		// autoreload not enabled by default
		new InteractiveTestRunner().start(sourceClass);
	}

	public InteractiveTestRunner() {
		super();
	}

	/**
	 * Runs a suite extracted from a TestCase subclass.
	 */
	public void start(Class<?> testClass) {

		// only set this if not set
		if (System.getProperty(FacadeConstants.FACADE_CLASS_NAME) == null) {
			System.setProperty(FacadeConstants.FACADE_CLASS_NAME, ParentReloadingOperationsFacade.class.getName());
		}
		DynamicContextHolder.init();

		InteractiveTestCommand command = new InteractiveTestCommand();
		Map<String, Command> commandMap = getCommandMap();
		Set<String> commandKeys = commandMap.keySet();
		
		for (String commandKey : commandKeys) {
			command.addCommand(commandKey, commandMap.get(commandKey));
		}

		CommandState commandState = new CommandState();
		CommandLineInputCapturer inputCapturer = new CommandLineInputCapturer();
		commandState.setInputCapturer(inputCapturer);

		System.out.println("--------------------");

		while (true) {
			commandState.capture(command);
			command.execute(commandState);
		}
	}
	
	protected Map<String, Command> getCommandMap() {
		Map<String, Command> commands = new HashMap<String, Command>();
		commands.put("exit", new ExitCommand());
		commands.put("reload-all", new ReloadCommand());
		return commands;
	}
}