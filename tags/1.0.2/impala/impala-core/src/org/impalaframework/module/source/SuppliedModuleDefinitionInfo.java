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

package org.impalaframework.module.source;

import java.util.List;

public class SuppliedModuleDefinitionInfo {
    private final String name;

    private final List<String> configLocations;

    private final String overrides;

    private final String type;

    public SuppliedModuleDefinitionInfo(final String name, final List<String> configLocations, final String overrides,
            final String type) {
        super();
        this.name = name;
        this.configLocations = configLocations;
        this.overrides = overrides;
        this.type = type;
    }

    public List<String> getContextLocations() {
        return configLocations;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getOverrides() {
        return overrides;
    }

}
