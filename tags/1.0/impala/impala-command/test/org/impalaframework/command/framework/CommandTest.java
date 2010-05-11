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

package org.impalaframework.command.framework;

import junit.framework.TestCase;

import org.impalaframework.command.basic.PrintDetailsCommand;

public class CommandTest extends TestCase {

    public void testCommand() {

        ExtendedPrintDetailsCommand command = new ExtendedPrintDetailsCommand();

        // now need to capture
        CommandState commandState = new CommandState();
        commandState.setInputCapturer(new CommandLineInputCapturer() {

            public String capture(CommandInfo info) {
                if (info.getPropertyName().equals("dateOfBirth"))
                    return "8 may 1969";
                if (info.getPropertyName().equals("name"))
                    return "phil";
                return null;
            }
        });

        commandState.capture(command);
        command.execute(commandState);

        assertEquals("8 may 1969", command.getInput("dateOfBirth"));
        assertEquals("phil", command.getInput("name"));
    }

    public void testGlobalHolder() {

        GlobalCommandState holder = GlobalCommandState.getInstance();
        holder.addProperty("residence", new CommandPropertyValue("Woodbridge", "Place of residence"));

        ExtendedPrintDetailsCommand command = new ExtendedPrintDetailsCommand();

        // now need to capture
        CommandState commandState = new CommandState();
        commandState.setInputCapturer(new CommandLineInputCapturer() {

            public String capture(CommandInfo info) {
                if (info.getPropertyName().equals("dateOfBirth"))
                    return null;
                if (info.getPropertyName().equals("name"))
                    return "phil";
                return null;
            }
        });

        commandState.capture(command);
        command.execute(commandState);

        // this is optional
        assertEquals(null, command.getInput("dateOfBirth"));

        // this is shared
        assertEquals("phil", command.getInput("name"));

        // this comes from global default
        assertEquals("Woodbridge", command.getInput("residence"));

        // verified that this has been shared
        assertEquals("phil", holder.getProperty("name").getValue());

        // verified that this has come from default
        assertEquals(null, holder.getProperty("IvyD"));
    }

    class ExtendedPrintDetailsCommand extends PrintDetailsCommand {

        private CommandState state;

        public ExtendedPrintDetailsCommand() {
            super();
        }

        @Override
        public boolean execute(CommandState commandState) {
            super.execute(commandState);
            this.state = commandState;
            return true;
        }

        public String getInput(String name) {
            assertNotNull(state);
            CommandPropertyValue commandPropertyValue = state.getProperties().get(name);
            if (commandPropertyValue == null)
                return null;
            return commandPropertyValue.getValue();
        }

    }

}
