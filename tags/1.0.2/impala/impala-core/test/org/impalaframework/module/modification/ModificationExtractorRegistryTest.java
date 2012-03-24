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

import junit.framework.TestCase;

import org.impalaframework.exception.NoServiceException;
import org.impalaframework.module.modification.ModificationExtractorRegistry;
import org.impalaframework.module.modification.StickyModificationExtractor;
import org.impalaframework.module.spi.ModificationExtractor;
import org.impalaframework.module.spi.ModificationExtractorType;

public class ModificationExtractorRegistryTest extends TestCase {

    private ModificationExtractorRegistry registry;

    @Override
    protected void setUp() throws Exception {
        registry = new ModificationExtractorRegistry();
    }

    public void testRegistry() throws Exception {
        try {
            assertNull(registry.getModificationExtractor(ModificationExtractorType.STICKY));
            fail();
        }
        catch (NoServiceException e) {
            assertEquals("No ModificationExtractor available for modification type STICKY", e.getMessage());
        }
        try {
            assertNull(registry.getModificationExtractor("sticky"));
            fail();
        }
        catch (NoServiceException e) {
            assertEquals("No ModificationExtractor available for modification type STICKY", e.getMessage());
        }

        StickyModificationExtractor stickyModificationExtractor = new StickyModificationExtractor();

        Map<ModificationExtractorType, ModificationExtractor> map = new HashMap<ModificationExtractorType, ModificationExtractor>();
        map.put(ModificationExtractorType.STICKY, stickyModificationExtractor);
        registry.setModificationExtractors(map);

        assertNotNull(registry.getModificationExtractor(ModificationExtractorType.STICKY));
        assertNotNull(registry.getModificationExtractor("sticky"));
    }

    public void testRegistryWithStringMap() throws Exception {
        StickyModificationExtractor stickyModificationExtractor = new StickyModificationExtractor();

        Map<String, ModificationExtractor> map = new HashMap<String, ModificationExtractor>();
        map.put("sticky", stickyModificationExtractor);
        registry.setModificationExtractorMap(map);

        assertNotNull(registry.getModificationExtractor(ModificationExtractorType.STICKY));
        assertNotNull(registry.getModificationExtractor("sticky"));
    }
}
