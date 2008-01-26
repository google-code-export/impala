package org.impalaframework.web.module;

import java.util.Set;

import javax.servlet.ServletContext;

import org.impalaframework.module.bootstrap.ModuleManagementFactory;
import org.impalaframework.module.monitor.BaseModuleChangeListener;
import org.impalaframework.module.monitor.ModuleChangeEvent;
import org.impalaframework.module.monitor.ModuleContentChangeListener;
import org.impalaframework.module.operation.ModuleOperation;
import org.impalaframework.module.operation.ModuleOperationConstants;
import org.impalaframework.module.operation.ModuleOperationInput;
import org.impalaframework.web.WebConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ServletContextAware;

public class WebModuleChangeListener extends BaseModuleChangeListener implements
		ModuleContentChangeListener, ServletContextAware {

	final Logger logger = LoggerFactory.getLogger(WebModuleChangeListener.class);

	private ServletContext servletContext;

	public WebModuleChangeListener() {
		super();
	}

	public void moduleContentsModified(ModuleChangeEvent event) {
		Set<String> modified = getModifiedPlugins(event);

		if (!modified.isEmpty()) {
			ModuleManagementFactory factory = (ModuleManagementFactory) servletContext
					.getAttribute(WebConstants.IMPALA_FACTORY_ATTRIBUTE);

			for (String moduleName : modified) {

				logger.info("Processing modified plugin {}", moduleName);

				ModuleOperation operation = factory.getModuleOperationRegistry().getOperation(ModuleOperationConstants.ReloadNamedModuleOperation);
				ModuleOperationInput moduleOperationInput = new ModuleOperationInput(null, null, moduleName);
				operation.execute(moduleOperationInput);
			}
		}
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

}
