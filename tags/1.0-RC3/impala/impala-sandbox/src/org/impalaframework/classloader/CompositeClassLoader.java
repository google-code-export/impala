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
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;

public class CompositeClassLoader extends ClassLoader {

	private List<BaseURLClassLoader> classLoaders;

	private ClassLoader parent;

	public CompositeClassLoader(List<BaseURLClassLoader> classLoaders) {
		super();
		Assert.notNull(classLoaders);
		Assert.notEmpty(classLoaders);

		final ClassLoader firstParent = classLoaders.get(0).getParent();
		if (classLoaders.size() > 1) {
			for (int i = 1; i < classLoaders.size(); i++) {
				BaseURLClassLoader cl = classLoaders.get(i);
				Assert.isTrue(
					cl.getParent() == firstParent,
					"All class loaders must share from the same parent. That is, they need to inherit from the same hierarchy. First parent is "
							+ firstParent + " while " + cl + " has parent " + cl.getParent());
			}
		}
		this.classLoaders = new ArrayList<BaseURLClassLoader>(classLoaders);
		this.parent = firstParent;
	}

	@Override
	public URL getResource(String name) {
		for (BaseURLClassLoader classLoader : classLoaders) {
			final URL customResource = classLoader.getCustomResource(name);
			if (customResource != null)
				return customResource;
		}
		return parent.getResource(name);
	}

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		final Class<?> parentClass = this.classLoaders.get(0).loadParentClass(name);

		if (parentClass != null) {
			return parentClass;
		}

		for (BaseURLClassLoader classLoader : classLoaders) {
			final Class<?> alreadyLoadedClass = classLoader.getAlreadyLoadedClass(name);
			if (alreadyLoadedClass != null)
				return alreadyLoadedClass;
		}

		for (BaseURLClassLoader classLoader : classLoaders) {
			final Class<?> customClass = classLoader.loadCustomClass(name);
			if (customClass != null)
				return customClass;
		}

		throw new ClassNotFoundException(name + " cannot be found using class loaders " + classLoaders);
	}

	public void addClassLoader(BaseURLClassLoader loader) {
		this.classLoaders.add(loader);
	}
	
	public boolean removeClassLoader(BaseURLClassLoader loader) {
		return this.classLoaders.remove(loader);
	}

}
