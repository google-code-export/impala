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

import junit.framework.TestCase;

public class TypeHelperTest extends TestCase {

    public void testMatch() throws Exception {
        assertTrue(TypeHelper.equalsBoolean("true", Boolean.TRUE));
        assertTrue(TypeHelper.equalsBoolean("false", Boolean.FALSE));
        assertFalse(TypeHelper.equalsBoolean("false", Boolean.TRUE));
        assertFalse(TypeHelper.equalsBoolean("true", Boolean.FALSE));

        assertFalse(TypeHelper.equalsByte("true", (byte)1));
        assertFalse(TypeHelper.equalsByte("2", (byte)1));
        assertTrue(TypeHelper.equalsByte("1", (byte)1));

        assertFalse(TypeHelper.equalsShort("true", (short)1));
        assertFalse(TypeHelper.equalsShort("2", (short)1));
        assertTrue(TypeHelper.equalsShort("1", (short)1));

        assertFalse(TypeHelper.equalsInteger("true", 1));
        assertFalse(TypeHelper.equalsInteger("2", 1));
        assertTrue(TypeHelper.equalsInteger("1", 1));

        assertFalse(TypeHelper.equalsLong("true", 1L));
        assertFalse(TypeHelper.equalsLong("2", 1L));
        assertTrue(TypeHelper.equalsLong("1", 1L));

        assertFalse(TypeHelper.equalsFloat("true", 1.0F));
        assertFalse(TypeHelper.equalsFloat("2", 1.0F));
        assertTrue(TypeHelper.equalsFloat("1", 1.0F));

        assertFalse(TypeHelper.equalsDouble("true", 1.0));
        assertFalse(TypeHelper.equalsDouble("2", 1.0));
        assertTrue(TypeHelper.equalsDouble("1", 1.0));

        assertFalse(TypeHelper.equalsCharacter("true", 'a'));
        assertFalse(TypeHelper.equalsCharacter("b", 'a'));
        assertTrue(TypeHelper.equalsCharacter("a", 'a'));
    }

}
