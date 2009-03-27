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

package org.impalaframework.service.contribution;

import org.impalaframework.service.ServiceRegistryReference;
import org.impalaframework.service.proxy.ProxyHelper;

/**
 * Map implementation which is dynamically backed by the service registry. It
 * implements <code>ServiceRegistryEventListener</code> so that it can pick up
 * and respond to changes in the service registry. Uses the
 * <code>ServiceRegistryContributionMapFilter</code> to filter out relevant
 * service entries from the service registry.
 * 
 * @see org.impalaframework.service.contribution.ServiceRegistryContributionMapFilter
 * @author Phil Zoio
 */
public class ServiceRegistryMap extends BaseServiceRegistryMap {
	
	//FIXME should wire this in
	private ProxyHelper proxyHelper;
	
	public ServiceRegistryMap() {
		super();
		proxyHelper = new ProxyHelper();
	}
	
	protected Object maybeGetProxy(ServiceRegistryReference reference) {
		return proxyHelper.maybeGetProxy(reference);
	}

	public void setProxyEntries(boolean proxyEntries) {
		this.proxyHelper.setProxyEntries(proxyEntries);
	}
	

	public void setProxyInterfaces(Class<?>[] proxyInterfaces) {
		this.proxyHelper.setProxyInterfaces(proxyInterfaces);
	}
}
