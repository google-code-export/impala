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

import org.impalaframework.config.PropertySource;

/**
 * Defines interface for collecting Impala configuration information into a
 * {@link ConfigurationSettings} object, with the help of a supplied
 * {@link PropertySource} object.
 * 
 * @author Phil Zoio
 */
public interface ContextLocationResolver {
    public boolean addContextLocations(ConfigurationSettings configSettings, PropertySource propertySource);
}
