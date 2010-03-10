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

package org.impalaframework.module;

import java.util.List;
import java.util.Map;


/**
 * {@link ModuleDefinition} exposes a number of methods which return information
 * on a module, such as name, type and the identity of parent and child modules.
 * ModuleDefinition has a similar role in Impala to the role of
 * {@link org.springframework.beans.factory.config.BeanDefinition}s in Spring.
 * 
 * A hierarchy of {@link ModuleDefinition}s can be considered an abstract
 * representation of the module hierarchy, which can be manipulated separately
 * from the runtime module hierarchy (which is essentially a hierarchy of Spring
 * application contexts). At the root of the a hierarchy of module definitions
 * is an instance of {@link RootModuleDefinition}, which exposes another couple
 * of methods specific to root modules.
 * 
 * @see RootModuleDefinition
 * @author Phil Zoio
 */
public interface ModuleDefinition extends Freezable, ModuleContainer {

    /**
     * Returns the type of the module. By default this will be 'application'
     */
    String getType();

    /**
     * Returns the name of the module, which is ordinarily the same as the
     * project containing the module.
     */
    String getName();

    /**
     * Returns the name of the runtime framework backing the module. By default
     * this is 'spring'
     */
    String getRuntimeFramework();

    /**
     * Returns a list of config locations for the module. For example, for
     * spring modules, these will be the class path locations of the Spring XML
     * configuration files. Again, in this example, the default will be
     * ${modulename}-context.xml.
     */
    List<String> getConfigLocations();

    /**
     * Returns the module definition of the parent module.
     */
    ModuleDefinition getParentDefinition();

    /**
     * Can be used to find module definitions of child modules. If exactMatch is
     * true, then an exact match of the module name is required. If false, then
     * the first module found whose name contains the supplied moduleName
     * argument will be returned.
     */
    ModuleDefinition findChildDefinition(String moduleName, boolean exactMatch);

    /**
     * Returns a list of modules on which the current module depends. If the
     * name of the parent module is not specified, then it will automatically be
     * added to the head of this list. If it is specified, then it will remain
     * in the specified position in this list. By default, classes contained in
     * dependent modules will be visible to the current module.
     */
    List<String> getDependentModuleNames();

    /**
     * Used to modify the parent module. This method is not designed for
     * clients, and cannot be called if the module has been frozen using the
     * {@link #freeze()} call.
     */
    void setParentDefinition(ModuleDefinition moduleDefinition);

    /**
     * Used to set the module state flag. Typically used to mark a module as
     * stale.
     */
    void setState(String state);

    /**
     * Gets the current state indicator for the module.
     */
    String getState();

    /**
     * Gets the set of attributes specified for the module. By default, any
     * attribute which is found in module.properties which does not have a
     * specific meaning in terms of the standard module definition reading
     * mechanism is added as an attribute, accessible via this method. Provides
     * a configuration hook for custom module types.
     */
    Map<String, String> getAttributes();

}
