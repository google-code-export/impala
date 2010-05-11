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

package org.impalaframework.spring.service.proxy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.service.reference.BasicServiceRegistryEntry;
import org.springframework.beans.factory.BeanNameAware;

/**
 * The <code>ContributionProxyFactoryBean</code> works under the assumption that the service was exported against a named key.
 * By default, the key is assumed to be the same name as the {@link ContributionProxyFactoryBean}'s bean name.
 * However, this can be overridden.
 * 
 * The service is retrieved using dynamic lookup following each invocation on the proxy.
 * 
 * @see BasicServiceRegistryEntry
 * @author Phil Zoio
 * @deprecated use {@link NamedServiceProxyFactoryBean} instead
 */
public class ContributionProxyFactoryBean extends NamedServiceProxyFactoryBean implements BeanNameAware {

    Log logger = LogFactory.getLog(ContributionProxyFactoryBean.class);
    
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("*************** WARNING ***************");
        String message = "You are using " + ContributionProxyFactoryBean.class.getName() 
                + " for bean " + getBeanName()
                + ". This class is deprecated and will be removed in the next release. Use "
                + NamedServiceProxyFactoryBean.class.getName() 
                + " or the 'import' element from the Impala 'service' namespace";
        System.out.println(message);
        logger.warn(message);
        System.out.println("*************** WARNING ***************");
        super.afterPropertiesSet();
    }

    public void setProxyInterfaces(Class<?>[] proxyInterfaces) {
        super.setProxyTypes(proxyInterfaces);
    }

    public void setExportName(String exportedBeanName) {
        super.setExportName(exportedBeanName);
    }
    
}
