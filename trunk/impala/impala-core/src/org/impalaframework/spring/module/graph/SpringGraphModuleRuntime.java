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

package org.impalaframework.spring.module.graph;

import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.holder.graph.GraphModuleStateHolder;
import org.impalaframework.spring.module.SpringModuleRuntime;
import org.impalaframework.util.ObjectUtils;
import org.springframework.context.ApplicationContext;

public class SpringGraphModuleRuntime extends SpringModuleRuntime {
	
	private String beanVisibilityType = "parentFirst";
	
	//other values: parentOnly

	@Override
	protected ApplicationContext getParentApplicationContext(ModuleDefinition definition) {
		
		//FIXME allow bean visibility inheritance to be configured at module level
		
		ApplicationContext parentApplicationContext = super.getParentApplicationContext(definition);
		
		if (parentApplicationContext == null) {
			return null;
		}
		
		//FIXME extract constants
		if (beanVisibilityType.equals("none")) {
			//FIXME test
			return null;		
		}
		
		if (beanVisibilityType.equals("parentOnly")) {
			//FIXME test
			return parentApplicationContext;
		}
		
		if (beanVisibilityType.equals("parentFirst")) {
			GraphModuleStateHolder graphModuleStateHolder = ObjectUtils.cast(getModuleStateHolder(), GraphModuleStateHolder.class);
			return new ParentFirstBeanGraphInheritanceStrategy().getParentApplicationContext(graphModuleStateHolder, parentApplicationContext, definition);		
		}
		
		if (beanVisibilityType.equals("graphOrdered")) {
			GraphModuleStateHolder graphModuleStateHolder = ObjectUtils.cast(getModuleStateHolder(), GraphModuleStateHolder.class);
			return new GraphOrderedBeanInheritanceStrategy().getParentApplicationContext(graphModuleStateHolder, parentApplicationContext, definition);
		}
		
		//FIXME
		throw new ConfigurationException("FIXME");
		
	}

	public void setBeanVisibilityType(String beanVisibilityType) {
		this.beanVisibilityType = beanVisibilityType;
	}

}
