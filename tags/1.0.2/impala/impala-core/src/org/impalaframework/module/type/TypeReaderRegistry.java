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

import java.util.Map;

import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.module.spi.TypeReader;
import org.impalaframework.registry.Registry;
import org.impalaframework.registry.RegistrySupport;

public class TypeReaderRegistry extends RegistrySupport implements Registry<TypeReader> {

    private TypeReader defaultTypeReader;

    public TypeReader getTypeReader(String type) {
        TypeReader typeReader = super.getEntry(type, TypeReader.class, false);
        
        if (typeReader == null) {
            if (defaultTypeReader == null) {
                throw new ConfigurationException("No type reader available for module type '" + type + "', and no default module type reader has been set");
            }
            return defaultTypeReader;
        }
        
        return typeReader;
    }
    
    public void addItem(String type, TypeReader typeReader) {
        super.addRegistryItem(type, typeReader);
    }

    @SuppressWarnings("unchecked")
    public Map<String, TypeReader> getTypeReaders() {
        final Map entries = super.getEntries();
        return entries;
    }

    public void setTypeReaders(Map<String, TypeReader> typeReaders) {
        super.setEntries(typeReaders);
    }   
    
    public void setDefaultTypeReader(TypeReader defaultTypeReader) {
        this.defaultTypeReader = defaultTypeReader;
    }
}
