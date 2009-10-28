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

import junit.framework.TestCase;

import org.impalaframework.module.definition.SimpleModuleDefinition;

public class TransitionResultSetTest extends TestCase {
    
    public void testSuccess() {
        TransitionResultSet successSet = newSuccessTransitionResultSet();
        assertTrue(successSet.hasResults());
        assertTrue(successSet.isSuccess());
        assertNull(successSet.getFirstError());
    } 
    
    public void testFailed() {
        TransitionResultSet successSet = newFailedTransitionResultSet();
        assertTrue(successSet.hasResults());
        assertFalse(successSet.isSuccess());
        assertEquals("stuff went wrong1", successSet.getFirstError().getMessage());
    }  
    
    public void testEmpty() {
        TransitionResultSet successSet = new TransitionResultSet();
        assertFalse(successSet.hasResults());
        assertTrue(successSet.isSuccess());
        assertNull(successSet.getFirstError());
    }     
    
    public static TransitionResultSet newSuccessTransitionResultSet() {
        TransitionResultSet result = new TransitionResultSet();
        result.addResult(new ModuleStateChange(Transition.LOADED_TO_UNLOADED, new SimpleModuleDefinition("module1")), new TransitionResult());
        result.addResult(new ModuleStateChange(Transition.LOADED_TO_UNLOADED, new SimpleModuleDefinition("module2")), new TransitionResult());
        return result;
    }
    
    public static TransitionResultSet newFailedTransitionResultSet() {
        TransitionResultSet result = new TransitionResultSet();
        result.addResult(new ModuleStateChange(Transition.LOADED_TO_UNLOADED, new SimpleModuleDefinition("module1")), new TransitionResult());
        result.addResult(new ModuleStateChange(Transition.LOADED_TO_UNLOADED, new SimpleModuleDefinition("module2")), new TransitionResult(new RuntimeException("stuff went wrong1")));
        result.addResult(new ModuleStateChange(Transition.LOADED_TO_UNLOADED, new SimpleModuleDefinition("module3")), new TransitionResult(new RuntimeException("stuff went wrong2")));
        return result;
    }

}
