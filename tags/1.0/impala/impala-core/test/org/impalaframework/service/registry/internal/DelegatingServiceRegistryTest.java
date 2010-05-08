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

package org.impalaframework.service.registry.internal;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.impalaframework.exception.InvalidStateException;
import org.impalaframework.service.ServiceReferenceFilter;
import org.impalaframework.service.ServiceRegistryEntry;
import org.impalaframework.service.ServiceRegistryEvent;
import org.impalaframework.service.ServiceRegistryEventListener;
import org.impalaframework.service.StaticServiceBeanReference;
import org.impalaframework.service.event.ServiceAddedEvent;
import org.impalaframework.service.event.ServiceRemovedEvent;
import org.impalaframework.service.reference.StaticServiceRegistryEntry;
import org.impalaframework.spring.bean.StringFactoryBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.util.ClassUtils;

public class DelegatingServiceRegistryTest extends TestCase {

    private DelegatingServiceRegistry registry;
    private ClassLoader classLoader;
    private Class<?>[] classes;
    private ServiceEntryRegistryDelegate entryDelegate;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        registry = new DelegatingServiceRegistry();
        entryDelegate = (ServiceEntryRegistryDelegate) registry.getEntryRegistryDelegate();
        
        classLoader = ClassUtils.getDefaultClassLoader();
        classes = new Class[]{String.class};
    }

    public void testRegistry() throws Exception {
        assertNull(registry.getService("notregistered", classes, false));

        final ServiceRegistryEntry ref = registryaddService("bean1", "module1", "some service", classLoader);
        assertEquals(1, entryDelegate.getBeanReferences("bean1").size());
        assertEquals(1, entryDelegate.getModuleReferences("module1").size());
        assertTrue(entryDelegate.getClassReferences(String.class).isEmpty());
        assertTrue(entryDelegate.hasService(ref));

        ServiceRegistryEntry service = registry.getService("bean1", classes, false);
        assertEquals("some service", service.getServiceBeanReference().getService());
        assertEquals("module1", service.getContributingModule());

        registry.remove(ref);
        assertNull(registry.getService("bean1", classes, false));
        assertEquals(0, entryDelegate.getBeanReferences("bean1").size());
        assertEquals(0, entryDelegate.getModuleReferences("module1").size());
        assertFalse(entryDelegate.hasService(ref));
    }
    
    public void testNoBeanNameOrExportTypes() throws Exception {

        List<Class<?>> exportTypes = new ArrayList<Class<?>>();
        exportTypes.add(String.class);
        
        registryaddService(null, "module1", "some service", exportTypes, null, classLoader);
        
        //this is okay too
        registryaddService(null, "module1", "some service", null, null, classLoader);
    }
    
    public void testRegisterByClass() throws Exception {

        List<Class<?>> exportTypes = new ArrayList<Class<?>>();
        exportTypes.add(String.class);
        exportTypes.add(Object.class);
        
        assertTrue(entryDelegate.getClassReferences(String.class).isEmpty());
        final ServiceRegistryEntry ref = registryaddService("bean1", "module1", "some service", exportTypes, null, classLoader);
        assertEquals(1, entryDelegate.getClassReferences(String.class).size());
        assertEquals(1, entryDelegate.getClassReferences(Object.class).size());
        
        registry.remove(ref);
        assertEquals(0, entryDelegate.getClassReferences(String.class).size());
        assertEquals(0, entryDelegate.getClassReferences(Object.class).size());
    }
    
    public void testEvict() throws Exception {

        registryaddService("bean1", "module1", "s1", classLoader);
        registryaddService("bean2", "module1", "s2", classLoader);
        registryaddService("bean3", "module2", "s3", classLoader);
        
        registry.evictModuleServices("module1");

        assertEquals(0, entryDelegate.getBeanReferences("bean1").size());
        assertEquals(0, entryDelegate.getBeanReferences("bean2").size());
        assertEquals(1, entryDelegate.getBeanReferences("bean3").size());
        assertEquals(0, entryDelegate.getModuleReferences("module1").size());
        assertEquals(1, entryDelegate.getModuleReferences("module2").size());
    }

    public void testClassMatching() throws Exception {
        final StringFactoryBean factoryBean = new StringFactoryBean();
        factoryBean.setValue("some service");
        registryaddService("bean1", "module1", factoryBean.getObject(), classLoader);

        ServiceRegistryEntry service = registry.getService("bean1", classes, false);
        assertEquals("some service", service.getServiceBeanReference().getService());

        //must match all classes provided
        assertNull(registry.getService("bean1", new Class<?>[]{String.class, Integer.class}, false));
        assertNull(registry.getService("bean1", new Class<?>[]{Integer.class}, false));
        
        //factory bean gets special case
        assertNull(registry.getService("bean1", new Class<?>[]{FactoryBean.class}, false));
    }
    
    @SuppressWarnings("unchecked")
    public void testGetServiceByExportTypes() throws Exception {
        
        classes = new Class[]{String.class};
        
        final StringFactoryBean factoryBean = new StringFactoryBean();
        factoryBean.setValue("some service");
        registryaddService("bean1", "module1", factoryBean, classLoader);

        try {
            registry.getService(null, classes, false);
            fail();
        }
        catch (IllegalArgumentException e) {
            assertEquals("Either bean name must be not null, or export types must be non-empty", e.getMessage());
        } 
        
        try {
            registry.getService(null, new Class[0], false);
            fail();
        }
        catch (IllegalArgumentException e) {
            assertEquals("Either bean name must be not null, or export types must be non-empty", e.getMessage());
        }

        try {
            registry.getServices((String)null, classes, false);
            fail();
        }
        catch (IllegalArgumentException e) {
            assertEquals("Either bean name must be not null, or export types must be non-empty", e.getMessage());
        } 
        
        try {
            registry.getServices((String)null, new Class[0], false);
            fail();
        }
        catch (IllegalArgumentException e) {
            assertEquals("Either bean name must be not null, or export types must be non-empty", e.getMessage());
        }    
        
        //not registered under any export types
        assertNull(registry.getService("bean1", classes, true));
        assertNull(registry.getService(null, classes, true));
        
        assertTrue(registry.getServices("bean1", classes, true).isEmpty());
        assertTrue(registry.getServices((String)null, classes, true).isEmpty());
        
        //now register bean with export types
        List list = Collections.singletonList(String.class);
        registryaddService(null, "module1", factoryBean.getObject(), list, null, classLoader);
        
        assertNotNull(registry.getService(null, classes, true));
        assertNull(registry.getService("bean1", classes, true));
        
        assertFalse(registry.getServices((String)null, classes, true).isEmpty());
        assertTrue(registry.getServices("bean1", classes, true).isEmpty());
        
        registryaddService("bean2", "module1", factoryBean.getObject(), list, null, classLoader);
        assertNotNull(registry.getService("bean2", classes, true));
        assertFalse(registry.getServices("bean2", classes, true).isEmpty());
    }
    
 
    public void testMultiExportTypes() throws Exception {
        
        classes = new Class[]{ ArrayList.class, List.class };
        ServiceRegistryEntry ref = registryaddService(null, "module1", new ArrayList<String>(), Arrays.asList(classes), null, classLoader);
        
        assertTrue(registry.getServices("bean1", new Class<?>[]{ String.class }, true).isEmpty());
        assertTrue(registry.getServices("bean1", classes, true).isEmpty());
        assertFalse(registry.getServices((String)null, classes, true).isEmpty());
        
        assertNull(registry.getService("bean1", new Class<?>[]{ String.class }, true));
        assertNull(registry.getService("bean1", classes, true));
        assertNotNull(registry.getService((String)null, classes, true));
        assertFalse(registry.isPresentInExportTypes(ref, new Class<?>[]{ String.class }));
        assertFalse(registry.isPresentInExportTypes(ref, new Class<?>[]{ ArrayList.class, LinkedList.class }));
        assertTrue(registry.isPresentInExportTypes(ref, new Class<?>[]{ List.class, ArrayList.class }));
        
        registryaddService("bean2", "module1", new ArrayList<String>(), Arrays.asList(classes), null, classLoader);

        assertTrue(registry.getServices("bean2", new Class<?>[]{ String.class }, true).isEmpty());
        assertFalse(registry.getServices("bean2", classes, true).isEmpty());
        assertFalse(registry.getServices((String)null, classes, true).isEmpty());
        
        assertNull(registry.getService("bean2", new Class<?>[]{ String.class }, true));
        assertNotNull(registry.getService("bean2", classes, true));
        assertNotNull(registry.getService((String)null, classes, true));
        
        //not matching on all so empty
        classes = new Class[]{ List.class, AbstractList.class, ArrayList.class };
        
        assertTrue(registry.getServices("bean2", classes, true).isEmpty());
        assertTrue(registry.getServices((String)null, classes, true).isEmpty());
        
        assertNull(registry.getService("bean2", classes, true));
        assertNull(registry.getService((String)null, classes, true));
        
        classes = new Class[]{ List.class, LinkedList.class };
        
        //not matching on export types
        assertTrue(registry.getServices("bean2", classes, true).isEmpty());
        assertTrue(registry.getServices((String)null, classes, true).isEmpty());
        
        assertNull(registry.getService("bean2", classes, true));
        assertNull(registry.getService((String)null, classes, true));
    }
    
    
    public void testGetServices() throws Exception {

        assertTrue(registry.getServices("bean1", new Class<?>[]{String.class}, false).isEmpty());
        
        final ServiceRegistryEntry ref1 = registryaddService("bean1", "module1", "some service 1", null, Collections.singletonMap("service.ranking", 100), classLoader);
        assertEquals(1, registry.getServices("bean1", new Class<?>[]{String.class}, false).size());
        assertEquals(0, registry.getServices("bean1", new Class<?>[]{Integer.class}, false).size());

        final ServiceRegistryEntry ref2 = registryaddService("bean1", "module2", "some service 2", null, Collections.singletonMap("service.ranking", 400), classLoader);
        List<ServiceRegistryEntry> services = registry.getServices("bean1", new Class<?>[]{ String.class }, false);
        assertEquals(2, services.size());
        assertEquals(ref2, services.get(0));
        
        assertEquals(0, registry.getServices("bean1", new Class<?>[]{Integer.class}, false).size());
        
        registry.remove(ref1);
        assertEquals(1, registry.getServices("bean1", new Class<?>[]{String.class}, false).size());
        
        registry.remove(ref2);
        assertTrue(registry.getServices("bean1", new Class<?>[]{String.class}, false).isEmpty());
    }
    
    public void testDuplicateBean() throws Exception {
        final ServiceRegistryEntry ref1 = registryaddService("bean1", "module1", "some service", classLoader);
        final ServiceRegistryEntry ref2 = registryaddService("bean1", "module2", "some service", classLoader);
        assertEquals(2, entryDelegate.getBeanReferences("bean1").size());
        registry.remove(ref1);
        assertEquals(1, entryDelegate.getBeanReferences("bean1").size());
        registry.remove(ref2);
        assertEquals(0, entryDelegate.getBeanReferences("bean1").size());
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
        
        registryaddService("bean1", "module1", service1, classLoader);
        final ServiceRegistryEntry ref2 = registryaddService("bean2", "module2", service2, classLoader);
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
        registryaddService("bean3", "module3", service3, classLoader);
        assertEquals(0, listener1.getEvents().size());
        assertEquals(1, listener2.getEvents().size());
        
        assertFalse(registry.removeEventListener(listener1));
    }
    
    public void testGetUsingFilter() {
        String service1 = "some service1";
        String service2 = "some service2";
        
        registryaddService("bean1", "module1", service1, classLoader);
        registryaddService("bean2", "module2", service2, classLoader);
        
        assertEquals(2, entryDelegate.getServices(ServiceEntryRegistryDelegate.IDENTIFY_FILTER, null, false).size());
        
        assertEquals(0, registry.getServices(new ServiceReferenceFilter(){
            public boolean matches(ServiceRegistryEntry reference) {
                return false;
            }}, null, false).size());
    }
    
    @SuppressWarnings("unchecked")
    public void testUsingExportTypesFilter() {
        List<String> service1 = new ArrayList<String>();
        List<String> service2 = new ArrayList<String>();
        
        List service1Classes = Collections.singletonList(ArrayList.class);
        List service2Classes = Collections.singletonList(List.class);
        
        registryaddService("bean1", "module1", service1, service1Classes, null, classLoader);
        registryaddService("bean2", "module2", service2, service2Classes, null, classLoader);
        
        Class[] arrayListMatch = new Class[] { ArrayList.class };
        Class[] listMatch = new Class[] { List.class };

        assertEquals(2, registry.getServices(ServiceEntryRegistryDelegate.IDENTIFY_FILTER, null, false).size());
        assertEquals(2, registry.getServices(ServiceEntryRegistryDelegate.IDENTIFY_FILTER, listMatch, false).size());
        assertEquals(2, registry.getServices(ServiceEntryRegistryDelegate.IDENTIFY_FILTER, arrayListMatch, false).size());
        
        //match exactly on export types
        assertEquals(1, registry.getServices(ServiceEntryRegistryDelegate.IDENTIFY_FILTER, arrayListMatch, true).size());
        assertEquals(1, registry.getServices(ServiceEntryRegistryDelegate.IDENTIFY_FILTER, listMatch, true).size());
    }
    
    @SuppressWarnings("unchecked")
    public void testUsingMultiFilters() {
        List<String> service1 = new ArrayList<String>();
        
        List service1Classes = new ArrayList();
        service1Classes.add(ArrayList.class);
        service1Classes.add(List.class);
        
        registryaddService("bean1", "module1", service1, service1Classes, null, classLoader);
        
        Class[] arrayListMatch = new Class[] { ArrayList.class };
        Class[] listMatch = new Class[] { List.class };
        Class[] allMatch = new Class[] { List.class, ArrayList.class };
        Class[] allMatchReverse = new Class[] { ArrayList.class, List.class };
        Class[] falseMatch = new Class[] { List.class, LinkedList.class };

        assertEquals(1, registry.getServices(ServiceEntryRegistryDelegate.IDENTIFY_FILTER, null, false).size());
        assertEquals(1, registry.getServices(ServiceEntryRegistryDelegate.IDENTIFY_FILTER, listMatch, false).size());
        assertEquals(1, registry.getServices(ServiceEntryRegistryDelegate.IDENTIFY_FILTER, arrayListMatch, false).size());
        assertEquals(1, registry.getServices(ServiceEntryRegistryDelegate.IDENTIFY_FILTER, allMatch, false).size());
        assertEquals(1, registry.getServices(ServiceEntryRegistryDelegate.IDENTIFY_FILTER, allMatchReverse, false).size());
        assertEquals(0, registry.getServices(ServiceEntryRegistryDelegate.IDENTIFY_FILTER, falseMatch, false).size());

        try {
            //true and null combo not allowed
            assertEquals(1, registry.getServices(ServiceEntryRegistryDelegate.IDENTIFY_FILTER, null, true).size());
            fail();
        }
        catch (IllegalArgumentException e) {
        }
        assertEquals(1, registry.getServices(ServiceEntryRegistryDelegate.IDENTIFY_FILTER, listMatch, true).size());
        assertEquals(1, registry.getServices(ServiceEntryRegistryDelegate.IDENTIFY_FILTER, arrayListMatch, true).size());
        assertEquals(1, registry.getServices(ServiceEntryRegistryDelegate.IDENTIFY_FILTER, allMatch, true).size());
        assertEquals(1, registry.getServices(ServiceEntryRegistryDelegate.IDENTIFY_FILTER, allMatchReverse, true).size());
        assertEquals(0, registry.getServices(ServiceEntryRegistryDelegate.IDENTIFY_FILTER, falseMatch, true).size());
    }
    
    public void testCheckClassesClassLoader() throws Exception {
        
        List<Class<?>> toCheck = new ArrayList<Class<?>>();
        toCheck.add(String.class);
        toCheck.add(Object.class);
        
        entryDelegate.checkClasses(new StaticServiceRegistryEntry("service", "bean", "module", toCheck, null, classLoader));
        
        try {
            //now create CustomClassLoader
            final URLClassLoader exceptionClassLoader = new URLClassLoader(new URL[0]){

                @Override
                protected synchronized Class<?> loadClass(String arg0, boolean arg1)
                        throws ClassNotFoundException {
                    throw new ClassNotFoundException();
                }
            };
            
            entryDelegate.checkClasses(new StaticServiceRegistryEntry("service", "bean", "module", toCheck, null, exceptionClassLoader));
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
            entryDelegate.checkClasses(new StaticServiceRegistryEntry(new Integer(1), "bean", "module", toCheck, null, classLoader));
            fail();
        } catch (InvalidStateException e) {
            System.out.println(e.getMessage());
            assertTrue(e.getMessage().startsWith("Service class 'java.lang.Integer contributed from module 'module' with bean name 'bean' is not assignable declared export type java.lang.String"));
        }
    }
    
    public void testCheckClassesFactoryBean() throws Exception {
        
        StringFactoryBean service = new StringFactoryBean();
        service.setValue("service");
        
        List<Class<?>> toCheck = new ArrayList<Class<?>>();
        toCheck.add(String.class);
        toCheck.add(Object.class);
        
        entryDelegate.checkClasses(new StaticServiceRegistryEntry(service.getObject(), "bean", "module", toCheck, null, classLoader));
    }
    
    public void testDeriveExports() throws Exception {
        assertTrue(entryDelegate.deriveExportTypes("service", null, null).isEmpty());
    }
    
    private ServiceRegistryEntry registryaddService(String beanName,
            String moduleName, Object service, ClassLoader classLoader) {
        return registry.addService(beanName, moduleName, new StaticServiceBeanReference(service), classLoader);
    }
    
    @SuppressWarnings("unchecked")
    private ServiceRegistryEntry registryaddService(String beanName,
            String moduleName, Object service, List<Class<?>> exportTypes, Map attributes, ClassLoader classLoader) {
        return registry.addService(beanName, moduleName, new StaticServiceBeanReference(service), exportTypes, attributes, classLoader);
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
