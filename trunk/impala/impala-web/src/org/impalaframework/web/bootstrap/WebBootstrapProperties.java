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
    
    boolean AUTO_RELOAD_MODULES_DEFAULT = false;

    /**
     * Whether to use a touch file to detect changes which could trigger a module reload.
     * If true, then will only check files for modifications if a modification in the
     * timestamp of the touch file is detected.
     */
    String USE_TOUCH_FILE = "use.touch.file";

    boolean USE_TOUCH_FILE_DEFAULT = false;

    /**
     * The touch file resource which will be monitored. If not present and 'use.touch.file'
     * is true, then auto.deploy is effectively turned off.
     */
    String TOUCH_FILE = "touch.file";

    String TOUCH_FILE_DEFAULT = "/WEB-INF/modules/touch.txt";
    
    /**
     * The period in seconds between successive checks for modifications when 
     * auto.deploy is used. 
     */
    String AUTO_RELOAD_CHECK_INTERVAL = "auto.reload.check.interval";
    
    int AUTO_RELOAD_CHECK_INTERVAL_DEFAULT = 2;
    
    /**
     * The delay in seconds before the first check for modifications when
     * auto.deploy is used. 
     */
    String AUTO_RELOAD_CHECK_DELAY = "auto.reload.check.delay";
    
    int AUTO_RELOAD_CHECK_DELAY_DEFAULT = 10;
        
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
    
    String AUTO_RELOAD_MONITORING_TYPE_DEFAULT = "default";
    
    /**
     * If {@link #AUTO_RELOAD_MONITORING_TYPE} is set to 'stagingDirectory',
     * this property allows you to configure the staging directory location.
     * Set relative to the module directory (typically WEB-INF/modules).
     * Default is ../staging.
     */
    String AUTO_RELOAD_STAGING_DIRECTORY = "auto.reload.staging.directory";
    
    String AUTO_RELOAD_STAGING_DIRECTORY_DEFAULT = "../staging";
    
    /**
     * Whether {@link ServletContext} attributes and resources should be partitioned
     * across modules. This makes it possible to set attributes and make resources
     * visible within modules only through {@link ServletContext} methods.
     */
    String PARTITIONED_SERVLET_CONTEXT = "partitioned.servlet.context";
    
    boolean PARTITIONED_SERVLET_CONTEXT_DEFAULT = true;

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

    boolean SESSION_MODULE_PROTECTION_DEFAULT = false;
    
    /**
     * This property must be set to true if you want to run a web application
     * embedded within the IDE, without any specific packaging required.
     * Suitable for a fast develop/deploy/test cycle. The alternative involves
     * creating a war file with modules placed in jars files in
     * <i>WEB-INF/modules</i>. This is the default, and is the valid production
     * setting.
     */
    String EMBEDDED_MODE = "embedded.mode";
    
    boolean EMBEDDED_MODE_DEFAULT = false;

    /**
     * If a session attribute is lost because it cannot be "reloaded" maintain the 
     * existing session. Default is <i>true</i>.
     */
    String PRESERVE_SESSION_ON_RELOAD_FAILURE = "preserve.session.on.reload.failure";
    
    boolean PRESERVE_SESSION_ON_RELOAD_FAILURE_DEFAULT = true;

    /**
     * Used to indicate that the URL to module mapping strategy supports top level paths.
     * By default, Impala will use the servlet path part of the URL to determine the module. For example,
     * the URL http://localhost:8080/myapp/module1/home.do will map to the module 'module1'.
     * With 'top.level.module.path.enabled' set to true, the module mapping strategy will also
     * support mapping URLs based on a portion of the top level path. For example, the URL
     * http://localhost:8080/myapp/module1home.do can be configured to point to the module module1.
     * In this example, the property 'top.level.module.prefixes' must contain 'module1' 
     * for this to work. This capability is useful for supporting frameworks which make it difficult
     * to use the servlet part of the URL for module selection. An example is Tapestry. However,
     * the downside is that all resources within the module must begin with the relevant 
     * top level prefix.
     * @see #TOP_LEVEL_MODULE_PREFIXES
     */
    String TOP_LEVEL_MODULE_PATH_ENABLED = "top.level.module.path.enabled";

    boolean TOP_LEVEL_MODULE_PATH_ENABLED_DEFAULT = false;
    
    /**
     * The portion of the path which is ignored for the purpose of module selection when dynamically
     * mapping requests to modules. For example, if a module name is 'webframeworks-webflow', then the 
     * URL http://localhost:8080/myapp/webflow/home.do will be mapped to the 'webframeworks-webflow' module.
     * Without this property set, then you would need to use the URL http://localhost:8080/myapp/webframeworks-webflow/home.do
     * to map to the 'webframeworks-webflow' module.
     */
    String WEB_MODULE_PREFIX = "web.module.prefix";

    String WEB_MODULE_PREFIX_DEFAULT = "";
    
    /**
     * If {@link #TOP_LEVEL_MODULE_PATH_ENABLED} is set to true, then this contains the comma-separated
     * list of prefixes which is used to direct requests to modules based on the top level path of the URL.
     * For example, if the value for 'top.level.module.prefixes' is <code>module1,module2</code>,
     * then a request with the URL http://localhost:8080/Module2resource will be directed to the module
     * 'module2'. Note that the prefix is case insensitive.
     * 
     * For MVC- or request-based web frameworks such as Spring MVC, this kind of module selection will typically not be necessary, as 
     * most of these give you a great deal of control over how you map requests to controllers or actions.
     * However, for some component based frameworks, much more restrictive assumptions are made about how
     * particular URLs map to elements of the application.
     */
    String TOP_LEVEL_MODULE_PREFIXES = "top.level.module.prefixes";

    String TOP_LEVEL_MODULE_PREFIXES_DEFAULT = "";

}
