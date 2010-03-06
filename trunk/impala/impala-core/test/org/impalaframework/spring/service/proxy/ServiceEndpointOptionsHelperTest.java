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

package org.impalaframework.spring.service.proxy;

import org.impalaframework.util.CollectionStringUtils;

import junit.framework.TestCase;

public class ServiceEndpointOptionsHelperTest extends TestCase {

    public void testNoExplicitValues() throws Exception {
        ServiceEndpointOptionsHelper optionsHelper = new ServiceEndpointOptionsHelper(null);
        optionsHelper.setLogWarningNoService(false);
        assertFalse(optionsHelper.isLogWarningNoService());
        
        optionsHelper.setProceedWithNoService(false);
        assertFalse(optionsHelper.isProceedWithNoService());
        
        optionsHelper.setSetContextClassLoader(false);
        assertFalse(optionsHelper.isSetContextClassLoader());
        
        optionsHelper.setRetryCount(10);
        assertEquals(10, optionsHelper.getRetryCount());
        
        optionsHelper.setRetryInterval(20);
        assertEquals(20, optionsHelper.getRetryInterval());

        optionsHelper.setLogWarningNoService(true);
        assertTrue(optionsHelper.isLogWarningNoService());
        
        optionsHelper.setProceedWithNoService(true);
        assertTrue(optionsHelper.isProceedWithNoService());
        
        optionsHelper.setSetContextClassLoader(true);
        assertTrue(optionsHelper.isSetContextClassLoader());
    }
    
    public void testSetExplicitValues() throws Exception {
        final String properties = "log.warning.no.service=false,allow.no.service=false,set.context.classloader=false," +
        		"missing.service.retry.count=2,missing.service.retry.interval=3";
        ServiceEndpointOptionsHelper optionsHelper = new ServiceEndpointOptionsHelper(CollectionStringUtils.parsePropertiesFromString(properties));
        
        optionsHelper.setLogWarningNoService(true);
        assertFalse(optionsHelper.isLogWarningNoService());
        
        optionsHelper.setProceedWithNoService(true);
        assertFalse(optionsHelper.isProceedWithNoService());
        
        optionsHelper.setSetContextClassLoader(true);
        assertFalse(optionsHelper.isSetContextClassLoader());
        
        optionsHelper.setRetryCount(10);
        assertEquals(2, optionsHelper.getRetryCount());
        
        optionsHelper.setRetryInterval(20);
        assertEquals(3, optionsHelper.getRetryInterval());
    }
    
    
}
