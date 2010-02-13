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

package org.impalaframework.web.servlet.qualifier;

import java.util.Enumeration;

public interface WebAttributeQualifier {
    
    /**
     * "Well-known" attribute against which the module qualifier can be stored in the request for use elsewhere
     */
    String MODULE_QUALIFIER_PREFIX = WebAttributeQualifier.class.getName() + "MODULE_QUALIFIER_PREFIX";

    /**
     * Gets the attribute name qualified by application id and module name.
     * 
     * @param attributeName the name of the attribute
     * @param applicationId the application id
     * @param moduleName the name of the module
     * @return the qualified attribute name.
     */
    String getQualifiedAttributeName(String attributeName, String applicationId, String moduleName);
    
    /**
     * Filters the attribute names to those beginning with the prefix as returned by {@link #getQualifierPrefix(String, String)}
     * @param attributeNames the input attribute {@link Enumeration}
     * @param applicationId the application 
     * @param moduleName the name of the module
     * @return a filtered attribute {@link Enumeration}
     */
    Enumeration<String> filterAttributeNames(Enumeration<String> attributeNames, String applicationId, String moduleName);
    
    /**
     * Returns the prefix used to qualify attributes, based on the application and module
     * @param applicationId the application id
     * @param moduleName the name of the module
     * @return the qualifier prefix.
     */
    String getQualifierPrefix(String applicationId, String moduleName);

}
