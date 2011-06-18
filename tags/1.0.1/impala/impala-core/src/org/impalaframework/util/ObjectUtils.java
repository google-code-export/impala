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

package org.impalaframework.util;

import org.impalaframework.exception.ExecutionException;


public class ObjectUtils {
    
     @SuppressWarnings("unchecked")
    public static <T extends Object> T cast(final Object o, Class<T> clazz) {
        if (o == null) return null;
        if (!(clazz.isAssignableFrom(o.getClass()))) {
            throw new ExecutionException("Object [" + org.springframework.util.ObjectUtils.identityToString(o) + "] is not an instance of "
                    + clazz.getSimpleName());
        }
        return (T)o;
    }
}
