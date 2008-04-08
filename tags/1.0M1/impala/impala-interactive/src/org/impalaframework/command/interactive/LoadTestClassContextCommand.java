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

package org.impalaframework.command.interactive;

import org.impalaframework.command.framework.Command;
import org.impalaframework.command.framework.CommandDefinition;
import org.impalaframework.command.framework.CommandState;
import org.impalaframework.command.framework.GlobalCommandState;
import org.impalaframework.facade.Impala;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.springframework.util.ClassUtils;

public class LoadTestClassContextCommand implements Command {

	public boolean execute(CommandState commandState) {
		Object property = GlobalCommandState.getInstance().getValue(CommandStateConstants.TEST_CLASS_NAME);
		if (property == null) {
			System.out.println("No test class set.");
			return false;
		}

		String testClassName = property.toString();
		return loadTestClass(testClassName);
	}

	private boolean loadTestClass(String testClassName) {

		Class<?> c = null;
		try {

			// FIXME this only allows you to switch directories to one containing a loaded module

			String directoryName = (String) GlobalCommandState.getInstance().getValue(
					CommandStateConstants.DIRECTORY_NAME);
			
			ClassLoader parent = null;

			try {
				if (directoryName != null && !InteractiveCommandUtils.isRootProject(directoryName)) {
					parent = Impala.getModuleContext(directoryName).getClassLoader();
				}
				else {
					parent = Impala.getRootContext().getClassLoader();
				}
			}
			catch (Exception e) {
				//TODO more precise error message required here
				System.out.println("Unable to load module corresponding with directory name " + (directoryName != null ? directoryName : "[not set]"));
				parent = ClassUtils.getDefaultClassLoader();
			}

			c = Class.forName(testClassName, false, parent);
			try {
				Object o = c.newInstance();
				if (o instanceof ModuleDefinitionSource) {
					ModuleDefinitionSource p = (ModuleDefinitionSource) o;
					GlobalCommandState.getInstance().addValue(CommandStateConstants.MODULE_DEFINITION_SOURCE, p);
				}

				GlobalCommandState.getInstance().addValue(CommandStateConstants.TEST_CLASS, c);
				return false;
			}
			catch (Throwable e) {
				System.out.println("Unable to instantiate " + testClassName);
				InteractiveCommandUtils.printException(e);
			}
		}
		catch (ClassNotFoundException e) {
			System.out.println("Unable to find test class " + testClassName);
			InteractiveCommandUtils.printException(e);
		}
		return true;
	}

	public CommandDefinition getCommandDefinition() {
		return new CommandDefinition();
	}

}
