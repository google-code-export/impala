/*
 * Copyright 2007 the original author or authors.
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

package net.java.impala.classloader;

import java.io.File;

import net.java.impala.location.ClassLocationResolver;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

/**
 * @author Phil Zoio
 */
public class DefaultContextResourceHelper implements ContextResourceHelper {

	private ClassLocationResolver classLocationResolver;

	public DefaultContextResourceHelper(ClassLocationResolver classLocationResolver) {
		super();
		Assert.notNull(classLocationResolver, ClassLocationResolver.class.getSimpleName() + " cannot be null");
		this.classLocationResolver = classLocationResolver;
	}

	public CustomClassLoader getApplicationPluginClassLoader(ClassLoader parent, String plugin) {
		File[] pluginClassLocations = this.classLocationResolver.getApplicationPluginClassLocations(plugin);
		return new CustomClassLoader(parent, pluginClassLocations);
	}

	public ClassLoader getParentClassLoader(ClassLoader existing, String plugin) {
		File[] parentClassLocations = classLocationResolver.getParentClassLocations(plugin);
		ParentClassLoader cl = new ParentClassLoader(existing, parentClassLocations);
		return cl;
	}

	public File[] getApplicationPluginClassLocations(String plugin) {
		return classLocationResolver.getParentClassLocations(plugin);
	}
	
	public ClassLoader getTestClassLoader(ClassLoader parentClassLoader, File[] locations, String name) {
		throw new UnsupportedOperationException();
	}

	public ClassLocationResolver getClassLocationResolver() {
		return classLocationResolver;
	}

	//FIXME add test
	public Resource getApplicationPluginSpringLocation(String plugin) {
		return new FileSystemResource(classLocationResolver.getApplicationPluginSpringLocation(plugin));
	}

}
