/*
 * Copyright 2007-2008 the original author or authors.
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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Holds the result of a set of module transition changes
 * @author Phil Zoio
 */
public class TransitionResultSet {
    
    private final Map<ModuleStateChange,TransitionResult> results = new LinkedHashMap<ModuleStateChange, TransitionResult>();
    
    public void addResult(ModuleStateChange stateChange, TransitionResult result) {
        results.put(stateChange, result);
    }
    
    public boolean succeededWithoutErrors() {
        //FIXME implement
        return true;
    }

    @Override
    public String toString() {
        return "TransitionResultSet [results=" + results + "]";
    }

    public boolean hasResults() {
        return (!results.isEmpty());
    }

    public boolean isSuccess() {
        if (results.isEmpty()) {
            return true;
        }
        
        Set<ModuleStateChange> keySet = results.keySet();
        for (ModuleStateChange moduleStateChange : keySet) {
            TransitionResult transitionResult = results.get(moduleStateChange);
            if (!transitionResult.isSuccess()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((results == null) ? 0 : results.hashCode());
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
        TransitionResultSet other = (TransitionResultSet) obj;
        if (results == null) {
            if (other.results != null)
                return false;
        }
        else if (!results.equals(other.results))
            return false;
        return true;
    }
    
    
    
}
