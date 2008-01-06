package org.impalaframework.groovy;

import groovy.lang.GroovyObject;
import groovy.lang.GroovyShell;

import java.util.List;

import org.impalaframework.groovy.GroovyParser;

import junit.framework.TestCase;

public class GroovyParserTest extends TestCase {

	public void testParse() {
		GroovyObject go = GroovyParser.parse("class Test { \n String doStuff() { return \"Do stuff\" } }");
		String result = (String) go.invokeMethod("doStuff", null);
		assertEquals("Do stuff", result);
		
		//GroovyEngine ge = new GroovyEngine();
		GroovyShell sh = new GroovyShell();
		List<?> evaluate = (List<?>) sh.evaluate("[1, 2, 3]");
		System.out.println(evaluate);
	}

}
