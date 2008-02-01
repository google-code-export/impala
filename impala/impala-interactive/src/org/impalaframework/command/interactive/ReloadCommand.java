package org.impalaframework.command.interactive;

import org.impalaframework.command.framework.Command;
import org.impalaframework.command.framework.CommandDefinition;
import org.impalaframework.command.framework.CommandState;
import org.impalaframework.command.framework.GlobalCommandState;
import org.impalaframework.facade.DynamicContextHolder;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.springframework.util.StopWatch;

public class ReloadCommand implements Command {

	public boolean execute(CommandState commandState) {
		if (GlobalCommandState.getInstance().getValue(CommandStateConstants.MODULE_DEFINITION_SOURCE) == null) {
			System.out.println("Cannot reload, as no module definition has been loaded.");
			return false;
		}
		reload();
		return true;
	}

	private void reload() {
		StopWatch watch = new StopWatch();
		watch.start();
		DynamicContextHolder.reloadRootModule();
		watch.stop();
		InteractiveCommandUtils.printReloadInfo(RootModuleDefinition.NAME, RootModuleDefinition.NAME, watch);
	}

	public CommandDefinition getCommandDefinition() {
		return new CommandDefinition("Reloads root module and all child modules");
	}

}
