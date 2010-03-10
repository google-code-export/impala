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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandLineInputCapturer implements InputCapturer {

    public String capture(CommandInfo info) {

        String commandString = null;
        System.out.println();

        System.out.println(info.getRequestString());
        
        String[] extraLines = info.getExtraLines();
        if (extraLines != null && extraLines.length > 0)
        {
            System.out.println();
            for (int i = 0; i < extraLines.length; i++) {
                System.out.println(extraLines[i]);
            }
        }

        System.out.print(">");
        
        try {
            commandString = printInput();
        }
        catch (Exception e) {
        }
        return commandString;
    }
    
    public String recapture(CommandInfo info) {

        String commandString = null;
        System.out.println();
        System.out.println(info.getRequestString());
        System.out.print(">");
        
        try {
            commandString = printInput();
        }
        catch (Exception e) {
        }
        return commandString;
    }
    

    public static String printInput() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String input = in.readLine();
        return input;
    }

    public void displayValidationMessage(String validationMessage) {
        System.out.println(validationMessage);
    }

}
