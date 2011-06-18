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

package org.impalaframework.web.servlet.invoker;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.impalaframework.module.spi.FrameworkLockHolder;

/**
 * @author Phil Zoio
 */
public class ReadWriteLockingInvoker implements HttpServiceInvoker {

    private static final long serialVersionUID = 1L;
    
    private Object target;
    private FrameworkLockHolder frameworkLockHolder;

    public ReadWriteLockingInvoker(Object target, FrameworkLockHolder frameworkLockHolder) {
        super();
        this.target = target;
        this.frameworkLockHolder = frameworkLockHolder;
    }

    public void invoke(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        frameworkLockHolder.readLock();
        try {
            ServletInvokerUtils.invoke(target, request, response, filterChain);
        }
        finally {
            frameworkLockHolder.readUnlock();
        }
    }
    
}
