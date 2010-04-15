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

package org.impalaframework.web.integration;

import org.springframework.util.Assert;

/**
 * Holds prefix key, used to map a request to the servlet, as well as optional
 * conetxt path and servlet path, to be used where specified to replace the
 * context path and/or servlet path specified by the container. This feature is
 * useful because it allows Impala to intercept URLs using a filter with the
 * mapping /*, and still have a servlet path specified as if the URL had been
 * mapped to the request in a more direct way.
 * 
 * @author Phil Zoio
 */
public class ModuleNameWithPath {
    
    private final String moduleName;
    
    private final String servletPath;
    
    private final String contextPath;

    public ModuleNameWithPath(String moduleName, String contextPath, String servletPath) {
        Assert.notNull(moduleName, "moduleName cannot be null");
        this.moduleName = moduleName;
        this.contextPath = contextPath;
        this.servletPath = servletPath;
    }

    public ModuleNameWithPath(String moduleName) {        
        this(moduleName, null, null);
    }

    public String getModuleName() {
        return moduleName;
    }

    public String getContextPath() {
        return contextPath;
    }
    
    public String getServletPath() {
        return servletPath;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((moduleName == null) ? 0 : moduleName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ModuleNameWithPath other = (ModuleNameWithPath) obj;
        if (moduleName == null) {
            if (other.moduleName != null)
                return false;
        }
        else if (!moduleName.equals(other.moduleName))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ModuleNameWithPath [moduleName=" + moduleName
                + ", contextPath=" + contextPath + ", servletPath="
                + servletPath + "]";
    }
    
}
