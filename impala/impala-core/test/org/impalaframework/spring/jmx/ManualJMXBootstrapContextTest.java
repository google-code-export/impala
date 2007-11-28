package org.impalaframework.spring.jmx;

public class ManualJMXBootstrapContextTest extends JMXBootstrapContextTest {

	@Override
	public void testBootstrapContext() throws Exception {
		super.testBootstrapContext();
		
		System.out.println("Enter to finish");
		System.in.read();
	}

}
