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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.OrderComparator;

public class RegistryContributionProcessor implements InitializingBean, ApplicationContextAware {

    private ApplicationContext applicationContext;
    
    @SuppressWarnings("unchecked")
    public void afterPropertiesSet() throws Exception {
        final Map<String,RegistryContributor> beansOfType = applicationContext.getBeansOfType(RegistryContributor.class);
        final List<RegistryContributor> values = new ArrayList<RegistryContributor>(beansOfType.values());
        sort(values);
        
        for (RegistryContributor registryContributor : values) {
            registryContributor.doContributions();
        }
    }

    @SuppressWarnings("unchecked")
    void sort(final List<RegistryContributor> values) {
        OrderComparator comparator = new OrderComparator();
        Collections.sort(values, comparator);
    }
    
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

}
