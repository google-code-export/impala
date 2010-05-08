/*
 * Copyright 2007-2010 the original author or authors.
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

package org.impalaframework.module.beanset;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.impalaframework.exception.ConfigurationException;

public class BeanSetMapReader {

	public Map<String, Set<String>> readBeanSetDefinition(String definition) {
		Map<String, Set<String>> map = new HashMap<String, Set<String>>();
		
		if (definition == null) {
			return map;
		}

		String[] beanSetLists = definition.split(";");

		for (String beanSetList : beanSetLists) {

			Set<String> set = new LinkedHashSet<String>();

			int colonIndex = beanSetList.indexOf(':');

			if (colonIndex < 0) {
				throw new ConfigurationException("Invalid beanset definition. Missing ':' from string '" + beanSetList
						+ "' in '" + definition + "'");
			}

			String fileName = beanSetList.substring(0, colonIndex).trim();
			String propertyListString = beanSetList.substring(colonIndex + 1).trim();

			if (propertyListString.length() > 0) {

				propertyListString = propertyListString.trim();
				// add the named modules into the property set
				String[] propertyList = propertyListString.split(",");

				boolean added = false;

				for (String moduleName : propertyList) {
					moduleName = moduleName.trim();
					if (moduleName.length() > 0) {
						set.add(moduleName);
						added = true;
					}
				}

				if (added)
					map.put(fileName, set);
			}
		}

		return map;
	}

}
