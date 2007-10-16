/*
 * Copyright 2007 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package net.java.impala.spring.monitor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.java.impala.monitor.FileMonitor;
import net.java.impala.monitor.FileMonitorImpl;
import net.java.impala.monitor.Reloadable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Monitors provided resource, looking for resources to reload
 * @author Phil Zoio
 */
@Deprecated
public class ContextReloader {

	private static final Log log = LogFactory.getLog(ContextReloader.class);

	private static final int DEFAULT_INITIAL_DELAY_SECONDS = 10;

	private static final int DEFAULT_INTERVAL_SECONDS = 5;

	private int initialDelay;

	private int interval;

	private long lastReloaded;

	private FileMonitor fileMonitor;

	private ScheduledExecutorService executor;

	public void start(final Reloadable reloadable) {

		setDefaultsIfNecessary();

		lastReloaded = System.currentTimeMillis();
		executor.scheduleWithFixedDelay(new Runnable() {

			public void run() {

				long start = System.currentTimeMillis();
				long lastModified = fileMonitor.lastModified(reloadable.getResourcesToMonitor());
				long end = System.currentTimeMillis();

				if (lastReloaded < lastModified) {
					logModified(reloadable, start, end, true);
					reloadable.reload();
					lastReloaded = System.currentTimeMillis();
				}
				else {
					logModified(reloadable, start, end, false);
				}

			}

			private void logModified(final Reloadable reloadable, long start, long end, boolean modified) {
				if (log.isDebugEnabled()) {
					String prefix = (modified ? "M" : "No m");
					log.debug(prefix + "modifications detected in " + reloadable.getResourcesToMonitor() + " in "
							+ (end - start) + "ms");
				}
			}

		}, DEFAULT_INITIAL_DELAY_SECONDS, DEFAULT_INTERVAL_SECONDS, TimeUnit.SECONDS);
	}

	void setDefaultsIfNecessary() {
		if (fileMonitor == null) {
			fileMonitor = new FileMonitorImpl();
		}

		if (initialDelay == 0) {
			initialDelay = DEFAULT_INITIAL_DELAY_SECONDS;
		}

		if (interval == 0) {
			interval = DEFAULT_INTERVAL_SECONDS;
		}

		if (executor == null)
			executor = Executors.newSingleThreadScheduledExecutor();
	}

	public void stop() {
		executor.shutdown();
	}

	/* ****************** package level getters ******************* */

	ScheduledExecutorService getExecutor() {
		return executor;
	}

	FileMonitor getFileMonitor() {
		return fileMonitor;
	}

	int getInitialDelay() {
		return initialDelay;
	}

	int getInterval() {
		return interval;
	}

	long getLastReloaded() {
		return lastReloaded;
	}

}
