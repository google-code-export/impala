package org.impalaframework.command.interactive;

import org.impalaframework.command.framework.Command;
import org.impalaframework.command.framework.CommandDefinition;
import org.impalaframework.command.framework.CommandState;
import org.impalaframework.command.framework.GlobalCommandState;
import org.impalaframework.module.definition.ModuleDefinitionSource;

public class LoadTestClassContextCommand implements Command {

	public boolean execute(CommandState commandState) {
		Object property = GlobalCommandState.getInstance().getValue(CommandStateConstants.TEST_CLASS_NAME);
		if (property == null) {
			System.out.println("No test class set.");
			return false;
		}
		
		String testClassName = property.toString();
		loadTestClass(testClassName);
		return true;
	}
	
	private void loadTestClass(String testClassName) {
		
		Class<?> c = null;
		try {
			c = Class.forName(testClassName);
			try {
				Object o = c.newInstance();
				if (o instanceof ModuleDefinitionSource) {
					ModuleDefinitionSource p = (ModuleDefinitionSource) o;
					GlobalCommandState.getInstance().addValue(CommandStateConstants.MODULE_DEFINITION_SOURCE, p);
				}
				
				GlobalCommandState.getInstance().addValue(CommandStateConstants.TEST_CLASS, c);
			}
			catch (Throwable e) {
				System.out.println("Unable to instantiate " + testClassName);
				InteractiveCommandUtils.printException(e);
			}
		}
		catch (ClassNotFoundException e) {
			System.out.println("Unable to find test class " + testClassName);
			InteractiveCommandUtils.printException(e);
		}
		
	}

	public CommandDefinition getCommandDefinition() {
		return new CommandDefinition();
	}

}
