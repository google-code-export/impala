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

import org.impalaframework.command.framework.CommandState;
import org.impalaframework.command.framework.GlobalCommandState;
import org.impalaframework.resolver.ModuleLocationResolver;

public class RerunTestCommand extends BaseRunTestCommand {

    public RerunTestCommand() {
        super();
    }

    protected RerunTestCommand(ModuleLocationResolver moduleLocationResolver) {
        super(moduleLocationResolver);
    }

    protected String getMethodName(CommandState commandState, Class<?> testClass) {
        String methodName = (String) GlobalCommandState.getInstance().getValue(CommandStateConstants.TEST_METHOD_NAME);
        if (methodName == null) {
            System.out.println("No test method has been set");
        }
        return methodName;
    }

}
