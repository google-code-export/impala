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

package org.impalaframework.plugin.spec;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * @author Phil Zoio
 */
public class SimpleParentSpec implements ParentSpec {

	static final Logger logger = LoggerFactory.getLogger(SimpleParentSpec.class);
	
	private static final long serialVersionUID = 1L;

	private ChildSpecContainer childContainer;
	
	private List<String> parentContextLocations;

	public SimpleParentSpec(String parentContextLocation) {
		this(new String[]{ parentContextLocation });
	}
	
	public SimpleParentSpec(String[] parentContextLocations) {
		super();
		Assert.notNull(parentContextLocations);
		this.parentContextLocations = new ArrayList<String>();
		for (int i = 0; i < parentContextLocations.length; i++) {
			Assert.notNull(parentContextLocations[i]);
			this.parentContextLocations.add(parentContextLocations[i]);
		}
		this.childContainer = new ChildSpecContainerImpl();
	}
	
	public SimpleParentSpec(List<String> parentContextLocations) {
		super();
		Assert.notNull(parentContextLocations);
		this.parentContextLocations = new ArrayList<String>(parentContextLocations);
		this.childContainer = new ChildSpecContainerImpl();
	}
	
	public String getName() {
		return ParentSpec.NAME;
	}

	public PluginSpec getParent() {
		//by definition Parent does not have a parent of its own
		return null;
	}
	
	public PluginSpec findPlugin(String pluginName, boolean exactMatch) {
		return PluginSpecUtils.findPlugin(pluginName, this, exactMatch);
	}
	
	public void setParent(PluginSpec parent) {
	}

	public Collection<String> getPluginNames() {
		return childContainer.getPluginNames();
	}

	public PluginSpec getPlugin(String pluginName) {
		return childContainer.getPlugin(pluginName);
	}

	public Collection<PluginSpec> getPlugins() {
		return childContainer.getPlugins();
	}

	public boolean hasPlugin(String pluginName) {
		return getPlugin(pluginName) != null;
	}

	public void add(PluginSpec pluginSpec) {
		childContainer.add(pluginSpec);
	}

	public PluginSpec remove(String pluginName) {
		return childContainer.remove(pluginName);
	}

	public List<String> getContextLocations() {
		return Collections.unmodifiableList(parentContextLocations);
	}

	public String getType() {
		return PluginTypes.ROOT;
	}
	
	public boolean containsAll(ParentSpec alternative) {
		if (alternative == null)
			return false;

		final List<String> alternativeLocations = alternative.getContextLocations();

		// check that each of the alternatives are contained in
		// parentContextLocations
		for (String alt : alternativeLocations) {
			if (!parentContextLocations.contains(alt)) {
				return false;
			}
		}

		return true;
	}

	public void addContextLocations(ParentSpec alternative) {
		List<String> contextLocations = alternative.getContextLocations();
		for (String location : contextLocations) {
			if (!parentContextLocations.contains(location)){
				parentContextLocations.add(location);
			}
		}
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((parentContextLocations == null) ? 0 : parentContextLocations.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final SimpleParentSpec other = (SimpleParentSpec) obj;
		if (parentContextLocations == null) {
			if (other.parentContextLocations != null)
				return false;
		}
		else if (!parentContextLocations.equals(other.parentContextLocations))
			return false;
		return true;
	}



}
