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

import org.springframework.core.io.Resource;

/**
 * @author Phil Zoio
 */
@Deprecated
public interface ContextResourceHelper {

	//FIXME convert to use of resources
	File[] getApplicationPluginClassLocations(String plugin);

	CustomClassLoader getApplicationPluginClassLoader(ClassLoader parent, String plugin);

	ClassLoader getParentClassLoader(ClassLoader existing, String plugin);

	Resource getApplicationPluginSpringLocation(String plugin);

}
