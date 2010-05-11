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

import javax.servlet.Servlet;

import org.impalaframework.exception.ConfigurationException;
import org.springframework.util.Assert;

/**
 * <code>FactoryBean</code> whose purpose is solely to create an instance of <code>InternalFrameworkIntegrationServlet</code>
 *
 * @author Phil Zoio
 */
public class InternalFrameworkIntegrationServletFactoryBean extends
        ServletFactoryBean {

    private Servlet delegateServlet;
    
    public Class<?> getObjectType() {
        return InternalFrameworkIntegrationServlet.class;
    }
    
    @Override
    protected void initServletProperties(Servlet servlet) {
        super.initServletProperties(servlet);
        Assert.notNull(delegateServlet, "delegateServlet cannot be null");
        
        if (!(servlet instanceof InternalFrameworkIntegrationServlet)) {
            throw new ConfigurationException(servlet + " must be an instanceof " + InternalFrameworkIntegrationServlet.class.getName());
        }
        
        InternalFrameworkIntegrationServlet integrationServlet = (InternalFrameworkIntegrationServlet) servlet;
        integrationServlet.setDelegateServlet(delegateServlet);
    }
    
    /* *************** Injected setters ***************** */

    public void setDelegateServlet(Servlet servlet) {
        this.delegateServlet = servlet;
    }

}
