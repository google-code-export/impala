package org.impalaframework.util;


public class InstantiationUtils {

	@SuppressWarnings("unchecked")
	public static <T extends Object> T instantiate(String className) {
		Class<T> clazz = null;
		
			//FIXME better exception catching
		
			try {
				clazz = org.springframework.util.ClassUtils.forName(className);
			}
			catch (Throwable e) {
				throw new IllegalStateException("Unable to find class of type class " + className);
			}
		
		T instance = null;
		try {
			instance = clazz.newInstance();
			return instance;
		}
		catch (Exception e) {
			throw new IllegalStateException("Error instantiating class of type " + className + ": " + e.getMessage(), e);
		}
	}
	
}
