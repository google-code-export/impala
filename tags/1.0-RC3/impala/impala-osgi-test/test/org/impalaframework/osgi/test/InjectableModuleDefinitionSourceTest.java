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

package org.impalaframework.osgi.test;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import junit.framework.TestCase;

import org.impalaframework.exception.InvalidStateException;
import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.module.definition.SimpleRootModuleDefinition;
import org.impalaframework.util.serialize.DefaultSerializationStreamFactory;
import org.impalaframework.util.serialize.SerializationStreamFactory;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

public class InjectableModuleDefinitionSourceTest extends TestCase {

    private BundleContext bundleContext;
    private Bundle bundle;
    private SimpleRootModuleDefinition moduleDefinition;
    private InjectableModuleDefinitionSource source;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        bundleContext = createMock(BundleContext.class);
        bundle = createMock(Bundle.class);
        moduleDefinition = new SimpleRootModuleDefinition("root", "root.xml");
        source = new InjectableModuleDefinitionSource(bundleContext);
    }   
    
    public void testInjectNull() {
        replayMocks();
        source.inject(null);
        
        assertNull(source.getModuleDefinition());
        verifyMocks();
    }
    
    public void testInjectNotSerializable() {
        replayMocks();
        
        try {
            source.inject(new Object());
            fail();
        } catch (InvalidStateException e) {
            assertEquals("Attempting to inject non-serializable module definition class 'java.lang.Object'", e.getMessage());
        }
        verifyMocks();
    }
    
    public void testInjectSource() throws Exception {
        InjectableModuleDefinitionSource source = new InjectableModuleDefinitionSource(bundleContext) {
            
            @Override
            SerializationStreamFactory newStreamFactory(ClassLoader classLoader) {
                return new DefaultSerializationStreamFactory();
            }
        };
        
        expect(bundleContext.getBundle()).andReturn(bundle);
        
        replayMocks();
        source.inject(moduleDefinition);
        
        final RootModuleDefinition sourceModuleDefinition = source.getModuleDefinition();
        assertFalse(moduleDefinition == sourceModuleDefinition);
        assertEquals(moduleDefinition.getClass(), sourceModuleDefinition.getClass());
        verifyMocks();
    }
    
    private void replayMocks() {
        replay(bundleContext);
        replay(bundle);
    }
    
    private void verifyMocks() {
        verify(bundleContext);
        verify(bundle);
    }

}
