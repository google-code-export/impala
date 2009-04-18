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
 * Node representing RFC 1960 substring expression (e.g. (param=*part1*part2*))
 * 
 * @author Phil Zoio
 */
class SubstringNode extends BaseNode {

    private List<String> values;

    SubstringNode(String key, List<String> values) {
        super(key);
        this.values = values;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer(getKey() + "=");
        for (String value : values) {
            buffer.append(ItemNode.getEncodedValue(value)).append("*");
        }
        buffer.delete(buffer.length()-1, buffer.length());
        return wrapBrackets(buffer.toString());
    }

    public boolean match(Map<?, ?> data) {
        return false;
    }

}
