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

package org.impalaframework.service.registry.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

import org.impalaframework.service.ServiceReferenceFilter;
import org.impalaframework.service.ServiceRegistryReference;
import org.impalaframework.service.registry.event.ServiceAddedEvent;
import org.impalaframework.service.registry.event.ServiceRegistryEvent;
import org.impalaframework.service.registry.event.ServiceRegistryEventListener;
import org.impalaframework.service.registry.event.ServiceRemovedEvent;
import org.impalaframework.service.registry.internal.ServiceRegistryImpl;
import org.springframework.util.ClassUtils;

public class ServiceRegistryImplTest extends TestCase {

	private ServiceRegistryImpl registry;
	private ClassLoader classLoader;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		registry = new ServiceRegistryImpl();
		classLoader = ClassUtils.getDefaultClassLoader();
	}

	public void testRegistry() throws Exception {
		assertNull(registry.getService("notregistered"));
		assertNull(registry.getService("notregistered", String.class));

		registry.addService("bean1", "module1", "some service", classLoader);

		ServiceRegistryReference service = registry.getService("bean1");
		assertEquals("some service", service.getBean());
		assertEquals("module1", service.getContributingModule());
		assertSame(service, registry.getService("bean1", String.class));
		assertSame(service, registry.getService("bean1", CharSequence.class));

		registry.remove("some service");
		assertNull(registry.getService("bean1"));
	}
	
	public void testListener() {
		TestListener listener1 = new TestListener();
		TestListener listener2 = new TestListener();
		registry.addEventListener(listener1);
		registry.addEventListener(listener2);

		String service1 = "some service1";
		String service2 = "some service2";
		
		registry.addService("bean1", "module1", service1, classLoader);
		registry.addService("bean2", "module2", service2, classLoader);
		registry.remove(service2);
		
		assertEquals(3, listener1.getEvents().size());
		assertEquals(3, listener2.getEvents().size());
		assertTrue(listener1.getEvents().get(0) instanceof ServiceAddedEvent);
		assertTrue(listener2.getEvents().get(0) instanceof ServiceAddedEvent);
		assertTrue(listener1.getEvents().get(2) instanceof ServiceRemovedEvent);
		assertTrue(listener2.getEvents().get(2) instanceof ServiceRemovedEvent);
	}

	public void testTaggedListener() {
		TestListener listener1 = new TestListener();
		TestListener listener2 = new TestListener();
		registry.addEventListener("tag1", listener1);
		registry.addEventListener("tag1", listener2);
	
		String service1 = "some service1";
		String service2 = "some service2";
		
		registry.addService("bean1", "module1", service1, Collections.singletonList("tag1"), classLoader);
		registry.addService("bean2", "module2", service2, Collections.singletonList("tag1"), classLoader);
		registry.remove(service2);
		
		assertEquals(3, listener1.getEvents().size());
		assertEquals(3, listener2.getEvents().size());
		assertTrue(listener1.getEvents().get(0) instanceof ServiceAddedEvent);
		assertTrue(listener2.getEvents().get(0) instanceof ServiceAddedEvent);
		assertTrue(listener1.getEvents().get(2) instanceof ServiceRemovedEvent);
		assertTrue(listener2.getEvents().get(2) instanceof ServiceRemovedEvent);		
	}
	
	public void testGetUsingFilter() {
		String service1 = "some service1";
		String service2 = "some service2";
		
		registry.addService("bean1", "module1", service1, Collections.singletonList("tag1"), classLoader);
		registry.addService("bean2", "module2", service2, Collections.singletonList("tag1"), classLoader);
		
		assertEquals(2, registry.getServices(new ServiceReferenceFilter(){
			public boolean matches(ServiceRegistryReference reference) {
				return true;
			}}).size());
		
		assertEquals(0, registry.getServices(new ServiceReferenceFilter(){
			public boolean matches(ServiceRegistryReference reference) {
				return false;
			}}).size());
	}
	
}

class TestListener implements ServiceRegistryEventListener {

	private List<ServiceRegistryEvent> events = new ArrayList<ServiceRegistryEvent>();

	public void handleServiceRegistryEvent(ServiceRegistryEvent event) {
		events.add(event);
	}

	public void reset() {
		events.clear();
	}

	public List<ServiceRegistryEvent> getEvents() {
		return events;
	}

}
