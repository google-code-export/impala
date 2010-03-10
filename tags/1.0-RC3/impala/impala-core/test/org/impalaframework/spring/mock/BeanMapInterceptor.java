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

package org.impalaframework.spring.mock;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;
import org.impalaframework.spring.DebuggingInterceptor;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

public class BeanMapInterceptor extends DebuggingInterceptor {

    private BeanMap beanMap;

    public BeanMapInterceptor(BeanMap beanMap) {
        super();
        Assert.notNull(beanMap);
        this.beanMap = beanMap;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        
        Class<?>[] parameterTypes = method.getParameterTypes();
        Method beanMapMethod = ReflectionUtils.findMethod(beanMap.getClass(), method.getName(), parameterTypes);
        
        if (beanMapMethod != null) {
            return ReflectionUtils.invokeMethod(beanMapMethod, beanMap, invocation.getArguments());
        }
        
        return super.invoke(invocation);
    }

}
