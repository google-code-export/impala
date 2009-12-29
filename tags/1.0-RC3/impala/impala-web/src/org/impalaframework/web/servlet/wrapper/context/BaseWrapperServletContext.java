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

package org.impalaframework.web.servlet.wrapper.context;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletContext;

import org.impalaframework.classloader.BaseURLClassLoader;
import org.impalaframework.classloader.NonDelegatingResourceClassLoader;
import org.impalaframework.web.servlet.qualifier.WebAttributeQualifier;

/**
 * Abstract implementation of {@link ServletContext} which overrides some methods
 * in the {@link DelegatingServletContext} superclass. Note that
 * this class does not include any special behaviour for
 * <code>getRealPath</code>. Calls to this method are delegated to the
 * wrapped <code>ServletContext</code>.
 * 
 * @author Phil Zoio
 */
public abstract class BaseWrapperServletContext extends
        DelegatingServletContext {

    private final WebAttributeQualifier webAttributeQualifier;
    private final ClassLoader moduleClassLoader;
    private final String applicationId;
    private final String moduleName;

    public BaseWrapperServletContext(ServletContext realContext, String applicationId, String moduleName, WebAttributeQualifier webAttributeQualifier, ClassLoader moduleClassLoader) {
        super(realContext);
        if (moduleClassLoader instanceof BaseURLClassLoader) {
            //use NonDelegatingResourceClassLoader in order that it only looks in locations of the moduleClassLoader, but not it's parents
            this.moduleClassLoader = new NonDelegatingResourceClassLoader((BaseURLClassLoader) moduleClassLoader);
        } else {
            this.moduleClassLoader = moduleClassLoader;
        }
        this.webAttributeQualifier = webAttributeQualifier;
        this.moduleName = moduleName;
        this.applicationId = applicationId;
    }
    
    /**
     * First attempts to find resource in module's class path. If not found,
     * calls the superclass, which results in a search to the usual
     * <code>ServletContext</code> resource directory. This allows resources
     * which would otherwise need to be placed in a servlet context folder (e.g.
     * WEB-INF) to instead be placed in the module class path. Note that only
     * the locations associated explicitly with the module's class loader are
	 * searched. Parent locations are not searched.
	 */
	@Override
	public URL getResource(String path) throws MalformedURLException {
		
		String tempPath = path;
		
		//remove the leading slash
		if (tempPath.startsWith("/")) {
			tempPath = tempPath.substring(1);
		}
		
		URL resource = moduleClassLoader.getResource(tempPath);
		if (resource != null) return resource;
		
		return super.getResource(path);
	}

	/**
	 * Exhibits same behaviour are {@link #getResource(String)}, but
	 * returns <code>InputStream</code> instead of <code>URL</code>.
	 */
	@Override
    public InputStream getResourceAsStream(String path)
    {
        try
        {
            URL url= this.getResource(path);
            if (url == null)
                return null;
            return url.openStream();
        }
        catch(Exception e)
        {
            log(e.getMessage(), e);
            return null;
        }
    }
	
    /**
     * For a given attribute name, first looks using a key constructed using the current module name 
     * as prefix. Only if not finding the attribute using this key, it searches using the
     * raw value supplied through the <code>name</code> parameter.
     */
    @Override
    public final Object getAttribute(String name) {

        String moduleKey = getWebAttributeQualifier().getQualifiedAttributeName(name, getApplicationId(), getModuleName());
        Object moduleAttribute = super.getAttribute(moduleKey);
        if (moduleAttribute != null) {
            return moduleAttribute;
        }
        
        return super.getAttribute(name);
    }
	
    /**
     * Calls the superclass's {@link #removeAttribute(String)} with the possibly modified attribute
     * name as determined by {@link #getWriteKeyToUse(String)}.
     */
    @Override
    public void removeAttribute(String name) {
        super.removeAttribute(getWriteKeyToUse(name));
    }
    
    /**
     * Calls the superclass's {@link #setAttribute(String)} with the possibly modified attribute
     * name as determined by {@link #getWriteKeyToUse(String)}.
     */
    @Override
    public void setAttribute(String name, Object value) {
        super.setAttribute(getWriteKeyToUse(name), value);
    }
    
    protected abstract String getWriteKeyToUse(String name);
    
    protected final String getApplicationId() {
        return applicationId;
    }
    
    protected final String getModuleName() {
        return moduleName;
    }
    
    protected final WebAttributeQualifier getWebAttributeQualifier() {
        return webAttributeQualifier;
    }
}
