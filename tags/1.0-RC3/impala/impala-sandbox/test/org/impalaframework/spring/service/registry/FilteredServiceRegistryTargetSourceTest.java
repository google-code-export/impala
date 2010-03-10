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

package org.impalaframework.spring.service.registry;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import junit.framework.TestCase;

import org.impalaframework.service.ServiceReferenceFilter;
import org.impalaframework.service.ServiceRegistry;
import org.impalaframework.service.ServiceRegistryEntry;
import org.impalaframework.service.filter.ldap.LdapServiceReferenceFilter;
import org.impalaframework.service.reference.BasicServiceRegistryEntry;
import org.impalaframework.service.reference.StaticServiceRegistryEntry;
import org.springframework.util.ClassUtils;

public class FilteredServiceRegistryTargetSourceTest extends TestCase {

    private ServiceRegistry serviceRegistry;
    private FilteredServiceRegistryTargetSource targetSource;
    private ServiceReferenceFilter filter;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        serviceRegistry = createMock(ServiceRegistry.class);
        filter = new LdapServiceReferenceFilter("(name=value)");
        this.targetSource = new FilteredServiceRegistryTargetSource(String.class, filter, serviceRegistry);
    }
    
    public void testGetNull() {
        final List<ServiceRegistryEntry> emptyList = Collections.emptyList();
        expect(serviceRegistry.getServices(filter, null, false)).andReturn(emptyList);
        
        replay(serviceRegistry);
        
        assertNull(this.targetSource.getServiceRegistryReference());
        
        verify(serviceRegistry);
    }
    
    public void testNoAttributes() throws Exception {
        
        final List<ServiceRegistryEntry> emptyList = new ArrayList<ServiceRegistryEntry>();
        final HashMap<String,?> attributes = new HashMap<String,Object>();
        
        final BasicServiceRegistryEntry ref1 = new StaticServiceRegistryEntry("bean1", "name1", "module", null, attributes, ClassUtils.getDefaultClassLoader());
        final BasicServiceRegistryEntry ref2 = new StaticServiceRegistryEntry("bean2", "name2", "module", null, attributes, ClassUtils.getDefaultClassLoader());
        
        emptyList.add(ref1);
        emptyList.add(ref2);
        expect(serviceRegistry.getServices(filter, null, false)).andReturn(emptyList);
        
        replay(serviceRegistry);
        
        final ServiceRegistryEntry reference = this.targetSource.getServiceRegistryReference();
        assertNotNull(reference);
        assertSame(ref1, reference);
        
        verify(serviceRegistry);        
    }
    
    public void testWrongType() throws Exception {
        
        final List<ServiceRegistryEntry> emptyList = new ArrayList<ServiceRegistryEntry>();
        final HashMap<String,?> attributes = new HashMap<String,Object>();
        
        final BasicServiceRegistryEntry ref1 = new StaticServiceRegistryEntry(new Integer(1), "name1", "module", null, attributes, ClassUtils.getDefaultClassLoader());
        
        emptyList.add(ref1);
        expect(serviceRegistry.getServices(filter, null, false)).andReturn(emptyList);
        
        replay(serviceRegistry);
        
        final ServiceRegistryEntry serviceRegistryReference = this.targetSource.getServiceRegistryReference();
        assertNull(serviceRegistryReference);
        
        verify(serviceRegistry);        
    }
}
