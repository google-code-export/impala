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
 * Type helper class for use in {@link FilterNode}
 *
 * @author Phil Zoio
 */
class TypeHelper {

    protected static boolean equalsBoolean(String external, Boolean value) {
        final Boolean objValue = getBoolean(external);
        return (objValue != null ? objValue.equals(value) : false);
    }

    protected static boolean equalsByte(String external, Byte value) {
        final Byte objValue = getByte(external);
        return (objValue != null ? objValue.equals(value) : false);
    }

    
    protected static boolean equalsCharacter(String external, Character value) {
        final Character objValue = getCharacter(external);
        return (objValue != null ? objValue.equals(value) : false);
    }

    
    protected static boolean equalsDouble(String external, Double value) {
        final Double objValue = getDouble(external);
        return (objValue != null ? objValue.equals(value) : false);
    }

    
    protected static boolean equalsFloat(String external, Float value) {
        final Float objValue = getFloat(external);
        return (objValue != null ? objValue.equals(value) : false);
    }

    
    protected static boolean equalsInteger(String external, Integer value) {
        final Integer objValue = getInteger(external);
        return (objValue != null ? objValue.equals(value) : false);
    }

    
    protected static boolean equalsLong(String external, Long value) {
        final Long objValue = getLong(external);
        return (objValue != null ? objValue.equals(value) : false);
    }

    
    protected static boolean equalsShort(String external, Short value) {
        final Short objValue = getShort(external);
        return (objValue != null ? objValue.equals(value) : false);
    }       
    
    protected static boolean greaterOrEqualToString(String objValue, String value) {
        return (objValue != null ? value.compareTo(objValue) >= 0 : false);
    }
    
    protected static boolean greaterOrEqualToByte(String external, Byte value) {
        final Byte objValue = getByte(external);
        return (objValue != null ? value.compareTo(objValue) >= 0: false);
    }

    
    protected static boolean greaterOrEqualToCharacter(String external, Character value) {
        final Character objValue = getCharacter(external);
        return (objValue != null ? value.compareTo(objValue) >= 0 : false);
    }

    
    protected static boolean greaterOrEqualToDouble(String external, Double value) {
        final Double objValue = getDouble(external);
        return (objValue != null ? value.compareTo(objValue)  >= 0: false);
    }

    
    protected static boolean greaterOrEqualToFloat(String external, Float value) {
        final Float objValue = getFloat(external);
        return (objValue != null ? value.compareTo(objValue) >= 0 : false);
    }

    
    protected static boolean greaterOrEqualToInteger(String external, Integer value) {
        final Integer objValue = getInteger(external);
        return (objValue != null ? value.compareTo(objValue) >= 0 : false);
    }

    
    protected static boolean greaterOrEqualToLong(String external, Long value) {
        final Long objValue = getLong(external);
        return (objValue != null ? value.compareTo(objValue) >= 0 : false);
    }

    
    protected static boolean greaterOrEqualToShort(String external, Short value) {
        final Short objValue = getShort(external);
        return (objValue != null ? value.compareTo(objValue) >= 0 : false);
    }
    
    protected static boolean lessOrEqualToString(String objValue, String value) {
        return (objValue != null ? objValue.compareTo(value) >= 0: false);
    }
    
    protected static boolean lessOrEqualToByte(String external, Byte value) {
        final Byte objValue = getByte(external);
        return (objValue != null ? objValue.compareTo(value) >= 0: false);
    }

    protected static boolean lessOrEqualToCharacter(String external, Character value) {
        final Character objValue = getCharacter(external);
        return (objValue != null ? objValue.compareTo(value) >= 0 : false);
    }

    
    protected static boolean lessOrEqualToDouble(String external, Double value) {
        final Double objValue = getDouble(external);
        return (objValue != null ? objValue.compareTo(value)  >= 0: false);
    }

    
    protected static boolean lessOrEqualToFloat(String external, Float value) {
        final Float objValue = getFloat(external);
        return (objValue != null ? objValue.compareTo(value) >= 0 : false);
    }

    
    protected static boolean lessOrEqualToInteger(String external, Integer value) {
        final Integer objValue = getInteger(external);
        return (objValue != null ? objValue.compareTo(value) >= 0 : false);
    }

    
    protected static boolean lessOrEqualToLong(String external, Long value) {
        final Long objValue = getLong(external);
        return (objValue != null ? objValue.compareTo(value) >= 0 : false);
    }

    
    protected static boolean lessOrEqualToShort(String external, Short value) {
        final Short objValue = getShort(external);
        return (objValue != null ? objValue.compareTo(value) >= 0 : false);
    }
    
    static Boolean getBoolean(String s) {
        return Boolean.valueOf(s);
    }
    
    static Byte getByte(String s) {
        try {
            return Byte.valueOf(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    static Short getShort(String s) {
        try {
            return Short.valueOf(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    static Integer getInteger(String s) {
        try {
            return Integer.valueOf(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    static Long getLong(String s) {
        try {
            return Long.valueOf(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    static Float getFloat(String s) {
        try {
            return Float.valueOf(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    static Double getDouble(String s) {
        try {
            return Double.valueOf(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    static Character getCharacter(String s) {
        try {
            if (s.length() > 1) return null;
            return s.charAt(0);
        } catch (NumberFormatException e) {
            return null;
        }
    }   
    
}
