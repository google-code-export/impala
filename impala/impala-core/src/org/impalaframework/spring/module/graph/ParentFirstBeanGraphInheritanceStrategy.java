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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.holder.graph.GraphModuleStateHolder;
import org.springframework.context.ApplicationContext;

public class ParentFirstBeanGraphInheritanceStrategy extends BaseBeanGraphInheritanceStrategy {

	protected boolean getDelegateGetBeanCallsToParent() {
		return true;
	}
	
	/**
	 * Returns the dependent {@link ApplicationContext}s for the current module, excluding those which are direct dependents
	 */
	protected List<ApplicationContext> getDependentApplicationContexts(
			ModuleDefinition definition,
			ApplicationContext parentApplicationContext,
			GraphModuleStateHolder graphModuleStateHolder) {
		
		//FIXME add test
		
		final List<ApplicationContext> applicationContexts = getDependentApplicationContexts(
				definition, graphModuleStateHolder);
		
		List<ApplicationContext> parentList = new ArrayList<ApplicationContext>();
		parentList.add(parentApplicationContext);
		
		if (parentApplicationContext != null) {
			ApplicationContext hierarchyParent = parentApplicationContext;
			while ((hierarchyParent = hierarchyParent.getParent()) != null) {
				parentList.add(hierarchyParent);
			}
		}
		
		applicationContexts.removeAll(parentList);

		//reverse the ordering so that the closest dependencies appear first
		Collections.reverse(applicationContexts);
		return applicationContexts;
	}
	
}
