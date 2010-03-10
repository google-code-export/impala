/*
 * Copyright 2007-2010 the original author or authors.
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

package org.impalaframework.interactive.command;

import org.impalaframework.command.framework.CommandDefinition;
import org.impalaframework.command.framework.CommandPropertyValue;
import org.impalaframework.command.framework.CommandState;
import org.impalaframework.command.framework.GlobalCommandState;
import org.impalaframework.command.framework.TextParsingCommand;
import org.impalaframework.facade.Impala;
import org.springframework.util.StopWatch;

public class ReloadCommand implements TextParsingCommand {
    
    static final String MODULE_NAME = ReloadCommand.class.getSimpleName() + ".moduleName";

    static final String ACTUAL_MODULE_RELOADED = ReloadCommand.class.getSimpleName() + ".actualModuleReloaded";
    
    public boolean execute(CommandState commandState) {
        if (GlobalCommandState.getInstance().getValue(CommandStateConstants.MODULE_DEFINITION_SOURCE) == null) {
            System.out.println("Cannot reload, as no module definition has been loaded.");
            return false;
        }
        return reload(commandState);
    }

    private boolean reload(CommandState commandState) {
        
        CommandPropertyValue commandPropertyValue = commandState.getProperties().get(MODULE_NAME);

        if (commandPropertyValue != null) {
            String moduleName = commandPropertyValue.getValue();
            reloadModule(moduleName, commandState);
            return true;
        } else {
            reloadRootModule();
            return true;
        }
    }

    void reloadRootModule() {
        StopWatch watch = new StopWatch();
        watch.start();
        Impala.reloadRootModule();
        watch.stop();
        String rootName = Impala.getRootModuleDefinition().getName();
        InteractiveCommandUtils.printReloadInfo(rootName, rootName, watch);
    }
    
    void reloadModule(String moduleToReload, CommandState commandState) {
        StopWatch watch = new StopWatch();
        watch.start();
        String actualModule = null;

        if (!Impala.reloadModule(moduleToReload)) {
            actualModule = Impala.reloadModuleLike(moduleToReload);
        } else {
            actualModule = moduleToReload;
        }
        watch.stop();

        if (moduleToReload != null)
            commandState.addProperty(ACTUAL_MODULE_RELOADED, new CommandPropertyValue(moduleToReload));
        InteractiveCommandUtils.printReloadInfo(moduleToReload, actualModule, watch);
    }

    public CommandDefinition getCommandDefinition() {
        return new CommandDefinition("Reloads root module and all child modules");
    }

    public void extractText(String[] text, CommandState commandState) {
        String name = text[0];
        if (name != null) {
            commandState.addProperty(MODULE_NAME,
                    new CommandPropertyValue(name.trim(), "Module name"));
        }
    }

}
