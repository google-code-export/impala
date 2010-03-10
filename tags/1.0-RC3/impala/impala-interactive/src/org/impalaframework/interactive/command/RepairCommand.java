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
import org.impalaframework.command.framework.CommandState;
import org.impalaframework.command.framework.TextParsingCommand;
import org.impalaframework.facade.Impala;
import org.impalaframework.module.RootModuleDefinition;
import org.springframework.util.StopWatch;

public class RepairCommand implements TextParsingCommand {
    
    public boolean execute(CommandState commandState) {
        
        final RootModuleDefinition rootModuleDefinition = Impala.getRootModuleDefinition();
        if (rootModuleDefinition == null) {
            System.out.println("Cannot reload, as no module definition has been loaded.");
            return false;
        }
        return reload(commandState);
    }

    private boolean reload(CommandState commandState) {
        
        StopWatch watch = new StopWatch();
        watch.start();
        Impala.repairModules();
        watch.stop();
        InteractiveCommandUtils.printExecutionInfo(watch);
        final RootModuleDefinition rootModuleDefinition = Impala.getRootModuleDefinition();
        
        System.out.println("Current module state:");
        System.out.println(rootModuleDefinition.toString());
        
        return true;
    }
    
    public CommandDefinition getCommandDefinition() {
        return new CommandDefinition("Attempts to load modules which previously failed to load");
    }

    public void extractText(String[] text, CommandState commandState) {
    }

}
