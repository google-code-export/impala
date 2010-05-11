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

import java.util.HashMap;
import java.util.Map;

import org.easymock.EasyMock;
import org.impalaframework.exception.NoServiceException;

import junit.framework.TestCase;

public class ModuleOperationRegistryTest extends TestCase {

    private ModuleOperationRegistry registry;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        registry = new ModuleOperationRegistry();
    }
    
    public final void testPutOperation() {
        ModuleOperation moduleOp = newModuleOp();
        registry.addItem("op1", moduleOp);
        
        assertSame(moduleOp, registry.getOperation("op1"));
    }
    
    public final void testUnknown() {       
        try {
            registry.getOperation("op1");
        }
        catch (NoServiceException e) {
            assertEquals("No instance of org.impalaframework.module.operation.ModuleOperation available for key 'op1'. Available entries: []", e.getMessage());
        }
    }
    
    public void testContributions() throws Exception {

        ModuleOperation moduleOp1 = newModuleOp();
        registry.addItem("op1", moduleOp1);
        
        Map<String, ModuleOperation> contributions = new HashMap<String, ModuleOperation>();
        ModuleOperation moduleOp2 = newModuleOp();
        contributions.put("op1", moduleOp2);
        
        registry.setOperations(contributions);
        assertSame(moduleOp2, registry.getOperation("op1"));
    }
    
    private ModuleOperation newModuleOp() {
        ModuleOperation moduleOp = EasyMock.createMock(ModuleOperation.class);
        return moduleOp;
    }

    public final void testEmptyRegistry() {
        Map<String, ModuleOperation> operations = registry.getOperations();
        assertEquals(0, operations.size());
        
        try {
            operations.put("name", EasyMock.createMock(ModuleOperation.class));
            fail("Unmodifiable, so should not be supported");
        }
        catch (UnsupportedOperationException e) {
        }
    }

}
