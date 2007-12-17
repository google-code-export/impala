package org.impalaframework.module.bootstrap;

import junit.framework.TestCase;

import org.impalaframework.module.bootstrap.StandaloneModuleLocationResolverFactoryBean;
import org.impalaframework.resolver.ClassLocationResolver;
import org.impalaframework.resolver.PropertyClassLocationResolver;

public class StandaloneModuleLocationResolverFactoryBeanTest extends TestCase {

	public final void testFactoryBean() throws Exception {
		StandaloneModuleLocationResolverFactoryBean factoryBean = new StandaloneModuleLocationResolverFactoryBean();
		assertEquals(ClassLocationResolver.class, factoryBean.getObjectType());
		assertEquals(true, factoryBean.isSingleton());
		
		factoryBean.afterPropertiesSet();
		assertTrue(factoryBean.getObject() instanceof PropertyClassLocationResolver);
	}

}
