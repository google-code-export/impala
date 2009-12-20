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
package org.impalaframework.command.basic;

import java.util.List;

import org.impalaframework.command.framework.CommandDefinition;
import org.impalaframework.command.framework.CommandInfo;
import org.impalaframework.command.framework.CommandLineInputCapturer;
import org.impalaframework.command.framework.CommandState;
import org.impalaframework.command.framework.GlobalCommandState;


public class ClassFindCommandTest extends ManualClassFindCommandTest {

    public void testCommandDefinition() {
        ClassFindCommand command = new ClassFindCommand();
        CommandDefinition commandDefinition = command.getCommandDefinition();
        assertEquals(1, commandDefinition.getCommandInfos().size());
        CommandInfo ci = commandDefinition.getCommandInfos().get(0);

        String nullOrEmptyText = "Please enter type (class or interface) to find";
        try {
            assertEquals(nullOrEmptyText, ci.validate(null));
            fail();
        }
        catch (IllegalArgumentException e) {
        }
        assertEquals("Search text should be at least 3 characters long", ci.validate("a"));

    }

    public void testFindClass() throws Exception {

        ClassFindCommand command = getCommand();

        doTest(command, "ClassFindFileRecurseHandler", 1);

        // show that it can handle packages correctly (if last part is correctly
        // specified)
        doTest(command, "ClassFindCommand", 2);
        doTest(command, "basic.ClassFindCommand", 2);

        // will not find inner class
        doTest(command, "PrintDetails", 1);
    }

    private void doTest(ClassFindCommand command, final String classNameToSearch, int expected)
            throws ClassNotFoundException {

        GlobalCommandState.getInstance().reset();
        
        System.out.println("----");
        // now need to capture
        CommandState commandState = new CommandState();

        CommandLineInputCapturer inputCapturer = getInputCapturer(classNameToSearch);
        commandState.setInputCapturer(inputCapturer);

        commandState.capture(command);
        command.execute(commandState);

        List<String> foundClasses = command.getFoundClasses();

        for (String className : foundClasses) {
            // check that we can instantiate classes
            System.out.println(className);
            Class.forName(className);
        }
        assertEquals(expected, foundClasses.size());
    }

    @Override
    protected CommandLineInputCapturer getInputCapturer(final String classNameToSearch) {
        CommandLineInputCapturer inputCapturer = new CommandLineInputCapturer() {

            public String capture(CommandInfo info) {
                if (info.getPropertyName().equals("class")) {
                    return classNameToSearch;
                }
                return null;
            }
        };
        return inputCapturer;
    }
}
