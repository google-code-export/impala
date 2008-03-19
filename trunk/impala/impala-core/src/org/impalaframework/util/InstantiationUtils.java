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
		Object o = null;
		try {
			o = clazz.newInstance();
			instance = (T) o;
			return instance;
		}
		catch (ClassCastException e) {
			// FIXME better exception catching
			String message = "Created object '" + o + "' is an instance of " + o.getClass().getName();
			throw new ExecutionException(message, e);
		}
		catch (Exception e) {
			// FIXME better exception catching
			String message = "Error instantiating class of type '" + className + "': " + e.getMessage();
			throw new ExecutionException(message, e);
		}
	}

}
