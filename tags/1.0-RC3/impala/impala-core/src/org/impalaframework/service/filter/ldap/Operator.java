/*
 * Copyright 2009 the original author or authors.
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

package org.impalaframework.service.filter.ldap;

/**
 * Enumeration of operator signs
 * 
 * @author Phil Zoio
 */
class Operator {

    private String sign;

    public static final Operator eq = new Operator("=");
    public static final Operator lt = new Operator("<=");
    public static final Operator gt = new Operator(">=");
    public static final Operator apprx = new Operator("~=");

    protected Operator(String sign) {
        super();
        this.sign = sign;
    }
    
    public String toString() {
        return sign;
    }

}
