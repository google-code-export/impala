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

package org.impalaframework.service.registry.event;

import org.impalaframework.service.ServiceRegistryReference;
import org.springframework.util.Assert;

public abstract class BaseServiceRegistryEvent implements ServiceRegistryEvent {
	private final ServiceRegistryReference serviceReference;

	public BaseServiceRegistryEvent(ServiceRegistryReference serviceReference) {
		super();
		Assert.notNull(serviceReference);
		this.serviceReference = serviceReference;
	}

	public final ServiceRegistryReference getServiceReference() {
		return serviceReference;
	}
	
	
}
