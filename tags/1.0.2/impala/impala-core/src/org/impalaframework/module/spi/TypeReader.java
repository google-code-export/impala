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

package org.impalaframework.module.spi;

import java.util.Properties;

import org.impalaframework.module.ModuleDefinition;
import org.w3c.dom.Element;

/**
 * Impala features the concept of a module type. Examples include root modules,
 * application modules, servlet modules, etc. The idea behind <code>TypeReader</code> is
 * that each of module types will require its own strategy for reading metadata
 * about the modules.
 * 
 * Type readers support a mechanism for reading module type information from XML
 * (in the form of an {@link Element} instance) or properties (for example
 * read from a properties file).
 * 
 * @see ModuleDefinition#getType()
 * @see ModuleLoader
 * @author Phil Zoio
 */
public interface TypeReader {

    ModuleDefinition readModuleDefinition(ModuleDefinition parent, String moduleName, Properties properties);

    ModuleDefinition readModuleDefinition(ModuleDefinition parent, String moduleName, Element definitionElement);
    
    void readModuleDefinitionProperties(Properties properties, String moduleName, Element definitionElement);
    
}
