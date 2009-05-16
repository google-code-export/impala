package org.impalaframework.service.proxy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import org.impalaframework.config.PropertySource;
import org.impalaframework.service.ServiceRegistryEntry;
import org.impalaframework.service.reference.StaticServiceRegistryEntry;
import org.impalaframework.spring.config.ExternalDynamicPropertySource;
import org.impalaframework.util.ReflectionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.ClassUtils;

public class ProxyHelperTest extends TestCase {
    
    private ProxyHelper proxyHelper;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        proxyHelper = new ProxyHelper();
    }
    
    
    public void testInterfaces() {
        ExternalDynamicPropertySource source = new ExternalDynamicPropertySource();
        System.out.println(Arrays.asList(ReflectionUtils.findInterfaces(source)));
    }
    
    @SuppressWarnings("unchecked")
    public void testGetProxyObject() {
        final ArrayList<String> list = new ArrayList<String>();
        list.add("Phil");
        final List<String> proxyObject = (List<String>) proxyHelper.maybeGetProxy(ref(list));
        
        assertEquals("Phil", proxyObject.iterator().next());
    }

    public void testProxyEntriesFalse() {
        proxyHelper.setProxyEntries(false);
        
        Integer integer = new Integer(1);
        Integer proxyObject = (Integer) proxyHelper.maybeGetProxy(ref(integer));
        
        assertEquals(1, proxyObject.intValue());
    }
    
    public void testPartialMatch() {
        proxyHelper.setProxyInterfaces(new Class[]{ PropertySource.class, Runnable.class });
        
        final Object proxyObject = proxyHelper.maybeGetProxy(ref(new ExternalDynamicPropertySource()));
        assertTrue(proxyObject instanceof PropertySource);
        assertTrue(proxyObject instanceof Runnable);
        assertFalse(proxyObject instanceof InitializingBean);
    }
    
    public void testGetProxyObjectNoInterfaceImplemented() {
        final ClassWithNoInterface instance = new ClassWithNoInterface();
        assertSame(instance, proxyHelper.maybeGetProxy(ref(instance)));
    }
    
    private ServiceRegistryEntry ref(Object o) {
        return new StaticServiceRegistryEntry(o,"beanName","moduleName",ClassUtils.getDefaultClassLoader());
    }

    
    class ClassWithNoInterface {
    }
}
