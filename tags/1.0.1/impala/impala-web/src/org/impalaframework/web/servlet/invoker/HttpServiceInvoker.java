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

/**
 * Interface useful for wrapping servlets or filters with additional functionality, for example, to set up 
 * read-write locks (see {@link ReadWriteLockingInvoker}) and for setting up the thread context class loader 
 * see {@link ThreadContextClassLoaderHttpServiceInvoker}.
 * 
 * @see ReadWriteLockingInvoker
 * @see ThreadContextClassLoaderHttpServiceInvoker
 * @author Phil Zoio
 */
public interface HttpServiceInvoker {

    void invoke(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException;

}
