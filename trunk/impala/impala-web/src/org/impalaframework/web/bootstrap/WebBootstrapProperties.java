/*
 * Copyright 2007-2008 the original author or authors.
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

package org.impalaframework.web.bootstrap;

import javax.servlet.ServletContext;

/**
 * Configuration properties which are specific to running Impala in a web environment.
 * @author Phil Zoio
 */
public interface WebBootstrapProperties {

    /**
     * Whether modules should be automatically reloaded if the file/directory
     * containing the modules has modified. If true, a background thread will
     * monitor module files/folders and automatically reload modules when
     * changes are detected. Default is <b>false</b>.
     */
    String AUTO_RELOAD_MODULES = "auto.reload.modules";

    /**
     * Whether to use a touch file to detect changes which could trigger a module reload.
     * If true, then will only check files for modifications if a modification in the
     * timestamp of the touch file is detected.
     */
    String USE_TOUCH_FILE = "use.touch.file";

    /**
     * The touch file resource which will be monitored. If not present and 'use.touch.file'
     * is true, then auto.deploy is effectively turned off.
     */
    String TOUCH_FILE = "touch.file";

    /**
     * The period in seconds between successive checks for modifications when 
     * auto.deploy is used. 
     */
    String AUTO_RELOAD_CHECK_INTERVAL = "auto.reload.check.interval";
    
    /**
     * The delay in seconds before the first check for modifications when
     * auto.deploy is used. 
     */
    String AUTO_RELOAD_CHECK_DELAY = "auto.reload.check.delay";
        
    /**
     * The type of monitoring that is used to check for modifications when
     * using the auto.deploy option. The default corresponds with 'default'
     * which results in direct class path monitoring. An alternative value is 
     * 'tmpfile' which results in monitoring of classes with the extension
     * '.tmp' instead of '.jar'. Modified files are copied to their
     * jar file location during the module reload process. The advantage of the
     * 'tmpfile' type is that it means that file copying does not come
     * out of step with module loading.
     */
    String AUTO_RELOAD_MONITORING_TYPE = "auto.reload.monitoring.type";
    
    /**
     * Whether {@link ServletContext} attributes and resources should be partitioned
     * across modules. This makes it possible to set attributes and make resources
     * visible within modules only through {@link ServletContext} methods.
     */
    String PARTITIONED_SERVLET_CONTEXT = "partitioned.servlet.context";

    /**
     * Attempt to protect session state when module is reloaded. If class loader
     * which loaded original object in session is replaced, the object in
     * session will no longer be visible to application code once module is
     * reloaded. The solution is to clone the object via Java serialization,
     * replacing the class loader of session object during the cloning process.
     * The cloned object is then added to the session in place of the old object
     * with the stale class loader.
     */
    String SESSION_MODULE_PROTECTION = "session.module.protection";
    
    /**
     * This property must be set to true if you want to run a web application
     * embedded within the IDE, without any specific packaging required.
     * Suitable for a fast develop/deploy/test cycle. The alternative involves
     * creating a war file with modules placed in jars files in
     * <i>WEB-INF/modules</i>. This is the default, and is the valid production
     * setting.
     */
    String EMBEDDED_MODE = "embedded.mode";

    /**
     * If a session attribute is lost because it cannot be "reloaded" maintain the 
     * existing session. Default is <i>true</i>.
     */
    String PRESERVE_SESSION_ON_RELOAD_FAILURE = "preserve.session.on.reload.failure";

    String SPRING_PATH_MAPPING_ENABLED = "spring.path.mapping.enabled";
    
    String WEB_MODULE_PREFIX = "web.module.prefix";
    
    String TOP_LEVEL_MODULE_SUFFIXES = "top.level.module.suffixes";

}
