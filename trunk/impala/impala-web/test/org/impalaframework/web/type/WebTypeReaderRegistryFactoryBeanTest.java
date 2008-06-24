package org.impalaframework.web.type;

import java.util.Map;

import org.impalaframework.module.type.TypeReader;
import org.impalaframework.module.type.TypeReaderRegistryFactoryBean;
import org.impalaframework.module.type.TypeReaderRegistryFactoryBeanTest;

public class WebTypeReaderRegistryFactoryBeanTest extends TypeReaderRegistryFactoryBeanTest {

	@Override
	public void testAfterPropertiesSet() throws Exception {
		super.testAfterPropertiesSet();
	}

	@Override
	public void testFactoryBean() {
		super.testFactoryBean();
	}	
	
	protected TypeReaderRegistryFactoryBean newFactoryBean() {
		return new WebTypeReaderRegistryFactoryBean();
	}

	protected Map<String, TypeReader> getTypeReaders() {
		Map<String, TypeReader> typeReaders = WebTypeReaderRegistryFactory.getTypeReaders();
		return typeReaders;
	}

}
