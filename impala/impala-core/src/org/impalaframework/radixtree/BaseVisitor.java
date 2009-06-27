package org.impalaframework.radixtree;

public abstract class BaseVisitor<T> implements Visitor<T> {

	private RadixTreeNode<T> currentNode;

	public void setCurrentNode(RadixTreeNode<T> node) {
		this.currentNode = node;
	}

	public RadixTreeNode<T> getCurrentNode() {
		return currentNode;
	}

}
