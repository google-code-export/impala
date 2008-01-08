package org.impalaframework.command.interactive;

import java.util.HashMap;
import java.util.Map;

import org.impalaframework.command.Command;
import org.impalaframework.command.CommandInfo;
import org.impalaframework.command.CommandPropertyValue;
import org.impalaframework.command.CommandSpec;
import org.impalaframework.command.CommandState;

public class InteractiveTestCommand implements Command {
	
	private Map<String, Command> registeredCommand = new HashMap<String, Command>();

	public boolean execute(CommandState commandState) {
		
		CommandPropertyValue fullCommandText = commandState.getProperties().get("commandText");
		
		String[] commandTerms = fullCommandText.getValue().split(" ");
		
		String commandName = commandTerms[0];
		Command command = registeredCommand.get(commandName);
		
		if (command != null) {
			command.execute(commandState);
		} else {
			System.out.println("Unrecognised command " + commandName);
		}
		
		commandState.getProperties().put("lastCommand", fullCommandText);		
		return false;
	}

	public void addCommand(String name, Command command) {
		registeredCommand.put(name, command);
	}
	
	public CommandSpec getCommandSpec() {
		CommandInfo commandInfo = new CommandInfo("c", "commandText", "Command text",
				"Please enter your command text", null, null, false, false, false);

		CommandSpec commandDefinititon = new CommandSpec();
		commandDefinititon.add(commandInfo);
		return commandDefinititon;	
	}

}
