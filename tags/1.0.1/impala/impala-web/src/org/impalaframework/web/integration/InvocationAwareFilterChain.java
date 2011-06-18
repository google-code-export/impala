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

package org.impalaframework.web.integration;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * The pupose of <code>InvocationAwareFilterChain</code> is to capture the fact that an application level module filter was
 * did not perform processing of a particular request. For example, if a Tapestry5 filter within a module does not 
 * process the request, it will call <code>FilterChain.doFilter()</code>. Rather than allowing this to occur within the module
 * against the real <code>FilterChain</code>, a substitute <code>InvocationAwareFilterChain</code>
 * instance is passed to the Tapestry5 filter. The <code>ModuleProxyFilter</code> will call <code>getWasInvoked</code>
 * to determine whether the application filter attempted to pass on the request. If so, the original <code>FilterChain</code>'s
 * <code>doFilter</code> method is called.
 * @author Phil Zoio
 */
public class InvocationAwareFilterChain implements FilterChain {

	private boolean wasInvoked;

	public void doFilter(ServletRequest request, ServletResponse response)
			throws IOException, ServletException {
		wasInvoked = true;
	}

	public boolean getWasInvoked() {
		return wasInvoked;
	}

}
