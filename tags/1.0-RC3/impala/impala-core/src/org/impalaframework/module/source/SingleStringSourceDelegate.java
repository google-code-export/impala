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

package org.impalaframework.module.source;

import java.util.LinkedList;
import java.util.List;

import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

class SingleStringSourceDelegate  {

    private ModuleDefinition moduleDefinition;

    private String definitionString;

    SingleStringSourceDelegate(ModuleDefinition moduleDefinition, String definitionString) {
        super();
        Assert.notNull(moduleDefinition);
        Assert.notNull(definitionString);
        this.moduleDefinition = moduleDefinition;
        this.definitionString = definitionString;
    }

    ModuleDefinition getModuleDefinition() {
        if (StringUtils.hasText(definitionString)) {
            String[] moduleNames = doDefinitionSplit();

            for (String moduleName : moduleNames) {
                new SimpleModuleDefinition(moduleDefinition, moduleName);
            }
        }
        return moduleDefinition;
    }

    String[] doDefinitionSplit() {

        List<Integer> indexes = new LinkedList<Integer>();

        char[] chars = definitionString.toCharArray();

        boolean inOverrides = false;

        indexes.add(-1);

        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '(') {
                if (inOverrides) {
                    invalidChar(chars, i);
                }
                inOverrides = true;
            }
            if (chars[i] == ')') {
                if (!inOverrides) {
                    invalidChar(chars, i);
                }
                inOverrides = false;
            }

            if (chars[i] == ',') {
                if (!inOverrides) {
                    indexes.add(i);
                }
            }
        }

        // add the length as the last index
        indexes.add(definitionString.length());

        List<String> segments = new LinkedList<String>();
        String moduleString = this.definitionString;

        for (int i = 1; i < indexes.size(); i++) {
            segments.add(moduleString.substring(indexes.get(i - 1) + 1, indexes.get(i)));
        }

        // convert to array
        String[] moduleNames = segments.toArray(new String[segments.size()]);

        // trim
        for (int i = 0; i < moduleNames.length; i++) {
            moduleNames[i] = moduleNames[i].trim();
        }
        return moduleNames;
    }

    private void invalidChar(char[] chars, int i) {
        throw new ConfigurationException("Invalid definition string " + definitionString + ". Invalid character '" + chars[i]
                + "' at column " + (i + 1));
    }

}
