package org.impalaframework.exception;

public class InvalidBeanTypeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private Class<?> beanType;

	private Class<?> requiredType;

	private String beanName;

	public InvalidBeanTypeException(String beanName, Class<?> requiredType, Class<?> beanType) {
		super("Bean named '" + beanName + "' must be of type [" + requiredType.getName()
				+ "], but was actually of type [" + beanType.getName() + "]. Class loader of bean: "
				+ beanType.getClassLoader() + ". Class loader of required type: " + requiredType.getClassLoader());
		this.beanType = beanType;
		this.requiredType = requiredType;
		this.beanName = beanName;
	}

	public Class<?> getBeanType() {
		return beanType;
	}

	public Class<?> getRequiredType() {
		return requiredType;
	}

	public String getBeanName() {
		return beanName;
	}

}
