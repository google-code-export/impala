package org.impalaframework.command.interactive;

import org.impalaframework.command.framework.CommandState;
import org.impalaframework.command.framework.GlobalCommandState;

public class RerunTestCommand extends BaseRunTestCommand {

	protected String getMethodName(CommandState commandState, Class<?> testClass) {
		String methodName = (String) GlobalCommandState.getInstance().getValue(CommandStateConstants.TEST_METHOD_NAME);
		if (methodName == null) {
			System.out.println("No test method has been set");
		}
		return methodName;
	}

}
