package org.impalaframework.command.interactive;

import java.util.HashMap;
import java.util.Map;

import org.impalaframework.command.Command;
import org.impalaframework.command.CommandInfo;
import org.impalaframework.command.CommandPropertyValue;
import org.impalaframework.command.CommandDefinition;
import org.impalaframework.command.CommandState;
import org.impalaframework.command.GlobalCommandState;

public class InteractiveTestCommand implements Command {
	
	private Map<String, Command> registeredCommand = new HashMap<String, Command>();

	public boolean execute(CommandState commandState) {
		
		CommandPropertyValue fullCommandText = commandState.getProperties().get("commandText");
		GlobalCommandState.getInstance().addProperty("lastCommand", fullCommandText);
		
		String[] commandTerms = fullCommandText.getValue().split(" ");
		
		String commandName = commandTerms[0];
		Command command = registeredCommand.get(commandName);
		
		if (command != null) {
			command.execute(commandState);
		} else {
			System.out.println("Unrecognised command " + commandName);
		}
			
		return false;
	}

	public void addCommand(String name, Command command) {
		registeredCommand.put(name, command);
	}
	
	public CommandDefinition getCommandDefinition() {
		CommandInfo commandInfo = new CommandInfo("c", "commandText", "Command text",
				"Please enter your command text", null, null, false, false, false);

		CommandDefinition commandDefinititon = new CommandDefinition();
		commandDefinititon.add(commandInfo);
		return commandDefinititon;	
	}

}
