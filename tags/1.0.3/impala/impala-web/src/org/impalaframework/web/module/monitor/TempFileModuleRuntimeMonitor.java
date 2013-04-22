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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.module.monitor.ModuleChangeMonitor;
import org.impalaframework.module.runtime.BaseModuleRuntime;
import org.springframework.core.io.Resource;

/**
 * Implements a strategy for passing a list of monitorable resources to
 * {@link ModuleChangeMonitor} based on the assumption that module updates will be first copied to
 * int a temporary file (with the extension .tmp) in WEB-INF/modules. 
 * 
 * Modified modules are copied from the staging directory before module loading occurs.
 * 
 * @author Phil Zoio
 */
public class TempFileModuleRuntimeMonitor extends BaseStagingFileModuleRuntimeMonitor {

    private static Log logger = LogFactory.getLog(BaseModuleRuntime.class);

    protected Resource getTempFileResource(Resource resource) {
        final File file = getFileFromResource(resource);
        if (file != null) {
            String name = file.getName();
            String tempFileName = name.replace(".jar", ".tmp");
            try {
                return resource.createRelative(tempFileName);
            } catch (Exception e) {
                logger.error("Problem creating relative file '" + tempFileName + "' for resource '" + resource.getDescription() + "'", e);
                return null;
            }
        } else {
            return null;
        }
    }
    
}
