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

package org.impalaframework.module.spi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Holds the result of a set of module transition changes.
 * @see ModuleStateChange
 * @see TransitionResult
 * @author Phil Zoio
 */
public class TransitionResultSet {
    
    private final List<TransitionResult> list = new ArrayList<TransitionResult>();
        
    public void addResult(TransitionResult result) {
        list.add(result);
    }

    public boolean hasResults() {
        return (!list.isEmpty());
    }

    public boolean isSuccess() {
        
        if (list.isEmpty()) {
            return true;
        }

        for (TransitionResult transitionResult : list) {
            if (!transitionResult.isSuccess()) {
                return false;
            }
        }
        return true;
    }
    
    public Throwable getFirstError() {
       
        for (TransitionResult transitionResult : list) {
            if (!transitionResult.isSuccess()) {
                return transitionResult.getError();
            }
        }
        return null;
    }

    public List<TransitionResult> getResults() {
        return Collections.unmodifiableList(list);
    }
    
    @Override
    public String toString() {
        return "TransitionResultSet [results=" + list + "]";
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((list == null) ? 0 : list.hashCode());
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
        if (list == null) {
            if (other.list != null)
                return false;
        }
        else if (!list.equals(other.list))
            return false;
        return true;
    }

}
