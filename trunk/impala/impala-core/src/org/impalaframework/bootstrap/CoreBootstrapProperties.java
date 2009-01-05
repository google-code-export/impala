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

package org.impalaframework.bootstrap;

import org.impalaframework.constants.LocationConstants;

public interface CoreBootstrapProperties {

	/**
	 * A comma or space separated list of context locations. If this property is set, then other properties which would otherwise
	 * be used to determine which Impala application context XML files to include will be ignored. The value set in this file is taken as
	 * the truth. Note that if the value does not end in .xml, the it is assumed to take the form impala-suppliedname.xml. In other words,
	 * <i>impala-</i> is used as a prefix, and <i>.xml</i> is used as a suffix.
	 */
	String ALL_LOCATIONS = "all.locations";
	
	/**
	 * An alias for {@link #ALL_LOCATIONS}
	 */
	String BOOTSTRAP_LOCATIONS = "bootstrapLocations";
	
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
	 * This is used to specify whether Impala should attempt to load classes by first checking the application or system class path, 
	 * before examining the module class path. The default is <i>true</i>.
	 */
	String PARENT_CLASS_LOADER_FIRST = "parent.classloader.first";
	
	String MODULE_CLASS_DIRECTORY = LocationConstants.MODULE_CLASS_DIR_PROPERTY;
	
	String WORKSPACE_ROOT = LocationConstants.WORKSPACE_ROOT_PROPERTY;

	String APPLICATION_VERSION = LocationConstants.APPLICATION_VERSION;
}
