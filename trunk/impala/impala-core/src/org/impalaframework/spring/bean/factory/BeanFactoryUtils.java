/*
 * Copyright 2007-2008 the original author or authors.
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

package org.impalaframework.spring.bean.factory;

import org.springframework.context.ApplicationContext;

public class BeanFactoryUtils {

    /**
     * Finds the {@link ApplicationContext} that contains a particular bean definition, if this exists,
     * by searching through an {@link ApplicationContext}'s parent hierarchy.
     */
    public static ApplicationContext maybeFindApplicationContext(
            ApplicationContext applicationContext, 
            String beanName) {
        
        //continue looping until you find a parent bean factory which contains the given bean
        while (applicationContext != null) {
            
            if (applicationContext.containsBeanDefinition(beanName)) {
                return applicationContext;
            }
            applicationContext = applicationContext.getParent();    
        }
        
        return null;
    }

}
