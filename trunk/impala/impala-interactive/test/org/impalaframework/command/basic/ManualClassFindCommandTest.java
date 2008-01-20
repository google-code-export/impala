package org.impalaframework.command.basic;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.impalaframework.command.framework.CommandLineInputCapturer;
import org.impalaframework.command.framework.CommandState;

//Enter the search patterns in the same order as the super class
//for this test to pass
public class ManualClassFindCommandTest extends TestCase {

	public void testFindClass() throws Exception {

		ClassFindCommand command = getCommand();

		// now need to capture
		CommandState commandState = new CommandState();

		CommandLineInputCapturer inputCapturer = getInputCapturer(null);
		commandState.setInputCapturer(inputCapturer);

		while (true) {

			System.out.println("--------------------");
			
			commandState.capture(command);
			command.execute(commandState);

			List<String> foundClasses = command.getFoundClasses();

			for (String className : foundClasses) {
				// check that we can instantiate classes
				System.out.println(className);
			}
			
			if (foundClasses.size() >= 2)
			{
				AlternativeInputCommand altInputCommand = new AlternativeInputCommand(foundClasses.toArray(new String[foundClasses.size()]));
				commandState.capture(altInputCommand);
				altInputCommand.execute(commandState);
				
				System.out.println("Selected class: " + altInputCommand.getSelectedAlternative());
			}
			else if (foundClasses.size() == 1)
			{
				System.out.println("Found class: " + foundClasses.get(0));
			}
			else
			{
				System.out.println("No class found");
			}
		}
	}

	protected ClassFindCommand getCommand() {
		File mainSrc = new File("../impala-interactive/bin");
		File mainTest = new File("../impala-web/bin");
		List<File> classDirectories = new ArrayList<File>();
		classDirectories.add(mainSrc);
		classDirectories.add(mainTest);

		ClassFindCommand command = new ClassFindCommand();
		command.setClassDirectories(classDirectories);
		return command;
	}

	protected CommandLineInputCapturer getInputCapturer(String classNameToSearch) {
		return new CommandLineInputCapturer();
	}

}
