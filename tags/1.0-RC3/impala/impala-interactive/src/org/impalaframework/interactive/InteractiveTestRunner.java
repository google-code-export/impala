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

package org.impalaframework.interactive;

import java.util.LinkedHashMap;
import java.util.Map;

import org.impalaframework.command.framework.Command;
import org.impalaframework.command.framework.CommandLineInputCapturer;
import org.impalaframework.command.framework.CommandState;
import org.impalaframework.command.framework.GlobalCommandState;
import org.impalaframework.command.framework.TerminatedApplicationException;
import org.impalaframework.facade.FacadeConstants;
import org.impalaframework.interactive.command.ChangeDirectoryCommand;
import org.impalaframework.interactive.command.CommandStateConstants;
import org.impalaframework.interactive.command.ExitCommand;
import org.impalaframework.interactive.command.InitRunnerCommand;
import org.impalaframework.interactive.command.InteractiveTestCommand;
import org.impalaframework.interactive.command.LoadDefinitionFromClassNameCommand;
import org.impalaframework.interactive.command.ReloadCommand;
import org.impalaframework.interactive.command.RepairCommand;
import org.impalaframework.interactive.command.RerunTestCommand;
import org.impalaframework.interactive.command.RunTestCommand;
import org.impalaframework.interactive.command.ShowModulesCommand;
import org.impalaframework.interactive.command.UsageCommand;
import org.impalaframework.interactive.command.listener.StopCheckerListener;
import org.impalaframework.interactive.facade.InteractiveOperationsFacade;
import org.impalaframework.module.ModuleDefinitionSource;

public class InteractiveTestRunner {

    public static void main(String[] args) {
        run();
    }

    public static void run() {
        new InteractiveTestRunner().start(null);
    }

    public static void run(Class<? extends ModuleDefinitionSource> sourceClass) {
        // autoreload not enabled by default
        new InteractiveTestRunner().start(sourceClass);
    }

    public InteractiveTestRunner() {
        super();
    }

    /**
     * Runs a suite extracted from a TestCase subclass.
     */
    public void start(Class<?> testClass) {

        //need to make sure that parent class loader does not get picked up first, otherwise won't be able to reload classes
        //which are visible to the application class path
        System.setProperty("parent.classloader.first", "false");
        
        String facadeClassName = System.getProperty(FacadeConstants.FACADE_CLASS_NAME);
        if (facadeClassName == null) {
            //set the InteractiveOperationsFacade to apply by default
            System.setProperty(FacadeConstants.FACADE_CLASS_NAME, InteractiveOperationsFacade.class.getName());
        }

        CommandState commandState = new CommandState();
        CommandLineInputCapturer inputCapturer = new CommandLineInputCapturer();
        commandState.setInputCapturer(inputCapturer);

        GlobalCommandState.getInstance().addValue(CommandStateConstants.TEST_CLASS, testClass);

        Command initCommand = getInitCommand();
        initCommand.execute(commandState);

        InteractiveTestCommand testCommand = new InteractiveTestCommand();
        StopCheckerListener stopCheckerListener = new StopCheckerListener();

        Integer maxInactiveSeconds = getMaxInactiveSeconds();

        if (maxInactiveSeconds != null) {
            stopCheckerListener.setMaxInactiveSeconds(maxInactiveSeconds);
            stopCheckerListener.start();
            testCommand.addTestListener(stopCheckerListener);
        }

        Map<String, String> aliasMap = getAliasMap();
        testCommand.setAliasMap(aliasMap);
        Map<String, Command> commandMap = getCommandMap();
        commandMap.put("usage", new UsageCommand(commandMap, aliasMap));
        testCommand.setCommandMap(commandMap);

        System.out.println("--------------------");

        while (true) {
            commandState.capture(testCommand);

            try {
                testCommand.execute(commandState);
            }
            catch (TerminatedApplicationException e) {
                break;
            }
        }
    }

    /**
     * Returns the maximum inactivity interval before the application
     * terminates. To turn off this feature, override and return null.
     * @return the number of inactivity before the program is terminated, or
     * null to keep it running indefinitely
     */
    protected Integer getMaxInactiveSeconds() {
        return 600;
    }

    protected Command getInitCommand() {
        return new InitRunnerCommand();
    }

    protected Map<String, Command> getCommandMap() {
        Map<String, Command> commands = new LinkedHashMap<String, Command>();

        commands.put("show", new ShowModulesCommand());
        commands.put("test", new RunTestCommand());
        commands.put("rerun-test", new RerunTestCommand());
        commands.put("reload", new ReloadCommand());
        commands.put("repair", new RepairCommand());
        commands.put("set-class", new LoadDefinitionFromClassNameCommand());
        commands.put("change-directory", new ChangeDirectoryCommand());
        commands.put("exit", new ExitCommand());
        return commands;
    }

    protected Map<String, String> getAliasMap() {
        Map<String, String> aliases = new LinkedHashMap<String, String>();
        aliases.put("e", "exit");
        aliases.put("rel", "reload");
        aliases.put("module", "reload");
        aliases.put("fix", "repair");
        aliases.put("t", "test");
        aliases.put("rt", "rerun-test");
        aliases.put("rerun", "rerun-test");
        aliases.put("cd", "change-directory");
        aliases.put("sc", "set-class");
        aliases.put("class", "set-class");
        aliases.put("u", "usage");
        aliases.put("s", "show");
        return aliases;
    }
}
