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

package org.impalaframework.bootstrap;

import org.impalaframework.constants.LocationConstants;

/**
 * Constants representing Impala bootstrap properties typically found in <i>impala.properties</i>.
 * @author Phil Zoio
 */
public interface CoreBootstrapProperties {

    /**
     * True if modules should be placed in directories on the file system rather than in jars. Main use is for 
     * running embedded within IDE
     */
    String EMBEDDED_MODE = "embedded.mode";
    
    /**
     * Default value for {@link #EMBEDDED_MODE} property
     */
    boolean EMBEDDED_MODE_DEFAULT = false;
    
    /**
     * Module which should be exposed outside of the Impala as the module root, for example for use in as the Spring 
     * web application root context
     */
    String EXTERNAL_ROOT_MODULE_NAME = "external.root.module.name";
    
    /**
     * Default value for {@link #EXTERNAL_ROOT_MODULE_NAME} property. Empty, meaning that the normal module root
     * should be used
     */
    String EXTERNAL_ROOT_MODULE_NAME_DEFAULT = "";

    /**
     * A comma or space separated list of context locations. If this property is set, then other properties which would otherwise
     * be used to determine which Impala application context XML files to include will be ignored. The value set in this file is taken as
     * the truth. Note that if the value does not end in .xml, the it is assumed to take the form impala-suppliedname.xml. In other words,
     * <i>impala-</i> is used as a prefix, and <i>.xml</i> is used as a suffix.
     */
    String ALL_LOCATIONS = "all.locations";
    
    /**
     * Used to add additional context locations. Use when you want simply want to add one more context locations to the set which would 
     * otherwise be used. Note that if the value does not end in .xml, the it is assumed to take the form impala-suppliedname.xml. In other words,
     * <i>impala-</i> is used as a prefix, and <i>.xml</i> is used as a suffix.
     */
    String EXTRA_LOCATIONS = "extra.locations";
    
    /**
     * The module management type to be used. Permissible values are <i>shared</i>, <i>hierarchical</i> and <i>graph</i>. 
     * The default value is <i>graph</i>.
     */
    String MODULE_TYPE = "classloader.type";
    
    /**
     * Indicates the visibility type of Spring beans when using the graph class
     * loader. Permissible values are <i>none</i>, <i>graphOrdered</i>,
     * <i>parentFirst</i> and <i>parentOnly</i>.
     */
    String GRAPH_BEAN_VISIBILITY_TYPE = "graph.bean.visibility.type";
    
    /**
     * Default value for {@link #GRAPH_BEAN_VISIBILITY_TYPE} property
     */
    String GRAPH_BEAN_VISIBILITY_TYPE_DEFAULT = "graphOrdered";
    
    /**
     * This is used to specify whether Impala should attempt to load classes by first checking the application or system class path, 
     * before examining the module class path. The default is <i>true</i>.
     */
    String PARENT_CLASS_LOADER_FIRST = "parent.classloader.first";
    
    /**
     * Whether class loader should be load time weaving aware
     */
    String LOAD_TIME_WEAVING_ENABLED = "load.time.weaving.enabled";
    
    /**
     * Default value for {@link #LOAD_TIME_WEAVING_ENABLED} property
     */
    boolean LOAD_TIME_WEAVING_ENABLED_DEFAULT = false;    
    
    /**
     * The module root directory, used as the base location when looking for module jars or directories
     */
    String WORKSPACE_ROOT = LocationConstants.WORKSPACE_ROOT_PROPERTY;
    
    /**
     * The directory or jar relative to {@link #WORKSPACE_ROOT} in which to find modules
     */
    String MODULE_CLASS_DIRECTORY = LocationConstants.MODULE_CLASS_DIR_PROPERTY;
    
    /**
     * The directory or jar relative to {@link #WORKSPACE_ROOT} in which to find module resources. May be null.
     */
    String MODULE_RESOURCE_DIRECTORY = LocationConstants.MODULE_RESOURCE_DIR_PROPERTY;

    /**
     * The version of the application. Used, for example, when searching for module jar files. For example, if version is 1.1
     * then <code>mymodule</code> may be found in a jar file called <code></code>mymodule-1.1.jar</code>
     */
    String APPLICATION_VERSION = LocationConstants.APPLICATION_VERSION;
    
    /**
     * Property which applies for creation of proxies.
     * True if the interceptor should allow the call to proceed (with a dummy
     * value returned) if no service is present. Primarily present for testing
     * purposes. Defaults to false.
     */
    String PROXY_ALLOW_NO_SERVICE = "proxy.allow.no.service";
    
    /**
     * Default value for {@link #PROXY_ALLOW_NO_SERVICE}
     */
    boolean PROXY_ALLOW_NO_SERVICE_DEFAULT = false;

    /**
     * Property which applies for creation of proxies.
     * Whether to set the context class loader to the class loader of the module
     * contributing the bean being invoked. This is to mitigate possibility of
     * exceptions being caused by calls to
     * <code>Thread.setContextClassLoader()</code> being propagated across
     * modules
     */
    String PROXY_SET_CONTEXT_CLASSLOADER = "proxy.set.context.classloader";
    
    /**
     * Default value for {@link #PROXY_SET_CONTEXT_CLASSLOADER}
     */
    boolean PROXY_SET_CONTEXT_CLASSLOADER_DEFAULT = true;

    /**
     * Property which applies for creation of proxies.
     * The number of times the interceptor should retry before giving up
     * attempting to obtain a proxy. For example, if retryCount is set to three,
     * Impala will try one initially, plus another three times, before giving up
     * attempting to obtain the service reference.
     */
    String PROXY_MISSING_SERVICE_RETRY_COUNT = "proxy.missing.service.retry.count";
    
    /**
     * Default value for {@link #PROXY_SERVICE_MISSING_RETRY_COUNT}
     */
    int PROXY_MISSING_SERVICE_RETRY_COUNT_DEFAULT = 0;

    /**
     * Property which applies for creation of proxies.
     * The amount of time in milliseconds between successive retries if
     * {@link #retryCount} is greater than zero
     */
    String PROXY_MISSING_SERVICE_RETRY_INTERVAL = "proxy.missing.service.retry.interval";
    
    /**
     * Default value for {@link #PROXY_SERVICE_MISSING_RETRY_INTERVAL}
     */
    int PROXY_MISSING_SERVICE_RETRY_INTERVAL_DEFAULT = 1000;
}
