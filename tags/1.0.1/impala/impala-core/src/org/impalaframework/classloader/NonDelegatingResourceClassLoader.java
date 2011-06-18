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

package org.impalaframework.classloader;

import java.net.URL;

import org.springframework.util.Assert;

/**
 * A ClassLoader which delegates <code>loadClass</code> and
 * <code>getResource</code> calls to a delegate <code>URLClassLoader</code>.
 * It's special behaviour is that when <code>getResources</code> is called, the
 * calls is delegated only to the
 * <code><strong>getCustomResource</strong></code>. This will result in only
 * resources directly visible to the delegate to be found. In other words, it
 * will not find resources visible to parent <code>ClassLoaders</code>s of the
 * delegate.
 * 
 * @author Phil Zoio
 */
public class NonDelegatingResourceClassLoader extends ClassLoader {

	private BaseURLClassLoader delegate;

	public NonDelegatingResourceClassLoader(BaseURLClassLoader delegate) {
		super();
		Assert.notNull(delegate);
		this.delegate = delegate;
	}

	@Override
	public URL getResource(String name) {
		return delegate.getCustomResource(name);
	}

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		return delegate.loadClass(name);
	}

}
