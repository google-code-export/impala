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

package org.impalaframework.interactive.listener;

import java.util.Set;

import org.impalaframework.facade.Impala;
import org.impalaframework.module.monitor.BaseModuleChangeListener;
import org.impalaframework.module.monitor.ModuleChangeEvent;
import org.impalaframework.module.monitor.ModuleContentChangeListener;


public class DynamicModuleChangeListener extends BaseModuleChangeListener implements ModuleContentChangeListener {

    public void moduleContentsModified(ModuleChangeEvent event) {
        Set<String> modified = getModifiedModules(event);
        
        for (String pluginName : modified) {
            Impala.reloadModule(pluginName);
        }
    }
}
