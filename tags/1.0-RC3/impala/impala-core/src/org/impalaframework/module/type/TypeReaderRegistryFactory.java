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

package org.impalaframework.module.type;

import java.util.HashMap;
import java.util.Map;

import org.impalaframework.module.definition.ModuleTypes;
import org.impalaframework.module.spi.TypeReader;

public class TypeReaderRegistryFactory {
    public static Map<String, TypeReader> getTypeReaders() {
        Map<String, TypeReader> typeReaders = new HashMap<String, TypeReader>();
        typeReaders.put(ModuleTypes.ROOT.toLowerCase(), new RootModuleTypeReader());
        typeReaders.put(ModuleTypes.APPLICATION.toLowerCase(), new ApplicationModuleTypeReader());
        return typeReaders;
    }
    
    public static TypeReaderRegistry getTypeReaderRegistry() {
        final Map<String, TypeReader> typeReaders = getTypeReaders();
        final TypeReaderRegistry registry = new TypeReaderRegistry();
        registry.setTypeReaders(typeReaders);
        return registry;
    }
}
