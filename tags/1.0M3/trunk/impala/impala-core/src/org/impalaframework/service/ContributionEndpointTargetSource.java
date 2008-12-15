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

package org.impalaframework.service;

import org.springframework.aop.TargetSource;

/**
 * Extension of <code>TargetSource</code> which represents capability of
 * accepting contributions from a child <code>ApplicationConext</code>
 * @author Phil Zoio
 */
public interface ContributionEndpointTargetSource extends TargetSource {

	/**
	 * Returns the <code>ServiceRegistryReference</code> corresponding with the target object
	 * @return
	 */
	public ServiceRegistryReference getServiceRegistryReference();
	
	/**
	 * Used to determine whether there is a target held by the
	 * <code>TargetSource</code>
	 * @return
	 */
	public boolean hasTarget();

	/**
	 * Registers a target for this <code>TargetSource</code>
	 * @param bean
	 */
	public void registerTarget(Object bean);

	/**
	 * Deregisters target for this <code>TargetSource</code>. Required if a
	 * child context is taken down
	 * @param bean
	 */
	public void deregisterTarget(Object bean);
}
