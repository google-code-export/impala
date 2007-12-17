package org.impalaframework.module.beanset;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.FatalBeanException;

public class BeanSetMapReader {

	public Map<String, Set<String>> readBeanSetSpec(String definition) {
		Map<String, Set<String>> map = new HashMap<String, Set<String>>();
		
		if (definition == null) {
			return map;
		}

		String[] beanSetLists = definition.split(";");

		for (String beanSetList : beanSetLists) {

			Set<String> set = new LinkedHashSet<String>();

			int colonIndex = beanSetList.indexOf(':');

			if (colonIndex < 0) {
				throw new FatalBeanException("Invalid beanset specification. Missing ':' from string '" + beanSetList
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
