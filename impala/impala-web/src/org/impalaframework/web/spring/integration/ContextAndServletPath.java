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

package org.impalaframework.web.spring.integration;

/**
 * Simple data holder for context path and servlet path.
 * @author Phil Zoio
 */
public class ContextAndServletPath {

    private final String contextPath;
    
    private final String servletPath;

    public ContextAndServletPath(String contextPath, String servletPath) {
        super();
        this.contextPath = contextPath;
        this.servletPath = servletPath;
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
                + ((contextPath == null) ? 0 : contextPath.hashCode());
        result = prime * result
                + ((servletPath == null) ? 0 : servletPath.hashCode());
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
        ContextAndServletPath other = (ContextAndServletPath) obj;
        if (contextPath == null) {
            if (other.contextPath != null)
                return false;
        }
        else if (!contextPath.equals(other.contextPath))
            return false;
        if (servletPath == null) {
            if (other.servletPath != null)
                return false;
        }
        else if (!servletPath.equals(other.servletPath))
            return false;
        return true;
    }
    
}
