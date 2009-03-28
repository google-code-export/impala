package org.impalaframework.service;

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
	 * Returns the arbitrary attributes attached to the service reference.
	 */
	Map<String, ?> getAttributes();
	
	/**
	 * Returns the class loader used to load the bean
	 */
	ClassLoader getBeanClassLoader();

}