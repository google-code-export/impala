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

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Stack;

import org.springframework.beans.factory.xml.XmlReaderContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Adds debugging capability to <code>BeanSetBeanDefinitionDocumentReader</code>
 * @author Phil Zoio
 */
public class RecordingImportingBeanDefinitionDocumentReader extends ImportingBeanDefinitionDocumentReader {
	
	private Stack<BeanSetNode> localStack;

	private List<BeanSetNode> topLevelNodes;

	public RecordingImportingBeanDefinitionDocumentReader(Properties overrides) {
		super(overrides);
		localStack = new Stack<BeanSetNode>();
		topLevelNodes = new LinkedList<BeanSetNode>();
	}

	@Override
	protected void doImportBeanDefinitionResource(String fileName, String beanSetName, Element element) {
		localStack.peek().addChild(new BeanSetNode(fileName, beanSetName));
		super.doImportBeanDefinitionResource(fileName, beanSetName, element);
	}

	@Override
	protected void doRegisterBeanDefinitions(String fileName, Document document, XmlReaderContext readerContext) {

		BeanSetNode topNode = localStack.empty() ? null : localStack.peek();
		BeanSetNode nextNode = null;
		if (topNode != null) {
			nextNode = topNode.getChildNode(fileName);
		}
		if (nextNode == null) {
			nextNode = new BeanSetNode(fileName, null);
		}

		localStack.push(nextNode);
		super.doRegisterBeanDefinitions(fileName, document, readerContext);

		if (localStack.size() == 1) {
			topLevelNodes.add(localStack.peek());
		}
		localStack.pop();
	}
	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		List<BeanSetNode> nodeList = topLevelNodes;

		for (BeanSetNode node : nodeList) {
			node.print(buffer, 0);
		}
		return buffer.toString();
	}

	protected List<BeanSetNode> getTopLevelNodes() {
		return topLevelNodes;
	}

}
