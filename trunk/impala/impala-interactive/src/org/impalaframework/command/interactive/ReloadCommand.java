package org.impalaframework.command.interactive;

import org.impalaframework.command.Command;
import org.impalaframework.command.CommandDefinition;
import org.impalaframework.command.CommandState;
import org.impalaframework.command.GlobalCommandState;
import org.impalaframework.testrun.DynamicContextHolder;

public class ReloadCommand implements Command {

	public boolean execute(CommandState commandState) {
		if (GlobalCommandState.getInstance().getValue("moduleDefinition") == null) {
			System.out.println("Cannot reload, as no module definition has been loaded.");
			return false;
		}
		DynamicContextHolder.reloadParent();
		return true;
	}

	public CommandDefinition getCommandDefinition() {
		return new CommandDefinition();
	}
	
	
}
