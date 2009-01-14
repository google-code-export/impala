package org.impalaframework.service.contribution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import junit.framework.TestCase;

import org.impalaframework.config.PropertySource;
import org.impalaframework.service.contribution.ServiceRegistryMap;
import org.impalaframework.spring.config.ExternalDynamicPropertySource;
import org.impalaframework.util.ReflectionUtils;
import org.springframework.beans.factory.InitializingBean;

public class ServiceRegistryMapTest extends TestCase {
	
	public void testInterfaces() {
		ExternalDynamicPropertySource source = new ExternalDynamicPropertySource();
		System.out.println(Arrays.asList(ReflectionUtils.findInterfaces(source)));
	}
	
	@SuppressWarnings("unchecked")
	public void testGetProxyObject() {
		ServiceRegistryMap<String, List<?>> map = new ServiceRegistryMap<String, List<?>>();
		final ArrayList<String> list = new ArrayList<String>();
		list.add("Phil");
		final List<String> proxyObject = (List<String>) map.maybeGetProxy(list);
		
		assertEquals("Phil", proxyObject.iterator().next());
	}
	
	public void testProxyEntriesFalse() {
		ServiceRegistryMap<String, Integer> map = new ServiceRegistryMap<String, Integer>();
		map.setProxyEntries(false);
		
		Integer integer = new Integer(1);
		Integer proxyObject = (Integer) map.maybeGetProxy(integer);
		
		assertEquals(1, proxyObject.intValue());
	}
	
	public void testPartialMatch() {
		ServiceRegistryMap<String, ExternalDynamicPropertySource> map = new ServiceRegistryMap<String, ExternalDynamicPropertySource>();
		map.setProxyInterfaces(new Class[]{ PropertySource.class, Runnable.class });
		
		final Object proxyObject = map.maybeGetProxy(new ExternalDynamicPropertySource());
		assertTrue(proxyObject instanceof PropertySource);
		assertTrue(proxyObject instanceof Runnable);
		assertFalse(proxyObject instanceof InitializingBean);
	}
	
	public void testGetProxyObjectNoInterfaceImplemented() {
		ServiceRegistryMap<String, ClassWithNoInterface> map = new ServiceRegistryMap<String, ClassWithNoInterface>();
		final ClassWithNoInterface instance = new ClassWithNoInterface();
		assertSame(instance, map.maybeGetProxy(instance));
	}
	
	class ClassWithNoInterface {
	}
	
}
