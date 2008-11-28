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

package org.impalaframework.facade;

import java.util.ArrayList;
import java.util.List;

import org.impalaframework.classloader.ClassLoaderFactory;
import org.impalaframework.classloader.CustomClassLoaderFactory;
import org.impalaframework.classloader.ParentClassLoader;

/**
 * Constructs application context in which the {@link ClassLoaderFactory}
 * is an instance of {@link CustomClassLoaderFactory}, which uses as the module class loader the 
 * class loader implementation {@link ParentClassLoader}. This ensures that any class loading will
 * first be delegated to the class loader's parent before being delegated to the module class loader.
 * Useful for running a suite of test cases from a project which includes all of the module projects
 * on the class path.
 * @author Phil Zoio
 */
public class SuiteOperationFacade extends BaseOperationsFacade {

	@Override
	protected List<String> getBootstrapContextLocations() {
		List<String> locations = new ArrayList<String>();
		locations.add("META-INF/impala-bootstrap.xml");
		locations.add("META-INF/impala-parent-loader-bootstrap.xml");
		return locations;
	}

}
