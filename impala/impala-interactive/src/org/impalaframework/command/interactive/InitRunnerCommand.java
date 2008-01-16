package org.impalaframework.command.interactive;

import org.impalaframework.command.framework.Command;
import org.impalaframework.command.framework.CommandDefinition;
import org.impalaframework.command.framework.CommandState;
import org.impalaframework.facade.FacadeConstants;
import org.impalaframework.facade.ParentReloadingOperationsFacade;
import org.impalaframework.testrun.DynamicContextHolder;

public class InitRunnerCommand implements Command {

	public boolean execute(CommandState commandState) {
		// only set this if not set
		if (System.getProperty(FacadeConstants.FACADE_CLASS_NAME) == null) {
			System.setProperty(FacadeConstants.FACADE_CLASS_NAME, ParentReloadingOperationsFacade.class.getName());
		}
		DynamicContextHolder.init();

		LoadDefinitionFromClassCommand command = new LoadDefinitionFromClassCommand();
		return command.execute(commandState);
	}

	public CommandDefinition getCommandDefinition() {
		return new CommandDefinition();
	}

}
