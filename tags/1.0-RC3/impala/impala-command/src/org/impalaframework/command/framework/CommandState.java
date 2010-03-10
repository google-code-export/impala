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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Encapsulates the current command state. This class is not thread safe, but
 * can be shared by successive commands using the same thread
 * @author Phil Zoio
 */
public class CommandState {

    // capture the commandinfo
    InputCapturer inputCapturer = new CommandLineInputCapturer();

    private Map<String, CommandPropertyValue> properties = new HashMap<String, CommandPropertyValue>();

    private GlobalCommandState globalStateHolder = GlobalCommandState.getInstance();

    public CommandInput capture(Command command) {
        properties.clear();
        return captureInput(command);
    }

    public CommandInput captureInput(Command command) {
        CommandDefinition commandDefinition = command.getCommandDefinition();

        if (commandDefinition == null) {
            throw new IllegalArgumentException("Command cannot have null commandDefinition");
        }

        List<CommandInfo> commandInfos = commandDefinition.getCommandInfos();
        for (CommandInfo info : commandInfos) {

            CommandPropertyValue existingLocal = getProperties().get(info.getPropertyName());
            CommandPropertyValue existingGlobal = globalStateHolder.getProperty(info.getPropertyName());

            boolean invalid = false;
            boolean ok = false;
            String value = null;

            while (!ok) {

                String input = null;

                input = existingLocal != null ? existingLocal.getValue() : null;

                if (input == null && info.isGlobalOverride())
                    input = existingGlobal != null ? existingGlobal.getValue() : null;
                    
                boolean recapture = false;

                if (invalid) {
                    invalid = false;
                    recapture = true;
                }

                if (recapture)
                    input = inputCapturer.recapture(info);
                else if (input == null)
                    input = inputCapturer.capture(info);

                if (input != null && input.trim().length() > 0) {

                    value = input.trim();

                    if (value.equalsIgnoreCase("back")) {
                        return new CommandInput(true);
                    }

                    if (value.equalsIgnoreCase("quit")) {
                        throw new TerminatedCommandException();
                    }

                    String validate = info.validate(value);
                    if (validate != null) {
                        inputCapturer.displayValidationMessage(validate);
                        invalid = true;
                    }
                }

                if (invalid)
                    continue;

                if (value == null) {
                    if (!info.isIsolated() && existingGlobal != null) {
                        value = existingGlobal.getValue();
                    }
                }

                if (value == null) {
                    value = info.getDefaultValue();
                }

                if (value != null) {
                    CommandPropertyValue cpv = new CommandPropertyValue(value, info.getDescription());
                    properties.put(info.getPropertyName(), cpv);

                    if (info.isShared()) {
                        globalStateHolder.addProperty(info.getPropertyName(), cpv);
                    } else {
                        globalStateHolder.clearProperty(info.getPropertyName());
                    }

                    ok = true;
                }
                else if (info.isOptional()) {
                    ok = true;
                }
            }
        }
        return new CommandInput(Collections.unmodifiableMap(properties));
    }

    public Map<String, CommandPropertyValue> getProperties() {
        return Collections.unmodifiableMap(properties);
    }

    public void addProperty(String propertyName, CommandPropertyValue propertyValue) {
        properties.put(propertyName, propertyValue);
    }

    public GlobalCommandState getGlobalStateHolder() {
        return globalStateHolder;
    }

    public void setInputCapturer(InputCapturer inputCapturer) {
        this.inputCapturer = inputCapturer;
    }

}
