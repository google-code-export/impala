package org.impalaframework.osgi.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import junit.framework.TestCase;

public class OsgiBootstrapIntegrationTest extends TestCase {
	
	public void testApplicationContext() throws Exception {
		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(
				new String[] { 
						"META-INF/impala-bootstrap.xml",
						"META-INF/impala-osgi-bootstrap.xml" 
						});
		assertNotNull(appContext);
	}
	
}
