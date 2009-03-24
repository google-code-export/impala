package org.impalaframework.spring.service.proxy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.service.ServiceRegistry;
import org.impalaframework.service.registry.ServiceRegistryAware;
import org.impalaframework.spring.service.ContributionEndpointTargetSource;
import org.impalaframework.spring.service.registry.ServiceRegistryTargetSource;
import org.springframework.aop.framework.ProxyFactory;

/**
 * Implementation of {@link ServiceProxyFactoryCreator} which is used to create proxy for a bean which holds a dynamic reference
 * to a bean obtained from the Impala service registry.
 * 
 * @author Phil Zoio
 */
public class DynamicServiceProxyFactoryCreator implements ServiceRegistryAware, ServiceProxyFactoryCreator {

	private static final Log logger = LogFactory.getLog(ContributionProxyFactoryBean.class);
	
	private ServiceRegistry serviceRegistry;
	
	/**
	 * True if the interceptor should allow the call to proceed (with a dummy
	 * value returned) if no service is present. Primarily present for testing
	 * purposes. Defaults to false.
	 */
	private boolean allowNoService = false;

	/**
	 * Whether to set the context class loader to the class loader of the module
	 * contributing the bean being invoked. This is to mitigate possibility of
	 * exceptions being caused by calls to
	 * <code>Thread.setContextClassLoader()</code> being propagated across
	 * modules
	 */
	private boolean setContextClassLoader = true;
	
	public ProxyFactory createProxy(Class<?>[] interfaces, String registryKeyName) {
		
		ContributionEndpointTargetSource targetSource = new ServiceRegistryTargetSource(registryKeyName, serviceRegistry);
		
		ProxyFactory proxyFactory = new ProxyFactory();
		for (int i = 0; i < interfaces.length; i++) {
			if (logger.isDebugEnabled()) {
				logger.debug("Adding interface " + interfaces[i] + " loaded from " + interfaces[i].getClassLoader());
			}
			proxyFactory.addInterface(interfaces[i]);
		}
		
		proxyFactory.setTargetSource(targetSource);
		
		ContributionEndpointInterceptor interceptor = new ContributionEndpointInterceptor(targetSource, registryKeyName);
		
		if (logger.isDebugEnabled()) {
			logger.debug("Creating proxy for " + registryKeyName + 
					" with allowNoService '" + allowNoService + "' and setContextClassLoader '" + setContextClassLoader + "'");
		}
		
		interceptor.setProceedWithNoService(allowNoService);
		interceptor.setSetContextClassLoader(setContextClassLoader);
		proxyFactory.addAdvice(interceptor);
		
		return proxyFactory;
	}

	/**
	 * Implementation of {@link ServiceRegistryAware}. Allows service registry to be automatically be injected.
	 */
	public void setServiceRegistry(ServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}

	/**
	 * Sets to true if the interceptor should allow the call to proceed (with a dummy value returned) if no service is
	 * present. Primarily present for testing purposes. Defaults to false.
	 */
	public void setAllowNoService(boolean allowNoService) {
		this.allowNoService = allowNoService;
	}
	
	/**
	 * Sets to true if the interceptor should set the current threads context class loader to the class loader of the module
	 * responsible for loading the target bean. Defaults to true.
	 * 
	 * @see Thread#setContextClassLoader(ClassLoader)
	 */
	public void setSetContextClassLoader(boolean setContextClassLoader) {
		this.setContextClassLoader = setContextClassLoader;
	}
	
}
