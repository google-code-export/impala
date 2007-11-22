package org.impalaframework.plugin.bootstrap;

import org.impalaframework.resolver.ClassLocationResolver;
import org.impalaframework.resolver.PropertyClassLocationResolver;

import junit.framework.TestCase;

public class StandaloneClassLocationResolverFactoryBeanTest extends TestCase {

	public final void testFactoryBean() throws Exception {
		StandaloneClassLocationResolverFactoryBean factoryBean = new StandaloneClassLocationResolverFactoryBean();
		assertEquals(ClassLocationResolver.class, factoryBean.getObjectType());
		assertEquals(true, factoryBean.isSingleton());
		
		factoryBean.afterPropertiesSet();
		assertTrue(factoryBean.getObject() instanceof PropertyClassLocationResolver);
	}

}
