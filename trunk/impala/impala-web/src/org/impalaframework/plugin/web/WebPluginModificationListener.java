package org.impalaframework.plugin.web;

import java.util.Collection;
import java.util.Set;

import javax.servlet.ServletContext;

import org.impalaframework.plugin.monitor.BasePluginModificationListener;
import org.impalaframework.plugin.monitor.PluginModificationEvent;
import org.impalaframework.plugin.monitor.PluginModificationListener;
import org.impalaframework.plugin.spec.PluginSpec;
import org.impalaframework.spring.SpringContextHolder;
import org.springframework.util.Assert;


//FIXME add test
//FIXME more sophisticated implementation which does not duplicate reloads
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
			SpringContextHolder contextHolder = (SpringContextHolder) servletContext
					.getAttribute(RegistryBasedImpalaContextLoader.CONTEXT_HOLDER_PARAM);

			for (String pluginName : modified) {
				PluginSpec loadedPlugin = contextHolder.getPlugin(pluginName);
				removePlugin(contextHolder, loadedPlugin);
			}
			for (String pluginName : modified) {
				PluginSpec loadedPlugin = contextHolder.getPlugin(pluginName);
				addPlugin(contextHolder, loadedPlugin);
			}
		}
	}

	private void removePlugin(SpringContextHolder contextHolder, PluginSpec plugin) {
		final Collection<PluginSpec> plugins = plugin.getPlugins();
		for (PluginSpec spec : plugins) {
			removePlugin(contextHolder, spec);
		}
		contextHolder.removePlugin(plugin, true);
	}

	private void addPlugin(SpringContextHolder contextHolder, PluginSpec plugin) {
		contextHolder.addPlugin(plugin);		
		final Collection<PluginSpec> plugins = plugin.getPlugins();
		for (PluginSpec spec : plugins) {
			addPlugin(contextHolder, spec);
		}
	}

}

