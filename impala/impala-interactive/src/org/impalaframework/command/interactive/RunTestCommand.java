package org.impalaframework.command.interactive;

import org.impalaframework.command.basic.SelectMethodCommand;
import org.impalaframework.command.framework.CommandState;

public class RunTestCommand extends BaseRunTestCommand {

	protected String getMethodName(CommandState commandState, Class<?> testClass) {
		SelectMethodCommand command = new SelectMethodCommand(testClass);
		command.execute(commandState);
		String methodName = command.getMethodName();
		
		if (methodName == null) {
			System.out.println("No matching test method found.");
		}
		
		return methodName;
	}

}
