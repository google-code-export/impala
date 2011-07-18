package org.impalaframework.jmx.bootstrap;

import java.util.List;
import java.util.Properties;

import junit.framework.TestCase;

import org.impalaframework.bootstrap.ConfigurationSettings;
import org.impalaframework.bootstrap.SimpleContextLocationResolver;
import org.impalaframework.config.PropertySource;
import org.impalaframework.config.StaticPropertiesPropertySource;

public class JMXContextLocationResolverTest extends TestCase {
    private JMXContextLocationResolver resolver;
    private Properties properties;
    private StaticPropertiesPropertySource propertySource;
    private ConfigurationSettings configSettings;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        resolver = new JMXContextLocationResolver();
        propertySource = new StaticPropertiesPropertySource();
        properties = new Properties();
        propertySource.setProperties(properties);
        configSettings = new ConfigurationSettings();
    }

    public void testDefaultExposeJmxOperations() {
        resolver.addJmxOperations(configSettings, propertySource);
        assertLocations("impala-jmx-boot");
    }

    public void testPreferPlaformMbeanServer() {
        properties.setProperty("jmx.prefer.platform.mbean.server", "true");
        resolver.addJmxOperations(configSettings, propertySource);
        assertEquals("true", configSettings.getPropertyValues().get("jmx.prefer.platform.mbean.server").getRawValue());
    }

    public void testExposeJmxOperations() {
        properties.setProperty("expose.jmx.operations", "false");
        resolver.addJmxOperations(configSettings, propertySource);
        assertLocations();
    }
    
    public void testMx4jPresent() throws Exception {
        assertTrue(resolver.isMX4JPresent());
    }
    
    public void testDefaultAddMx4jAdaptor() throws Exception {
        resolver.addMx4jAdaptorContext(configSettings, propertySource);
        assertLocations();
    }
    
    public void testAddMx4jAdaptorNoJmx() throws Exception {
        properties.setProperty("expose.mx4j.adaptor", "true");
        resolver.addMx4jAdaptorContext(configSettings, propertySource);
        assertLocations();
    }
    
    public void testAddMx4jAdaptor() throws Exception {
        configSettings.add("META-INF/impala-jmx-bootstrap.xml");
        properties.setProperty("expose.mx4j.adaptor", "true");
        resolver.addMx4jAdaptorContext(configSettings, propertySource);
        assertLocations("META-INF/impala-jmx-bootstrap.xml", "META-INF/impala-jmx-adaptor-bootstrap.xml");
    }
    
    public void testAddMx4jAdaptorLibrariesNotPresent() throws Exception {
        configSettings.add("META-INF/impala-jmx-bootstrap.xml");
        properties.setProperty("expose.mx4j.adaptor", "true");
        
        resolver = new JMXContextLocationResolver() {

            @Override
            boolean isMX4JPresent() {
                return false;
            }
            
        };
        
        resolver.addMx4jAdaptorContext(configSettings, propertySource);
        assertLocations("META-INF/impala-jmx-bootstrap.xml");
    }

    public void testAddJmxLocations() throws Exception {
        //notice how this differs from the same test in SimpleContextLocationResolverTest
        properties.setProperty("expose.jmx.operations", "true");
        new TestSimpleResolver().maybeAddJmxLocations(configSettings, propertySource);
        assertLocations("META-INF/impala-jmx-bootstrap.xml");
    }   
    
    private void assertLocations(String... locations) {
        final List<String> configLocations = configSettings.getContextLocations();
        assertEquals(locations.length, configLocations.size());
        for (int i = 0; i < locations.length; i++) {
            String actualLocation = configLocations.get(i);
            String expectedLocation = locations[i];
            assertTrue(actualLocation.contains(expectedLocation));
            assertTrue(actualLocation.contains("impala"));
        }
    }
}

class TestSimpleResolver extends SimpleContextLocationResolver {

    @Override
    protected void maybeAddJmxLocations(ConfigurationSettings configLocations, PropertySource propertySource) {
        super.maybeAddJmxLocations(configLocations, propertySource);
    }
    
}
