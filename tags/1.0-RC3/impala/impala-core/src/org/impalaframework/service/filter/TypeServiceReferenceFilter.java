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

package org.impalaframework.service.filter;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.impalaframework.service.ServiceReferenceFilter;
import org.impalaframework.service.ServiceRegistryEntry;

public class TypeServiceReferenceFilter implements ServiceReferenceFilter {

    private Collection<Class<?>> types;
    private boolean matchAny;
    
    public boolean matches(ServiceRegistryEntry entry) {
        Object bean = entry.getServiceBeanReference().getService();
        
        if (types == null || types.isEmpty()) {
            return false;
        }
        
        for (Class<?> type : types) {   
            if (type.isAssignableFrom(bean.getClass())) {
                if (matchAny)
                    return true;
            } else {
                if (!matchAny) {
                    return false;
                }
            }
        }
        return true;
    }

    public void setType(Class<?> type) {
        List<Class<?>> list = new LinkedList<Class<?>>();
        list.add(type);
        this.types = list;
    }

    public void setTypes(Collection<Class<?>> types) {
        this.types = types;
    }

    public void setMatchAny(boolean matchAny) {
        this.matchAny = matchAny;
    }

}
