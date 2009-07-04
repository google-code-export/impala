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

package org.impalaframework.web.integration;

/**
 * Holds details of a mapping of a URL to a module for a particular request.
 * Returned by {@link RequestModuleMapper} implementations.
 * @author Phil Zoio
 */
public class RequestModuleMapping {

    /**
     * The name of the module to which the request is mapped
     */
    private final String moduleName;

    /**
     * The part of the URL which is responsible for mapping the request to the
     * module. Similar in intent to
     * {@link javax.servlet.http.HttpServletRequest#getServletPath()}
     */
    private final String path;

    public RequestModuleMapping(String moduleName, String path) {
        super();
        this.moduleName = moduleName;
        this.path = path;
    }

    public String getModuleName() {
        return moduleName;
    }

    public String getPath() {
        return path;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((moduleName == null) ? 0 : moduleName.hashCode());
        result = prime * result + ((path == null) ? 0 : path.hashCode());
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
        RequestModuleMapping other = (RequestModuleMapping) obj;
        if (moduleName == null) {
            if (other.moduleName != null)
                return false;
        }
        else if (!moduleName.equals(other.moduleName))
            return false;
        if (path == null) {
            if (other.path != null)
                return false;
        }
        else if (!path.equals(other.path))
            return false;
        return true;
    }
    
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer(500);
        buffer.append(this.getClass().getName()).append(": ");
        buffer.append("moduleName = ");
        buffer.append(this.moduleName);
        buffer.append(", ");
        buffer.append("path = ");
        buffer.append(this.path);
        buffer.append("\n");
        return  buffer.toString();
    }

}
