/*
 * Copyright 2007-2008 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.impalaframework.command.interactive;

import org.impalaframework.command.framework.CommandDefinition;
import org.impalaframework.command.framework.CommandInfo;
import org.impalaframework.command.framework.CommandPropertyValue;
import org.impalaframework.command.framework.CommandState;
import org.impalaframework.command.framework.TextParsingCommand;
import org.impalaframework.facade.Impala;
import org.springframework.util.Assert;
import org.springframework.util.StopWatch;

public class ReloadModuleCommand implements TextParsingCommand {

	static final String MODULE_NAME = ReloadModuleCommand.class.getSimpleName() + ".moduleName";

	static final String ACTUAL_MODULE_RELOADED = ReloadModuleCommand.class.getSimpleName() + ".actualModuleReloaded";

	public boolean execute(CommandState commandState) {
		CommandPropertyValue commandPropertyValue = commandState.getProperties().get(MODULE_NAME);

		// just an extra check - the commandDefinition and capture process
		// should ensure that this is trueF
		Assert.notNull(commandPropertyValue);

		String moduleName = commandPropertyValue.getValue();
		reloadModule(moduleName, commandState);
		return true;
	}

	public void extractText(String[] text, CommandState commandState) {
		String name = text[0];
		if (name != null) {
			commandState.getGlobalStateHolder().addProperty(MODULE_NAME,
					new CommandPropertyValue(name.trim(), "Module name"));
		}
	}

	public CommandDefinition getCommandDefinition() {

		// note that globalOverrides is true, so that the user will not be
		// prompted for the module name if it already there
		CommandInfo info = new CommandInfo(MODULE_NAME, "Module name", "Please enter the name of the module to reload",
				null, null, false, false, false, true);

		CommandDefinition definition = new CommandDefinition("Reloads named module");
		definition.add(info);
		return definition;
	}

	void reloadModule(String moduleToReload, CommandState commandState) {
		StopWatch watch = new StopWatch();
		watch.start();
		String actualModule = null;

		if (!Impala.reload(moduleToReload)) {
			actualModule = Impala.reloadLike(moduleToReload);
		} else {
			actualModule = moduleToReload;
		}
		watch.stop();

		if (moduleToReload != null)
			commandState.addProperty(ACTUAL_MODULE_RELOADED, new CommandPropertyValue(moduleToReload));
		InteractiveCommandUtils.printReloadInfo(moduleToReload, actualModule, watch);
	}
}
