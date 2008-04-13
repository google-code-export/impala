package org.impalaframework.service.registry;

public class ServiceReference {

	private Object bean;
	private String contributingModule;

	public ServiceReference(Object bean, String contributingModule) {
		super();
		this.bean = bean;
		this.contributingModule = contributingModule;
	}

	Object getBean() {
		return bean;
	}

	String getContributingModule() {
		return contributingModule;
	}

}
