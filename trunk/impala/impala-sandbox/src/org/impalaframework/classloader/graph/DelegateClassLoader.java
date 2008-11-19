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

package org.impalaframework.classloader.graph;

import java.util.List;

//FIXME comment and test
public class DelegateClassLoader extends ClassLoader {

	private List<GraphClassLoader> gcls;
	
	public DelegateClassLoader(List<GraphClassLoader> gcls) {
		this.gcls = gcls;
	}

	@Override
	public Class<?> loadClass(String name)
			throws ClassNotFoundException {
		
		List<GraphClassLoader> gclss = this.gcls;
		for (GraphClassLoader graphClassLoader : gclss) {
			Class<?> loadClass = graphClassLoader.loadClass(name, false);
			if (loadClass != null) {
				return loadClass;
			}
		}
		
		return null;
	}
	
	

}
