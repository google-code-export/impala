package org.impalaframework.facade;

import org.springframework.context.ApplicationContext;


public interface InternalOperationsFacade extends OperationsFacade {
	ApplicationContext getModule(String pluginName);
}