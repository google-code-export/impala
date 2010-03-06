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

package org.impalaframework.file.handler;

import java.io.File;

import junit.framework.TestCase;

/**
 * @author Phil Zoio
 */
public class DefaultClassFilterTest extends TestCase {

    public void testAccept() {
        DefaultClassFilter test = new DefaultClassFilter();
        check(test, "AClass.class", true);
        check(test, "AClass.txt", false);
        check(test, "AClass$WithInnerClass.class", false);
        check(test, "apackage/AClass.class", true);
        //a directory
        check(test, "src", true);
        check(test, ".hidden", false);
    }

    private void check(DefaultClassFilter test, String fileName, boolean expected) {
        assertTrue(test.accept(new File(fileName)) == expected);
    }

}
