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

import java.util.List;
import java.util.Map;

/**
 * Node representing RFC 1960 and operation (|)
 * 
 * @author Phil Zoio
 */
class OrNode extends BaseBranchNode {

    public OrNode(List<FilterNode> list) {
        super(list);
    }
    

    @Override
    public String toString() {
        return "(|" + super.toString() + ")";
    }

    public boolean match(Map<?, ?> data) {
        final List<FilterNode> children = getChildren();
        for (FilterNode filterNode : children) {
            final boolean nodeMatches = filterNode.match(data);
            if (nodeMatches) return true;
        }
        
        return false;
    }

}
