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

package org.impalaframework.service.contribution;

import static org.easymock.EasyMock.*;

import java.util.Collections;

import org.impalaframework.service.ServiceRegistry;
import org.impalaframework.service.filter.ldap.LdapServiceReferenceFilter;
import org.impalaframework.service.reference.BasicServiceRegistryReference;
import org.springframework.util.ClassUtils;

import junit.framework.TestCase;

public class ServiceRegistryMonitorTest extends TestCase {

    private ServiceActivityNotifiable serviceActivityNotifiable;
    private ServiceRegistryMonitor monitor;
    private ServiceRegistry serviceRegistry;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        monitor = new ServiceRegistryMonitor();
        serviceActivityNotifiable = createMock(ServiceActivityNotifiable.class);
        monitor.setServiceActivityNotifiable(serviceActivityNotifiable);
        serviceRegistry = createMock(ServiceRegistry.class);
        monitor.setServiceRegistry(serviceRegistry);
    }

    public void testHandleServiceNoTypes() {
        
        BasicServiceRegistryReference ref = new BasicServiceRegistryReference("service", "beanName", "module", null, Collections.singletonMap("name", "somevalue"), ClassUtils.getDefaultClassLoader());
        
        expect(serviceActivityNotifiable.getServiceReferenceFilter()).andReturn(new LdapServiceReferenceFilter("(name=*)"));
        expect(serviceActivityNotifiable.getProxyTypes()).andReturn(null);
        expect(serviceActivityNotifiable.getExportTypes()).andReturn(null);
        
        expect(serviceActivityNotifiable.add(ref)).andReturn(true);
        
        replay(serviceActivityNotifiable);
        monitor.handleReferenceAdded(ref);
        verify(serviceActivityNotifiable);
    }

    public void testHandleServiceWithCorrectTypes() {
        
        BasicServiceRegistryReference ref = new BasicServiceRegistryReference("service", "beanName", "module", null, Collections.singletonMap("name", "somevalue"), ClassUtils.getDefaultClassLoader());
        
        expect(serviceActivityNotifiable.getServiceReferenceFilter()).andReturn(new LdapServiceReferenceFilter("(name=*)"));
        expect(serviceActivityNotifiable.getProxyTypes()).andReturn(new Class<?>[] {String.class});
        expect(serviceActivityNotifiable.getExportTypes()).andReturn(null);
        
        expect(serviceActivityNotifiable.add(ref)).andReturn(true);
        
        replay(serviceActivityNotifiable);
        monitor.handleReferenceAdded(ref);
        verify(serviceActivityNotifiable);
    }

    public void testTypeNotMatches() {
        
        BasicServiceRegistryReference ref = new BasicServiceRegistryReference("service", "beanName", "module", null, Collections.singletonMap("name", "somevalue"), ClassUtils.getDefaultClassLoader());
        
        expect(serviceActivityNotifiable.getServiceReferenceFilter()).andReturn(new LdapServiceReferenceFilter("(name=*)"));
        expect(serviceActivityNotifiable.getProxyTypes()).andReturn(new Class<?>[] {Integer.class});
        expect(serviceActivityNotifiable.getExportTypes()).andReturn(null);
        //no call to add
        
        replay(serviceActivityNotifiable);
        monitor.handleReferenceAdded(ref);
        verify(serviceActivityNotifiable);
    }
    
    public void testWithExportTypesNotMatches() {
        
        BasicServiceRegistryReference ref = new BasicServiceRegistryReference("service", "beanName", "module", null, Collections.singletonMap("name", "somevalue"), ClassUtils.getDefaultClassLoader());
        
        expect(serviceActivityNotifiable.getServiceReferenceFilter()).andReturn(new LdapServiceReferenceFilter("(name=*)"));
        Class<?>[] exportTypes = new Class<?>[] {Integer.class};
        expect(serviceActivityNotifiable.getExportTypes()).andReturn(exportTypes);
        expect(serviceRegistry.isPresentInExportTypes(ref, exportTypes)).andReturn(false);
        //no call to add
        
        replay(serviceActivityNotifiable);
        replay(serviceRegistry);
        monitor.handleReferenceAdded(ref);
        verify(serviceActivityNotifiable);
        verify(serviceRegistry);
    }
    
    public void testWithExportTypesMatches() {
        
        BasicServiceRegistryReference ref = new BasicServiceRegistryReference("service", "beanName", "module", null, Collections.singletonMap("name", "somevalue"), ClassUtils.getDefaultClassLoader());
        
        expect(serviceActivityNotifiable.getServiceReferenceFilter()).andReturn(new LdapServiceReferenceFilter("(name=*)"));
        Class<?>[] exportTypes = new Class<?>[] {Integer.class};
        expect(serviceActivityNotifiable.getExportTypes()).andReturn(exportTypes);
        expect(serviceRegistry.isPresentInExportTypes(ref, exportTypes)).andReturn(true);
        
        expect(serviceActivityNotifiable.add(ref)).andReturn(true);
        
        replay(serviceActivityNotifiable);
        replay(serviceRegistry);
        monitor.handleReferenceAdded(ref);
        verify(serviceActivityNotifiable);
        verify(serviceRegistry);
    }

    public void testHandleServiceNotMatches() {
        
        BasicServiceRegistryReference ref = new BasicServiceRegistryReference("service", "beanName", "module", null, Collections.singletonMap("name", "somevalue"), ClassUtils.getDefaultClassLoader());
        
        expect(serviceActivityNotifiable.getServiceReferenceFilter()).andReturn(new LdapServiceReferenceFilter("(missing=*)"));
        expect(serviceActivityNotifiable.getProxyTypes()).andReturn(null);
        expect(serviceActivityNotifiable.getExportTypes()).andReturn(null);
        
        replay(serviceActivityNotifiable);
        monitor.handleReferenceAdded(ref);
        verify(serviceActivityNotifiable);
    }

}
