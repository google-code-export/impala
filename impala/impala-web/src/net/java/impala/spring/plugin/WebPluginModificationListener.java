package net.java.impala.spring.plugin;

import java.util.Collection;
import java.util.Set;

import javax.servlet.ServletContext;

import org.springframework.util.Assert;

import net.java.impala.spring.SpringContextHolder;
import net.java.impala.spring.monitor.BasePluginModificationListener;
import net.java.impala.spring.monitor.PluginModificationEvent;
import net.java.impala.spring.monitor.PluginModificationListener;
import net.java.impala.spring.web.RegistryBasedImpalaContextLoader;

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
		contextHolder.removePlugin(plugin.getName());
	}

	private void addPlugin(SpringContextHolder contextHolder, PluginSpec plugin) {
		contextHolder.addPlugin(plugin);		
		final Collection<PluginSpec> plugins = plugin.getPlugins();
		for (PluginSpec spec : plugins) {
			addPlugin(contextHolder, spec);
		}
	}

}

