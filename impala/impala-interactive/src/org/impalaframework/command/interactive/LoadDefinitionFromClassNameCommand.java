package org.impalaframework.command.interactive;

import java.util.Arrays;

import org.impalaframework.command.basic.ClassFindCommand;
import org.impalaframework.command.basic.ModuleDefinitionAwareClassFilter;
import org.impalaframework.command.basic.SearchClassCommand;
import org.impalaframework.command.framework.CommandDefinition;
import org.impalaframework.command.framework.CommandInfo;
import org.impalaframework.command.framework.CommandState;
import org.impalaframework.command.framework.GlobalCommandState;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.impalaframework.util.PathUtils;
import org.impalaframework.util.ResourceUtils;
import org.springframework.core.io.Resource;

public class LoadDefinitionFromClassNameCommand extends BaseLoadDefinitionCommand {

	private ModuleLocationResolver moduleLocationResolver;

	private String changeClass(CommandState commandState) {
		final String currentDirectoryName = PathUtils.getCurrentDirectoryName();

		final Resource[] testClassLocations = moduleLocationResolver.getModuleTestClassLocations(currentDirectoryName);

		if (testClassLocations == null) {
			System.out.println("Unable to find any test class locations corresponding with " + currentDirectoryName);
			return null;
		}

		SearchClassCommand command = new SearchClassCommand() {

			@Override
			protected ClassFindCommand newClassFindCommand() {
				final ClassFindCommand classFindCommand = super.newClassFindCommand();
				classFindCommand.setDirectoryFilter(new ModuleDefinitionAwareClassFilter());
				return classFindCommand;
			}

		};
		command.setClassDirectories(Arrays.asList(ResourceUtils.getFiles(testClassLocations)));
		command.execute(commandState);
		String className = command.getClassName();
		return className;
	}	
	
	public boolean execute(CommandState commandState) {
		String testClass = (String) GlobalCommandState.getInstance().getValue(CommandStateConstants.TEST_CLASS_NAME);

		if (testClass == null) {
			testClass = changeClass(commandState);
		}
		
		if (testClass != null) {
			System.out.println("Test class set to " + testClass);
			doLoad(commandState);
			return true;
		}
		else {
			System.out.println("Test class not set");
			return false;
		}
	}

	public CommandDefinition getCommandDefinition() {

		// note that globalOverrides is true, so that the user will not be
		// prompted for the module name if it already there
		CommandInfo info = new CommandInfo(CommandStateConstants.TEST_CLASS_NAME, "Test class name", "Please enter the name of the module to reload",
				null, null, false, false, false, true);

		CommandDefinition definition = new CommandDefinition("Loads module definition using supplied test class");
		definition.add(info);
		return definition;
	}
	
}
