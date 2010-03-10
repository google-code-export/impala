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

import org.impalaframework.command.framework.Command;
import org.impalaframework.command.framework.CommandDefinition;
import org.impalaframework.command.framework.CommandState;
import org.impalaframework.command.framework.GlobalCommandState;
import org.impalaframework.exception.NoServiceException;
import org.impalaframework.facade.Impala;
import org.impalaframework.module.ModuleDefinitionSource;
import org.impalaframework.module.RuntimeModule;
import org.springframework.util.ClassUtils;

public class LoadTestClassContextCommand implements Command {

    public boolean execute(CommandState commandState) {
        Object property = GlobalCommandState.getInstance().getValue(CommandStateConstants.TEST_CLASS_NAME);
        if (property == null) {
            System.out.println("No test class set.");
            return false;
        }

        String testClassName = property.toString();
        return loadTestClass(testClassName);
    }

    private boolean loadTestClass(String testClassName) {

        Class<?> c = null;
        try {
            String directoryName = (String) GlobalCommandState.getInstance().getValue(
                    CommandStateConstants.DIRECTORY_NAME);
            
            ClassLoader parent = null;

            RuntimeModule runtimeModule = null;
            
            try {               
                if (directoryName != null && !InteractiveCommandUtils.isRootProject(directoryName)) {
                    runtimeModule = Impala.getRuntimeModule(directoryName);
                }
                else {
                    runtimeModule = Impala.getRootRuntimeModule();
                }
            }
            catch (NoServiceException e) {
                //we're not terribly interested in this situation - simply means that the module context has not been loaded
            }
            
            if (runtimeModule != null) {
                parent = runtimeModule.getClassLoader();
            } else {
                parent = ClassUtils.getDefaultClassLoader();
            }

            c = Class.forName(testClassName, false, parent);
            try {
                Object o = c.newInstance();
                if (o instanceof ModuleDefinitionSource) {
                    ModuleDefinitionSource p = (ModuleDefinitionSource) o;
                    GlobalCommandState.getInstance().addValue(CommandStateConstants.MODULE_DEFINITION_SOURCE, p);
                }

                GlobalCommandState.getInstance().addValue(CommandStateConstants.TEST_CLASS, c);
                return false;
            }
            catch (Throwable e) {
                System.out.println("Unable to instantiate " + testClassName);
                InteractiveCommandUtils.printException(e);
            }
        }
        catch (ClassNotFoundException e) {
            System.out.println("Unable to find test class " + testClassName);
            InteractiveCommandUtils.printException(e);
        }
        return true;
    }

    public CommandDefinition getCommandDefinition() {
        return new CommandDefinition();
    }

}
