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

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.module.spi.Application;
import org.impalaframework.module.spi.ModuleStateChange;
import org.impalaframework.module.spi.ModuleStateChangeNotifier;
import org.impalaframework.module.spi.ModuleStateHolder;
import org.impalaframework.module.spi.TransitionManager;
import org.impalaframework.module.spi.TransitionProcessor;
import org.impalaframework.module.spi.TransitionResult;
import org.impalaframework.module.spi.TransitionResultSet;
import org.impalaframework.module.spi.TransitionSet;
import org.springframework.util.Assert;

/**
 * Default implementation of {@link TransitionManager}, responsible for calling
 * {@link TransitionProcessor} instances and collecting the results of these operations
 * in a {@link TransitionResultSet} instance.
 * 
 * @author Phil Zoio
 */
public class DefaultTransitionManager implements TransitionManager {

    private static Log logger = LogFactory.getLog(DefaultTransitionManager.class);

    private TransitionProcessorRegistry transitionProcessorRegistry;
    
    private ModuleStateChangeNotifier moduleStateChangeNotifier;

    private TransitionsLogger transitionsLogger = new TransitionsLogger();
    
    public DefaultTransitionManager() {
        super();
    }

    public TransitionResultSet processTransitions(ModuleStateHolder moduleStateHolder, Application application, TransitionSet transitions) {

        TransitionResultSet resultSet = new TransitionResultSet();
        
        try {
            Assert.notNull(transitionProcessorRegistry, TransitionProcessorRegistry.class.getSimpleName() + " cannot be null");

            Collection<? extends ModuleStateChange> changes = transitions.getModuleTransitions();

            for (ModuleStateChange change : changes) {
                
                if (logger.isDebugEnabled()) {
                    logger.debug("Processing module state change: " + change);
                }
                
                String transition = change.getTransition();
                ModuleDefinition currentModuleDefinition = change.getModuleDefinition();

                TransitionProcessor transitionProcessor = transitionProcessorRegistry.getTransitionProcessor(transition);
                
                TransitionResult result;
      
                try {
                    transitionProcessor.process(application, transitions.getNewRootModuleDefinition(), currentModuleDefinition);
                    result = new TransitionResult(change);
                }
                catch (Throwable error) {
                    result = new TransitionResult(change, error);
                }
                
                resultSet.addResult(result);
            
                if (result.getError() == null && moduleStateChangeNotifier != null) {
                    moduleStateChangeNotifier.notify(moduleStateHolder, result);
                }
            }
            
            transitionsLogger.logTransitions(resultSet);
            
        } finally {
            RootModuleDefinition rootModuleDefinition = transitions.getNewRootModuleDefinition();
            moduleStateHolder.setRootModuleDefinition(rootModuleDefinition);
        }
        
        return resultSet;
    }

    /* ******************** injected setters ******************** */
    
    public void setTransitionProcessorRegistry(TransitionProcessorRegistry transitionProcessorRegistry) {
        this.transitionProcessorRegistry = transitionProcessorRegistry;
    }

    public void setModuleStateChangeNotifier(ModuleStateChangeNotifier moduleStateChangeNotifier) {
        this.moduleStateChangeNotifier = moduleStateChangeNotifier;
    }

}
