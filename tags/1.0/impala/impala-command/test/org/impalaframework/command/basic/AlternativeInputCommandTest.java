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

import org.impalaframework.command.framework.CommandDefinition;
import org.impalaframework.command.framework.CommandInfo;
import org.impalaframework.command.framework.CommandLineInputCapturer;

public class AlternativeInputCommandTest extends ManualAlternativeInputCommandTest {

    public void testCommandDefinition()
    {
        AlternativeInputCommand command = new AlternativeInputCommand(new String[] { "one", "two" });
        CommandDefinition commandDefinition = command.getCommandDefinition();
        assertEquals(1, commandDefinition.getCommandInfos().size());
        CommandInfo ci = commandDefinition.getCommandInfos().get(0);
        
        String numberErrorMessage = "Invalid Selection. Selected number does not correspond with one of the values.\nPlease select a number between 1 and 2";
        assertEquals(numberErrorMessage, ci.validate("-1"));
        assertEquals(numberErrorMessage, ci.validate("3"));
        
        String alphaErrorMessage = "Invalid Selection. Please select a number corresponding with one of the alternative choices";
        assertEquals(alphaErrorMessage , ci.validate("a value"));
        assertEquals(null, ci.validate("2"));
    }

    protected CommandLineInputCapturer getInputCapturer() {
        CommandLineInputCapturer inputCapturer = new CommandLineInputCapturer(){
            @Override
            public String capture(CommandInfo info) {
                return "1";
            }};
        return inputCapturer;
    }

}
