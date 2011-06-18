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

public class CompositePropertySourceTest extends TestCase {

    private PropertySource source1;
    private PropertySource source2;
    private CompositePropertySource source;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        source1 = createMock(PropertySource.class);
        source2 = createMock(PropertySource.class);
        List<PropertySource> list = new ArrayList<PropertySource>();
        list.add(source1);
        list.add(source2);
        
        source = new CompositePropertySource(list);
    }

    public void testGetValueNull() throws Exception {
        
        expect(source1.getValue("name")).andReturn(null);
        expect(source2.getValue("name")).andReturn(null);
        
        replay(source1, source2);
        
        assertNull(source.getValue("name"));
        
        verify(source1, source2);
    }
    
    public void testGetValueFirst() throws Exception {
        
        expect(source1.getValue("name")).andReturn("value1");
        
        replay(source1, source2);
        
        assertEquals("value1", source.getValue("name"));
        
        verify(source1, source2);
    }
    
    public void testGetValueSecond() throws Exception {
        
        expect(source1.getValue("name")).andReturn(null);
        expect(source2.getValue("name")).andReturn("value2");
        
        replay(source1, source2);
        
        assertEquals("value2", source.getValue("name"));
        
        verify(source1, source2);
    }
    
}
