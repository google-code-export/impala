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

import java.util.Arrays;
import java.util.List;

import org.impalaframework.command.basic.ClassFindCommand;
import org.impalaframework.command.basic.ModuleDefinitionAwareClassFilter;
import org.impalaframework.command.basic.SearchClassCommand;
import org.impalaframework.command.framework.CommandDefinition;
import org.impalaframework.command.framework.CommandState;
import org.impalaframework.command.framework.GlobalCommandState;
import org.impalaframework.facade.Impala;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.impalaframework.util.PathUtils;
import org.impalaframework.util.ResourceUtils;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

public class LoadDefinitionFromClassNameCommand extends BaseLoadDefinitionCommand {

    private ModuleLocationResolver moduleLocationResolver;

    public LoadDefinitionFromClassNameCommand() {
        super();
        this.moduleLocationResolver = Impala.getFacade().getModuleManagementFacade().getModuleLocationResolver();
    }

    public LoadDefinitionFromClassNameCommand(ModuleLocationResolver moduleLocationResolver) {
        super();
        Assert.notNull(moduleLocationResolver, "moduleLocationResolver cannot be null");
        this.moduleLocationResolver = moduleLocationResolver;
    }

    private String changeClass(CommandState commandState) {

        String currentDirectoryName = (String) GlobalCommandState.getInstance().getValue(
                CommandStateConstants.DIRECTORY_NAME);
        if (currentDirectoryName == null) {
            currentDirectoryName = PathUtils.getCurrentDirectoryName();
            GlobalCommandState.getInstance().addValue(CommandStateConstants.DIRECTORY_NAME, currentDirectoryName);
        }

        final List<Resource> testClassLocations = moduleLocationResolver.getModuleTestClassLocations(currentDirectoryName);

        if (testClassLocations == null) {
            System.out.println("Unable to find any test class locations corresponding with " + currentDirectoryName);
            return null;
        }

        SearchClassCommand command = new SearchClassCommand() {

            @Override
            protected ClassFindCommand newClassFindCommand() {
                final ClassFindCommand classFindCommand = super.newClassFindCommand();
                classFindCommand.setDirectoryFilter(new ModuleDefinitionAwareClassFilter());
                return classFindCommand;
            }

        };
        command.setClassDirectories(Arrays.asList(ResourceUtils.getFiles(testClassLocations)));
        command.execute(commandState);
        String className = command.getClassName();
        return className;
    }

    public boolean execute(CommandState commandState) {
        String testClass = changeClass(commandState);
        GlobalCommandState.getInstance().addValue(CommandStateConstants.TEST_CLASS_NAME, testClass);

        if (testClass != null) {
            System.out.println("Test class set to " + testClass);
            doLoad(commandState);
            return true;
        }
        else {
            System.out.println("Test class not set");
            return false;
        }
    }

    public CommandDefinition getCommandDefinition() {
        return new CommandDefinition("Loads module definition using supplied test class");
    }

}
