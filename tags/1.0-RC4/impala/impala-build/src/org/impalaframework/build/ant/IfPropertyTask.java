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

package org.impalaframework.build.ant;


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
public class IfPropertyTask extends ConditionalPropertyTask {

    @Override
    protected boolean shouldExecute(boolean isTrue) {
        return isTrue;
    }

}
