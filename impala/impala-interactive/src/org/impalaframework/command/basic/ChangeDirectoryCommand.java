package org.impalaframework.command.basic;

import java.util.List;

import org.impalaframework.command.framework.CommandDefinition;
import org.impalaframework.command.framework.CommandInfo;
import org.impalaframework.command.framework.CommandPropertyValue;
import org.impalaframework.command.framework.CommandState;
import org.impalaframework.command.framework.GlobalCommandState;
import org.impalaframework.command.framework.TextParsingCommand;
import org.impalaframework.command.interactive.CommandStateConstants;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.impalaframework.resolver.StandaloneModuleLocationResolverFactory;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

public class ChangeDirectoryCommand implements TextParsingCommand {

	static final String DIRECTORY_NAME = ChangeDirectoryCommand.class.getName() + ".directoryName";

	private ModuleLocationResolver moduleLocationResolver;

	public ChangeDirectoryCommand() {
		super();
		ModuleLocationResolver moduleLocationResolver = new StandaloneModuleLocationResolverFactory()
				.getClassLocationResolver();
		this.moduleLocationResolver = moduleLocationResolver;
	}

	public boolean execute(CommandState commandState) {
		CommandPropertyValue suppliedValue = commandState.getProperties().get(DIRECTORY_NAME);
		
		//we can call this because the getCommandDefinition contract demands this
		Assert.notNull(suppliedValue);

		String candidateValue = suppliedValue.getValue();

		List<Resource> locations = moduleLocationResolver.getApplicationModuleClassLocations(candidateValue);

		boolean exists = false;

		for (Resource resource : locations) {
			if (!exists && resource.exists()) {
				exists = true;
			}
		}

		if (exists == false) {
			System.out.println("No module locations corresponding to " + candidateValue + " exist");
			return false;
		}

		GlobalCommandState.getInstance().addValue(CommandStateConstants.DIRECTORY_NAME, candidateValue);
		GlobalCommandState.getInstance().clearProperty(CommandStateConstants.TEST_CLASS);
		GlobalCommandState.getInstance().clearProperty(CommandStateConstants.TEST_CLASS_NAME);
		GlobalCommandState.getInstance().clearProperty(CommandStateConstants.TEST_METHOD_NAME);
		GlobalCommandState.getInstance().clearProperty(CommandStateConstants.MODULE_DEFINITION_SOURCE);
		
		System.out.println("Current directory set to " + candidateValue);
		return true;
	}

	public CommandDefinition getCommandDefinition() {

		// note that globalOverrides is true, so that the user will not be
		// prompted for the module name if it already there
		CommandInfo info = new CommandInfo(DIRECTORY_NAME, "Directory name",
				"Please enter directory to use as current working directory.", null, null, false, false, true, false);

		CommandDefinition definition = new CommandDefinition("Changes working directory");
		definition.add(info);
		return definition;
	}

	public void extractText(String[] text, CommandState commandState) {

		if (text.length > 0) {
			commandState.addProperty(DIRECTORY_NAME, new CommandPropertyValue(text[0]));
		}

	}

}
