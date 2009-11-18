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

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.util.Assert;

/**
 * Contains logic to qualify and unqualify attribute names, for {@link HttpServletRequest}
 * {@link HttpSession} and {@link ServletContext}
 * @author Phil Zoio
 */
public class WebAttributeQualifier {
    
    private static final String SHARED_PREFIX = "shared:";

    /**
     * Gets the attribute name qualified by application id and module name.
     * If the attribute name has already been qualified (starts with the qualified prefix), 
     * then it will not be qualified again.
     * 
     * Attributes beginning with "shared:" do not get qualified.
     * 
     * @param attributeName the name of the attribute
     * @param applicationId the application 
     * @param moduleName the name of the module
     * @return the qualified attribute name.
     */
    public String getQualifiedAttributeName(String attributeName, String applicationId, String moduleName) {
        Assert.notNull(attributeName);
        Assert.notNull(applicationId);
        Assert.notNull(moduleName);
        
        final String keyToUse;
        if (attributeName.startsWith(SHARED_PREFIX)) {
            keyToUse = attributeName.substring(SHARED_PREFIX.length());
        } else {
            keyToUse = getModuleServletContextKey(attributeName, applicationId, moduleName);
        }
        return keyToUse;
    }
    
    private String getModuleServletContextKey(String attributeName, String applicationId, String moduleName) {
        String moduleServletContextPrefix = getModuleServletContextPrefix(applicationId, moduleName);
        
        if (!attributeName.startsWith(moduleServletContextPrefix)) {
            return moduleServletContextPrefix + attributeName;
        }
        
        return attributeName;
    }

    public static String getModuleServletContextPrefix(String applicationId, String moduleName) {
        return "application_" + applicationId + "_module_" + moduleName + ":";
    }
    
}
