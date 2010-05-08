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

package org.impalaframework.web.module.source;

import org.impalaframework.facade.Impala;
import org.impalaframework.module.source.InternalXmlModuleDefinitionSource;
import org.impalaframework.module.type.TypeReaderRegistry;
import org.impalaframework.resolver.ModuleLocationResolver;

public class InternalWebXmlModuleDefinitionSource extends InternalXmlModuleDefinitionSource {

    public InternalWebXmlModuleDefinitionSource() {
        super(Impala.getFacade().getModuleManagementFacade().getModuleLocationResolver(), 
                Impala.getFacade().getModuleManagementFacade().getTypeReaderRegistry());
    }

    public InternalWebXmlModuleDefinitionSource(ModuleLocationResolver moduleLocationResolver, TypeReaderRegistry typeReaderRegistry) {
        super(moduleLocationResolver, typeReaderRegistry);
    }

}
