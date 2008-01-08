package org.impalaframework.command.interactive;

import org.impalaframework.command.Command;
import org.impalaframework.command.CommandDefinition;
import org.impalaframework.command.CommandState;
import org.impalaframework.command.GlobalCommandState;
import org.impalaframework.module.definition.ConstructedModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.testrun.DynamicContextHolder;

public class InitContextCommand implements Command {

	public boolean execute(CommandState commandState) {
		RootModuleDefinition moduleDefinition = (RootModuleDefinition) GlobalCommandState.getInstance().getValue("moduleDefinition");
		if (moduleDefinition == null) {
			System.out.println("Cannot initialize, as no module definition has been loaded.");
			return false;
		}
		
		DynamicContextHolder.init(new ConstructedModuleDefinitionSource(moduleDefinition));		
		return true;
	}

	public CommandDefinition getCommandDefinition() {
		return new CommandDefinition();
	}
	
	
}
