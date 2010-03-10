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

package org.impalaframework.osgi.util;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import junit.framework.TestCase;

import org.impalaframework.facade.InternalOperationsFacade;
import org.impalaframework.facade.ModuleManagementFacade;
import org.impalaframework.facade.OperationsFacade;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class ImpalaOsgiUtilsTest extends TestCase {

    private BundleContext bundleContext;
    private InternalOperationsFacade operationsFacade;
    private ModuleManagementFacade managementFacade;
    private Bundle bundle;
    private ServiceReference reference;


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        bundle = createMock(Bundle.class);
        bundleContext = createMock(BundleContext.class);
        operationsFacade = createMock(InternalOperationsFacade.class);
        managementFacade = createMock(ModuleManagementFacade.class);
        reference = createMock(ServiceReference.class);
    }   
    
    public void testGetManagementFacade() {
        
        expectOperationsFacade();
        expect(operationsFacade.getModuleManagementFacade()).andReturn(managementFacade);
        
        replayMocks();
        assertSame(managementFacade, ImpalaOsgiUtils.getManagementFacade(bundleContext));
        verifyMocks();
    }

    public void testGetOperationsFacade() {
        
        expectOperationsFacade();
        
        replayMocks();
        assertSame(operationsFacade, ImpalaOsgiUtils.getOperationsFacade(bundleContext));
        verifyMocks();
    }

    private void expectOperationsFacade() {
        expect(bundleContext.getServiceReference(OperationsFacade.class.getName())).andReturn(reference);
        expect(bundleContext.getService(reference)).andReturn(operationsFacade);
    }
    
    private void replayMocks() {
        replay(bundleContext);
        replay(operationsFacade);
        replay(managementFacade);
        replay(bundle);
        replay(reference);
    }
    
    private void verifyMocks() {
        verify(bundleContext);
        verify(operationsFacade);
        verify(managementFacade);
        verify(bundle); 
        verify(reference);
    }

}
