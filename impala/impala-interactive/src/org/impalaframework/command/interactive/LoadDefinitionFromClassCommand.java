package org.impalaframework.command.interactive;

import org.impalaframework.command.framework.CommandDefinition;
import org.impalaframework.command.framework.CommandState;
import org.impalaframework.command.framework.GlobalCommandState;

public class LoadDefinitionFromClassCommand extends BaseLoadDefinitionCommand {

	public boolean execute(CommandState commandState) {
		Class<?> testClass = (Class<?>) GlobalCommandState.getInstance().getValue(CommandStateConstants.TEST_CLASS);

		if (testClass != null) {
			System.out.println("Test class set to " + testClass.getName());
			
			GlobalCommandState.getInstance().addValue(CommandStateConstants.TEST_CLASS_NAME, testClass.getName());
			doLoad(commandState);
			return true;
		}
		else {
			System.out.println("Test class not set");
			return false;
		}
	}

	public CommandDefinition getCommandDefinition() {
		return new CommandDefinition("Loads module definition using supplied test class");
	}

	
	
}
