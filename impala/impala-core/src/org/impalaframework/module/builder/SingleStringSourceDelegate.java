package org.impalaframework.module.builder;

import java.util.LinkedList;
import java.util.List;

import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.SimpleBeansetModuleDefinition;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

class SingleStringSourceDelegate  {

	private ModuleDefinition moduleDefinition;

	private String definitionString;

	SingleStringSourceDelegate(ModuleDefinition moduleDefinition, String definitionString) {
		super();
		Assert.notNull(moduleDefinition);
		Assert.notNull(definitionString);
		this.moduleDefinition = moduleDefinition;
		this.definitionString = definitionString;
	}

	ModuleDefinition getModuleDefinition() {
		if (StringUtils.hasText(definitionString)) {
			String[] moduleNames = doDefinitionSplit();

			for (String moduleName : moduleNames) {
				int openBracketIndex = moduleName.indexOf('(');
				if (openBracketIndex < 0) {
					new SimpleModuleDefinition(moduleDefinition, moduleName);
				}
				else {
					int closeBracketIndex = moduleName.indexOf(')');
					// doDefinitionSplit() will check this, but just to make sure
					Assert.isTrue(closeBracketIndex > openBracketIndex);
					String name = moduleName.substring(0, openBracketIndex);
					String beanSetString = moduleName.substring(openBracketIndex + 1, closeBracketIndex);
					if (StringUtils.hasText(beanSetString))
						new SimpleBeansetModuleDefinition(moduleDefinition, name.trim(), beanSetString.trim());
					else
						new SimpleBeansetModuleDefinition(moduleDefinition, name.trim());
				}
			}
		}
		return moduleDefinition;
	}

	String[] doDefinitionSplit() {

		List<Integer> indexes = new LinkedList<Integer>();

		char[] chars = definitionString.toCharArray();

		boolean inOverrides = false;

		indexes.add(-1);

		for (int i = 0; i < chars.length; i++) {
			if (chars[i] == '(') {
				if (inOverrides) {
					invalidChar(chars, i);
				}
				inOverrides = true;
			}
			if (chars[i] == ')') {
				if (!inOverrides) {
					invalidChar(chars, i);
				}
				inOverrides = false;
			}

			if (chars[i] == ',') {
				if (!inOverrides) {
					indexes.add(i);
				}
			}
		}

		// add the length as the last index
		indexes.add(definitionString.length());

		List<String> segments = new LinkedList<String>();
		String moduleString = this.definitionString;

		for (int i = 1; i < indexes.size(); i++) {
			segments.add(moduleString.substring(indexes.get(i - 1) + 1, indexes.get(i)));
		}

		// convert to array
		String[] moduleNames = segments.toArray(new String[segments.size()]);

		// trim
		for (int i = 0; i < moduleNames.length; i++) {
			moduleNames[i] = moduleNames[i].trim();
		}
		return moduleNames;
	}

	private void invalidChar(char[] chars, int i) {
		throw new ConfigurationException("Invalid definition string " + definitionString + ". Invalid character '" + chars[i]
				+ "' at column " + (i + 1));
	}

}
