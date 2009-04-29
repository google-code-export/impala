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

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

import org.impalaframework.exception.InvalidStateException;
import org.impalaframework.service.ServiceReferenceFilter;
import org.impalaframework.service.ServiceRegistryEvent;
import org.impalaframework.service.ServiceRegistryEventListener;
import org.impalaframework.service.ServiceRegistryReference;
import org.impalaframework.service.event.ServiceAddedEvent;
import org.impalaframework.service.event.ServiceRemovedEvent;
import org.impalaframework.service.reference.BasicServiceRegistryReference;
import org.impalaframework.spring.bean.StringFactoryBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.util.ClassUtils;

public class ServiceRegistryImplTest extends TestCase {

    private ServiceRegistryImpl registry;
    private ClassLoader classLoader;
    private Class<?>[] classes;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        registry = new ServiceRegistryImpl();
        classLoader = ClassUtils.getDefaultClassLoader();
        classes = new Class[]{String.class};
    }

    public void testRegistry() throws Exception {
        assertNull(registry.getService("notregistered", classes, false));

        final ServiceRegistryReference ref = registry.addService("bean1", "module1", "some service", classLoader);
        assertEquals(1, registry.getBeanReferences("bean1").size());
        assertEquals(1, registry.getModuleReferences("module1").size());
        assertTrue(registry.getClassReferences(String.class).isEmpty());
        assertTrue(registry.hasService(ref));

        ServiceRegistryReference service = registry.getService("bean1", classes, false);
        assertEquals("some service", service.getBean());
        assertEquals("module1", service.getContributingModule());

        registry.remove(ref);
        assertNull(registry.getService("bean1", classes, false));
        assertEquals(0, registry.getBeanReferences("bean1").size());
        assertEquals(0, registry.getModuleReferences("module1").size());
        assertFalse(registry.hasService(ref));
    }
    
    public void testNoBeanNameOrExportTypes() throws Exception {

        List<Class<?>> exportTypes = new ArrayList<Class<?>>();
        exportTypes.add(String.class);
        
        registry.addService(null, "module1", "some service", exportTypes, null, classLoader);
        try {
            registry.addService(null, "module1", "some service", null, null, classLoader);
            fail();
        } catch (InvalidStateException e) {
            assertEquals("Attempted to register bean from module 'module1' with no bean name and no export types available.", e.getMessage());
        }
    }
    
    public void testRegisterByClass() throws Exception {

        List<Class<?>> exportTypes = new ArrayList<Class<?>>();
        exportTypes.add(String.class);
        exportTypes.add(Object.class);
        
        assertTrue(registry.getClassReferences(String.class).isEmpty());
        final ServiceRegistryReference ref = registry.addService("bean1", "module1", "some service", exportTypes, null, classLoader);
        assertEquals(1, registry.getClassReferences(String.class).size());
        assertEquals(1, registry.getClassReferences(Object.class).size());
        
        registry.remove(ref);
        assertEquals(0, registry.getClassReferences(String.class).size());
        assertEquals(0, registry.getClassReferences(Object.class).size());
    }
    
    public void testEvict() throws Exception {

        registry.addService("bean1", "module1", "s1", classLoader);
        registry.addService("bean2", "module1", "s2", classLoader);
        registry.addService("bean3", "module2", "s3", classLoader);
        
        registry.evictModuleServices("module1");

        assertEquals(0, registry.getBeanReferences("bean1").size());
        assertEquals(0, registry.getBeanReferences("bean2").size());
        assertEquals(1, registry.getBeanReferences("bean3").size());
        assertEquals(0, registry.getModuleReferences("module1").size());
        assertEquals(1, registry.getModuleReferences("module2").size());
    }

    public void testClassMatching() throws Exception {
        final StringFactoryBean factoryBean = new StringFactoryBean();
        factoryBean.setValue("some service");
        registry.addService("bean1", "module1", factoryBean, classLoader);

        ServiceRegistryReference service = registry.getService("bean1", classes, false);
        assertEquals(factoryBean, service.getBean());

        //must match all classes provided
        assertNull(registry.getService("bean1", new Class<?>[]{String.class, Integer.class}, false));
        assertNull(registry.getService("bean1", new Class<?>[]{Integer.class}, false));
        
        //factory bean gets special case
        assertNull(registry.getService("bean1", new Class<?>[]{FactoryBean.class}, false));
    }

    public void testGetServices() throws Exception {

        assertTrue(registry.getServices("bean1", new Class<?>[]{String.class}, false).isEmpty());
        
        final ServiceRegistryReference ref1 = registry.addService("bean1", "module1", "some service 1", null, Collections.singletonMap("service.ranking", 100), classLoader);
        assertEquals(1, registry.getServices("bean1", new Class<?>[]{String.class}, false).size());
        assertEquals(0, registry.getServices("bean1", new Class<?>[]{Integer.class}, false).size());

        final ServiceRegistryReference ref2 = registry.addService("bean1", "module2", "some service 2", null, Collections.singletonMap("service.ranking", 400), classLoader);
        List<ServiceRegistryReference> services = registry.getServices("bean1", new Class<?>[]{String.class}, false);
        assertEquals(2, services.size());
        assertEquals(ref2, services.get(0));
        
        assertEquals(0, registry.getServices("bean1", new Class<?>[]{Integer.class}, false).size());
        
        registry.remove(ref1);
        assertEquals(1, registry.getServices("bean1", new Class<?>[]{String.class}, false).size());
        
        registry.remove(ref2);
        assertTrue(registry.getServices("bean1", new Class<?>[]{String.class}, false).isEmpty());
    }
    
    public void testDuplicateBean() throws Exception {
        final ServiceRegistryReference ref1 = registry.addService("bean1", "module1", "some service", classLoader);
        final ServiceRegistryReference ref2 = registry.addService("bean1", "module2", "some service", classLoader);
        assertEquals(2, registry.getBeanReferences("bean1").size());
        registry.remove(ref1);
        assertEquals(1, registry.getBeanReferences("bean1").size());
        registry.remove(ref2);
        assertEquals(0, registry.getBeanReferences("bean1").size());
    }
    
    public void testListener() {
        TestListener listener1 = new TestListener();
        TestListener listener2 = new TestListener();
        assertTrue(registry.addEventListener(listener1));
        assertTrue(registry.addEventListener(listener2));
        
        //attempting to add listener again has no effect
        assertFalse(registry.addEventListener(listener2));

        String service1 = "some service1";
        String service2 = "some service2";
        
        registry.addService("bean1", "module1", service1, classLoader);
        final ServiceRegistryReference ref2 = registry.addService("bean2", "module2", service2, classLoader);
        registry.remove(ref2);
        
        assertEquals(3, listener1.getEvents().size());
        assertEquals(3, listener2.getEvents().size());
        assertTrue(listener1.getEvents().get(0) instanceof ServiceAddedEvent);
        assertTrue(listener2.getEvents().get(0) instanceof ServiceAddedEvent);
        assertTrue(listener1.getEvents().get(2) instanceof ServiceRemovedEvent);
        assertTrue(listener2.getEvents().get(2) instanceof ServiceRemovedEvent);

        assertTrue(registry.removeEventListener(listener1));
        listener1.reset();
        listener2.reset();

        String service3 = "some service3";
        registry.addService("bean3", "module3", service3, classLoader);
        assertEquals(0, listener1.getEvents().size());
        assertEquals(1, listener2.getEvents().size());
        
        assertFalse(registry.removeEventListener(listener1));
    }
    
    public void testGetUsingFilter() {
        String service1 = "some service1";
        String service2 = "some service2";
        
        registry.addService("bean1", "module1", service1, classLoader);
        registry.addService("bean2", "module2", service2, classLoader);
        
        assertEquals(2, registry.getServices(ServiceRegistryImpl.IDENTIFY_FILTER, null, false).size());
        
        assertEquals(0, registry.getServices(new ServiceReferenceFilter(){
            public boolean matches(ServiceRegistryReference reference) {
                return false;
            }}, null, false).size());
    }
    
    @SuppressWarnings("unchecked")
    public void testUsingExportTypesFilter() {
        List<String> service1 = new ArrayList<String>();
        List<String> service2 = new ArrayList<String>();
        
        List service1Classes = Collections.singletonList(ArrayList.class);
        List service2Classes = Collections.singletonList(List.class);
        
        registry.addService("bean1", "module1", service1, service1Classes, null, classLoader);
        registry.addService("bean2", "module2", service2, service2Classes, null, classLoader);
        
        Class[] arrayListMatch = new Class[] { ArrayList.class };
        Class[] listMatch = new Class[] { List.class };

        assertEquals(2, registry.getServices(ServiceRegistryImpl.IDENTIFY_FILTER, null, false).size());
        assertEquals(2, registry.getServices(ServiceRegistryImpl.IDENTIFY_FILTER, listMatch, false).size());
        assertEquals(2, registry.getServices(ServiceRegistryImpl.IDENTIFY_FILTER, arrayListMatch, false).size());
        
        //match exactly on export types
        assertEquals(1, registry.getServices(ServiceRegistryImpl.IDENTIFY_FILTER, arrayListMatch, true).size());
        assertEquals(1, registry.getServices(ServiceRegistryImpl.IDENTIFY_FILTER, listMatch, true).size());
    }
    
    @SuppressWarnings("unchecked")
    public void testUsingMultiFilters() {
        List<String> service1 = new ArrayList<String>();
        
        List service1Classes = new ArrayList();
        service1Classes.add(ArrayList.class);
        service1Classes.add(List.class);
        
        registry.addService("bean1", "module1", service1, service1Classes, null, classLoader);
        
        Class[] arrayListMatch = new Class[] { ArrayList.class };
        Class[] listMatch = new Class[] { List.class };
        Class[] allMatch = new Class[] { List.class, ArrayList.class };
        Class[] allMatchReverse = new Class[] { ArrayList.class, List.class };
        Class[] falseMatch = new Class[] { List.class, LinkedList.class };

        assertEquals(1, registry.getServices(ServiceRegistryImpl.IDENTIFY_FILTER, null, false).size());
        assertEquals(1, registry.getServices(ServiceRegistryImpl.IDENTIFY_FILTER, listMatch, false).size());
        assertEquals(1, registry.getServices(ServiceRegistryImpl.IDENTIFY_FILTER, arrayListMatch, false).size());
        assertEquals(1, registry.getServices(ServiceRegistryImpl.IDENTIFY_FILTER, allMatch, false).size());
        assertEquals(1, registry.getServices(ServiceRegistryImpl.IDENTIFY_FILTER, allMatchReverse, false).size());
        assertEquals(0, registry.getServices(ServiceRegistryImpl.IDENTIFY_FILTER, falseMatch, false).size());

        try {
            //true and null combo not allowed
            assertEquals(1, registry.getServices(ServiceRegistryImpl.IDENTIFY_FILTER, null, true).size());
            fail();
        }
        catch (IllegalArgumentException e) {
        }
        assertEquals(1, registry.getServices(ServiceRegistryImpl.IDENTIFY_FILTER, listMatch, true).size());
        assertEquals(1, registry.getServices(ServiceRegistryImpl.IDENTIFY_FILTER, arrayListMatch, true).size());
        assertEquals(1, registry.getServices(ServiceRegistryImpl.IDENTIFY_FILTER, allMatch, true).size());
        assertEquals(1, registry.getServices(ServiceRegistryImpl.IDENTIFY_FILTER, allMatchReverse, true).size());
        assertEquals(0, registry.getServices(ServiceRegistryImpl.IDENTIFY_FILTER, falseMatch, true).size());
    }
    
    public void testCheckClassesClassLoader() throws Exception {
        
        List<Class<?>> toCheck = new ArrayList<Class<?>>();
        toCheck.add(String.class);
        toCheck.add(Object.class);
        
        registry.checkClasses(new BasicServiceRegistryReference("service", "bean", "module", toCheck, null, classLoader));
        
        try {
            //now create CustomClassLoader
            final URLClassLoader exceptionClassLoader = new URLClassLoader(new URL[0]){

                @Override
                protected synchronized Class<?> loadClass(String arg0, boolean arg1)
                        throws ClassNotFoundException {
                    throw new ClassNotFoundException();
                }
            };
            
            registry.checkClasses(new BasicServiceRegistryReference("service", "bean", "module", toCheck, null, exceptionClassLoader));
            fail();
        } catch (InvalidStateException e) {
            System.out.println(e.getMessage());
            assertTrue(e.getMessage().startsWith("Class entry 'java.lang.String' contributed from module 'module' with bean name 'bean' could not be found using class loader"));
        }
    }
    
    public void testCheckClassesType() throws Exception {

        List<Class<?>> toCheck = new ArrayList<Class<?>>();
        toCheck.add(String.class);
        toCheck.add(Object.class);
        
        try {
            registry.checkClasses(new BasicServiceRegistryReference(new Integer(1), "bean", "module", toCheck, null, classLoader));
            fail();
        } catch (InvalidStateException e) {
            System.out.println(e.getMessage());
            assertTrue(e.getMessage().startsWith("Service class 'java.lang.Integer contributed from module 'module' with bean name 'bean' is not assignable declared export type java.lang.String"));
        }
    }
    
    public void testCheckClassesFactoryBean() {
        
        StringFactoryBean service = new StringFactoryBean();
        service.setValue("service");
        
        List<Class<?>> toCheck = new ArrayList<Class<?>>();
        toCheck.add(String.class);
        toCheck.add(Object.class);
        
        registry.checkClasses(new BasicServiceRegistryReference(service, "bean", "module", toCheck, null, classLoader));
    }
    
    public void testDeriveExports() throws Exception {
        assertTrue(registry.deriveExportTypes("service", null, null).isEmpty());
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
