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

package org.impalaframework.module.transition;

import static org.easymock.EasyMock.createMock;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.impalaframework.module.spi.Transition;
import org.impalaframework.module.spi.TransitionProcessor;

public class TransitionProcessorRegistryTest extends TestCase {

    private TransitionProcessorRegistry registry;

    public void setUp() {
        registry = new TransitionProcessorRegistry();
    }

    public void testSetMap() {
        Map<String, TransitionProcessor> transitionProcessors = new HashMap<String, TransitionProcessor>();

        TransitionProcessor transitionProcessor1 = createMock(TransitionProcessor.class);
        transitionProcessors.put("LOADED_TO_UNLOADED", transitionProcessor1);

        TransitionProcessor transitionProcessor2 = createMock(TransitionProcessor.class);
        transitionProcessors.put("UNLOADED_TO_LOADED", transitionProcessor2);
        registry.setTransitionProcessors(transitionProcessors);

        assertSame(transitionProcessor1, registry.getTransitionProcessor(Transition.LOADED_TO_UNLOADED));
        assertSame(transitionProcessor2, registry.getTransitionProcessor(Transition.UNLOADED_TO_LOADED));
    }   

}
