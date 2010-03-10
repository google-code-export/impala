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

package org.impalaframework.spring.bean;

import junit.framework.TestCase;

public class BooleanFactoryBeanTest extends TestCase {

    private BooleanFactoryBean factoryBean;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        factoryBean = new BooleanFactoryBean();
    }

    public void testFactoryBean() throws Exception {
        assertTrue(factoryBean.isSingleton());
        assertEquals(Boolean.class, factoryBean.getObjectType());
        assertEquals(Boolean.FALSE, factoryBean.getObject());
    }
    
    public void testValue() throws Exception {
        factoryBean.setValue(true);
        assertEquals(Boolean.TRUE, factoryBean.getObject());
        factoryBean.setValue(false);
        assertEquals(Boolean.FALSE, factoryBean.getObject());
        
    }
}
