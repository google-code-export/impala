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

import java.util.ArrayList;

import java.util.List;

import org.springframework.util.Assert;

public abstract class ArrayUtils {

    /**
     * Returns true if source array is null or empty
     */
    public static <T extends Object> boolean isNullOrEmpty(T[] array) {
        return (array == null || array.length == 0);
    }
    
    /**
     * Returns false if source array is null or empty
     */
    public static <T extends Object> boolean notNullOrEmpty(T[] array) {
        return !isNullOrEmpty(array);
    }
    
    /**
     * Trims individual elements in source array
     */
    public static String[] trim(String[] array) {
        if (array == null) {
            return null;
        }
        for (int i = 0; i < array.length; i++) {
            array[i] = array[i].trim();
        }
        return array;
    }
    
    public static List<String> toList(String[] array) {
        Assert.notNull(array);
        java.util.List<String> list = new ArrayList<String>(array.length);
        for (String entry : array) {
            list.add(entry);
        }
        return list;
    }

    public static String[] asArray(List<String> list) {
        if (list == null) return null;
        return list.toArray(new String[0]);
    }
}
