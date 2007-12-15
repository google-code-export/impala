package org.impalaframework.plugin.bootstrap;

import junit.framework.TestCase;

import org.impalaframework.resolver.ClassLocationResolver;
import org.impalaframework.resolver.PropertyClassLocationResolver;

public class StandaloneClassLocationResolverFactoryBeanTest extends TestCase {

	public final void testFactoryBean() throws Exception {
		StandaloneClassLocationResolverFactoryBean factoryBean = new StandaloneClassLocationResolverFactoryBean();
		assertEquals(ClassLocationResolver.class, factoryBean.getObjectType());
		assertEquals(true, factoryBean.isSingleton());
		
		factoryBean.afterPropertiesSet();
		assertTrue(factoryBean.getObject() instanceof PropertyClassLocationResolver);
	}

}
