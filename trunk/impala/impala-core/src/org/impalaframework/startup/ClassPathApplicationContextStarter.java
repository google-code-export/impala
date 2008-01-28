package org.impalaframework.startup;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ClassPathApplicationContextStarter implements ContextStarter {
	public ApplicationContext startContext(String[] locations) {
		return new ClassPathXmlApplicationContext(locations);
	}
}