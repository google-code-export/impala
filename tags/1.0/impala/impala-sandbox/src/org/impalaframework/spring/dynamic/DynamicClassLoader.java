/*
 * Copyright 2007-2010 the original author or authors.
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

package org.impalaframework.spring.dynamic;

import org.impalaframework.spring.classloader.ClassLoaderFactory;

public class DynamicClassLoader extends ClassLoader {

	private ClassLoaderFactory factory;

	private ClassLoader classLoader;

	public DynamicClassLoader() {
		super(DynamicClassLoader.class.getClassLoader());
	}

	public DynamicClassLoader(ClassLoader parent) {
		super(parent);
	}

	public synchronized Class<?> loadClass(String className) throws ClassNotFoundException {

		if (this.factory == null)
			throw new IllegalStateException("Class loader factory cannot be null");

		if (this.classLoader == null) {
			refresh();
		}

		Class<?> clazz = classLoader.loadClass(className);

		return clazz;
	}

	public synchronized void refresh() {
		this.classLoader = factory.getClassLoader();
		System.out.println("Replaced classloader " + this.classLoader);
	}

	public void setFactory(ClassLoaderFactory factory) {
		this.factory = factory;
	}

}