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

package org.impalaframework.web.module.monitor;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.monitor.DefaultModuleRuntimeMonitor;
import org.impalaframework.module.monitor.ModuleChangeMonitor;
import org.impalaframework.module.runtime.BaseModuleRuntime;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

/**
 * Implements a strategy for passing a list of monitorable resources to
 * {@link ModuleChangeMonitor} based on the assumption that module updates will be first copied to
 * a staging directory. It is this directory which is monitored for changed modules.
 * 
 * Modified modules are copied to the application directory after module loading occurs.
 * 
 * @author Phil Zoio
 */
public abstract class BaseStagingFileModuleRuntimeMonitor extends DefaultModuleRuntimeMonitor {

    private static Log logger = LogFactory.getLog(BaseModuleRuntime.class);

    /**
     * Identifies the staging resource which will replace the existing module resource
     */
    protected abstract Resource getTempFileResource(Resource resource);
    
    /**
     * If .jar file resource is found in module locations, then attempts to copy a file which 
     * has the same name but has a .tmp extension in place of the .jar file.
     */
    @Override
    public void beforeModuleLoads(ModuleDefinition definition) {
        
        //get list of locations for module
        final List<Resource> locations = getLocations(definition.getName());
        
        final Iterator<Resource> iterator = locations.iterator();
        boolean found = false;
        while (iterator.hasNext() && !found) {
            
            Resource resource = iterator.next();
            
            //get file associated with resource
            File file = getFileFromResource(resource);
            if (file.getName().endsWith(".jar")) {
                found = true;
                maybeCopyToResource(resource, file);
            }
        }
    }

    int maybeCopyToResource(Resource resource, File file) {
        
        int result = 0;
        final Resource tempFileResource = getTempFileResource(resource);
        if (tempFileResource.exists()) {
            final File tempFile = getFileFromResource(tempFileResource);
            
            File backup = new File(file.getParentFile(), file.getName()+".backup");
            boolean renamed = file.renameTo(backup);
            
            try {
                FileCopyUtils.copy(tempFile, file);
                result++;
                
                if (renamed) {
                    backup.delete();
                    tempFile.delete();
                    result++;
                }
                
            } catch (IOException e) {
                logger.error("Unable to copy '" + tempFile + "' to '" + file + "'", e);

                result--;
                
                //set backup back to renamed file
                if (renamed) {
                    result--;
                    final boolean renamedBack = backup.renameTo(file);
                    if (renamedBack) {
                        result--;
                    }
                }
            }
        }
        
        //0 no tempfile present
        //1 means file was copied but no backup could be taken
        //2 means file was copied and backup was taken
        //-1 means that copy failed and no backup was taken
        //-2 means copy failed, backup taken but could not be renamed back
        //-3 means copy failed, backup taken and renamed back
        
        return result;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    protected List<Resource> getMonitorableLocations(ModuleDefinition definition, List<Resource> classLocations) {

        Resource classLocationResource = getTemporaryResourceName(classLocations);
        List<Resource> monitorableLocations = (classLocationResource != null ? Collections.singletonList(classLocationResource) : Collections.EMPTY_LIST);
        return monitorableLocations;
    }

    Resource getTemporaryResourceName(List<Resource> classLocations) {
        
        for (Resource resource : classLocations) {
            File file = getFileFromResource(resource);
            if (file != null && file.getName().endsWith(".jar")) {
                return getTempFileResource(resource);
            }
        }
        return null;
    }

    /**
     * Attempts to return {@link File} from resource. If this fails, logs and returns null.
     */
    protected File getFileFromResource(Resource resource) {
        File file;
        try {
            file = resource.getFile();
        } catch (IOException e) {
            logger.error("Problem getting file for module resource " + resource.getDescription(), e);
            file = null;
        }
        return file;
    }
    
}
