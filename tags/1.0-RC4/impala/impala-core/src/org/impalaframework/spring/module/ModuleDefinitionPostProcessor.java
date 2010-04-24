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

package org.impalaframework.spring.module;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.definition.ModuleDefinitionAware;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.Assert;

/**
 * {@link BeanPostProcessor} implementation which calls
 * {@link ModuleDefinitionAware#setModuleDefinition(ModuleDefinition)} for any
 * bean which implements the {@link ModuleDefinitionAware} interface.
 * 
 * @author Phil Zoio
 */
public class ModuleDefinitionPostProcessor implements BeanPostProcessor {

    private final ModuleDefinition moduleDefinition;

    public ModuleDefinitionPostProcessor(ModuleDefinition moduleDefinition) {
        Assert.notNull(moduleDefinition);
        this.moduleDefinition = moduleDefinition;
    }

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof ModuleDefinitionAware) {
            ModuleDefinitionAware psa = (ModuleDefinitionAware) bean;
            psa.setModuleDefinition(moduleDefinition);
        }
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String name) {
        return bean;
    }

}
