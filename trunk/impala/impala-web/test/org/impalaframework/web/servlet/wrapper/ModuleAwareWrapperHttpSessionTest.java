/*
 * Copyright 2007-2008 the original author or authors.
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

package org.impalaframework.web.servlet.wrapper;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.File;
import java.util.Properties;

import javax.servlet.http.HttpSession;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.impalaframework.classloader.ModuleClassLoader;
import org.impalaframework.config.PropertySource;
import org.impalaframework.config.PropertySourceHolder;
import org.impalaframework.config.StaticPropertiesPropertySource;
import org.impalaframework.util.InstantiationUtils;
import org.impalaframework.util.serialize.ClassLoaderAwareSerializationStreamFactory;
import org.impalaframework.util.serialize.SerializationHelper;
import org.impalaframework.web.bootstrap.WebBootstrapProperties;
import org.impalaframework.web.servlet.wrapper.ModuleAwareWrapperHttpSession;
import org.springframework.util.ClassUtils;

public class ModuleAwareWrapperHttpSessionTest extends TestCase {

	private HttpSession session;

	public void testGetAttributeNoWrapping() {
		//test the case where the classloader is compatible
		
		session = createMock(HttpSession.class);
		ValueHolder valueHolder = new ValueHolder();
		expect(session.getAttribute("myAttribute")).andReturn(valueHolder);
		
		replay(session);

		ModuleAwareWrapperHttpSession wrappedSession = new ModuleAwareWrapperHttpSession(session, ClassUtils.getDefaultClassLoader());
		assertSame(valueHolder, wrappedSession.getAttribute("myAttribute"));
		verify(session);
	}
	

	public void testGetAttributeWrappingSerializableValueHolder() {
		//test the case where the classloader is compatible
		
		session = createMock(HttpSession.class);
		SerializableValueHolder valueHolder = new SerializableValueHolder();
		SerializationHelper helper = new SerializationHelper(new ClassLoaderAwareSerializationStreamFactory(newModuleClassLoader()));
		
		Object clonedObject = helper.clone(valueHolder);
		expect(session.getAttribute("myAttribute")).andReturn(clonedObject);
		session.setAttribute(eq("myAttribute"), EasyMock.anyObject());
		
		replay(session);

		ModuleAwareWrapperHttpSession wrappedSession = new ModuleAwareWrapperHttpSession(session, newModuleClassLoader());
		assertFalse(valueHolder == wrappedSession.getAttribute("myAttribute"));
		verify(session);
	}
	
	public void testGetAttributeWrappingWithFailedSerialization() {
		//test the case where the classloader is compatible
		
		session = createMock(HttpSession.class);
		SerializableValueHolder valueHolder = new SerializableValueHolder();
		SerializationHelper helper = new SerializationHelper(new ClassLoaderAwareSerializationStreamFactory(newModuleClassLoader()));
		
		Object clonedObject = helper.clone(valueHolder);
		expect(session.getAttribute("myAttribute")).andReturn(clonedObject);
		session.removeAttribute("myAttribute");
		
		replay(session);

		ModuleAwareWrapperHttpSession wrappedSession = cloneFailingSession();
		assertFalse(valueHolder == wrappedSession.getAttribute("myAttribute"));
		verify(session);
	}
	
	
	public void testGetAttributeWrappingWithSessionNoPreserved() {
		Properties properties = new Properties();
		properties.setProperty(WebBootstrapProperties.PRESERVE_SESSION_ON_RELOAD_FAILURE, "false");
		PropertySource source = new StaticPropertiesPropertySource(properties);
		
		PropertySourceHolder.getInstance().setPropertySource(source);
		
		session = createMock(HttpSession.class);
		SerializableValueHolder valueHolder = new SerializableValueHolder();
		SerializationHelper helper = new SerializationHelper(new ClassLoaderAwareSerializationStreamFactory(newModuleClassLoader()));
		
		Object clonedObject = helper.clone(valueHolder);
		expect(session.getAttribute("myAttribute")).andReturn(clonedObject);
		
		//note the call to invalidate
		session.invalidate();
		
		replay(session);

		ModuleAwareWrapperHttpSession wrappedSession = cloneFailingSession();
		assertFalse(valueHolder == wrappedSession.getAttribute("myAttribute"));
		verify(session);
	}
	
	
	private ModuleAwareWrapperHttpSession cloneFailingSession() {
		ModuleAwareWrapperHttpSession wrappedSession = new ModuleAwareWrapperHttpSession(session, newModuleClassLoader()){

			@Override
			Object clone(Object attribute, SerializationHelper helper) {
				throw new RuntimeException();
			}
			
		};
		return wrappedSession;
	}

	public void testGetAttributeWrappingValueHolder() {
		//test the case where the classloader is incompatible
		//in this case the object is removed from the session and getAttribute returns null
		
		session = createMock(HttpSession.class);
	
		Object attribute = InstantiationUtils.instantiate(ValueHolder.class.getName(), newModuleClassLoader());
		expect(session.getAttribute("myAttribute")).andReturn(attribute);
		session.removeAttribute("myAttribute");
		
		replay(session);

		ModuleAwareWrapperHttpSession wrappedSession = new ModuleAwareWrapperHttpSession(session, newModuleClassLoader());
		assertNull(wrappedSession.getAttribute("myAttribute"));
		verify(session);
	}


	private ModuleClassLoader newModuleClassLoader() {
		ModuleClassLoader moduleClassLoader = new ModuleClassLoader(new File[]{ new File("../impala-web/bin")});
		return moduleClassLoader;
	}

}
