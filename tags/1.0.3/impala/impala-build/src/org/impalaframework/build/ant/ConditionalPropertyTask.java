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

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.TaskContainer;
import org.apache.tools.ant.taskdefs.Sequential;

/**
 * Base implementation class shared by {@link IfPropertyTask} and {@link UnlessPropertyTask}.
 * Defines an abstract link {@link #shouldExecute(boolean)} which subclasses need to implement.
 * 
 * @author Phil Zoio
 */
public abstract class ConditionalPropertyTask extends Sequential implements TaskContainer {
    
    private String property;
    private String equals;

    protected abstract boolean shouldExecute(boolean isTrue);
    
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
        
        boolean execute = shouldExecute(isTrue);
        
        if (execute) {
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
