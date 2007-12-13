package org.impalaframework.plugin.web;

import javax.servlet.ServletContext;

import org.impalaframework.plugin.bootstrap.BootstrapBeanFactory;
import org.impalaframework.plugin.builder.PluginSpecBuilder;
import org.impalaframework.plugin.modification.ModificationCalculationType;
import org.impalaframework.plugin.modification.PluginModificationCalculator;
import org.impalaframework.plugin.modification.PluginTransitionSet;
import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.transition.PluginStateManager;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.web.context.ServletContextAware;

@ManagedResource(objectName = "impala:service=webPluginOperations", description = "MBean exposing reconfiguration of web application")
public class WebPluginReloader implements ServletContextAware {

	private ServletContext servletContext;
	
	@ManagedOperation(description = "Uses the PluginSpecBuilder which loaded the initial application context")
	public void reloadPlugins() {
		
		//FIXME test
		
		BootstrapBeanFactory factory = (BootstrapBeanFactory) servletContext.getAttribute(WebConstants.IMPALA_FACTORY_ATTRIBUTE);
		if (factory == null) {
			//FIXME throw
		}
		
		PluginSpecBuilder builder = (PluginSpecBuilder) servletContext.getAttribute(WebConstants.PLUGIN_SPEC_BUILDER_ATTRIBUTE);
		if (builder == null) {
			//FIXME throw
		}
		
		PluginStateManager pluginStateManager = factory.getPluginStateManager();
		ParentSpec oldPluginSpec = pluginStateManager.cloneParentSpec();
		ParentSpec newPluginSpec = builder.getParentSpec(); 
		
		// figure out the plugins to reload
		// FIXME extract into processor class
		PluginModificationCalculator calculator = factory.getPluginModificationCalculatorRegistry()
				.getPluginModificationCalculator(ModificationCalculationType.STRICT);
		PluginTransitionSet transitions = calculator.getTransitions(oldPluginSpec, newPluginSpec);
		pluginStateManager.processTransitions(transitions);
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

}
