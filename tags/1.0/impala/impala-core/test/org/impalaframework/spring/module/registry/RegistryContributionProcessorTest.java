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

package org.impalaframework.spring.module.registry;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.LinkedHashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;

public class RegistryContributionProcessorTest extends TestCase {

    private RegistryContributor contributor1;
    private RegistryContributor contributor2;
    private RegistryContributionProcessor processor;
    private ApplicationContext applicationContext;
    private Map<String,RegistryContributor> contributors;
    
    
    public void setUp() {
        
        contributor1 = createMock(RegistryContributor.class);
        contributor2 = createMock(RegistryContributor.class);
        applicationContext = createMock(ApplicationContext.class);

        contributors = new LinkedHashMap<String, RegistryContributor>();
        contributors.put("contribution1", contributor1);
        contributors.put("contribution2", contributor2);
        
        processor = new RegistryContributionProcessor();
        processor.setApplicationContext(applicationContext);
    }
    
    public void testContribute() throws Exception {
        expect(applicationContext.getBeansOfType(RegistryContributor.class)).andReturn(contributors);
        contributor1.doContributions();
        contributor2.doContributions();
        
        replay(contributor1, contributor2, applicationContext);
        
        processor.afterPropertiesSet();
        
        verify(contributor1, contributor2, applicationContext);
    }   

}
