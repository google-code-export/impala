package org.impalaframework.plugin.bootstrap;

import org.impalaframework.resolver.ClassLocationResolver;
import org.impalaframework.resolver.StandaloneClassLocationResolverFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class StandaloneModuleLocationResolverFactoryBean extends StandaloneClassLocationResolverFactory implements
		InitializingBean, FactoryBean {

	private ClassLocationResolver classLocationResolver;

	public void afterPropertiesSet() throws Exception {
		classLocationResolver = getClassLocationResolver();
	}

	public Object getObject() throws Exception {
		return classLocationResolver;
	}

	public Class getObjectType() {
		return ClassLocationResolver.class;
	}

	public boolean isSingleton() {
		return true;
	}

}
