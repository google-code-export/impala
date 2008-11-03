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

package org.impalaframework.osgi.classloader;

import org.impalaframework.classloader.ClassLoaderFactory;
import org.impalaframework.exception.InvalidStateException;
import org.osgi.framework.Bundle;
import org.springframework.osgi.util.BundleDelegatingClassLoader;

/**
 * Implementation of {@link ClassLoaderFactory} which returns a class loader backed by a
 * OSGi {@link Bundle}.
 * @author Phil Zoio
 */
public class OsgiClassLoaderFactory implements ClassLoaderFactory {

	/**
	 * Returns a {@link BundleDelegatingClassLoader} instance. Note that 
	 * <code>data</code> must be an instance of {@link Bundle}.
	 */
	public ClassLoader newClassLoader(ClassLoader parent, Object data) {
		if (!(data instanceof Bundle)) {
			throw new InvalidStateException("'data' must be instance of Bundle. Actual type: " + ((data != null) ? data.getClass().getName() : null));
		}
		Bundle bundle = (Bundle) data;
		return BundleDelegatingClassLoader.createBundleClassLoaderFor(bundle);
	}

}
