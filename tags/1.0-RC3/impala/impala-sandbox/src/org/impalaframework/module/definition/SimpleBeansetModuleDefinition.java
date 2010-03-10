/*
 * Copyright 2007-2010 the original author or authors.
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

package org.impalaframework.module.definition;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.beanset.BeanSetMapReader;
import org.springframework.util.Assert;

/**
 * @author Phil Zoio
 */
public class SimpleBeansetModuleDefinition extends SimpleModuleDefinition implements BeansetModuleDefinition {

	private static final long serialVersionUID = 1L;
	
	private Map<String, Set<String>> properties;
	
	@SuppressWarnings("unchecked")
	public SimpleBeansetModuleDefinition(String name) {
		this(name, Collections.EMPTY_MAP);
	}
	
	public SimpleBeansetModuleDefinition(String name, String overrides) {
		this(name, new BeanSetMapReader().readBeanSetDefinition(overrides));
	}
	
	public SimpleBeansetModuleDefinition(String name, Map<String, Set<String>> overrides) {
		super(name);
		Assert.notNull(overrides);
		this.properties = Collections.unmodifiableMap(overrides);
	}
	
	@SuppressWarnings("unchecked")
	public SimpleBeansetModuleDefinition(ModuleDefinition parent, String name) {
		this(parent, name, Collections.EMPTY_MAP);
	}
	
	public SimpleBeansetModuleDefinition(ModuleDefinition parent, String name, String overrides) {
		this(parent, name, null, overrides);
	}
	
	public SimpleBeansetModuleDefinition(ModuleDefinition parent, String name, String[] configLocations, String overrides) {
		this(parent, name, configLocations, new BeanSetMapReader().readBeanSetDefinition(overrides));
	}
	
	public SimpleBeansetModuleDefinition(ModuleDefinition parent, String name, Map<String, Set<String>> overrides) {
		this(parent, name, null, overrides);
	}

	@SuppressWarnings("unchecked")
	public SimpleBeansetModuleDefinition(ModuleDefinition parent, String name, String[] configLocations) {
		this(parent, name, configLocations, Collections.EMPTY_MAP);
	}
	
	public SimpleBeansetModuleDefinition(ModuleDefinition parent, String name, String[] configLocations, Map<String, Set<String>> overrides) {
		super(parent, name, configLocations);
		Assert.notNull(overrides);
		this.properties = Collections.unmodifiableMap(overrides);
	}

	@Override
	public String getType() {
		return "APPLICATION_WITH_BEANSETS";
	}

	public Map<String, Set<String>> getOverrides() {
		return properties;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = super.hashCode();
		result = PRIME * result + ((properties == null) ? 0 : properties.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) {
			return false;
		}
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		final SimpleBeansetModuleDefinition other = (SimpleBeansetModuleDefinition) obj;
		if (properties == null) {
			if (other.properties != null)
				return false;
		}
		else if (!properties.equals(other.properties))
			return false;
		return true;
	}

	@Override
	public String toString() {
		ToStringCallback callback = new ToStringCallback();
		ModuleDefinitionWalker.walkModuleDefinition(this, callback);
		return callback.toString();
	}

	@Override
	public void toString(StringBuffer buffer) {
		super.toString(buffer);
		buffer.append(", overrides = " + properties);
	}
	
	

}
