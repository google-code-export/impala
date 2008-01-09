package org.impalaframework.command.interactive;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.impalaframework.command.Command;
import org.impalaframework.command.CommandDefinition;
import org.impalaframework.command.CommandState;
import org.impalaframework.command.GlobalCommandState;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.testrun.DynamicContextHolder;

public class StartContextCommand implements Command {

	public boolean execute(CommandState commandState) {
		Object property = GlobalCommandState.getInstance().getValue("testClass");
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
					GlobalCommandState.getInstance().addValue("moduleDefinitionSource", p);
					DynamicContextHolder.init(p);
					System.out.println("Started " + p.getModuleDefinition().getName());
				}
			}
			catch (Throwable e) {
				System.out.println("Unable to instantiate " + testClassName);
				print(e);
			}
		}
		catch (ClassNotFoundException e) {
			System.out.println("Unable to find test class " + testClassName);
			print(e);
		}
	}
	
	private void print(Throwable e) {
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		e.printStackTrace(printWriter);
		System.out.println(stringWriter.toString());
	}

	public CommandDefinition getCommandDefinition() {
		return new CommandDefinition();
	}

}
