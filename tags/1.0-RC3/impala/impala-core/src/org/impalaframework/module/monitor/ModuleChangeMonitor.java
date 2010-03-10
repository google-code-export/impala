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

package org.impalaframework.module.monitor;

import org.springframework.core.io.Resource;

/**
 * Represents contract for detecting changes in resources used in an Impala
 * module. `ModuleChangeMonitor` is used optionally to implement modification
 * detection and automatically reload modules. For the <i>web-listener-bootstrap</i>
 * Impala configuration, a file system polling mechanism is used.
 * 
 * @see ScheduledModuleChangeMonitor
 * @author Phil Zoio
 */
public interface ModuleChangeMonitor {
    public void start();
    public void setResourcesToMonitor(String moduleName, Resource[] resources);
    public void addModificationListener(ModuleContentChangeListener listener);
    public void stop();
}
