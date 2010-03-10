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

package org.impalaframework.spring.service.proxy;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import junit.framework.TestCase;

import org.impalaframework.service.ServiceRegistryEntry;
import org.impalaframework.spring.service.ServiceEndpointTargetSource;

public class InfrastructureProxyIntroductionTest extends TestCase {

    private ServiceEndpointTargetSource targetSource;
    private InfrastructureProxyIntroduction infrastructureProxyIntroduction;
    private ServiceRegistryEntry serviceRegistryEntry;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        targetSource = createMock(ServiceEndpointTargetSource.class);
        serviceRegistryEntry = createMock(ServiceRegistryEntry.class);
        infrastructureProxyIntroduction = new InfrastructureProxyIntroduction(targetSource);
    }
    
    public void testGetInterfaces() throws Exception {
        infrastructureProxyIntroduction.getInterfaces();
    }
    
    public void testGetWrappedObject() throws Exception {
        Object target = new Object();
        // expect(targetSource.getServiceRegistryReference()).andReturn(serviceRegistryEntry);
        expect(targetSource.getTarget()).andReturn(target);
        
        replay(targetSource, serviceRegistryEntry);
        
        final Object wrappedObject = infrastructureProxyIntroduction.getWrappedObject();
        assertSame(target, wrappedObject);

        verify(targetSource, serviceRegistryEntry);
    }

}
