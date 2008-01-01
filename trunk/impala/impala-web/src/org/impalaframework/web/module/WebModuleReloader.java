package org.impalaframework.web.module;

import javax.servlet.ServletContext;

import org.impalaframework.module.bootstrap.ModuleManagementFactory;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.operation.ModuleOperation;
import org.impalaframework.module.operation.ModuleOperationConstants;
import org.impalaframework.module.operation.ModuleOperationInput;
import org.impalaframework.web.WebConstants;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.util.Assert;
import org.springframework.web.context.ServletContextAware;

@ManagedResource(objectName = "impala:service=webPluginOperations", description = "MBean exposing reconfiguration of web application")
public class WebModuleReloader implements ServletContextAware {

	private ServletContext servletContext;

	@ManagedOperation(description = "Uses the current ModuleDefintitionSource to perform a full reload of the module hierarchy")
	public void reloadPlugins() {
		Assert.notNull(servletContext);

		ModuleManagementFactory factory = (ModuleManagementFactory) servletContext
				.getAttribute(WebConstants.IMPALA_FACTORY_ATTRIBUTE);
		if (factory == null) {
			throw new IllegalStateException(
					"No instance of "
							+ ModuleManagementFactory.class.getName()
							+ " found. Your context loader needs to be configured to create an instance of this class and attach it to the ServletContext using the attribue WebConstants.IMPALA_FACTORY_ATTRIBUTE");
		}

		ModuleDefinitionSource source = (ModuleDefinitionSource) servletContext
				.getAttribute(WebConstants.MODULE_DEFINITION_SOURCE_ATTRIBUTE);
		if (source == null) {
			throw new IllegalStateException(
					"No instance of "
							+ ModuleDefinitionSource.class.getName()
							+ " found. Your context loader needs to be configured to create an instance of this class and attach it to the ServletContext using the attribue WebConstants.MODULE_DEFINITION_SOURCE_ATTRIBUTE");

		}

		ModuleOperationInput moduleOperationInput = new ModuleOperationInput(source, null, null);
		
		ModuleOperation operation = factory.getModuleOperationRegistry().getOperation(ModuleOperationConstants.ReloadRootModuleOperation);
		operation.execute(moduleOperationInput);
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

}
