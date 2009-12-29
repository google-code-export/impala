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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.impalaframework.command.framework.CommandLineInputCapturer;
import org.impalaframework.command.framework.CommandState;

//Enter the search patterns in the same order as the super class
//for this test to pass
public class ManualClassFindCommandTest extends TestCase {

    public void testFindClass() throws Exception {

        ClassFindCommand command = getCommand();

        // now need to capture
        CommandState commandState = new CommandState();

        CommandLineInputCapturer inputCapturer = getInputCapturer(null);
        commandState.setInputCapturer(inputCapturer);

        while (true) {

            System.out.println("--------------------");
            
            commandState.capture(command);
            command.execute(commandState);

            List<String> foundClasses = command.getFoundClasses();

            for (String className : foundClasses) {
                // check that we can instantiate classes
                System.out.println(className);
            }
            
            if (foundClasses.size() >= 2)
            {
                AlternativeInputCommand altInputCommand = new AlternativeInputCommand(foundClasses.toArray(new String[foundClasses.size()]));
                commandState.capture(altInputCommand);
                altInputCommand.execute(commandState);
                
                System.out.println("Selected class: " + altInputCommand.getSelectedAlternative());
            }
            else if (foundClasses.size() == 1)
            {
                System.out.println("Found class: " + foundClasses.get(0));
            }
            else
            {
                System.out.println("No class found");
            }
        }
    }

    protected ClassFindCommand getCommand() {
        File mainSrc = new File("../impala-command/testbin");
        File mainTest = new File("../impala-web/bin");
        List<File> classDirectories = new ArrayList<File>();
        classDirectories.add(mainSrc);
        classDirectories.add(mainTest);

        ClassFindCommand command = new ClassFindCommand();
        command.setClassDirectories(classDirectories);
        return command;
    }

    protected CommandLineInputCapturer getInputCapturer(String classNameToSearch) {
        return new CommandLineInputCapturer();
    }

}
