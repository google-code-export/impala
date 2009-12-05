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

import org.springframework.util.Assert;

/**
 * Value holder for application id and {@link RequestModuleMapping} instance
 * @author Phil Zoio
 */
public class ApplicationRequestModuleMapping {

    private final String applicationId;
    
    private final RequestModuleMapping requestModuleMapping;

    public ApplicationRequestModuleMapping(String applicationId,
            RequestModuleMapping requestModuleMapping) {
        super();
        Assert.notNull(applicationId, "applicationId cannot be null");
        Assert.notNull(requestModuleMapping, "requestModuleMapping cannot be null");
        
        this.applicationId = applicationId;
        this.requestModuleMapping = requestModuleMapping;
    }
    
    public String getApplicationId() {
        return applicationId;
    }
    
    public RequestModuleMapping getRequestModuleMapping() {
        return requestModuleMapping;
    }   
    
    
}
