package org.impalaframework.module.resource;

import junit.framework.TestCase;

import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.definition.SimpleRootModuleDefinition;
import org.impalaframework.spring.resource.ClassPathResourceLoader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;

public class ApplicationModuleLocationsResourceLoaderTest extends TestCase {
    
    private String rootModuleName = "project1";
    private ClassPathResourceLoader resourceLoader;
    private ModuleLocationsResourceLoader loader;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        resourceLoader = new ClassPathResourceLoader();
        loader = new ModuleLocationsResourceLoader();
        loader.setResourceLoader(resourceLoader);
    }

    public final void testApplicationModuleSpringLocationsResourceLoader() {

        ModuleDefinition definition = new SimpleRootModuleDefinition(rootModuleName, new String[] { "parentTestContext.xml" });

        Resource[] springLocations = loader.getSpringLocations(definition, ClassUtils.getDefaultClassLoader());
        assertEquals(1, springLocations.length);
        
        for (Resource resource : springLocations) {
            assertTrue(resource.exists());
            assertTrue(resource instanceof ClassPathResource);
        }
    }
    
    public final void testNotFound() {

        ModuleDefinition definition = new SimpleRootModuleDefinition(rootModuleName, new String[] { "unknown.xml" });

        try {
            loader.getSpringLocations(definition, ClassUtils.getDefaultClassLoader());
            fail();
        }
        catch (ConfigurationException e) {
            assertEquals("Unable to load resource from location 'unknown.xml' for module definition 'project1'", e.getMessage());
        }
    }

}
