package org.impalaframework.module.bootstrap;

import junit.framework.TestCase;

import org.impalaframework.module.bootstrap.StandaloneModuleLocationResolverFactoryBean;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.impalaframework.resolver.PropertyModuleLocationResolver;

public class StandaloneModuleLocationResolverFactoryBeanTest extends TestCase {

	public final void testFactoryBean() throws Exception {
		StandaloneModuleLocationResolverFactoryBean factoryBean = new StandaloneModuleLocationResolverFactoryBean();
		assertEquals(ModuleLocationResolver.class, factoryBean.getObjectType());
		assertEquals(true, factoryBean.isSingleton());
		
		factoryBean.afterPropertiesSet();
		assertTrue(factoryBean.getObject() instanceof PropertyModuleLocationResolver);
	}

}
