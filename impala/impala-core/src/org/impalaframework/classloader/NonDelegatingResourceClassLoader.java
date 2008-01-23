package org.impalaframework.classloader;

import java.net.URL;

import org.springframework.util.Assert;

/**
 * A ClassLoader which delegates <code>loadClass</code> and <code>getResource</code> calls
 * to a delegate <code>URLClassLoader</code>. It's special behaviour is that when
 * <code>getResources</code> is called, the calls is delegated only to the 
 * <code><strong>getCustomResource</strong></code>. This will result in only 
 * resources directly visible to the delegate to be found. In other words, it will
 * not find resources visible to parent <code>ClassLoaders</code>s of the delegate.
 * @author Phil Zoio
 */
public class NonDelegatingResourceClassLoader extends ClassLoader {

	private URLClassLoader delegate;

	public NonDelegatingResourceClassLoader(URLClassLoader delegate) {
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
