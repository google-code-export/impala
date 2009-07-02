package org.impalaframework.radixtree;

import java.util.Stack;

public abstract class BaseVisitor<T> implements Visitor<T> {

	private Stack<RadixTreeNode<T>> node = new Stack<RadixTreeNode<T>>();

	public void setCurrentRealNode(RadixTreeNode<T> node) {
		this.node.push(node);
	}

	public RadixTreeNode<T> getCurrentNode() {
		return node.isEmpty() ? null : node.peek();
	}

}
