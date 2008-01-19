package org.impalaframework.util;

import org.impalaframework.exception.ExecutionException;

public class InstantiationUtils {

	@SuppressWarnings("unchecked")
	public static <T extends Object> T instantiate(String className) {
		Class<T> clazz = null;
		try {
			clazz = org.springframework.util.ClassUtils.forName(className);
		}
		catch (ClassNotFoundException e) {
			throw new ExecutionException("Unable to find class of type '" + className + "'");
		}

		T instance = null;
		try {
			instance = (T) clazz.newInstance();
			return instance;
		}
		catch (Exception e) {
			// FIXME better exception catching
			String message = "Error instantiating class of type '" + className + "': " + e.getMessage();
			throw new ExecutionException(message, e);
		}
	}

}
