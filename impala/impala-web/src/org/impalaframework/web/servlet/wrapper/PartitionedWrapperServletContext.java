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

package org.impalaframework.web.servlet.wrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletContext;

import org.impalaframework.web.helper.WebServletUtils;
import org.impalaframework.web.servlet.qualifier.WebAttributeQualifier;
import org.springframework.util.Assert;

/**
 * Extension of {@link BaseModuleAwareWrapperServletContext}. Basically used to
 * partition the servlet context so that only writes to the
 * {@link ServletContext} attributes using module-based prefix. However, it does
 * allow you to read shared attributes, that is, those without the module-based
 * prefix.
 * @author Phil Zoio
 */
public class PartitionedWrapperServletContext extends
        BaseModuleAwareWrapperServletContext {

    public PartitionedWrapperServletContext(
            ServletContext realContext,
            String moduleName, 
            WebAttributeQualifier webAttributeQualifier, 
            ClassLoader moduleClassLoader) {
        //FIXME add application id
        super(realContext, moduleName, webAttributeQualifier, moduleClassLoader);
    }
    
    /**
     * Retrieves only the attributes which have the module specific prefix
     * for this module. Note, however, that {@link #getAttribute(String)}
     * will also get shared values, but it cannot set or remove them, 
     * or iterate through them.
     */
    @SuppressWarnings("unchecked")
    public Enumeration getAttributeNames() {

        Enumeration attributeNames = super.getAttributeNames();
        List<String> list = new ArrayList<String>();
        
        String prefix = WebServletUtils.getModuleServletContextPrefix(this.getModuleName());
        while (attributeNames.hasMoreElements()) {
            Object nextElement = attributeNames.nextElement();
            String asString = nextElement.toString();
            if (asString.startsWith(prefix)) {
                list.add(asString);
            }
        }
        
        return Collections.enumeration(list);
    };

    /**
     * Determines which key is used for writing values to the servlet context.
     * 
     */
    protected String getWriteKeyToUse(String name) {

        Assert.notNull(name);
        
        final String keyToUse;
        if (name.startsWith(SHARED_PREFIX)) {
            keyToUse = name.substring(SHARED_PREFIX.length());
        } else {
            //FIXME wire in an use DefaultWebAttributeQualifier
            keyToUse = WebServletUtils.getModuleServletContextKey(this.getModuleName(), name);
        }
        return keyToUse;
    }
    
}
