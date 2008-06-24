package org.impalaframework.web.type;

import org.impalaframework.module.type.TypeReaderRegistryFactoryBean;

public class WebTypeReaderRegistryFactoryBean extends
		TypeReaderRegistryFactoryBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		getTypeReaders().putAll(WebTypeReaderRegistryFactory.getTypeReaders());
	}
	
}
