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

package org.impalaframework.module.modification;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.impalaframework.exception.NoServiceException;
import org.impalaframework.module.spi.ModificationExtractor;
import org.impalaframework.module.spi.ModificationExtractorType;
import org.springframework.util.Assert;

public class ModificationExtractorRegistry {

    private Map<ModificationExtractorType, ModificationExtractor> modificationExtractors = new HashMap<ModificationExtractorType, ModificationExtractor>();

    public ModificationExtractor getModificationExtractor(ModificationExtractorType type) {
        ModificationExtractor calculator = modificationExtractors.get(type);

        if (calculator == null) {
            throw new NoServiceException("No " + ModificationExtractor.class.getSimpleName()
                    + " available for modification type " + type);
        }

        return calculator;
    }

    public ModificationExtractor getModificationExtractor(String type) {
        Assert.notNull(type);
        ModificationExtractorType enumType = ModificationExtractorType.valueOf(type.toUpperCase());
        return getModificationExtractor(enumType);
    }

    public void setModificationExtractors(
            Map<ModificationExtractorType, ModificationExtractor> calculators) {
        this.modificationExtractors.clear();
        this.modificationExtractors.putAll(calculators);
    }

    public void setModificationExtractorMap(Map<String, ModificationExtractor> calculators) {
        this.modificationExtractors.clear();
        Set<String> keySet = calculators.keySet();
        for (String typeName : keySet) {
            ModificationExtractorType type = ModificationExtractorType.valueOf(typeName.toUpperCase());
            addModificationExtractorType(type, calculators.get(typeName));
        }
    }

    public void addModificationExtractorType(ModificationExtractorType type, ModificationExtractor calculator) {
        this.modificationExtractors.put(type, calculator);
    }

}
