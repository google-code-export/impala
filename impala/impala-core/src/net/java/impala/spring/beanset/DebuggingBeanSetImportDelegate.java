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

package net.java.impala.spring.beanset;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Stack;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Adds import debugging capability to <code>BeanSetImportDelegate</code>
 * @author Phil Zoio
 */

@Deprecated
public class DebuggingBeanSetImportDelegate extends BeanSetImportDelegate {

	private static final Log log = LogFactory.getLog(DebuggingBeanSetImportDelegate.class);

	static ThreadLocal<Stack<BeanSetNode>> localStack = initNodeStack();

	static ThreadLocal<List<BeanSetNode>> topLevelNodes = initNodeList();

	public DebuggingBeanSetImportDelegate(Properties properties) {
		super(properties);
	}

	private static ThreadLocal<Stack<BeanSetNode>> initNodeStack() {
		ThreadLocal<Stack<BeanSetNode>> currentNode = new ThreadLocal<Stack<BeanSetNode>>();
		currentNode.set(new Stack<BeanSetNode>());
		return currentNode;
	}

	private static ThreadLocal<List<BeanSetNode>> initNodeList() {
		ThreadLocal<List<BeanSetNode>> currentNode = new ThreadLocal<List<BeanSetNode>>();
		currentNode.set(new LinkedList<BeanSetNode>());
		return currentNode;
	}

	@Override
	public void beforeRefresh(ConfigurableApplicationContext context) {
		super.beforeRefresh(context);
		localStack.get().clear();
		topLevelNodes.get().clear();
	}

	@Override
	public void afterRefresh(ConfigurableApplicationContext context) {
		log.info("Bean hierarchy:");
		String toString = toString();
		log.info(toString);
		super.afterRefresh(context);
	}

	@Override
	public void initBeanDefinitionReader(XmlBeanDefinitionReader beanDefinitionReader) {
		beanDefinitionReader.setDocumentReaderClass(DebuggingBeanSetDefinitionDocumentReader.class);
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		List<BeanSetNode> nodeList = topLevelNodes.get();

		for (BeanSetNode node : nodeList) {
			node.print(buffer, 0);
		}
		return buffer.toString();
	}

	public static ThreadLocal<Stack<BeanSetNode>> getLocalStack() {
		return localStack;
	}

	public static ThreadLocal<List<BeanSetNode>> getTopLevelNodes() {
		return topLevelNodes;
	}

}
