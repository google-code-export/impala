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

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import junit.framework.TestCase;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.spi.Application;

public class BaseModuleOperationTest extends TestCase {
	
	private BaseModuleOperation operation;
	private ModuleDefinition definition;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		operation = new BaseModuleOperation(){

            @Override
            protected ModuleOperationResult doExecute(
                    Application application, ModuleOperationInput moduleOperationInput) {
                System.out.println("After locking, before unlocking");
                return null;
            }
        };
		
		definition = createMock(ModuleDefinition.class);
	}
	
	public void testIsReloadable() throws Exception {
		
		expect(definition.isReloadable()).andReturn(true);
		replay(definition);
		
	operation.isReloadPermitted(definition);
		
		verify(definition);
	}
	
	public void testNotReloadableNotEnforced() throws Exception {
		
		operation.setEnforceReloadability(false);
		expect(definition.isReloadable()).andReturn(false);
		replay(definition);
		
		operation.isReloadPermitted(definition);
		
		verify(definition);
	}
	
	public void testNotReloadableIsEnforced() throws Exception {
		
		operation.setEnforceReloadability(true);
		expect(definition.isReloadable()).andReturn(false);
		replay(definition);
		
		try {
			operation.isReloadPermitted(definition);
			fail();
		} catch (Exception e) {
		}
		
		verify(definition);
	}

}
