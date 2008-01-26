package org.impalaframework.web.servlet;

import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.module.bootstrap.ModuleManagementFactory;
import org.impalaframework.module.holder.ModuleStateChangeListener;
import org.impalaframework.module.holder.ModuleStateChangeNotifier;
import org.impalaframework.module.holder.ModuleStateHolder;
import org.impalaframework.module.modification.ModuleStateChange;
import org.impalaframework.module.modification.Transition;
import org.impalaframework.web.WebConstants;
import org.springframework.beans.BeansException;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.context.WebApplicationContext;

public class ExternalLoadingImpalaServlet extends BaseImpalaServlet {

	private static final long serialVersionUID = 1L;
	
	private boolean initialized;

	@Override
	protected WebApplicationContext createWebApplicationContext() throws BeansException {

		// the superclass closes the plugins
		ModuleManagementFactory factory = (ModuleManagementFactory) getServletContext().getAttribute(
				WebConstants.IMPALA_FACTORY_ATTRIBUTE);

		if (factory == null) {
			throw new ConfigurationException("Unable to load " + ExternalLoadingImpalaServlet.class.getName()
					+ " as no attribute '" + WebConstants.IMPALA_FACTORY_ATTRIBUTE
					+ "' has been set up. Have you set up your Impala ContextLoader correctly?");
		}

		String servletName = getServletName();
		ModuleStateHolder moduleStateHolder = factory.getModuleStateHolder();
		
		if (!initialized) {
			
			ModuleStateChangeNotifier moduleStateChangeNotifier = factory.getModuleStateChangeNotifier();
			moduleStateChangeNotifier.addListener(new ModuleStateChangeListener() {

				public void moduleStateChanged(ModuleStateHolder moduleStateHolder, ModuleStateChange change) {
					try {
						initServletBean();
					}
					catch (Exception e) {
						logger.error("Unable to reinitialize servlet " + getServletName(), e);
					}
				}

				public String getModuleName() {
					return getServletName();
				}

				public Transition getTransition() {
					return Transition.UNLOADED_TO_LOADED;
				}
				
			});
			this.initialized = true;
		}

		ConfigurableApplicationContext context = moduleStateHolder.getModule(servletName);
		if (context != null) {
			if (context instanceof WebApplicationContext) {
				return (WebApplicationContext) context;
			}
			else {
				throw new ConfigurationException("Module registered under name of servlet '" + servletName
						+ "' needs to be an instance of " + WebApplicationContext.class.getName());
			}
		}
		else {
			throw new ConfigurationException("No module registered under the name of servlet '" + servletName + "'");
		}
	}

}
