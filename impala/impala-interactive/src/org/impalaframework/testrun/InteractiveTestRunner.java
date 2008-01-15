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
import java.util.LinkedHashMap;
import java.util.Map;

import org.impalaframework.command.framework.Command;
import org.impalaframework.command.framework.CommandLineInputCapturer;
import org.impalaframework.command.framework.CommandState;
import org.impalaframework.command.framework.GlobalCommandState;
import org.impalaframework.command.interactive.CommandStateConstants;
import org.impalaframework.command.interactive.ExitCommand;
import org.impalaframework.command.interactive.InitRunnerCommand;
import org.impalaframework.command.interactive.InteractiveTestCommand;
import org.impalaframework.command.interactive.ReloadCommand;
import org.impalaframework.command.interactive.ReloadModuleCommand;
import org.impalaframework.command.interactive.RunTestCommand;
import org.impalaframework.command.interactive.UsageCommand;
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

		CommandState commandState = new CommandState();
		CommandLineInputCapturer inputCapturer = new CommandLineInputCapturer();
		commandState.setInputCapturer(inputCapturer);
		
		GlobalCommandState.getInstance().addValue(CommandStateConstants.TEST_CLASS, testClass);

		Command initCommand = getInitCommand();
		initCommand.execute(commandState);
		
		InteractiveTestCommand testCommand = new InteractiveTestCommand();
		Map<String, String> aliasMap = getAliasMap();
		testCommand.setAliasMap(aliasMap);
		Map<String, Command> commandMap = getCommandMap();
		commandMap.put("usage", new UsageCommand(commandMap, aliasMap));
		testCommand.setCommandMap(commandMap);
		
		
		System.out.println("--------------------");

		while (true) {
			commandState.capture(testCommand);
			testCommand.execute(commandState);
		}
	}

	protected Command getInitCommand() {
		return new InitRunnerCommand();
	}

	protected Map<String, Command> getCommandMap() {
		Map<String, Command> commands = new LinkedHashMap<String, Command>();
		commands.put("reload-all", new ReloadCommand());
		commands.put("reload-module", new ReloadModuleCommand());
		commands.put("test", new RunTestCommand());
		commands.put("exit", new ExitCommand());
		return commands;
	}
	
	protected Map<String, String> getAliasMap() {
		Map<String, String> aliases = new HashMap<String, String>();
		aliases.put("e", "exit");
		aliases.put("rel", "reload-all");
		aliases.put("rm", "reload-module");
		aliases.put("t", "test");
		aliases.put("u", "usage");
		return aliases;
	}
}