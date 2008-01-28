package org.impalaframework.startup;

import org.springframework.context.ApplicationContext;

public interface ContextStarter {
	
	public ApplicationContext startContext(String[] locations);
	
}
