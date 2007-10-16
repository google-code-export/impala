package net.java.impala.spring.monitor;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.java.impala.monitor.FileMonitor;
import net.java.impala.monitor.FileMonitorImpl;
import net.java.impala.util.ResourceUtils;

import org.springframework.core.io.Resource;

/**
 * @author Phil Zoio
 */
public class ScheduledPluginMonitor implements PluginMonitor {

	private static final int DEFAULT_INITIAL_DELAY_SECONDS = 10;

	private static final int DEFAULT_INTERVAL_SECONDS = 2;

	private int initialDelay = DEFAULT_INTERVAL_SECONDS;

	private int checkInterval = DEFAULT_INITIAL_DELAY_SECONDS;

	private FileMonitor fileMonitor;

	private ScheduledExecutorService executor;

	private List<PluginModificationListener> modificationListeners = new ArrayList<PluginModificationListener>();

	private Map<String, ResourceInfo> resourcesToMonitor = new ConcurrentHashMap<String, ResourceInfo>();

	public void addModificationListener(PluginModificationListener listener) {
		modificationListeners.add(listener);
	}

	public void setResourcesToMonitor(String pluginName, Resource[] resources) {
		if (resources != null) {
			System.out.println("Monitoring plugin " + pluginName);
			resourcesToMonitor.put(pluginName, new ResourceInfo(System.currentTimeMillis(), resources));
		}
		else {
			System.out.println("No resources to monitor for plugin " + pluginName);
			// FIXME warning message goes here. Maybe enforce usins assert
		}
	}

	protected void setInitialDelay(int initialDelay) {
		this.initialDelay = initialDelay;
	}

	protected void setInterval(int interval) {
		this.checkInterval = interval;
	}

	void setDefaultsIfNecessary() {
		if (fileMonitor == null) {
			fileMonitor = new FileMonitorImpl();
		}

		if (executor == null)
			executor = Executors.newSingleThreadScheduledExecutor();
	}

	public void start() {

		setDefaultsIfNecessary();
		executor.scheduleWithFixedDelay(new Runnable() {

			public void run() {

				try {

					List<PluginModificationInfo> modified = new LinkedList<PluginModificationInfo>();

					final Set<String> pluginNames = resourcesToMonitor.keySet();
					for (String pluginName : pluginNames) {
						ResourceInfo ri = resourcesToMonitor.get(pluginName);
						if (ri != null) {
							// should be null except for case where item is
							// removed
							File[] files = ResourceUtils.getFiles(ri.resources);
							long lastModified = fileMonitor.lastModified(files);

							if (lastModified > ri.lastModified) {
								// add to the list of modified plugins
								modified.add(new PluginModificationInfo(pluginName));
								// set the ResourceInfo object
								ri.lastModified = lastModified;
							}
						}
					}

					if (!modified.isEmpty()) {
						System.out.println("Found modified plugins " + modified);
						final PluginModificationEvent event = new PluginModificationEvent(modified);
						for (PluginModificationListener listener : modificationListeners) {
							listener.pluginModified(event);
						}
					}
					else {
						//FIXME debug System.out.println("No modified plugin found");
					}

				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, initialDelay, checkInterval, TimeUnit.SECONDS);
	}

	public void stop() {
		executor.shutdown();
	}

	private class ResourceInfo {
		private long lastModified;

		private Resource[] resources;

		public ResourceInfo(long lastModified, Resource[] resources) {
			super();
			this.lastModified = lastModified;
			this.resources = resources;
		}
	}
}
