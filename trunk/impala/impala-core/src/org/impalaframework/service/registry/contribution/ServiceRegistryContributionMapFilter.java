package org.impalaframework.service.registry.contribution;

import java.util.List;

import org.impalaframework.exception.InvalidStateException;
import org.impalaframework.service.registry.ServiceReference;
import org.impalaframework.service.registry.event.ServiceReferenceFilter;

class ServiceRegistryContributionMapFilter<K> implements ServiceReferenceFilter {

	private String contributedBeanAttributeName = "contributedBeanName";
	private String tagName;	
	
	public boolean matches(ServiceReference ref) {
		K contributionKeyName = getContributionKeyName(ref);
				
		if (contributionKeyName != null) {
			return true;
		}
		return false;
	}
	
	K getContributionKeyName(ServiceReference ref) {
		K contributionKeyName = null;
		List<String> tags = ref.getTags();
		if (tags.contains(tagName)) {
			Object keyName = ref.getAttributes().get(contributedBeanAttributeName);
			contributionKeyName = castKeyName(keyName);	
		}
		return contributionKeyName;
	}

	@SuppressWarnings("unchecked")
	private K castKeyName(Object keyName) {
		K contributionKeyName = null;
		try {
			contributionKeyName = (K) keyName;
		} catch (RuntimeException e) {
			throw new InvalidStateException("key " + contributionKeyName + " coudld not be cast to the correct type", e);
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
