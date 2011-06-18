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

package org.impalaframework.command.basic;

import java.util.Map;

import org.impalaframework.command.framework.Command;
import org.impalaframework.command.framework.CommandDefinition;
import org.impalaframework.command.framework.CommandInfo;
import org.impalaframework.command.framework.CommandPropertyValue;
import org.impalaframework.command.framework.CommandState;


public class PrintDetailsCommand implements Command {

    private CommandDefinition commandDefinition;

    public PrintDetailsCommand() {
        super();
        CommandDefinition commandDefinition = newCommandDefinition();
        this.commandDefinition = commandDefinition;
    }

    protected CommandDefinition newCommandDefinition() {

        // name is shared
        CommandInfo ci1 = new CommandInfo("name", "Name", "Please give your name", null, null, true, false, false, false);

        // date of birth is optional here
        CommandInfo ci2 = new CommandInfo("dateOfBirth", "Date of birth", "Please give your date of birth", null,
                null, false, true, false, false);

        // residence is supplied via global
        CommandInfo ci3 = new CommandInfo("residence", "Residence", "Where do you live", null, null, false, true, false,
                false);

        // house name is supplied via default
        CommandInfo ci4 = new CommandInfo("housename", "House name", "Name of your house?", "IvyD", null, false, true,
                false, false);

        CommandDefinition commandDefinition = new CommandDefinition();
        commandDefinition.add(ci1);
        commandDefinition.add(ci2);
        commandDefinition.add(ci3);
        commandDefinition.add(ci4);
        return commandDefinition;
    }

    public CommandDefinition getCommandDefinition() {
        return commandDefinition;
    }

    public boolean execute(CommandState commandState) {
        Map<String, CommandPropertyValue> properties = commandState.getProperties();
        System.out.println();
        CommandPropertyValue dob = properties.get("dateOfBirth");

        System.out.println("Name " + properties.get("name").getValue() + ", dob " + dob != null ? "unknown" : dob
                .getValue());
        return true;
    }

}
