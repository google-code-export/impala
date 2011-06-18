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

import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import junit.framework.TestCase;

import org.impalaframework.facade.ModuleManagementFacade;
import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.module.modification.ModificationExtractorRegistry;
import org.impalaframework.module.spi.Application;
import org.impalaframework.module.spi.ApplicationManager;
import org.impalaframework.module.spi.FrameworkLockHolder;
import org.impalaframework.module.spi.ModificationExtractor;
import org.impalaframework.module.spi.ModificationExtractorType;
import org.impalaframework.module.spi.ModuleStateHolder;
import org.impalaframework.module.spi.TestApplicationManager;
import org.impalaframework.module.spi.TransitionManager;
import org.impalaframework.module.spi.TransitionSet;

public abstract class BaseModuleOperationTest extends TestCase {

    private ModuleManagementFacade moduleManagementFacade;

    protected LockingModuleOperation operation;

    protected FrameworkLockHolder frameworkLockHolder;

    protected ModuleStateHolder moduleStateHolder;

    protected ModificationExtractor strictModificationExtractor;

    protected ModificationExtractor stickyModificationExtractor;
    
    protected ModificationExtractor repairModificationExtractor;

    protected ModificationExtractorRegistry modificationExtractorRegistry;

    protected RootModuleDefinition originalDefinition;

    protected RootModuleDefinition newDefinition;

    protected TransitionSet transitionSet;

    protected TransitionManager transitionManager;

    protected ApplicationManager applicationManager;

    protected Application application;

    protected abstract LockingModuleOperation getOperation();
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        moduleManagementFacade = createMock(ModuleManagementFacade.class);
        moduleStateHolder = createMock(ModuleStateHolder.class);
        transitionManager = createMock(TransitionManager.class);
        strictModificationExtractor = createMock(ModificationExtractor.class);
        stickyModificationExtractor = createMock(ModificationExtractor.class);
        repairModificationExtractor = createMock(ModificationExtractor.class);
        modificationExtractorRegistry = new ModificationExtractorRegistry();
        modificationExtractorRegistry.addModificationExtractorType(ModificationExtractorType.STRICT,
                strictModificationExtractor);
        modificationExtractorRegistry.addModificationExtractorType(ModificationExtractorType.STICKY,
                stickyModificationExtractor);
        modificationExtractorRegistry.addModificationExtractorType(ModificationExtractorType.REPAIR,
                repairModificationExtractor);
        originalDefinition = createMock(RootModuleDefinition.class);
        newDefinition = createMock(RootModuleDefinition.class);
        transitionSet = createMock(TransitionSet.class);
        frameworkLockHolder = createMock(FrameworkLockHolder.class);
        
        applicationManager = TestApplicationManager.newApplicationManager(null, moduleStateHolder, null);
        application = applicationManager.getCurrentApplication();
        
        operation = getOperation();
        
    }

    protected void replayMocks() {
        replay(moduleManagementFacade);
        replay(moduleStateHolder);
        replay(transitionManager);
        replay(strictModificationExtractor);
        replay(stickyModificationExtractor);
        replay(repairModificationExtractor);
        replay(originalDefinition);
        replay(newDefinition);
        replay(transitionSet);
    }

    protected void verifyMocks() {
        verify(moduleManagementFacade);
        verify(moduleStateHolder);
        verify(transitionManager);
        verify(strictModificationExtractor);
        verify(repairModificationExtractor);
        verify(originalDefinition);
        verify(newDefinition);
        verify(transitionSet);
    }

}
