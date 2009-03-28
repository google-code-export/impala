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

import java.util.List;

import org.impalaframework.exception.InvalidStateException;
import org.impalaframework.service.ServiceReferenceFilter;
import org.impalaframework.service.ServiceRegistryReference;

/**
 * Implementation of <code>ServiceReferenceFilter</code> which filters from the service registry
 * all entries which contain the specific named tag.
 * @author Phil Zoio
 * @deprecated to be replaced by generic Filter implementation
 */
class ServiceRegistryContributionMapFilter implements ServiceReferenceFilter {

	private String contributedBeanAttributeName = "contributedBeanName";
	private String tagName;	
	
	public boolean matches(ServiceRegistryReference ref) {
		String contributionKeyName = getContributionKeyName(ref);
				
		if (contributionKeyName != null) {
			return true;
		}
		return false;
	}
	
	String getContributionKeyName(ServiceRegistryReference ref) {
		String contributionKeyName = null;
		List<String> tags = ref.getTags();
		if (tags.contains(tagName)) {
			Object keyName = ref.getAttributes().get(contributedBeanAttributeName);
			contributionKeyName = castKeyName(keyName);	
		}
		return contributionKeyName;
	}

	private String castKeyName(Object keyName) {
		String contributionKeyName = null;
		try {
			contributionKeyName = (String) keyName;
		} catch (RuntimeException e) {
			throw new InvalidStateException("key " + contributionKeyName + " could not be cast to the correct type", e);
		}
		return contributionKeyName;
	}

    void setContributedBeanAttributeName(String contributedBeanAttributeName) {
		this.contributedBeanAttributeName = contributedBeanAttributeName;
	}

	void setTagName(String tagName) {
		this.tagName = tagName;
	}

}
