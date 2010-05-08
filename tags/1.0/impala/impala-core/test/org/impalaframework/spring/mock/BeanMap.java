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

package org.impalaframework.spring.mock;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.util.Assert;

public class BeanMap {

    private Map<String, Object> beanMap = new HashMap<String, Object>();

    public Object getBean(String name) throws BeansException {
        Assert.notNull(name);
        return beanMap.get(name);
    }

    @SuppressWarnings("unchecked")
    public Object getBean(String name, Class requiredType) throws BeansException {
        Object bean = getBean(name);
        Assert.isTrue(requiredType.isAssignableFrom(bean.getClass()), "bean must be an instance of "
                + requiredType.getClass().getName());
        return bean;
    }

    public void putBean(String beanName, Object bean) {
        beanMap.put(beanName, bean);
    }

    public boolean containsBean(String name) {
        return beanMap.containsKey(name);
    }

    public Class<?> getType(String name) throws NoSuchBeanDefinitionException {
        return getBean(name).getClass();
    }

}
