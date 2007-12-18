package org.impalaframework.module.bootstrap;

import org.impalaframework.resolver.ModuleLocationResolver;
import org.impalaframework.resolver.StandaloneModuleLocationResolverFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class StandaloneModuleLocationResolverFactoryBean extends StandaloneModuleLocationResolverFactory implements
		InitializingBean, FactoryBean {

	private ModuleLocationResolver moduleLocationResolver;

	public void afterPropertiesSet() throws Exception {
		moduleLocationResolver = getClassLocationResolver();
	}

	public Object getObject() throws Exception {
		return moduleLocationResolver;
	}

	public Class getObjectType() {
		return ModuleLocationResolver.class;
	}

	public boolean isSingleton() {
		return true;
	}

}
