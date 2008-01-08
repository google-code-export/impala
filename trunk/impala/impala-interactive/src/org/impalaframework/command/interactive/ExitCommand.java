package org.impalaframework.command.interactive;

import org.impalaframework.command.Command;
import org.impalaframework.command.CommandSpec;
import org.impalaframework.command.CommandState;
import org.impalaframework.testrun.DynamicContextHolder;

public class ExitCommand implements Command {

	public boolean execute(CommandState commandState) {

		System.out.println("Exiting application");
		
		try {
			DynamicContextHolder.unloadParent();
		}
		catch (RuntimeException e) {
		}		
		
		System.exit(0);
		return false;
	}

	public CommandSpec getCommandSpec() {
		return new CommandSpec();
	}

}
