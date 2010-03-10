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

package org.impalaframework.config;

import java.io.File;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

/**
 * Class with the responsibility for tracking whether a set of resources have been modified
 * @author Phil Zoio
 */
public class LocationModificationStateHolder {

    private Log log = LogFactory.getLog(LocationModificationStateHolder.class);
    
    private Resource[] locations;

    private long lastModified = 0L;
    
    private boolean returnOnFirstCheck = false;
    
    public boolean isModifiedSinceLastCheck() {
        
        Assert.notNull(locations, "locations cannot be null");
        
        boolean load = false;
        
        long newLastModified = 0L;

        for (Resource resource : locations) {
            try {
                File file = resource.getFile();
                long fileLastModified = file.lastModified();
                newLastModified = Math.max(newLastModified, fileLastModified);
                
                if (log.isDebugEnabled()) log.debug("Last modified for resource " + file + ": " + fileLastModified);
                
            } catch (IOException e) {
                System.out.println("Unable to get last modified for resource " + resource);
            }
        }

        long oldLastModified = this.lastModified;
        long diff = newLastModified - oldLastModified;
        if (diff > 0) {
            if (oldLastModified == 0L) {
                load = returnOnFirstCheck;
            } else {
                load = true;
            }
            
            if (log.isDebugEnabled()) 
                log.debug("File has been updated more recently - reloading. Old: " + oldLastModified + ", new: " + newLastModified);
        } 

        this.lastModified = newLastModified;
        
        return load;
    }

    public void setLocation(Resource resourceLocation) {
        setLocations(new Resource[] { resourceLocation });
    }
    
    public void setLocations(Resource[] resourceLocations) {
        this.locations = resourceLocations;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public void setReturnOnFirstCheck(boolean returnOnFirstCheck) {
        this.returnOnFirstCheck = returnOnFirstCheck;
    }

    public Resource[] getLocations() {
        return locations;
    }

    public long getLastModified() {
        return lastModified;
    }
    
}
