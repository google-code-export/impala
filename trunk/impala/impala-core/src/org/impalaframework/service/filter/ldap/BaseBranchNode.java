/*
 * Copyright 2009 the original author or authors.
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

package org.impalaframework.service.filter.ldap;

import java.util.LinkedList;
import java.util.List;

/**
 * Base class for RFC 1960 branch node (including and and or operators)
 * 
 * @author Phil Zoio
 */
abstract class BaseBranchNode implements BranchNode {

    private List<FilterNode> children = new LinkedList<FilterNode>();
    
    protected BaseBranchNode(List<FilterNode> children) {
        super();
        this.children = children;
    }

    public List<FilterNode> getChildren() {
        return children;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        final List<FilterNode> children = getChildren();
        for (FilterNode filterNode : children) {
            buffer.append(filterNode.toString());
        }
        return buffer.toString();
    }

}
