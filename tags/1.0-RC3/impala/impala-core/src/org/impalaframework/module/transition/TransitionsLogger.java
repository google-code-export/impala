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

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.module.spi.ModuleStateChange;
import org.impalaframework.module.spi.TransitionResult;
import org.impalaframework.module.spi.TransitionResultSet;
import org.impalaframework.util.ExceptionUtils;

/**
 * Class which can be set to log transitions more or less verbosely
 * @author Phil Zoio
 */
public class TransitionsLogger {
    
    private static final Log logger = LogFactory.getLog(TransitionsLogger.class);

    public void logTransitions(TransitionResultSet resultSet) {

        if (logger.isDebugEnabled()) {
            String toLog = getTransitionString(resultSet, true);
            logger.debug(toLog);
        } else if (logger.isInfoEnabled()) {
            String toLog = getTransitionString(resultSet, false);
            logger.info(toLog);
        }
        
    }

    String getTransitionString(TransitionResultSet resultSet, boolean verbose) {

        List<TransitionResult> results = resultSet.getResults();
        
        StringBuffer buffer = new StringBuffer();
        buffer.append("Module operations succeeded: ").append(resultSet.isSuccess()).append("\n");
        buffer.append("Number of operations: ").append(results.size()).append("\n");
        
        for (TransitionResult transitionResult : results) {
            ModuleStateChange moduleStateChange = transitionResult.getModuleStateChange();
            buffer.append("  ");
            buffer.append(moduleStateChange.getModuleDefinition().getName()).append(": ");
            buffer.append(moduleStateChange.getTransition()).append("\n");
            Throwable error = transitionResult.getError();
            if (error != null) {
                buffer.append("    ");
                buffer.append(error.getClass().getName());
                buffer.append(": ").append(error.getMessage()).append("\n");
                
                if (verbose) {
                    buffer.append(ExceptionUtils.getFullExceptionAsString(error, 10));
                }
            }
        }
        return buffer.toString();
    }

}
