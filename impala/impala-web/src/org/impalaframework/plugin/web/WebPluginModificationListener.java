package org.impalaframework.plugin.web;

import java.util.Set;

import javax.servlet.ServletContext;

import org.impalaframework.plugin.bootstrap.ImpalaBootstrapFactory;
import org.impalaframework.plugin.modification.PluginModificationCalculator;
import org.impalaframework.plugin.modification.PluginTransitionSet;
import org.impalaframework.plugin.modification.StrictPluginModificationCalculator;
import org.impalaframework.plugin.monitor.BasePluginModificationListener;
import org.impalaframework.plugin.monitor.PluginModificationEvent;
import org.impalaframework.plugin.monitor.PluginModificationListener;
import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.transition.PluginStateManager;
import org.springframework.util.Assert;
import org.springframework.web.context.ServletContextAware;

//FIXME add test
public class WebPluginModificationListener extends BasePluginModificationListener implements PluginModificationListener, ServletContextAware {

	private ServletContext servletContext;

	private WebPluginModificationListener() {
		super();
	}

	public WebPluginModificationListener(ServletContext servletContext) {
		super();
		Assert.notNull(servletContext, "servletContext cannot be null");
		this.servletContext = servletContext;
	}

	public void pluginModified(PluginModificationEvent event) {
		Set<String> modified = getModifiedPlugins(event);

		if (!modified.isEmpty()) {
			ImpalaBootstrapFactory factory = (ImpalaBootstrapFactory) servletContext
					.getAttribute(ImpalaContextLoader.IMPALA_FACTORY_PARAM);
			
			PluginStateManager pluginStateManager = factory.getPluginStateManager();

			ParentSpec originalSpec = pluginStateManager.getParentSpec();
			ParentSpec newSpec = pluginStateManager.cloneParentSpec();
			for (String pluginName : modified) {
				PluginModificationCalculator calculator = new StrictPluginModificationCalculator();
				PluginTransitionSet transitions = calculator.reload(originalSpec, newSpec, pluginName);
				pluginStateManager.processTransitions(transitions);
			}
		}
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

}
