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

package org.impalaframework.resolver;

/**
 * Constants used in determing module locations
 * @author Phil Zoio
 */
public interface LocationConstants {

	public static final String MODULE_CLASS_DIR_PROPERTY = "impala.module.class.dir";
	public static final String MODULE_TEST_DIR_PROPERTY = "impala.module.test.dir";
	public static final String ROOT_PROJECTS_PROPERTY = "impala.root.projects";
	public static final String WORKSPACE_ROOT_PROPERTY = "workspace.root";
	public static final String APPLICATION_VERSION = "application.version";

}
