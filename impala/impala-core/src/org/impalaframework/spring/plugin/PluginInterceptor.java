/*
 * Copyright 2007 the original author or authors.
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

package org.impalaframework.spring.plugin;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.impalaframework.exception.NoServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Interceptor which satisfies parent dependency
 * @author Phil Zoio
 */
public class PluginInterceptor implements MethodInterceptor {

	final Logger log = LoggerFactory.getLogger(PluginInterceptor.class);

	private PluginContributionTargetSource targetSource;

	private String beanName;

	private boolean proceedWithNoService;

	public PluginInterceptor(PluginContributionTargetSource targetSource, String beanName) {
		this.targetSource = targetSource;
		this.beanName = beanName;
	}

	public Object invoke(MethodInvocation invocation) throws Throwable {
		if (targetSource.hasTarget()) {
			return invocation.proceed();
		}
		else {
			if (proceedWithNoService)
				return invokeDummy(invocation);
			else
				throw new NoServiceException("No service available for bean " + beanName);
		}
	}

	public Object invokeDummy(MethodInvocation invocation) throws Throwable {

		log.debug("Calling method {}", invocation);
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

	public void setProceedWithNoService(boolean proceedWithNoService) {
		this.proceedWithNoService = proceedWithNoService;
	}

}
