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

import junit.framework.TestCase;

/**
 * Additional test added to test the {@link RadixTreeImpl#findContainedNode(String)}
 * method, not present in the original source from the radixtree project (http://radixtree.googlecode.com/).
 * @author Phil Zoio
 */
public class RadixTreeSupplementaryTest extends TestCase {

    public void testFindContainedNode() throws Exception {
        RadixTree<String> r = new RadixTreeImpl<String>();
        r.insert("AB", "1");
        r.insert("B", "2");
        r.insert("BS", "11");
        r.insert("BT1", "12");
        r.insert("BT13", "13");
        r.insert("BT14", "14");
        
        check(r, "BT11 4QY", "12");
        check(r, "BT13", "13");
        check(r, "BT134QY", "13");
        check(r, "BT1", "12");
        check(r, "BT15 4QY", "12");
        check(r, "BT14ZXX", "14");
    }

    private void check(RadixTree<String> trie, final String selection, String expected) {
        assertEquals(expected, trie.findContainedNode(selection).getValue());
    }
    
}
