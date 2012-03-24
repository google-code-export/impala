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

package org.impalaframework.util;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

/**
 * @author Phil Zoio
 */
public class MemoryUtilsTest extends TestCase {

    public void testUsedMemory() {

        List<Object> list = new ArrayList<Object>();

        for (int i = 0; i < 500000; i++) {
            list.add(new Object());
        }
        MemoryUtils.printMemoryInfo();
        assert (MemoryUtils.usedMemory() > 0);
        assert (MemoryUtils.maxMemory(Runtime.getRuntime()) > 0);
        assert (MemoryUtils.usedMemory(Runtime.getRuntime()) > 0);

    }

}
