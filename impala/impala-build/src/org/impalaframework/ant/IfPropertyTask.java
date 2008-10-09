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

package org.impalaframework.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.TaskContainer;
import org.apache.tools.ant.taskdefs.Sequential;

/**
 * Task which which contains nested tasks, which will evaluate based on the
 * presence of a property, or whether the property evaluates to a particular
 * value.
 * 
 * <p>
 * In the following example, 'Hi Phil' will only be printed out if you have a property <code>myname</code> present
 * 
 * <pre>  &lt;target name = &quot;ifproperty&quot;
 *     description=&quot;If property test&quot;&gt;
 *     &lt;ifproperty property = &quot;myname&quot;&gt;
 *        &lt;echo&gt;Hi Phil&lt;/echo&gt;
 *     &lt;/ifproperty&gt;
 *  &lt;/target&gt;
 * </pre>
 * 
 * <p>
 * In the following example, 'Hi Phil' will only be printed out if you have a property <code>myname</code> with a value of 'phil'
 * 
 * <pre>  &lt;target name = &quot;ifproperty&quot;
 *     description=&quot;If property test&quot;&gt;
 *     &lt;ifproperty property = &quot;myname&quot; equals = &quot;phil&quot;&gt;
 *        &lt;echo&gt;Hi Phil&lt;/echo&gt;
 *     &lt;/ifproperty&gt;
 *  &lt;/target&gt;
 * </pre>
 * 
 * @author Phil Zoio
 */
public class IfPropertyTask extends Sequential implements TaskContainer {
	
	private String property;
	private String equals;
    
	@Override
	public void execute() throws BuildException {
		if (property == null) {
			throw new BuildException("Property 'property' has not been specified.", getLocation());
		}
		
		boolean isTrue = false;
		
		final String propertyValue = getProject().getProperty(property);
		if (equals == null) {
			isTrue = (propertyValue != null);
		} else {
			isTrue = (equals.equals(propertyValue));
		}
		
		if (isTrue) {
			super.execute();
		}
	}
    
    /* ******************** injected properties ********************* */

	public void setProperty(String property) {
		this.property = property;
	}

	public void setEquals(String equals) {
		this.equals = equals;
	}

}
