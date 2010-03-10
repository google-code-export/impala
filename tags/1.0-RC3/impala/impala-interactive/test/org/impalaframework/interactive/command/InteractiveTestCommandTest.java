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

import org.impalaframework.command.framework.CommandInfo;
import org.impalaframework.command.framework.CommandLineInputCapturer;
import org.impalaframework.command.framework.CommandPropertyValue;
import org.impalaframework.command.framework.GlobalCommandState;

public class InteractiveTestCommandTest extends ManualInteractiveTestCommandTest {

    @Override
    public void testInteractive() throws Exception {
        super.testInteractive();
        CommandPropertyValue property = GlobalCommandState.getInstance().getProperty("lastCommand");
        assertEquals("run", property.getValue());
    }

    @Override
    protected CommandLineInputCapturer getInputCapturer() {
        CommandLineInputCapturer inputCapturer = new CommandLineInputCapturer() {
            @Override
            public String capture(CommandInfo info) {
                if (info.getPropertyName().equals("commandText")) {
                    return "run";
                }
                return null;
            }

        };
        return inputCapturer;
    }

}
