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

package org.impalaframework.radixtree;

public interface NodeAwareVisitor<T> extends Visitor<T> {
  
    /**
     * Used to the current real node of the node being visited
     */
    public void setCurrentRealNode(RadixTreeNode<T> node);
    
    /**
     * Returns the current (or most recent) node visited by the visitor. 
     */
    public RadixTreeNode<T> getCurrentRealNode();
    
}
