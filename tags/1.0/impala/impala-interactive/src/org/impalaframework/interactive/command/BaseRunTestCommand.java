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

import java.io.File;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.impalaframework.command.framework.Command;
import org.impalaframework.command.framework.CommandDefinition;
import org.impalaframework.command.framework.CommandState;
import org.impalaframework.command.framework.GlobalCommandState;
import org.impalaframework.facade.Impala;
import org.impalaframework.interactive.classloader.TestClassLoader;
import org.impalaframework.module.RuntimeModule;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.impalaframework.util.PathUtils;
import org.impalaframework.util.ResourceUtils;
import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;

public abstract class BaseRunTestCommand implements Command {

    private ModuleLocationResolver moduleLocationResolver;

    public BaseRunTestCommand() {
        super();
        this.moduleLocationResolver = Impala.getFacade().getModuleManagementFacade().getModuleLocationResolver();
    }
    
    protected BaseRunTestCommand(ModuleLocationResolver moduleLocationResolver) {
        super();
        this.moduleLocationResolver = moduleLocationResolver;
    }

    public boolean execute(CommandState commandState) {

        Class<?> testClass = (Class<?>) GlobalCommandState.getInstance().getValue(CommandStateConstants.TEST_CLASS);
        if (testClass == null) {
            System.out.println("No test class set.");
            return false;
        }

        String testClassName = testClass.getName();
        ClassLoader testClassLoader = getTestClassLoader(testClassName);
        ClassLoader existingClassLoader = ClassUtils.getDefaultClassLoader();

        try {

            Thread.currentThread().setContextClassLoader(testClassLoader);

            Class<?> loadedTestClass = testClassLoader.loadClass(testClassName);
            GlobalCommandState.getInstance().addValue(CommandStateConstants.TEST_CLASS, loadedTestClass);

            String methodName = getMethodName(commandState, loadedTestClass);

            if (methodName != null) {
                GlobalCommandState.getInstance().addValue(CommandStateConstants.TEST_METHOD_NAME, methodName);

                TestRunner runner = new TestRunner();

                System.out.println("Running test " + methodName);
                Test test = TestSuite.createTest(loadedTestClass, methodName);
                runner.doRun(test);
                return true;
            }
            else {
                return false;
            }
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        finally {
            Thread.currentThread().setContextClassLoader(existingClassLoader);
        }

    }

    protected abstract String getMethodName(CommandState commandState, Class<?> testClass);

    private ClassLoader getTestClassLoader(String testClassName) {
        String currentDirectoryName = getCurrentDirectoryName(true);

        RuntimeModule runtimeModule = null;

        try {
            if (currentDirectoryName != null && !InteractiveCommandUtils.isRootProject(currentDirectoryName)) {
                runtimeModule = Impala.getRuntimeModule(currentDirectoryName);
            }
            else {
                runtimeModule = Impala.getRootRuntimeModule();
            }
        }
        catch (RuntimeException e) {
            System.out.println("No module loaded for current directory: " + currentDirectoryName);
            runtimeModule = Impala.getRootRuntimeModule();
        }

        ClassLoader parentClassLoader = null;

        if (runtimeModule != null)
            parentClassLoader = runtimeModule.getClassLoader();
        else
            parentClassLoader = ClassUtils.getDefaultClassLoader();
        
        ClassLoader testClassLoader = getTestClassLoader(parentClassLoader, testClassName);
        return testClassLoader;
    }

    private ClassLoader getTestClassLoader(ClassLoader parentClassLoader, String name) {

        String currentDirectoryName = getCurrentDirectoryName(true);

        List<Resource> locationResources = moduleLocationResolver.getModuleTestClassLocations(currentDirectoryName);
        File[] locations = ResourceUtils.getFiles(locationResources);

        return new TestClassLoader(parentClassLoader, locations, name);
    }

    private String getCurrentDirectoryName(boolean useDefault) {
        String currentDirectoryName = (String) GlobalCommandState.getInstance().getValue(
                CommandStateConstants.DIRECTORY_NAME);

        if (useDefault && currentDirectoryName == null) {
            currentDirectoryName = PathUtils.getCurrentDirectoryName();
        }

        return currentDirectoryName;
    }

    public CommandDefinition getCommandDefinition() {
        return new CommandDefinition("Runs test");
    }

}
