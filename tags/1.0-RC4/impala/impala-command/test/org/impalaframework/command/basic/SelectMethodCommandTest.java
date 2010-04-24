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

import org.impalaframework.command.framework.CommandInfo;
import org.impalaframework.command.framework.CommandLineInputCapturer;

public class SelectMethodCommandTest extends ManualSelectMethodCommandTest {
    
    public void testSelectMethodCommand() throws Exception {
        SelectMethodCommand command = getCommand();
        assertTrue(doTest(command));
        assertEquals("testFiltering", command.getMethodName());
    }
    
    public void testCommandWithNoTestMethod() throws Exception {
        SelectMethodCommand command =  new SelectMethodCommand(SpecAwareClass.class);
        assertFalse(doTest(command));
        assertEquals(null, command.getMethodName());
    }
    
    protected CommandLineInputCapturer getInputCapturer() {
        CommandLineInputCapturer inputCapturer = new CommandLineInputCapturer()
        {
            @Override
            public String capture(CommandInfo info) {
                if (info.getPropertyName().equals("selection")){;
                    return "1";
                }
                return null;
            }
            
        };
        return inputCapturer;
    }
}
