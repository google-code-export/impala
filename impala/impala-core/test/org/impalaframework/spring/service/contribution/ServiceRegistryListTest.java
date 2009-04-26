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

package org.impalaframework.spring.service.contribution;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

import org.impalaframework.service.ServiceRegistry;
import org.impalaframework.service.ServiceRegistryReference;
import org.impalaframework.service.filter.ldap.LdapServiceReferenceFilter;
import org.impalaframework.service.reference.BasicServiceRegistryReference;
import org.springframework.util.ClassUtils;

public class ServiceRegistryListTest extends TestCase {

    private ServiceRegistryList list;
    private ServiceRegistry serviceRegistry;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        list = new ServiceRegistryList();
        serviceRegistry = createMock(ServiceRegistry.class);
        list.setServiceRegistry(serviceRegistry);
        list.setSupportedTypes(new Class[]{ List.class });
    }
    
    public void testWithList() throws Exception {
        LdapServiceReferenceFilter filter = new LdapServiceReferenceFilter("(name=myname)");
        list.setFilter(filter);
        
        List<String> service = new ArrayList<String>();
        ServiceRegistryReference ref = new BasicServiceRegistryReference(service, "mybean", "mymodule", ClassUtils.getDefaultClassLoader());
        List<ServiceRegistryReference> singletonList = Collections.singletonList(ref);
        expect(serviceRegistry.getServices(filter, null)).andReturn(singletonList);
        expect(serviceRegistry.addEventListener(list)).andReturn(true);
        
        replay(serviceRegistry);
        
        list.afterPropertiesSet();
        assertFalse(list.isEmpty());
        
        assertTrue(list.get(0) instanceof List);
        assertFalse(list.get(0) instanceof ArrayList);
        
        verify(serviceRegistry);
        
        list.remove(ref);
        assertTrue(list.isEmpty());
    }
    
    static class ValueClass {
        public void sayHello() {
            System.out.println("Hello");
        }
    }
    
}
