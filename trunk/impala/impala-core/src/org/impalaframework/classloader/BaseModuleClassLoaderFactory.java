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

package org.impalaframework.classloader;

import java.io.File;
import java.net.URL;

import org.impalaframework.exception.InvalidStateException;

public abstract class BaseModuleClassLoaderFactory implements ClassLoaderFactory {

	public abstract ClassLoader newClassLoader(ClassLoader parent, File[] files);

	public abstract ClassLoader newClassLoader(ClassLoader parent, URL[] urls);

	//FIXME convert to using ModuleDefinitions and wire in ModuleLocationResolver
	public ClassLoader newClassLoader(ClassLoader parent, Object data) {
		if (data instanceof File[]) {
			return newClassLoader(parent, (File[])data);
		}
		if (data instanceof URL[]) {
			return newClassLoader(parent, (File[])data);
		}
		else {
			throw new InvalidStateException("'data' must be instance of File[] or URL[]. Actual type: " + ((data != null) ? data.getClass().getName() : null));
		}
	}
}
