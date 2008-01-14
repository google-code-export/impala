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
	
	private Map<String, Command> commandMap = new HashMap<String, Command>();
	private Map<String, String> aliasMap = new HashMap<String, String>();
	
	public boolean execute(CommandState commandState) {
		
		CommandPropertyValue fullCommandText = commandState.getProperties().get(CommandStateConstants.COMMAND_TEXT);
		GlobalCommandState.getInstance().addProperty(CommandStateConstants.LAST_COMMAND, fullCommandText);
		
		String[] commandTerms = fullCommandText.getValue().split(" ");
		
		final String commandName = commandTerms[0];
		Command command = commandMap.get(commandName);
		
		if (command == null && aliasMap != null) {
			//treat command name as an alias
			String aliasName = commandName;
			String aliasCommandName = aliasMap.get(aliasName);
			command = commandMap.get(aliasCommandName);
		}
		
		if (command != null) {
			command.execute(commandState);
		} else {
			System.out.println("Unrecognised command or alias " + commandName);
		}
			
		return false;
	}

	public void addCommand(String name, Command command) {
		commandMap.put(name, command);
	}
	
	public void setCommandMap(Map<String, Command> commandMap) {
		this.commandMap.clear();
		this.commandMap.putAll(commandMap);
	}

	public void addAlias(String name, String alias) {
		aliasMap.put(name, alias);
	}
	
	public void setAliasMap(Map<String, String> aliasMap) {
		this.aliasMap.clear();
		this.aliasMap.putAll(aliasMap);
	}

	public CommandDefinition getCommandDefinition() {
		CommandInfo commandInfo = new CommandInfo("c", "commandText", "Command text",
				"Please enter your command text", null, null, false, false, false);

		CommandDefinition commandDefinititon = new CommandDefinition();
		commandDefinititon.add(commandInfo);
		return commandDefinititon;	
	}

}
