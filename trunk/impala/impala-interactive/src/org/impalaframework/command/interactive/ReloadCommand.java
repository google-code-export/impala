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
