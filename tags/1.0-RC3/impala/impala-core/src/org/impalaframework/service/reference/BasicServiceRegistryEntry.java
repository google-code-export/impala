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

package org.impalaframework.service.reference;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.impalaframework.service.ServiceBeanReference;
import org.impalaframework.service.ServiceRegistryEntry;
import org.springframework.util.Assert;

/**
 * Default implementation of {@link ServiceRegistryEntry}. Objects of this class are immutable.
 * @author Phil Zoio
 */
public class BasicServiceRegistryEntry implements ServiceRegistryEntry {

    private final ServiceBeanReference bean;
    private final String beanName;
    private final String contributingModule;
    private final Map<String, ?> attributes;
    private final ClassLoader beanClassLoader;
    private List<Class<?>> exportTypes;

    @SuppressWarnings("unchecked")
    public BasicServiceRegistryEntry(ServiceBeanReference bean, 
            String beanName,
            String contributingModule, 
            ClassLoader classLoader) {
        this(bean, beanName, contributingModule, null, Collections.EMPTY_MAP, classLoader);
    }

    @SuppressWarnings("unchecked")
    public BasicServiceRegistryEntry(ServiceBeanReference bean, 
            String beanName,
            String contributingModule, 
            List<Class<?>> exportTypes,
            Map<String, ?> attributes, 
            ClassLoader classLoader) {
        super();
        Assert.notNull(bean, "service instance cannot be null");
        Assert.notNull(contributingModule, "contributingModule cannot be null");
        Assert.notNull(classLoader, "classLoader cannot be null");
        this.bean = bean;
        this.beanName = beanName;
        this.contributingModule = contributingModule;
        this.exportTypes = (exportTypes != null ? Collections.unmodifiableList(new LinkedList<Class<?>>(exportTypes)) : Collections.EMPTY_LIST);
        this.attributes = (attributes != null ? Collections.unmodifiableMap(new HashMap(attributes)) : Collections.EMPTY_MAP);
        this.beanClassLoader = classLoader;
    }

    public final ServiceBeanReference getServiceBeanReference() {
        return bean;
    }
    
    public List<Class<?>> getExportTypes() {
        return exportTypes;
    }

    public final String getBeanName() {
        return beanName;
    }

    public final String getContributingModule() {
        return contributingModule;
    }

    public final Map<String, ?> getAttributes() {
        return attributes;
    }

    public final ClassLoader getBeanClassLoader() {
        return beanClassLoader;
    }
    
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer(500);
        buffer.append(this.getClass().getName()).append(": ");
        buffer.append("bean = ");
        if ( this.bean!= null )
        buffer.append(this.bean.toString());
        else buffer.append("value is null"); 
        buffer.append(", ");
        buffer.append("beanName = ");
        buffer.append(this.beanName);
        buffer.append(", ");
        buffer.append("contributingModule = ");
        buffer.append(this.contributingModule);
        buffer.append(", ");
        buffer.append("attributes = ");
        if ( this.attributes!= null )
        buffer.append(this.attributes.toString());
        else buffer.append("value is null"); 
        buffer.append(", ");
        buffer.append("beanClassLoader = ");
        if ( this.beanClassLoader!= null )
        buffer.append(this.beanClassLoader.toString());
        else buffer.append("value is null"); 
        buffer.append(", ");
        buffer.append("exportTypes = ");
        if ( this.exportTypes!= null )
        buffer.append(this.exportTypes.toString());
        else buffer.append("value is null"); 
        buffer.append("\n");
        return  buffer.toString();
    }

}
