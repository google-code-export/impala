package org.impalaframework.service;

import java.util.List;
import java.util.Map;

public interface ServiceRegistryReference {

	/**
	 * Returns the bean backing the service reference
	 */
	Object getBean();

	/**
	 * Returns the name of the bean 
	 */
	String getBeanName();

	/**
	 * Returns the name of the contributing module for the service reference
	 */
	String getContributingModule();

	/**
	 * Returns a list of tags for the service reference. Tags can be used to find particular service reference entries
	 */
	List<String> getTags();

	/**
	 * Returns the arbitrary attributes attached to the service reference.
	 */
	Map<String, ?> getAttributes();
	
	/**
	 * Returns the class loader used to load the bean
	 */
	ClassLoader getBeanClassLoader();

}