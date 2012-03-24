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
import java.util.Map;

/**
 * Encapsulates command input, which can either be a map of property name to
 * input <code>CommandPropertyValue</code>, or an instruction to go back.
 * Users of this class should check whether isGoBack() is false before
 * attempting to use the properties collected
 * @author Phil Zoio 
 */
public class CommandInput {

    final Map<String, CommandPropertyValue> properties;

    final boolean goBack;

    public CommandInput(boolean goBack) {
        super();
        this.goBack = goBack;
        this.properties = Collections.emptyMap();
    }

    public CommandInput(Map<String, CommandPropertyValue> properties) {
        super();
        this.properties = properties;
        this.goBack = false;
    }

    public boolean isGoBack() {
        return goBack;
    }

    public Map<String, CommandPropertyValue> getProperties() {
        return properties;
    }

}
