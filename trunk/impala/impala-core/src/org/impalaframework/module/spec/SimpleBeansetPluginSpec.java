/*
 * Copyright 2007 the original author or authors.
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

package org.impalaframework.module.spec;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.impalaframework.module.beanset.BeanSetMapReader;
import org.springframework.util.Assert;

/**
 * @author Phil Zoio
 */
public class SimpleBeansetPluginSpec extends SimplePluginSpec implements BeansetPluginSpec {

	private static final long serialVersionUID = 1L;
	
	private Map<String, Set<String>> properties;
	
	@SuppressWarnings("unchecked")
	public SimpleBeansetPluginSpec(String name) {
		this(name, Collections.EMPTY_MAP);
	}
	
	public SimpleBeansetPluginSpec(String name, String overrides) {
		this(name, new BeanSetMapReader().readBeanSetSpec(overrides));
	}
	
	public SimpleBeansetPluginSpec(String name, Map<String, Set<String>> overrides) {
		super(name);
		Assert.notNull(overrides);
		this.properties = Collections.unmodifiableMap(overrides);
	}
	
	@SuppressWarnings("unchecked")
	public SimpleBeansetPluginSpec(PluginSpec parent, String name) {
		this(parent, name, Collections.EMPTY_MAP);
	}
	
	public SimpleBeansetPluginSpec(PluginSpec parent, String name, String overrides) {
		this(parent, name, new BeanSetMapReader().readBeanSetSpec(overrides));
	}
	
	public SimpleBeansetPluginSpec(PluginSpec parent, String name, Map<String, Set<String>> overrides) {
		super(parent, name);
		Assert.notNull(overrides);
		this.properties = Collections.unmodifiableMap(overrides);
	}

	@Override
	public String getType() {
		return PluginTypes.APPLICATION_WITH_BEANSETS;
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
		final SimpleBeansetPluginSpec other = (SimpleBeansetPluginSpec) obj;
		if (properties == null) {
			if (other.properties != null)
				return false;
		}
		else if (!properties.equals(other.properties))
			return false;
		return true;
	}

}
