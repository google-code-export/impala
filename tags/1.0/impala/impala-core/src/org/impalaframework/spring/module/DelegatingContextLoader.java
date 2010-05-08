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

package org.impalaframework.spring.module;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.spi.ModuleLoader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * {@link DelegatingContextLoader} is used as an alternative strategy for module loading to {@link ModuleLoader}.
 * <p>
 * Implementations of {@link DelegatingContextLoader} can be used to back a
 * Spring module for types of modules for which the restrictions which apply for
 * {@link ModuleLoader} implementations is not appropriate. For example, there
 * is a <i>web_placeholder</i> module type, which is backed by an empty
 * instance of {@link org.springframework.web.context.support.GenericWebApplicationContext}, to cover servlet modules
 * which are present in <i>web.xml</i> but not actually backed by any real
 * module functionality.
 * <p>
 * {@link DelegatingContextLoader} could also be used for integrating with
 * Impala specific frameworks which use a Spring application context in a very
 * specialized way, for example, through their own subclass implementations of
 * {@link org.springframework.web.context.WebApplicationContext}. An example might be a framework such as Grails.
 * 
 * @see ModuleLoader
 * @author Phil Zoio
 */
public interface DelegatingContextLoader {

    ConfigurableApplicationContext loadApplicationContext(ApplicationContext parent, ModuleDefinition definition);

}
