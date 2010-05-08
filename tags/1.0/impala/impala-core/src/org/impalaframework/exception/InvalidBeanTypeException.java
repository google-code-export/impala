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

package org.impalaframework.exception;

/**
 * Extension of {@link ClassCastException} which provides additional information about the
 * expected type of an object, it's actual type, and their associated class loaders.
 * 
 * @author Phil Zoio
 */
public class InvalidBeanTypeException extends ClassCastException {

    private static final long serialVersionUID = 1L;

    private Class<?> beanType;

    private Class<?> requiredType;

    private String beanName;

    public InvalidBeanTypeException(String beanName, Class<?> requiredType, Class<?> beanType) {
        super("Bean named '" + beanName + "' must be of type [" + requiredType.getName()
                + "], but was actually of type [" + beanType.getName() + "]. Class loader of bean: "
                + beanType.getClassLoader() + ". Class loader of required type: " + requiredType.getClassLoader());
        this.beanType = beanType;
        this.requiredType = requiredType;
        this.beanName = beanName;
    }

    public Class<?> getBeanType() {
        return beanType;
    }

    public Class<?> getRequiredType() {
        return requiredType;
    }

    public String getBeanName() {
        return beanName;
    }

}
