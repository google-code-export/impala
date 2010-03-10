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

import static org.impalaframework.module.spi.TransitionResultSetTest.*;

import org.impalaframework.module.spi.TransitionResultSet;

import junit.framework.TestCase;

public class ModuleOperationResultTest extends TestCase {

    public void testSuccess() throws Exception {
        ModuleOperationResult result = new ModuleOperationResult(newSuccessTransitionResultSet());
        assertTrue(result.isSuccess());
        assertTrue(result.isErrorFree());
        assertTrue(result.hasResults());
    }
    
    public void testFailed() throws Exception {
        ModuleOperationResult result = new ModuleOperationResult(newFailedTransitionResultSet());
        assertFalse(result.isSuccess());
        assertFalse(result.isErrorFree());
        assertTrue(result.hasResults());
    }
    
    public void testEmpty() throws Exception {
        ModuleOperationResult result = new ModuleOperationResult(new TransitionResultSet());
        assertFalse(result.isSuccess());
        assertTrue(result.isErrorFree());
        assertFalse(result.hasResults());
    }
    
}
