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

package org.impalaframework.module;


/**
 * This interface defines a strategy for loading module definitions. The module
 * definition hierarchy can be loaded by any implementation of
 * {@link ModuleDefinitionSource}. Module definitions are not metadata. When
 * you load a {@link ModuleDefinition} you are not loading the module, just an
 * abstract representation of what the module is supposed to contain.
 * 
 * There are a number of implementations of ModuleDefintionSource. Which
 * implementation is best to use depends on the circumstances.
 * 
 * {@link org.impalaframework.module.source.XMLModuleDefinitionSource} uses by default an <i>moduledefinitions.xml</i>
 * placed on the web application class loader's class path (for example, in
 * _WEB-INF\classes).
 * 
 * For integration tests, it's easier to implement {@link  org.impalaframework.module.source.InternalModuleDefinitionSource} in
 * code the test directly. Here's an example:
 * 
 * <pre>
 *  public class EntryDAOTest implements ModuleDefinitionSource {
 *  ...
 * 
 *  public RootModuleDefinition getModuleDefinition() {
 *  return new InternalModuleDefinitionSource(new String[]{&quot;example-dao&quot;, &quot;example-hibernate&quot;}).getModuleDefinition();
 *  }
 *  }
 * </pre>
 * 
 * The example above uses {@link InternalModuleDefinitionSource}, which
 * involves a passed in array of names of modules.
 * 
 * @see InternalModuleDefinitionSource
 * @see XMLModuleDefinitionSource
 * @author Phil Zoio
 */
public interface ModuleDefinitionSource {
	RootModuleDefinition getModuleDefinition();
}
