package org.impalaframework.command.interactive;

import java.util.Arrays;

import org.impalaframework.command.basic.ClassFindCommand;
import org.impalaframework.command.basic.ModuleDefinitionAwareClassFilter;
import org.impalaframework.command.basic.SearchClassCommand;
import org.impalaframework.command.framework.CommandDefinition;
import org.impalaframework.command.framework.CommandState;
import org.impalaframework.command.framework.GlobalCommandState;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.impalaframework.resolver.StandaloneModuleLocationResolverFactory;
import org.impalaframework.util.PathUtils;
import org.impalaframework.util.ResourceUtils;
import org.springframework.core.io.Resource;

public class LoadDefinitionFromClassNameCommand extends BaseLoadDefinitionCommand {

	private ModuleLocationResolver moduleLocationResolver;

	public LoadDefinitionFromClassNameCommand(ModuleLocationResolver moduleLocationResolver) {
		super();
		this.moduleLocationResolver = moduleLocationResolver;
	}

	private String changeClass(CommandState commandState) {

		String currentDirectoryName = (String) GlobalCommandState.getInstance().getValue(
				CommandStateConstants.DIRECTORY_NAME);
		if (currentDirectoryName == null) {
			currentDirectoryName = PathUtils.getCurrentDirectoryName();
			GlobalCommandState.getInstance().addValue(CommandStateConstants.DIRECTORY_NAME, currentDirectoryName);
		}

		if (moduleLocationResolver == null) {
			ModuleLocationResolver moduleLocationResolver = new StandaloneModuleLocationResolverFactory()
					.getClassLocationResolver();
			this.moduleLocationResolver = moduleLocationResolver;
		}

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
		String testClass = changeClass(commandState);
		GlobalCommandState.getInstance().addValue(CommandStateConstants.TEST_CLASS_NAME, testClass);

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
		return new CommandDefinition("Loads module definition using supplied test class");
	}

}
