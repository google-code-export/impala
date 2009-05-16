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

import java.util.Map;

/**
 * Implements not !(...) expression in RFC 1960 syntax.
 * 
 * @author Phil Zoio
 */
class NotNode implements FilterNode {

    private FilterNode filterNode;

    NotNode(FilterNode filterNode) {
        super();
        this.filterNode = filterNode;
    }

    FilterNode getFilterNode() {
        return filterNode;
    }   @Override
    
    public String toString() {
        return "(!" + filterNode + ")";
    }
    
    public boolean match(Map<?, ?> data) {
        return !filterNode.match(data);
    }

}
