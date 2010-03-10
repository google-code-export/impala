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

package org.impalaframework.module.transition;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.spi.ModuleStateChange;
import org.impalaframework.module.spi.ModuleStateChangeListener;
import org.impalaframework.module.spi.ModuleStateChangeNotifier;
import org.impalaframework.module.spi.ModuleStateHolder;
import org.impalaframework.module.spi.TransitionResult;

/**
 * Default implementation of {@link ModuleStateChangeNotifier} interfaces.
 * @author Phil Zoio
 */
public class DefaultModuleStateChangeNotifier implements ModuleStateChangeNotifier {

    private static final Log logger = LogFactory.getLog(DefaultModuleStateChangeNotifier.class);
    
    private List<ModuleStateChangeListener> listeners = new LinkedList<ModuleStateChangeListener>();

    /**
     * For each of the registered {@link ModuleStateChangeListener} listener
     * instances, the {@link ModuleStateChangeListener#getTransition()} is
     * called to determine whether the listener's
     * {@link ModuleStateChangeListener#moduleStateChanged(ModuleStateHolder, ModuleStateChange)}
     * should be called.
     */
    public void notify(ModuleStateHolder moduleStateHolder, TransitionResult transitionResult) {
        
        ModuleStateChange moduleStateChange = transitionResult.getModuleStateChange();
        
        ModuleDefinition moduleDefinition = moduleStateChange.getModuleDefinition();

        for (ModuleStateChangeListener moduleStateChangeListener : listeners) {
            String moduleName = moduleStateChangeListener.getModuleName();

            boolean notify = true;

            if (moduleName != null) {
                if (!moduleName.equals(moduleDefinition.getName())) {
                    notify = false;
                }
            }

            if (notify) {

                String transition = moduleStateChangeListener.getTransition();

                if (transition != null) {
                    if (!transition.equals(moduleStateChange.getTransition())) {
                        notify = false;
                    }
                }
            }

            if (notify) {
                try {
                    moduleStateChangeListener.moduleStateChanged(moduleStateHolder, transitionResult);
                }
                catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

    public void setListeners(List<ModuleStateChangeListener> listeners) {
        this.listeners.clear();
        this.listeners.addAll(listeners);
    }

    public void addListener(ModuleStateChangeListener listener) {
        this.listeners.add(listener);
    }

    public boolean removeListener(ModuleStateChangeListener listener) {
        return this.listeners.remove(listener);
    }

    List<ModuleStateChangeListener> getListeners() {
        return Collections.unmodifiableList(listeners);
    }
    
}
