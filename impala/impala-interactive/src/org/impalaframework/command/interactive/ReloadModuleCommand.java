package org.impalaframework.command.interactive;

import org.impalaframework.command.CommandDefinition;
import org.impalaframework.command.CommandInfo;
import org.impalaframework.command.CommandPropertyValue;
import org.impalaframework.command.CommandState;
import org.impalaframework.command.TextParsingCommand;
import org.impalaframework.testrun.DynamicContextHolder;
import org.impalaframework.util.MemoryUtils;
import org.springframework.util.StopWatch;

public class ReloadModuleCommand implements TextParsingCommand {

	public boolean execute(CommandState commandState) {
		// FIXME test
		
		CommandPropertyValue commandPropertyValue = commandState.getProperties().get("moduleName");
		String moduleName = commandPropertyValue.getValue();
		reloadPlugin(moduleName);
		return true;
	}

	public void extractText(String[] text, CommandState commandState) {
		String name = text[0];
		if (name != null) {
			commandState.getGlobalStateHolder().addProperty("moduleName",
					new CommandPropertyValue(name, true, "Module name"));
		}
	}

	public CommandDefinition getCommandDefinition() {

		CommandInfo info = new CommandInfo("n", "moduleName", "Module name", "Please the name for the module name",
				null, null, false, false, false, false);

		CommandDefinition definition = new CommandDefinition("Reloads named module");
		definition.add(info);
		return definition;
	}

	private void reloadPlugin(String pluginToReload) {
		StopWatch watch = startWatch();

		if (DynamicContextHolder.reload(pluginToReload)) {
			watch.stop();
			printReloadInfo(pluginToReload, watch);
		}
		else {
			String actual = DynamicContextHolder.reloadLike(pluginToReload);
			watch.stop();
			printReloadInfo(actual, watch);
		}
	}

	private void printReloadInfo(String pluginToReload, StopWatch watch) {
		if (pluginToReload != null) {
			System.out.println("Plugin " + pluginToReload + " loaded in " + watch.getTotalTimeSeconds() + " seconds");
			System.out.println(MemoryUtils.getMemoryInfo());
		}
		else {
			System.out.println("No matching plugin found to reload");
		}
	}

	private StopWatch startWatch() {
		StopWatch watch = new StopWatch();
		watch.start();
		return watch;
	}
}
