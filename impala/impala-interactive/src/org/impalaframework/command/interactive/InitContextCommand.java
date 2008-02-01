package org.impalaframework.command.interactive;

import org.impalaframework.command.framework.Command;
import org.impalaframework.command.framework.CommandDefinition;
import org.impalaframework.command.framework.CommandState;
import org.impalaframework.command.framework.GlobalCommandState;
import org.impalaframework.facade.DynamicContextHolder;
import org.impalaframework.module.definition.ModuleDefinitionSource;

public class InitContextCommand implements Command {

	public boolean execute(CommandState commandState) {
		DynamicContextHolder.init();
		
		ModuleDefinitionSource moduleDefinitionSource = (ModuleDefinitionSource) GlobalCommandState.getInstance()
				.getValue(CommandStateConstants.MODULE_DEFINITION_SOURCE);
		if (moduleDefinitionSource == null) {
			System.out.println("Cannot initialize, as no module definition has been loaded.");
			return false;
		}

		DynamicContextHolder.init(moduleDefinitionSource);
		return true;
	}

	public CommandDefinition getCommandDefinition() {
		return new CommandDefinition();
	}

}
