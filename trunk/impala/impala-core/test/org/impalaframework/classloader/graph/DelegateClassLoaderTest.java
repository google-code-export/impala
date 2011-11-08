package org.impalaframework.classloader.graph;

import java.util.ArrayList;

import junit.framework.TestCase;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;

public class DelegateClassLoaderTest extends TestCase {

    private DelegateClassLoader dcl;
    private GraphClassLoader gcl2;
    private GraphClassLoader gcl1;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        ArrayList<GraphClassLoader> classLoaders = new ArrayList<GraphClassLoader>();
        gcl1 = createMock(GraphClassLoader.class);
        gcl2 = createMock(GraphClassLoader.class);
        classLoaders.add(gcl1);
        classLoaders.add(gcl2);
        
        dcl = new DelegateClassLoader(classLoaders);
    }
    
    public void testLoadClassNull() throws Exception {
        expect(gcl1.loadApplicationClass("myclass", false)).andReturn(null);
        expect(gcl2.loadApplicationClass("myclass", false)).andReturn(null);
        
        replayMocks();
        
        assertNull(dcl.loadApplicationClass("myclass"));
        
        verifyMocks();
    }

    private void verifyMocks() {
        verify(gcl1);
        verify(gcl2);
    }

    private void replayMocks() {
        replay(gcl1);
        replay(gcl2);
    }
    
}
