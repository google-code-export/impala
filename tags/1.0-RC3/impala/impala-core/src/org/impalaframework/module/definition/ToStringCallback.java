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

package org.impalaframework.module.definition;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.util.ObjectUtils;


/**
 * Implementation of {@link ModuleDefinitionCallback} used to assist in toString
 * method implementation
 * 
 * @author Phil Zoio
 */
public class ToStringCallback implements ChildModuleDefinitionCallback {

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private StringBuffer buffer = new StringBuffer();
    private int spaces;
    private boolean hasMatched;

    public boolean matches(ModuleDefinition moduleDefinition) {

        final ToStringAppendable appendable = ObjectUtils.cast(moduleDefinition, ToStringAppendable.class);
        
        if (hasMatched) {
            buffer.append(LINE_SEPARATOR);
        }
        
        hasMatched = true;
        for (int i = 0; i < spaces; i++) {
            buffer.append(" ");
        }
        appendable.toString(buffer);
        return false;
    }

    public void beforeChild(ModuleDefinition moduleDefinition) {
        spaces += 2;
    }

    public void afterChild(ModuleDefinition moduleDefinition) {
        spaces -= 2;
    }
    
    public String toString() {
        return buffer.toString();
    }
}
