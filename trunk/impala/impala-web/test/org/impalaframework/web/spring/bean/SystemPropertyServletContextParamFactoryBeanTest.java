package org.impalaframework.web.spring.bean;

import javax.servlet.ServletContext;

import org.impalaframework.web.spring.bean.SystemPropertyServletContextParamFactoryBean;

import static org.easymock.EasyMock.*;

import junit.framework.TestCase;

public class SystemPropertyServletContextParamFactoryBeanTest extends TestCase {

    private ServletContext servletContext;
    private SystemPropertyServletContextParamFactoryBean bean;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.clearProperty("myParam");
        servletContext = createMock(ServletContext.class);
        
        bean = new SystemPropertyServletContextParamFactoryBean();
        bean.setServletContext(servletContext);
        
        bean.setDefaultValue("defaultValue");
        bean.setParameterName("myParam");
        
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        System.clearProperty("myParam");
    }
    
    public void testProperties() throws Exception {
        assertEquals(String.class, bean.getObjectType());
        assertEquals(true, bean.isSingleton());
    }
    
    public void testGetDefaultValue() throws Exception {
        expect(servletContext.getInitParameter("myParam")).andReturn(null);
        replay(servletContext);
        
        bean.afterPropertiesSet();
        String value = (String) bean.getObject();
        assertEquals("defaultValue", value);
        
        verify(servletContext);
    }
    
    public void testGetInitParam() throws Exception {
        expect(servletContext.getInitParameter("myParam")).andReturn("scValue");
        replay(servletContext);
        
        bean.afterPropertiesSet();
        String value = (String) bean.getObject();
        assertEquals("scValue", value);
        
        verify(servletContext);
    }
    
    public void testGetSystemProperty() throws Exception {
        System.setProperty("myParam", "someValue");
        replay(servletContext);
        
        bean.afterPropertiesSet();
        String value = (String) bean.getObject();
        assertEquals("someValue", value);
        
        verify(servletContext);
    }

    
    public void testGetSystemPropertyPrefix() throws Exception {
        System.setProperty("myParam", "someValue");
        bean.setPrefix("myprefix-");
        replay(servletContext);
        
        bean.afterPropertiesSet();
        String value = (String) bean.getObject();
        assertEquals("myprefix-someValue", value);
        
        verify(servletContext);
    }
    
    

}
