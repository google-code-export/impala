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

package org.impalaframework.service.registry.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.service.ServiceRegistry;
import org.impalaframework.service.ServiceRegistryEvent;
import org.impalaframework.service.ServiceRegistryEventListener;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

/**
 * Implementation of {@link ServiceRegistry}, which holds services which can be shared across modules.
 * @author Phil Zoio
 */
public class ServiceEventListenerRegistryDelegate implements InvokingServiceEventListenerRegistry {

    private static Log logger = LogFactory.getLog(ServiceEventListenerRegistryDelegate.class);

    // use CopyOnWriteArrayList to support non-blocking thread-safe iteration
    private List<ServiceRegistryEventListener> listeners = new CopyOnWriteArrayList<ServiceRegistryEventListener>();

    private Object listenersLock = new Object();
    
    /* ************ listener related methods * ************** */

    /**
     * Adds to global event listeners to which all service registry events will
     * be broadcast. Note that a single event listener instance can only be added once.
     * Any subsequent attempts to add this listener will not have any effect.
     */
    public boolean addEventListener(ServiceRegistryEventListener listener) {
        Assert.notNull(listener);
        
        synchronized (listenersLock) {
            if (this.listeners.contains(listener)) {
                logger.warn("Listener " + ObjectUtils.identityToString(listener) + " already a listener for this service registry");
                return false;
            } else {
                this.listeners.add(listener);
                if (logger.isDebugEnabled()) {
                    logger.debug("Added service registry listener " + listener);
                }
                return true;
            }
        }
    }
    
    /**
     * Removes global event listeners to which all service registry events will
     * be broadcast
     * @return true if listener was removed.
     */
    public boolean removeEventListener(ServiceRegistryEventListener listener) {
        List<ServiceRegistryEventListener> listeners = getCopyOfListeners();
        
        return removeListener(listener, listeners);
    }

    public void invokeListeners(ServiceRegistryEvent event) {
        
        List<ServiceRegistryEventListener> listeners = getCopyOfListeners();
        
        //inform all listeners of service listener event
        for (ServiceRegistryEventListener listener : listeners) {
            listener.handleServiceRegistryEvent(event);
        }
    }
    
    /* ************ helper methods * ************** */
    
    private List<ServiceRegistryEventListener> getCopyOfListeners() {
        //not synchronized as we are using CopyOnWriteArrayList
        return new ArrayList<ServiceRegistryEventListener>(listeners);
    }

    private boolean removeListener(ServiceRegistryEventListener listener, List<ServiceRegistryEventListener> listeners) {
        
        for (ServiceRegistryEventListener currentListener : listeners) {
            
            if (currentListener == listener) {
                //not synchronized as we are using CopyOnWriteArrayList
                boolean removed = this.listeners.remove(currentListener);
                if (logger.isDebugEnabled())
                    logger.debug("Removed service registry listener " + listener + ": " + removed);
                return removed;
            }
        }
        return false;
    }
}

