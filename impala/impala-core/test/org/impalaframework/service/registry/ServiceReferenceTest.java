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

package org.impalaframework.service.registry;

import java.util.Collections;

import junit.framework.TestCase;

public class ServiceReferenceTest extends TestCase {

	public void testConstruct() throws Exception {
		ServiceRegistryReference serviceReference = new ServiceRegistryReference("service1","beanName",
				"moduleName");
		assertEquals(0, serviceReference.getTags().size());
		assertEquals(0, serviceReference.getAttributes().size());
	}
	
	public void testConstructTags() throws Exception {
		ServiceRegistryReference serviceReference = new ServiceRegistryReference("service1","beanName",
				"moduleName",  Collections.singletonList("one"));
		assertNotNull(serviceReference.getTags());
		assertEquals(0, serviceReference.getAttributes().size());
	}
	
	public void testConstructAttributes() throws Exception {
		ServiceRegistryReference serviceReference = new ServiceRegistryReference("service1","beanName",
				"moduleName", Collections.singletonMap("attribute","value"));
		assertEquals(0, serviceReference.getTags().size());
		assertEquals(1, serviceReference.getAttributes().size());
	}

	public void testConstructTagsAttributes() throws Exception {
		ServiceRegistryReference serviceReference = new ServiceRegistryReference("service1","beanName",
				"moduleName", Collections.singletonList("one"), Collections.singletonMap("attribute","value"));
		assertEquals(1, serviceReference.getTags().size());
		assertEquals(1, serviceReference.getAttributes().size());
	}

	public void testConstructTagsAttributesNull() throws Exception {
		ServiceRegistryReference serviceReference = new ServiceRegistryReference("service1","beanName",
				"moduleName", null, null);
		assertEquals(0, serviceReference.getTags().size());
		assertEquals(0, serviceReference.getAttributes().size());
	}	
}
