package org.impalaframework.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * http://blog.araneaframework.org/2006/11/21/zero-turn-around-in-java/
 * @author Phil Zoio
 */
public class RemoteCloner {

	private static final Log logger = LogFactory.getLog(RemoteCloner.class);
	 
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
