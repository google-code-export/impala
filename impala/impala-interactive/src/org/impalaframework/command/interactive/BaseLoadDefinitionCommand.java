package org.impalaframework.command.interactive;

import org.impalaframework.command.framework.Command;
import org.impalaframework.command.framework.CommandState;

public abstract class BaseLoadDefinitionCommand implements Command {

	protected void doLoad(CommandState commandState) {
		LoadTestClassContextCommand loadCommand = new LoadTestClassContextCommand();
		loadCommand.execute(commandState);
		InitContextCommand initCommand = new InitContextCommand();
		initCommand.execute(commandState);
	}	
	
}
