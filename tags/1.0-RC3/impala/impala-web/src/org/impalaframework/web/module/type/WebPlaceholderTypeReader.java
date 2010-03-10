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

package org.impalaframework.web.module.type;

import java.util.Properties;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.spi.TypeReader;
import org.impalaframework.web.spring.module.WebPlaceholderModuleDefinition;
import org.w3c.dom.Element;

public class WebPlaceholderTypeReader implements TypeReader {

    public ModuleDefinition readModuleDefinition(ModuleDefinition parent,
            String moduleName, Properties properties) {
        return new WebPlaceholderModuleDefinition(parent, moduleName);
    }

    public ModuleDefinition readModuleDefinition(ModuleDefinition parent,
            String moduleName, Element definitionElement) {
        return new WebPlaceholderModuleDefinition(parent, moduleName);
    }

    public void readModuleDefinitionProperties(Properties properties, String moduleName,
            Element definitionElement) {
    }

}
