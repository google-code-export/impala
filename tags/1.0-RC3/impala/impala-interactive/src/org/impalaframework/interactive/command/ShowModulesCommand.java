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

import org.impalaframework.command.framework.Command;
import org.impalaframework.command.framework.CommandDefinition;
import org.impalaframework.command.framework.CommandState;
import org.impalaframework.facade.Impala;
import org.impalaframework.module.RootModuleDefinition;

/**
 * {@link Command} which dumps metadata on modules to System.out.
 * @author Phil Zoio
 */
public class ShowModulesCommand implements Command {

    public boolean execute(CommandState commandState) {
        final RootModuleDefinition rootModuleDefinition = Impala.getRootModuleDefinition();
        
        printModuleInfo(rootModuleDefinition);
        return false;
    }

    void printModuleInfo(final RootModuleDefinition rootModuleDefinition) {
        if (rootModuleDefinition != null) {
            System.out.println(rootModuleDefinition.toString());
        } else {
            System.out.print("No modules loaded");
        }
    }

    public CommandDefinition getCommandDefinition() {
        return new CommandDefinition("Displays metadata on loaded modules");
    }

}
