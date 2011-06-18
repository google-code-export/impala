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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Defines all the information required for executing a command
 * @author phil
 */
public class CommandDefinition {

    private String description;

    private List<CommandInfo> commandInfos = new ArrayList<CommandInfo>();

    public CommandDefinition() {
        super();
    }

    public CommandDefinition(String description) {
        super();
        this.description = description;
    }

    public static final CommandDefinition EMPTY = new CommandDefinition() {
        @Override
        public final void add(CommandInfo commandInfo) {
            throw new UnsupportedOperationException();
        }

        @Override
        public final List<CommandInfo> getCommandInfos() {
            return Collections.emptyList();
        }
    };

    public void add(CommandInfo commandInfo) {
        commandInfos.add(commandInfo);
    }

    public List<CommandInfo> getCommandInfos() {
        return Collections.unmodifiableList(commandInfos);
    }

    public String getDescription() {
        return description;
    }

}
