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

/**
 * Base is a base implementation for {@link FilterNode} which 
 * holds the key for a value.
 * 
 * @author Phil Zoio
 */
abstract class BaseNode implements FilterNode {

    private String key;

    protected BaseNode(String key) {
        super();
        this.key = key;
    }   
    
    String getKey() {
        return key;
    }
    
    protected static String wrapBrackets(String string) {
        return "(" + string + ")";
    }

    static String getEncodedValue(String storedValue) {
        StringBuffer buffer = new StringBuffer(storedValue.length() + 5);
        
        char[] chars = storedValue.toCharArray();
        int length = chars.length;
        int position = 0;
        while (position < length) {
            char c = chars[position];
            
            switch (c) {

                case ')':
                case '(':
                case '*':
                case '\\': {
                    buffer.append('\\');
                }
                default: {
                    buffer.append(c);
                    break;
                }
            }
            position++;
        }
        
        return buffer.toString();
    }
    
    public abstract String toString();
}
