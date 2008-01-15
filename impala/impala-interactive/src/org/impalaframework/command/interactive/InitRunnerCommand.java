package org.impalaframework.command.interactive;

import org.impalaframework.command.framework.Command;
import org.impalaframework.command.framework.CommandDefinition;
import org.impalaframework.command.framework.CommandState;
import org.impalaframework.command.framework.GlobalCommandState;
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

		Class<?> testClass = (Class<?>) GlobalCommandState.getInstance().getValue(CommandStateConstants.TEST_CLASS);

		if (testClass != null) {
			System.out.println("Test class set to " + testClass.getName());
			
			GlobalCommandState.getInstance().addValue(CommandStateConstants.TEST_CLASS_NAME, testClass.getName());

			LoadTestClassContextCommand loadCommand = new LoadTestClassContextCommand();
			loadCommand.execute(commandState);
			InitContextCommand initCommand = new InitContextCommand();
			initCommand.execute(commandState);
		}
		else {
			System.out.println("Test class not set");
		}
		return true;
	}

	public CommandDefinition getCommandDefinition() {
		return new CommandDefinition();
	}

}
