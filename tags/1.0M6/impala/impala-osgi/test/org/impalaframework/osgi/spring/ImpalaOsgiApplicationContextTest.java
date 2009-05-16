package org.impalaframework.osgi.spring;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import java.io.IOException;

import junit.framework.TestCase;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.Resource;

public class ImpalaOsgiApplicationContextTest extends TestCase {

    public void testLoadBeanDefinitionsXmlBeanDefinitionReader() throws BeansException, IOException {
        
        ImpalaOsgiApplicationContext context = new ImpalaOsgiApplicationContext();
        final Resource resource1 = createMock(Resource.class);
        final Resource resource2 = createMock(Resource.class);
        
        Resource[] resources = new Resource[]{ resource1, resource2 };
        final XmlBeanDefinitionReader reader = createMock(XmlBeanDefinitionReader.class);
        context.setConfigResources(resources);
        
        //set expectations
        expect(reader.loadBeanDefinitions(resource1)).andReturn(10);
        expect(reader.loadBeanDefinitions(resource2)).andReturn(10);
        
        replay(reader);
        
        context.loadBeanDefinitions(reader);
        
        verify(reader);
    }

}
