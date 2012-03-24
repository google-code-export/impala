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
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import junit.framework.TestCase;

import org.impalaframework.module.spi.Application;
import org.impalaframework.module.spi.FrameworkLockHolder;
import org.impalaframework.module.spi.ModuleStateHolder;
import org.impalaframework.module.spi.TestApplicationManager;

public class LockingModuleOperationTest extends TestCase {

    public void testExecute() {
        
        LockingModuleOperation operation = new LockingModuleOperation(){

            @Override
            protected ModuleOperationResult doExecute(
                    Application application, ModuleOperationInput moduleOperationInput) {
                System.out.println("After locking, before unlocking");
                return null;
            }
            
        };
        
        FrameworkLockHolder frameworkLockHolder = createMock(FrameworkLockHolder.class);
        operation.setFrameworkLockHolder(frameworkLockHolder);
        
        frameworkLockHolder.writeLock();
        frameworkLockHolder.writeUnlock();
        
        replay(frameworkLockHolder);
        
        ModuleStateHolder moduleStateHolder = createMock(ModuleStateHolder.class);
        Application application = TestApplicationManager.newApplicationManager(null, moduleStateHolder, null).getCurrentApplication();
        operation.execute(application, null);
        
        verify(frameworkLockHolder);
    }

}
