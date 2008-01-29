package org.impalaframework.startup;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Assert;

public class ClassPathApplicationContextStarter implements ContextStarter {
	public ApplicationContext startContext(List<String> locations) {
		Assert.notNull(locations);
		
		String[] locationArray = new String[locations.size()];
		return new ClassPathXmlApplicationContext(locations.toArray(locationArray));
	}
}