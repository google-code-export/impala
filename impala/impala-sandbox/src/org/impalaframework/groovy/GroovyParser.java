package org.impalaframework.groovy;

import org.impalaframework.exception.ExecutionException;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;

/**
 * Returns a Groovy object
 */
public class GroovyParser {

	public static GroovyObject parse(String args) {
		GroovyClassLoader cl = new GroovyClassLoader();
		try {
			final Class<?> clazz = cl.parseClass(args);
			final GroovyObject newInstance = (GroovyObject) clazz.newInstance();
			return newInstance;
		}
		catch (Exception e) {
			throw new ExecutionException(e.getMessage(), e);
		}
	}
}
