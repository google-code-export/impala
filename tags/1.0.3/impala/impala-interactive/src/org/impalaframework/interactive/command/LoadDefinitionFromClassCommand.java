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
import org.impalaframework.command.framework.GlobalCommandState;

public class LoadDefinitionFromClassCommand extends BaseLoadDefinitionCommand {

    public boolean execute(CommandState commandState) {
        Class<?> testClass = (Class<?>) GlobalCommandState.getInstance().getValue(CommandStateConstants.TEST_CLASS);

        if (testClass != null) {
            System.out.println("Test class set to " + testClass.getName());
            
            GlobalCommandState.getInstance().addValue(CommandStateConstants.TEST_CLASS_NAME, testClass.getName());
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
