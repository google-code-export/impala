/*
 * Copyright 2007 the original author or authors.
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

package tests;

import net.java.impala.spring.plugin.PluginSpec;
import net.java.impala.spring.plugin.SimplePluginSpec;
import net.java.impala.testrun.DynamicContextHolder;
import net.java.impala.testrun.PluginSpecAware;
import net.java.impala.testrun.PluginTestRunner;
import interfaces.Child;
import interfaces.Parent;
import junit.framework.TestCase;

public class ParentChildTest extends TestCase implements PluginSpecAware {

	public static void main(String[] args) {
		PluginTestRunner.run(ParentChildTest.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testOne() {
		System.out.println("Doing stuff in testOne ...");

		Parent bean = DynamicContextHolder.getBean(this, "parent", Parent.class);
		System.out.println("Got bean of type " + bean);

		Child child = bean.tryGetChild();
		try {
			child.childMethod();
			fail();
		}
		catch (RuntimeException e) {
			// e.printStackTrace();
		}
	}

	public PluginSpec getPluginSpec() {
		return new SimplePluginSpec("parent-context.xml", new String[] { "plugin1" });
	}

}