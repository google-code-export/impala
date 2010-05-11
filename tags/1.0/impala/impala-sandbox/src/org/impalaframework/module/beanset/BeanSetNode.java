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

package org.impalaframework.module.beanset;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.util.Assert;

/**
 * Used by <code>DebuggingBeanSetImportDelegate</code> to track the nodes
 * names in an application context
 * @author Phil Zoio
 */
public class BeanSetNode {

	private static String lineSeparator = System.getProperty("line.separator");

	private String resource;

	private String beanSetName;

	private Set<BeanSetNode> children = new LinkedHashSet<BeanSetNode>();

	public BeanSetNode(String resource, String beanSetName) {
		Assert.notNull(resource);
		this.resource = resource;
		this.beanSetName = beanSetName;
	}

	public String getResource() {
		return resource;
	}

	@Override
	public String toString() {
		print(new StringBuffer(), 0);
		return resource;
	}

	public void addChild(BeanSetNode child) {
		this.children.add(child);
	}

	public BeanSetNode getChildNode(String location) {
		for (BeanSetNode node : children) {
			if (node.getResource().equals(location)) {
				return node;
			}
		}
		return new BeanSetNode(location, null);
	}

	public void print(StringBuffer buffer, int indent) {
		for (int i = 0; i < indent; i++) {
			buffer.append(" ");
		}

		buffer.append(resource);

		if (beanSetName != null) {
			buffer.append(" [").append(beanSetName).append("]");
		}

		buffer.append(lineSeparator);

		for (BeanSetNode child : children) {
			child.print(buffer, indent + 2);
		}
	}

	public Set<BeanSetNode> getChildren() {
		return Collections.unmodifiableSet(children);
	}
	
	

}
