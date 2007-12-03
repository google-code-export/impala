package org.impalaframework.classloader;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.springframework.util.ClassUtils;

public abstract class ClassLoaderUtils {
	
	public static List<String> getClassHierarchyNames(String className) {
		
		List<String> classNames = new LinkedList<String>();
		ClassLoader defaultClassLoader = ClassUtils.getDefaultClassLoader();
		
		try {
			Class<?> loadClass = defaultClassLoader.loadClass(className);
			classNames.add(loadClass.getName());
			findSuperClass(classNames, loadClass);
			return classNames;
		}
		catch (ClassNotFoundException e) {
			return Collections.emptyList();
		}
	}

	private static void findSuperClass(List<String> classNames, Class<?> loadClass) {
		Class<?> superClass = loadClass.getSuperclass();
		if (superClass != null) {
			classNames.add(superClass.getName());
			findSuperClass(classNames, superClass);
		}
	}
}
