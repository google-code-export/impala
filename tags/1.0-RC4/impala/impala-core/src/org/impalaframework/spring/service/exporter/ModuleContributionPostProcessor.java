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

package org.impalaframework.spring.service.exporter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;


/**
 * See {@link NamedServiceAutoExportPostProcessor}
 * 
 * @author Phil Zoio
 * @deprecated Use {@link NamedServiceAutoExportPostProcessor} instead
 */
@Deprecated 
public class ModuleContributionPostProcessor extends NamedServiceAutoExportPostProcessor {
    
    Log logger = LogFactory.getLog(ModuleContributionPostProcessor.class);
    
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName)
            throws BeansException {

        System.out.println("*************** WARNING ***************");
        String message = "You are using " + ModuleContributionPostProcessor.class.getName() 
                + ". This class is deprecated and will be removed in the next release. Use "
                + NamedServiceAutoExportPostProcessor.class.getName() 
                + " or the 'auto-export' element from the Impala 'service' namespace";
        System.out.println(message);
        logger.warn(message);
        System.out.println("*************** WARNING ***************");
        
        return super.postProcessAfterInitialization(bean, beanName);
    }
    
    
}
