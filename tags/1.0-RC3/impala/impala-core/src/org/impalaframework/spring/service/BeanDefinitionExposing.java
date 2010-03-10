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

package org.impalaframework.spring.service;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

/**
 * Interface that allows implementer to expose {@link BeanDefinition} without exposing all methods of 
 * {@link BeanDefinitionRegistry}. Uses {@link BeanDefinitionRegistry#getBeanDefinition(String)}
 * 
 * @see BeanDefinitionRegistry
 * 
 * @author Phil Zoio
 */
public interface BeanDefinitionExposing {

    /**
     * Returns the {@link BeanDefinition} associated with the name. Returns null if implementor does not 
     * have access to the underlying {@link BeanDefinitionRegistry}.
     */
    public BeanDefinition getBeanDefinition(String beanName);
    
}
