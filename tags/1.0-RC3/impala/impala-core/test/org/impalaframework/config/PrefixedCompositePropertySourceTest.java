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

package org.impalaframework.config;

import static org.easymock.EasyMock.*;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class PrefixedCompositePropertySourceTest extends TestCase {

    private PropertySource source1;
    private PrefixedCompositePropertySource source;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        source1 = createMock(PropertySource.class);
        List<PropertySource> list = new ArrayList<PropertySource>();
        list.add(source1);
        
        source = new PrefixedCompositePropertySource("impala.", list);
    }

    public void testGetValueNull() throws Exception {

        expect(source1.getValue("impala.name")).andReturn(null);
        expect(source1.getValue("name")).andReturn(null);
        
        replay(source1);
        
        assertNull(source.getValue("name"));
        
        verify(source1);
    }

    public void testNameAlreadyHasPrefix() throws Exception {

        //don't call getValue("impala.impala.name");
        expect(source1.getValue("impala.name")).andReturn(null);
        replay(source1);
        
        assertNull(source.getValue("impala.name"));
        
        verify(source1);
    }
    
    public void testGetValueImpalaNull() throws Exception {

        expect(source1.getValue("impala.name")).andReturn(null);
        expect(source1.getValue("name")).andReturn("value");
        
        replay(source1);
        
        assertEquals("value", source.getValue("name"));
        
        verify(source1);
    }
    
    public void testGetValueImpalaNotNull() throws Exception {

        expect(source1.getValue("impala.name")).andReturn("impala.value");
        
        replay(source1);
        
        assertEquals("impala.value", source.getValue("name"));
        
        verify(source1);
    }
}
