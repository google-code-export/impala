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

import org.impalaframework.service.filter.ldap.LdapServiceReferenceFilter;
import org.impalaframework.service.registry.BasicServiceRegistryReference;
import org.springframework.util.ClassUtils;

import junit.framework.TestCase;

public class ServiceRegistryMonitorTest extends TestCase {

	private ServiceActivityNotifiable serviceActivityNotifiable;
	private ServiceRegistryMonitor monitor;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		monitor = new ServiceRegistryMonitor();
		serviceActivityNotifiable = createMock(ServiceActivityNotifiable.class);
		monitor.setServiceActivityNotifiable(serviceActivityNotifiable);
	}

	public void testHandleServiceAdded() {
		
		BasicServiceRegistryReference ref = new BasicServiceRegistryReference("service", "beanName", "module", null, Collections.singletonMap("name", "somevalue"), ClassUtils.getDefaultClassLoader());
		
		expect(serviceActivityNotifiable.getServiceReferenceFilter()).andReturn(new LdapServiceReferenceFilter("(name=*)"));
		serviceActivityNotifiable.add(ref);
		
		replay(serviceActivityNotifiable);
		monitor.handleReferenceAdded(ref);
		verify(serviceActivityNotifiable);
	}

	public void testTypeNotMatches() {
		
		BasicServiceRegistryReference ref = new BasicServiceRegistryReference("service", "beanName", "module", null, Collections.singletonMap("name", "somevalue"), ClassUtils.getDefaultClassLoader());
		
		expect(serviceActivityNotifiable.getServiceReferenceFilter()).andReturn(new LdapServiceReferenceFilter("(name=*)"));
		//no call to add
		
		replay(serviceActivityNotifiable);
		monitor.setImplementationTypes(new Class<?>[] {Integer.class});
		monitor.handleReferenceAdded(ref);
		verify(serviceActivityNotifiable);
	}

	public void testHandleServiceNotMatches() {
		
		BasicServiceRegistryReference ref = new BasicServiceRegistryReference("service", "beanName", "module", null, Collections.singletonMap("name", "somevalue"), ClassUtils.getDefaultClassLoader());
		
		expect(serviceActivityNotifiable.getServiceReferenceFilter()).andReturn(new LdapServiceReferenceFilter("(missing=*)"));
		
		replay(serviceActivityNotifiable);
		monitor.handleReferenceAdded(ref);
		verify(serviceActivityNotifiable);
	}

}
