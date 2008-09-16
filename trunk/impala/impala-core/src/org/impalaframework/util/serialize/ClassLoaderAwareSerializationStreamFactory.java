package org.impalaframework.util.serialize;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

import org.springframework.util.Assert;

/**
 * Based on http://blog.araneaframework.org/2006/11/21/zero-turn-around-in-java/
 * @author Phil Zoio
 */
public class ClassLoaderAwareSerializationStreamFactory extends DefaultSerializationStreamFactory {

	private ClassLoader classLoader;

	public ClassLoaderAwareSerializationStreamFactory(ClassLoader classLoader) {
		super();
		Assert.notNull(classLoader);
		this.classLoader = classLoader;
	}

	@Override
	public ObjectInputStream getInputStream(InputStream input)
			throws IOException {
		return new ClassLoaderAwareInputStream(input, classLoader);
	}
	
	private static class ClassLoaderAwareInputStream extends ObjectInputStream {
		
		private ClassLoader classLoader;
		
		ClassLoaderAwareInputStream(InputStream in, ClassLoader classLoader) throws IOException {
			super(in);
			this.classLoader = classLoader;
		}

		@Override
		protected Class<?> resolveClass(ObjectStreamClass osc)
				throws IOException, ClassNotFoundException {
			String className = osc.getName();
			return classLoader.loadClass(className);
		}
	}
}
