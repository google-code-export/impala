/*
 * Copyright 2009 the original author or authors.
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

package org.impalaframework.service.filter.ldap;

import java.util.Map;

import org.impalaframework.service.ServiceReferenceFilter;
import org.impalaframework.service.ServiceRegistryEntry;

/**
 * Implementation of {@link ServiceReferenceFilter} which uses RFC 1960 String
 * for filters. Based on the same specification as OSGi. The main difference
 * currently is that case insensitive key matching is not supported.
 * 
 * See http://www.faqs.org/rfcs/rfc1960.html
 * 
 * @author Phil Zoio
 */
public class LdapServiceReferenceFilter implements ServiceReferenceFilter {
    
    private String expression;
    
    private FilterNode rootNode;

    public LdapServiceReferenceFilter(String expression) {
        super();
        this.expression = expression;
        
        FilterParser filterParser = new FilterParser(this.expression);
        rootNode = filterParser.parse();
    }

    public boolean matches(ServiceRegistryEntry entry) {
        final Map<String, ?> attributes = entry.getAttributes();
        if (attributes == null) {
            return false;
        }
        return rootNode.match(attributes);
    }

}
