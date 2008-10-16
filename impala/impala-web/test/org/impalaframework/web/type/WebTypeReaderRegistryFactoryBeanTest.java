/*
 * Copyright 2007-2008 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.impalaframework.web.type;

import java.util.Map;

import org.impalaframework.module.TypeReader;
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
