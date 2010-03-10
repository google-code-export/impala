/*
 * Copyright 2007-2010 the original author or authors.
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

package org.impalaframework.module.monitor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.impalaframework.util.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

/**
 * Class which watches a particular resource for new bean definitions to load for a a particular
 * application contexts
 */
public class RootModuleContextMonitor {

	private static final Log logger = LogFactory.getLog(RootModuleContextMonitor.class);

	private final BeanDefinitionReader beanDefinitionReader;

	private final Resource resourceToWatch;

	private final ScheduledExecutorService executor;

	private Set<String> locations = new HashSet<String>();

	public RootModuleContextMonitor(BeanDefinitionReader beanDefinitionReader, Resource resource,
			ScheduledExecutorService executor) {
		super();

		Assert.notNull(beanDefinitionReader);
		Assert.notNull(resource);
		Assert.notNull(beanDefinitionReader.getRegistry());
		
		this.beanDefinitionReader = beanDefinitionReader;
		this.resourceToWatch = resource;
		this.executor = executor;
	}

	void addConfigLocations(String location) {
		// Allow a subclass to provide custom initialization of the reader,
		// then proceed with actually loading the bean definitions.
		int loaded = beanDefinitionReader.loadBeanDefinitions(location);

		if (logger.isInfoEnabled())
			logger.info("Added " + loaded + " bean definitions from location " + location);
	}

	public void setupMonitor() {

		Runnable runnable = new Runnable() {
			public void run() {
				try {

					if (logger.isDebugEnabled())
						logger.debug("Inspecting for new context files to load");

					File file = resourceToWatch.getFile();

					if (file.exists()) {

						InputStreamReader reader = new InputStreamReader(new FileInputStream(file), "UTF8");
						List<String> readLines = FileUtils.readLines(reader);

						for (String line : readLines) {
							line = line.trim();
							if (line.length() > 0 && !line.startsWith("#")) {
								if (!locations.contains(line)) {
									
									if (logger.isDebugEnabled())
										logger.debug("Adding location " + line);
									
									RootModuleContextMonitor.this.addConfigLocations(line);
									locations.add(line);
								}
							}
						}
					}
					else {
						if (logger.isDebugEnabled())
							logger.debug("File " + file + " does not exist");
					}
				}
				catch (IOException e) {
					logger.error("Unable to read file " + resourceToWatch.getFilename());
				}
			}
		};

		executor.scheduleWithFixedDelay(runnable, 10, 10, TimeUnit.SECONDS);
	}

}
