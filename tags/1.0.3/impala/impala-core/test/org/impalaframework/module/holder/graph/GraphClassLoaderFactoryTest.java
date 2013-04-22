package org.impalaframework.module.holder.graph;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import org.impalaframework.classloader.ClassRetriever;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

public class GraphClassLoaderFactoryTest extends TestCase {
    
    private GraphClassLoaderFactory factory;
    private ModuleLocationResolver moduleLocationResolver;

    @Override
    protected void setUp() throws Exception {
        factory = new GraphClassLoaderFactory();
        moduleLocationResolver = createMock(ModuleLocationResolver.class);
        factory.setModuleLocationResolver(moduleLocationResolver);
    }

    public void testNewModuleClassResourceRetriever() {
        List<Resource> resources = Arrays.asList((Resource)new FileSystemResource(new File("f1")));
        expect(moduleLocationResolver.getApplicationModuleClassLocations("m1")).andReturn(resources);
        
        replay(moduleLocationResolver);
        
        final ClassRetriever retriever = factory.newModuleClassResourceRetriever(new SimpleModuleDefinition("m1"));
        assertNotNull(retriever);
        
        verify(moduleLocationResolver);
    }

    public void testModuleJarRetriever() {
        List<Resource> resources = Arrays.asList((Resource)new FileSystemResource(new File("f1")));
        expect(moduleLocationResolver.getApplicationModuleLibraryLocations("m1")).andReturn(resources);
        
        replay(moduleLocationResolver);
        
        final ClassRetriever retriever = factory.newModuleLibraryResourceRetriever(new SimpleModuleDefinition("m1"));
        assertNotNull(retriever);
        
        verify(moduleLocationResolver);
    }

}
