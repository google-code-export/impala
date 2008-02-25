package org.impalaframework.command.interactive;

import org.impalaframework.command.framework.Command;
import org.impalaframework.command.framework.CommandDefinition;
import org.impalaframework.command.framework.CommandState;
import org.impalaframework.command.framework.GlobalCommandState;
import org.impalaframework.facade.DynamicContextHolder;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.springframework.util.ClassUtils;

public class LoadTestClassContextCommand implements Command {

	public boolean execute(CommandState commandState) {
		Object property = GlobalCommandState.getInstance().getValue(CommandStateConstants.TEST_CLASS_NAME);
		if (property == null) {
			System.out.println("No test class set.");
			return false;
		}

		String testClassName = property.toString();
		return loadTestClass(testClassName);
	}

	private boolean loadTestClass(String testClassName) {

		Class<?> c = null;
		try {

			// FIXME this only allows you to switch directories to one containing a loaded module

			String directoryName = (String) GlobalCommandState.getInstance().getValue(
					CommandStateConstants.DIRECTORY_NAME);
			ClassLoader parent = null;

			try {
				if (directoryName != null) {
					parent = DynamicContextHolder.getModuleContext(directoryName).getClassLoader();
				}
				else {
					parent = DynamicContextHolder.get().getClassLoader();
				}
			}
			catch (Exception e) {
				//TODO more precise error message required here
				System.out.println("Unable to load module corresponding with directory name " + (directoryName != null ? directoryName : "[not set]"));
				parent = ClassUtils.getDefaultClassLoader();
			}

			c = Class.forName(testClassName, false, parent);
			try {
				Object o = c.newInstance();
				if (o instanceof ModuleDefinitionSource) {
					ModuleDefinitionSource p = (ModuleDefinitionSource) o;
					GlobalCommandState.getInstance().addValue(CommandStateConstants.MODULE_DEFINITION_SOURCE, p);
				}

				GlobalCommandState.getInstance().addValue(CommandStateConstants.TEST_CLASS, c);
				return false;
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
		return true;
	}

	public CommandDefinition getCommandDefinition() {
		return new CommandDefinition();
	}

}
