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

package org.impalaframework.resolver;

import java.util.List;

import org.springframework.core.io.Resource;

/**
 * Interface representing strategy to find the class path locations of a
 * particular module as a list of {@link Resource}s.
 * 
 * {@link ModuleLocationResolver} is responsible for determining where to find
 * modules. Impala supports a number of deployment configurations, such as
 * standalone deployment of jar files, deployment of modules as jar files in a
 * web application's <i>WEB-INF/modules_ directory</i>, and deployment directly from
 * the _bin_ directory of an Eclipse project on the file system. Each of these
 * scenarios requires a different strategy for module loading, and hence a
 * different implementation of {@link ModuleLocationResolver}
 * 
 * @author Phil Zoio
 */
public interface ModuleLocationResolver {
    
    /**
     * Returns the directory locations for test classes for a parent project
     */
    public List<Resource> getModuleTestClassLocations(String moduleName);

    /**
     * Returns the directory locations for module classes
     */
    public List<Resource> getApplicationModuleClassLocations(String moduleName);

}
