package org.impalaframework.startup;

import java.util.List;

import org.springframework.context.ApplicationContext;

public interface ContextStarter {
	
	public ApplicationContext startContext(List<String> locations);
	
}
