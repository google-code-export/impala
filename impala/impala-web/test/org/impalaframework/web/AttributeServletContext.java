package org.impalaframework.web;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

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

public class AttributeServletContext implements ServletContext {

    private String contextPath;
    
    private Map<String,Object> attributes = new HashMap<String, Object>();
  
    private Map<String,String> initParams = new HashMap<String, String>();
      
    public AttributeServletContext() {
        super();
    }

    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    @SuppressWarnings("unchecked")
    public Enumeration getAttributeNames() {
        return Collections.enumeration(attributes.keySet());
    }

    public ServletContext getContext(String uriPath) {
        return null;
    }
    
    public String getContextPath() {
        return contextPath;
    }


    public String getInitParameter(String name) {
        return (initParams != null ? initParams.get(name) : null);
    }

    @SuppressWarnings("unchecked")
    public Enumeration getInitParameterNames() {
        return null;
    }

    public int getMajorVersion() {
        return 2;
    }

    public String getMimeType(String file) {
        return null;
    }

    public int getMinorVersion() {
        return 5;
    }

    public RequestDispatcher getNamedDispatcher(String name) {
        return null;
    }

    public String getRealPath(String path) {
        return null;
    }

    public RequestDispatcher getRequestDispatcher(String path) {
        return null;
    }

    public URL getResource(String path) throws MalformedURLException {
        return null;
    }

    public InputStream getResourceAsStream(String path) {
        return null;
    }

    @SuppressWarnings("unchecked")
    public Set getResourcePaths(String path) {
        return null;
    }

    public String getServerInfo() {
        return null;
    }

    public Servlet getServlet(String name) throws ServletException {
        return null;
    }

    public String getServletContextName() {
        return null;
    }

    public Enumeration<?> getServletNames() {
        return null;
    }

    public Enumeration<?> getServlets() {
        return null;
    }

    public void log(String message) {
    }

    public void log(Exception exception, String message) {
    }

    public void log(String message, Throwable throwable) {
    }

    public void removeAttribute(String name) {
        attributes.remove(name);
    }

    public void setAttribute(String name, Object value) {
        attributes.put(name, value);
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }
    
    public void setInitParams(Map<String, String> initParams) {
        this.initParams = initParams;
    }
    
}
