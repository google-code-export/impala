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

package org.impalaframework.web.module.listener;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.module.monitor.ScheduledModuleChangeMonitor;
import org.springframework.core.io.Resource;

/**
 * Extends {@link ScheduledModuleChangeMonitor} by providing an implementation for 
 * {@link #checkForChanges()}. Here, it uses an optionally wired in {@link #touchFile}
 * @author Phil Zoio
 */
public class WebScheduledModuleChangeMonitor extends ScheduledModuleChangeMonitor {
    
    private static final Log logger = LogFactory.getLog(WebScheduledModuleChangeMonitor.class);
    
    private boolean useTouchFile;
    
    private Resource touchFile;
    
    private AtomicLong timestamp = new AtomicLong();

    /**
     * Sets the last modified timestamp if the resource is available.
     */
    @Override
    public void start() {
        
        if (!useTouchFile) {

            if (logger.isDebugEnabled()) {
                logger.debug("Not using touch file");
            }
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("Starting " + WebScheduledModuleChangeMonitor.class.getName() + " with touch file " + touchFile + ". File exists: " + touchFile.exists());
            }
            
            long lastModified = getLastModified();
            timestamp.set(lastModified);
        }
        super.start();
    }

    /**
     * If touchFile is null then returns null.
     * If touchFile is not null but is not present, then returns false.
     * If touchFile is not null and is present, then return false if the touchFile's timestamp has been updated.
     * Updates the {@link #timestamp} property only if a later timestamp is retrieved.
     */
    @Override
    protected boolean checkForChanges() {
        if (touchFile == null || !useTouchFile) {
            return super.checkForChanges();
        } 
        
        final long currentModified = timestamp.get();
        final long newModified = getLastModified();
        
        if (newModified > currentModified) {
            timestamp.set(newModified);
            
            if (logger.isDebugEnabled()) {
                logger.debug("New timestamp for touch file '" + touchFile + "': " + new Date(newModified));
            }
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Returns the last modified of the touch file. If the touch file is null or does not exist, return 0
     * @return
     */
    long getLastModified() {
        long lastModified = 0L;
        
        if (touchFile != null && touchFile.exists()) {
            
            File file;
            try {
                file = touchFile.getFile();
                lastModified = file.lastModified();

                if (logger.isDebugEnabled()) {
                    logger.debug("Last modified for touch file '" + touchFile + "': " + new Date(lastModified));
                }
            } catch (IOException e) {
            }
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("No valid touch file to get last modified " + touchFile);
            }
        }
        return lastModified;
    }

    /**
     * @param touchFile the touch file, which can be null
     */
    public void setTouchFile(Resource touchFile) {
        this.touchFile = touchFile;
    }

    public void setUseTouchFile(boolean useTouchFile) {
        this.useTouchFile = useTouchFile;
    }
}
