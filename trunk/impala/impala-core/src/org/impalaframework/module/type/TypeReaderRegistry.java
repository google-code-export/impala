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

package org.impalaframework.module.type;

import java.util.Map;

import org.impalaframework.module.registry.RegistrySupport;
import org.impalaframework.module.spi.Registry;
import org.impalaframework.module.spi.TypeReader;

public class TypeReaderRegistry extends RegistrySupport implements Registry<TypeReader> {

	public TypeReader getTypeReader(String type) {
		return super.getEntry(type, TypeReader.class);
	}
	
	public void addItem(String type, TypeReader typeReader) {
		super.addItem(type, typeReader);
	}

	@SuppressWarnings("unchecked")
	public Map<String, TypeReader> getTypeReaders() {
		final Map entries = super.getEntries();
		return entries;
	}

	public void setTypeReaders(Map<String, TypeReader> typeReaders) {
		super.setEntries(typeReaders);
	}
}
