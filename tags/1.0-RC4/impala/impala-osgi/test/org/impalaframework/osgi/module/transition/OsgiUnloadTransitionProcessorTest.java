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

package org.impalaframework.osgi.module.transition;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import junit.framework.TestCase;

import org.impalaframework.exception.ExecutionException;
import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

public class OsgiUnloadTransitionProcessorTest extends TestCase {

    private BundleContext bundleContext;
    private OsgiUnloadTransitionProcessor processor;
    private Bundle bundle;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        bundleContext = createMock(BundleContext.class);
        bundle = createMock(Bundle.class);
        initProcessor(bundle);
    }

    private void initProcessor(Bundle bundle) {
        processor = new TestUnloadProcessor(bundle);
        processor.setBundleContext(bundleContext);
    }
    
    public void testFindAndUnloadBundle() throws BundleException {
        bundle.stop();
        
        replayMocks();
        
        processor.findAndUnloadBundle(new SimpleModuleDefinition("myModule"));
        
        verifyMocks();
    }
    
    public void testFindAndUnloadBundleNull() throws BundleException {
        initProcessor(null);
        
        replayMocks();
        
        processor.findAndUnloadBundle(new SimpleModuleDefinition("myModule"));
        
        verifyMocks();
    }
    
    public void testFindAndUnloadBundleThrowException() throws BundleException {
        bundle.stop();
        expectLastCall().andThrow(new BundleException("Stop failed"));
        expect(bundle.getSymbolicName()).andReturn(null);
        
        replayMocks();
        
        try {
            processor.findAndUnloadBundle(new SimpleModuleDefinition("myModule"));
            fail();
        } catch (ExecutionException e) {
        }
        
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

class TestUnloadProcessor extends OsgiUnloadTransitionProcessor {

    private Bundle bundle;

    public TestUnloadProcessor(Bundle bundle) {
        super();
        this.bundle = bundle;
    }

    @Override
    Bundle findBundle(ModuleDefinition moduleDefinition) {
        return bundle;
    }
    
}
