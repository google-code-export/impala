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

package org.impalaframework.module.operation;

import java.util.Collections;
import java.util.Map;

import org.impalaframework.module.spi.Application;
import org.impalaframework.module.spi.TransitionResultSet;
import org.springframework.util.Assert;

/**
 * Encapsulates the result of a {@link ModuleOperation#execute(Application, ModuleOperationInput)} invocation. 
 * 
 * @author Phil Zoio
 */
public class ModuleOperationResult {

    public static final ModuleOperationResult EMPTY = new ModuleOperationResult(new TransitionResultSet());
    
    private final TransitionResultSet transitionResultSet;

    private final Map<String, Object> outputParameters;

    @SuppressWarnings("unchecked")
    public ModuleOperationResult(TransitionResultSet transitionResultSet) {
        this(transitionResultSet, Collections.EMPTY_MAP);
    }

    public ModuleOperationResult(TransitionResultSet transitionResultSet, final Map<String, Object> outputValues) {
        super();
        Assert.notNull(transitionResultSet, "transitionResultSet cannot be null");
        Assert.notNull(outputValues);
        this.outputParameters = outputValues;
        this.transitionResultSet = transitionResultSet;
    }

    public Map<String, Object> getOutputParameters() {
        return outputParameters;
    }
    
    public TransitionResultSet getTransitionResultSet() {
        return transitionResultSet;
    }

    public boolean hasResults() {
        return transitionResultSet.hasResults();
    }

    public boolean isSuccess() {
        return transitionResultSet.isSuccess() && transitionResultSet.hasResults();
    }
    
    public boolean isErrorFree() {
        return transitionResultSet.isSuccess();
    }
    
    @Override
    public String toString() {
        return "ModuleOperationResult [outputParameters=" + outputParameters
                + ", transitionResultSet=" + transitionResultSet + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime
                * result
                + ((outputParameters == null) ? 0 : outputParameters.hashCode());
        result = prime
                * result
                + ((transitionResultSet == null) ? 0 : transitionResultSet
                        .hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ModuleOperationResult other = (ModuleOperationResult) obj;
        if (outputParameters == null) {
            if (other.outputParameters != null)
                return false;
        }
        else if (!outputParameters.equals(other.outputParameters))
            return false;
        if (transitionResultSet == null) {
            if (other.transitionResultSet != null)
                return false;
        }
        else if (!transitionResultSet.equals(other.transitionResultSet))
            return false;
        return true;
    }

}
