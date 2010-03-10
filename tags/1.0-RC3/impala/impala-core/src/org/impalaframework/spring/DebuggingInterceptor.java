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

package org.impalaframework.spring;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DebuggingInterceptor implements MethodInterceptor {

    private static final Log logger = LogFactory.getLog(DebuggingInterceptor.class);

    public Object invoke(MethodInvocation invocation) throws Throwable {

        logger.debug("Calling method " +  invocation);
        Class<?> returnType = invocation.getMethod().getReturnType();

        if (Void.TYPE.equals(returnType))
            return null;
        if (Byte.TYPE.equals(returnType))
            return (byte) 0;
        if (Short.TYPE.equals(returnType))
            return (short) 0;
        if (Integer.TYPE.equals(returnType))
            return (int) 0;
        if (Long.TYPE.equals(returnType))
            return 0L;
        if (Float.TYPE.equals(returnType))
            return 0f;
        if (Double.TYPE.equals(returnType))
            return 0d;
        if (Boolean.TYPE.equals(returnType))
            return false;

        return null;
    }

}
