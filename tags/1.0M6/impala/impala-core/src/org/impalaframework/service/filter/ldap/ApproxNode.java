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
 * Implements ~ operator. Similar to equals in matching except that it provides
 * its own implementation of equals, which ignores whitespace and case
 * differences
 * 
 * @author Phil Zoio
 */
class ApproxNode extends EqualsNode {

    ApproxNode(String key, String value) {
        super(key, value);
    }

    @Override
    public String toString() {
        return wrapBrackets(getKey() + "~=" + getEncodedValue());
    }

    @Override
    protected boolean matchString(String external) {
        final String value = getValue();
        
        String canonicalValue = approx(value);
        String canonicalExternal = approx(external);
        
        return canonicalValue.equals(canonicalExternal);
    }

    static String approx(String value) {
        boolean modified = false;
        value = value.toLowerCase();
        char[] chars = value.toCharArray();
        int position = 0;

        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (Character.isWhitespace(c)) {
                modified = true;
                continue;
            }
            chars[position] = c;
            position++;
        }
        return (modified? new String(chars, 0, position) : value);
    }

}
