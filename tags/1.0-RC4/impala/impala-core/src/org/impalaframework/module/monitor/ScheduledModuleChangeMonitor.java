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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.file.FileMonitor;
import org.impalaframework.file.monitor.FileMonitorImpl;
import org.impalaframework.file.monitor.MonitorFileFilter;
import org.impalaframework.util.ArrayUtils;
import org.impalaframework.util.ResourceUtils;
import org.springframework.core.io.Resource;

/**
 * {@link ModuleChangeMonitor} which scans for changes in modules and automatically 
 * reloads modules when detecting changes.
 * 
 * @author Phil Zoio
 */
public class ScheduledModuleChangeMonitor implements ModuleChangeMonitor {

    private static final Log logger = LogFactory.getLog(ScheduledModuleChangeMonitor.class);
    
    private static final int DEFAULT_INITIAL_DELAY_SECONDS = 10;

    private static final int DEFAULT_INTERVAL_SECONDS = 2;

    private int initialDelay = DEFAULT_INTERVAL_SECONDS;

    private int checkInterval = DEFAULT_INITIAL_DELAY_SECONDS;
    
    private String[] autoReloadFileIncludes;
    
    private String[] autoReloadFileExcludes;
    
    private FileMonitor fileMonitor;

    private ScheduledExecutorService executor;

    public ScheduledModuleChangeMonitor() {
        super();
    }   

    private List<ModuleContentChangeListener> modificationListeners = new ArrayList<ModuleContentChangeListener>();

    private Map<String, ResourceInfo> resourcesToMonitor = new ConcurrentHashMap<String, ResourceInfo>();
    
    /* ************************* ModuleChangeMonitor methods ************************** */

    /**
     * Adds {@link ModuleContentChangeListener}
     */
    public void addModificationListener(ModuleContentChangeListener listener) {
        modificationListeners.add(listener);
    }

    /**
     * Wires in the {@link Resource} instances which should be monitored on behalf of individual modules
     */
    public void setResourcesToMonitor(String moduleName, Resource[] resources) {
        if (!ArrayUtils.isNullOrEmpty(resources)) {
            logger.info("Monitoring for changes in module " + moduleName + ": " + Arrays.toString(resources));
            resourcesToMonitor.put(moduleName, new ResourceInfo(System.currentTimeMillis(), resources));
        }
        else {
            logger.info("No resources to monitor for module " + moduleName);
        }
    }

    /**
     * Starts the {@link ScheduledExecutorService}, settings off a task which periodically 
     * checks the monitorable resources and potentially initiates a module reload when necessary, by sending a 
     * {@link ModuleChangeEvent} to registered {@link ModuleContentChangeListener}s.
     */
    public void start() {

        logger.info("Starting " + this.getClass().getName() + " with fixed delay of "  + initialDelay + " and interval of " + checkInterval);
        
        setDefaultsIfNecessary();
        executor.scheduleWithFixedDelay(new Runnable() {

            public void run() {

                try {
                    
                    if (!checkForChanges()) {
                        return;
                    }

                    List<ModuleChangeInfo> modified = new LinkedList<ModuleChangeInfo>();

                    final Set<String> moduleNames = resourcesToMonitor.keySet();
                    
                    for (String moduleName : moduleNames) {
                        
                        ResourceInfo ri = resourcesToMonitor.get(moduleName);
                        if (ri != null) {
                            // should be null except for case where item is
                            // removed
                            File[] files = ResourceUtils.getFiles(ri.resources);
                            
                            //get the last modified files
                            long lastModified = fileMonitor.lastModified(files);

                            if (lastModified > ri.lastModified) {
                                // add to the list of modified modules
                                modified.add(new ModuleChangeInfo(moduleName));
                                // set the ResourceInfo object
                                ri.lastModified = lastModified;
                            }
                        }
                    }

                    if (!modified.isEmpty()) {
                        logger.info("Found modified modules: " + modified);
                        final ModuleChangeEvent event = new ModuleChangeEvent(modified);
                        for (ModuleContentChangeListener listener : modificationListeners) {
                            listener.moduleContentsModified(event);
                        }
                    }
                    else {
                        if (logger.isDebugEnabled()) logger.debug("Completed check for modified modules. No modified module contents found");
                    }

                }
                catch (Exception e) {
                    logger.error("Error invoking module content change listeners: " + e.getMessage(), e);
                }
            }
        }, initialDelay, checkInterval, TimeUnit.SECONDS);
    }

    public void stop() {
        executor.shutdown();
    }

    /* ************************* Helper methods ************************** */
    
    void setDefaultsIfNecessary() {
        if (fileMonitor == null) {
            FileMonitorImpl fileMonitor = new FileMonitorImpl();
            final List<String> includes = autoReloadFileIncludes != null ? Arrays.asList(autoReloadFileIncludes) : new ArrayList<String>();
            final List<String> excludes = autoReloadFileExcludes != null ? Arrays.asList(autoReloadFileExcludes) : new ArrayList<String>();
            fileMonitor.setFileFilter(new MonitorFileFilter(includes, excludes));
            this.fileMonitor = fileMonitor;
        }

        if (executor == null)
            executor = Executors.newSingleThreadScheduledExecutor();
    }

    /* ************************* Protected methods ************************** */
    
    /**
     * Simply returns true. More sophisticated implementations can implement other strategies for determining whether to 
     * initiate a module reload, such as by inspecting a touch file.
     */
    protected boolean checkForChanges() {
        return true;
    }

    /* ************************* Dependency injection setters ************************** */
    
    public void setModificationListeners(List<ModuleContentChangeListener> modificationListeners) {
        this.modificationListeners.clear();
        this.modificationListeners.addAll(modificationListeners);
    }

    public void setInitialDelay(int initialDelay) {
        this.initialDelay = initialDelay;
    }

    public void setCheckInterval(int interval) {
        this.checkInterval = interval;
    }
    
    public void setAutoReloadFileIncludes(String[] autoReloadIncludes) {
        this.autoReloadFileIncludes = autoReloadIncludes;
    }
    
    public void setAutoReloadFileExcludes(String[] autoReloadExcludes) {
        this.autoReloadFileExcludes = autoReloadExcludes;
    }

    /* ************************* ResourceInfo private class ************************** */
    
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
