package org.impalaframework.command.interactive;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.impalaframework.command.framework.Command;
import org.impalaframework.command.framework.CommandLineInputCapturer;
import org.impalaframework.command.framework.CommandState;

public class ManualInteractiveTestCommandTest extends TestCase {

	public void testInteractive() throws Exception {

		InteractiveTestCommand command = getCommand();
		Map<String, Command> commandMap = getCommandMap();
		Set<String> commandKeys = commandMap.keySet();
		
		for (String commandKey : commandKeys) {
			command.addCommand(commandKey, commandMap.get(commandKey));
		}

		// now need to capture
		CommandState commandState = new CommandState();

		CommandLineInputCapturer inputCapturer = getInputCapturer();
		commandState.setInputCapturer(inputCapturer);

		System.out.println("--------------------");

		commandState.capture(command);
		command.execute(commandState);
	}
	
	protected Map<String, Command> getCommandMap() {
		Map<String, Command> commands = new HashMap<String, Command>();
		commands.put("exit", new ExitCommand());
		commands.put("reload-all", new ReloadCommand());
		return commands;
	}

	protected InteractiveTestCommand getCommand() {
		InteractiveTestCommand command = new InteractiveTestCommand();
		return command;
	}

	protected CommandLineInputCapturer getInputCapturer() {
		return new CommandLineInputCapturer();
	}
}
