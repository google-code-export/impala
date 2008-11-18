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
