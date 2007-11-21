package org.impalaframework.plugin.web;

import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.commons.lang.SerializationUtils;
import org.impalaframework.plugin.monitor.BasePluginModificationListener;
import org.impalaframework.plugin.monitor.PluginModificationEvent;
import org.impalaframework.plugin.monitor.PluginModificationListener;
import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.spec.modification.PluginModificationCalculator;
import org.impalaframework.plugin.spec.modification.PluginTransitionSet;
import org.impalaframework.plugin.spec.transition.PluginStateManager;
import org.springframework.util.Assert;

//FIXME add test
public class WebPluginModificationListener extends BasePluginModificationListener implements PluginModificationListener {

	private ServletContext servletContext;

	public WebPluginModificationListener(ServletContext servletContext) {
		super();
		Assert.notNull(servletContext, "servletContext cannot be null");
		this.servletContext = servletContext;
	}

	public void pluginModified(PluginModificationEvent event) {
		Set<String> modified = getModifiedPlugins(event);

		if (!modified.isEmpty()) {
			PluginStateManager contextHolder = (PluginStateManager) servletContext
					.getAttribute(RegistryBasedImpalaContextLoader.CONTEXT_HOLDER_PARAM);

			ParentSpec originalSpec = contextHolder.getParentSpec();
			ParentSpec newSpec = (ParentSpec) SerializationUtils.clone(originalSpec);
			for (String pluginName : modified) {
				PluginModificationCalculator calculator = new PluginModificationCalculator();
				PluginTransitionSet transitions = calculator.reload(originalSpec, newSpec, pluginName);
				contextHolder.processTransitions(transitions);
			}
		}
	}
}
