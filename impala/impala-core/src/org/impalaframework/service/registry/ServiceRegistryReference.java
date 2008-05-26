package org.impalaframework.service.registry;

import java.util.List;
import java.util.Map;

public interface ServiceRegistryReference {

	public abstract Object getBean();

	public abstract String getBeanName();

	public abstract String getContributingModule();

	public abstract List<String> getTags();

	public abstract Map<String, ?> getAttributes();

}