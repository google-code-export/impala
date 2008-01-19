package org.impalaframework.util;

import org.impalaframework.exception.ExecutionException;


public class ObjectUtils {
	
	 @SuppressWarnings("unchecked")
	public static <T extends Object> T cast(final Object o, Class<T> clazz) {
		if (!(clazz.isAssignableFrom(o.getClass()))) {
			throw new ExecutionException(o.getClass().getName() + " is not an instance of "
					+ clazz.getSimpleName());
		}
		return (T)o;
	}
}
