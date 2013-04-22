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

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.impalaframework.util.ReflectionUtils;

/**
 * {@link FilterNode} in RFC 1960 which represents one of the item or leaf expressions
 * including equals, substring, etc.
 * 
 * @author Phil Zoio
 */
abstract class ItemNode extends BaseNode implements FilterNode {

    private String value;

    ItemNode(String key, String value) {
        super(key);
        this.value = value;
    }

    String getValue() {
        return value;
    }

    public boolean match(Map<?, ?> data) {
        Object value = data.get(getKey());
        return match(value);
    }

    private boolean match(Object value) {
        if (value == null) {
            return false;
        }
        
        if (value instanceof String) {
            return matchString((String) value);
        }
        Class<?> c = value.getClass();
        if (c.isArray()) {
            Class<?> type = c.getComponentType();

            if (type.isPrimitive()) {
                return matchPrimitiveArray(value, type);
            } else {
                return matchObjectArray(value);
            }
        }
        
        if (value instanceof Collection<?>) {
            return matchCollection((Collection<?>)value);
        }
        
        if (value instanceof Integer) {
            return matchInteger((Integer)value);
        }
        if (value instanceof Long) {
            return matchLong((Long)value);
        }       
        if (value instanceof Boolean) {
            return matchBoolean((Boolean) value);
        }
        if (value instanceof Character) {
            return matchCharacter((Character)value);
        }
        if (value instanceof Float) {
            return matchFloat((Float)value);
        }
        if (value instanceof Double) {
            return matchDouble((Double)value);
        }
        if (value instanceof Byte) {
            return matchByte((Byte)value);
        }
        if (value instanceof Short) {
            return matchShort((Short)value);
        }
        if (value instanceof Comparable<?>) {
            return matchComparable((Comparable<?>)value);
        }
        return matchUnknown(value);
    }

    private boolean matchUnknown(Object value) {
        Constructor<?> constructor = ReflectionUtils.findConstructor(value.getClass(), new Class[]{ String.class });
        if (constructor == null) {
            return false;
        }
    
        Object constructed = ReflectionUtils.invokeConstructor(constructor, new Object[]{ getValue() }, true);
        return value.equals(constructed);
    }

    @SuppressWarnings("unchecked")
    private <T extends Object> boolean matchComparable(Comparable<T> value) {
        Constructor<?> constructor = ReflectionUtils.findConstructor(value.getClass(), new Class[]{ String.class });
        if (constructor == null) {
            return false;
        }
        
        Comparable<T> constructed = (Comparable<T>) ReflectionUtils.invokeConstructor(constructor, new Object[]{ getValue() }, true);
        return matchComparable(constructed, value);
    }

    protected abstract <T extends Object> boolean matchComparable(Comparable<T> internal, Comparable<T> external);

    private boolean matchPrimitiveArray(Object value, Class<?> type) {
        
        if (Integer.TYPE.isAssignableFrom(type)) {
            int[] array = (int[]) value;
            int size = array.length;

            for (int i = 0; i < size; i++) {
                if (matchInteger(array[i])) {
                    return true;
                }
            }
            return false;
        }
        if (Long.TYPE.isAssignableFrom(type)) {
            long[] array = (long[]) value;
            int size = array.length;

            for (int i = 0; i < size; i++) {
                if (matchLong(array[i])) {
                    return true;
                }
            }
            return false;
        }
        if (Byte.TYPE.isAssignableFrom(type)) {
            byte[] array = (byte[]) value;
            int size = array.length;

            for (int i = 0; i < size; i++) {
                if (matchByte(array[i])) {
                    return true;
                }
            }
            return false;
        }
        if (Short.TYPE.isAssignableFrom(type)) {
            short[] array = (short[]) value;
            int size = array.length;

            for (int i = 0; i < size; i++) {
                if (matchShort(array[i])) {
                    return true;
                }
            }
            return false;
        }
        if (Float.TYPE.isAssignableFrom(type)) {
            float[] array = (float[]) value;
            int size = array.length;

            for (int i = 0; i < size; i++) {
                if (matchFloat(array[i])) {
                    return true;
                }
            }
            return false;
        }
        if (Double.TYPE.isAssignableFrom(type)) {
            double[] array = (double[]) value;
            int size = array.length;

            for (int i = 0; i < size; i++) {
                if (matchDouble(array[i])) {
                    return true;
                }
            }
            return false;
        }
        if (Character.TYPE.isAssignableFrom(type)) {
            char[] array = (char[]) value;
            int size = array.length;

            for (int i = 0; i < size; i++) {
                if (matchCharacter(array[i])) {
                    return true;
                }
            }
            return false;
        }
        if (Boolean.TYPE.isAssignableFrom(type)) {
            boolean[] array = (boolean[]) value;
            int size = array.length;

            for (int i = 0; i < size; i++) {
                if (matchBoolean(array[i])) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    private boolean matchObjectArray(Object value) {
        Object[] array = (Object[]) value;
        int size = array.length;

        for (int i = 0; i < size; i++) {
            if (match(array[i])) {
                return true;
            }
        }
        return false;
    }
    
    private boolean matchCollection(Collection<?> value) {
        final Iterator<?> iterator = value.iterator();
        while (iterator.hasNext()) {
            if (match(iterator.next())) {
                return true;
            }
        }
        return false;
    }

    protected boolean matchBoolean(Boolean value) {
        return false;
    }

    protected boolean matchCharacter(Character value) {
        return false;
    }

    protected boolean matchDouble(Double value) {
        return false;
    }

    protected boolean matchFloat(Float value) {
        return false;
    }

    protected boolean matchLong(Long value) {
        return false;
    }

    protected boolean matchInteger(Integer value) {
        return false;
    }

    protected boolean matchShort(Short value) {
        return false;
    }

    protected boolean matchByte(Byte value) {
        return false;
    }

    protected boolean matchString(String value) {
        return false; 
    }

    protected String getEncodedValue() {
        return getEncodedValue(value);
    }
    
    public abstract String toString();
}

interface ArrayMatcher {
    boolean match(Object o);
}
