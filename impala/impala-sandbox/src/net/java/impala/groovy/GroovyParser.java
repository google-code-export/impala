package net.java.impala.groovy;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import net.java.impala.exception.ExecutionException;

/**
 * Returns a Groovy object
 */
public class GroovyParser {

	public static GroovyObject parse(String args) {
		GroovyClassLoader cl = new GroovyClassLoader();
		try {
			final Class clazz = cl.parseClass(args);
			final GroovyObject newInstance = (GroovyObject) clazz.newInstance();
			return newInstance;
		}
		catch (Exception e) {
			throw new ExecutionException(e.getMessage(), e);
		}
	}
}
