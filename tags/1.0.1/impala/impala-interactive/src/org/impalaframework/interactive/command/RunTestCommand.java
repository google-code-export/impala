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

import org.impalaframework.command.basic.SelectMethodCommand;
import org.impalaframework.command.framework.CommandState;
import org.impalaframework.resolver.ModuleLocationResolver;

public class RunTestCommand extends BaseRunTestCommand {

    public RunTestCommand() {
        super();
    }

    protected RunTestCommand(ModuleLocationResolver moduleLocationResolver) {
        super(moduleLocationResolver);
    }

    protected String getMethodName(CommandState commandState, Class<?> testClass) {
        SelectMethodCommand command = new SelectMethodCommand(testClass);
        command.execute(commandState);
        String methodName = command.getMethodName();
        
        if (methodName == null) {
            System.out.println("No matching test method found.");
        }
        
        return methodName;
    }

}
