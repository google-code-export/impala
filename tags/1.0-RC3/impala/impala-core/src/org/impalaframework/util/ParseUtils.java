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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class with static utility methods for parseing {@link Date}s, {@link Double}s, {@link Long}s and {@link Boolean}s.
 * @author Phil Zoio
 */
public abstract class ParseUtils {

    private static String[] DEFAULT_PATTERNS = new String[] {
        "yyyy-MM-dd HH:mm:ss.SSS",
        "yyyy-MM-dd HH:mm:ss",
        "yyyy-MM-dd"
    };

    /**
     * Attempts to parse <code>text</code> as java.util.Date. Will use the
     * formats "yyyy-MM-dd HH:mm:ss.SSS", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd".
     * @param text the input String
     * @return null if no value is provided or if value could not be parsed as
     * date using one of the patterns provided.
     */
    public static Date maybeParseDate(String text) {
        
        return maybeParseDate(text, DEFAULT_PATTERNS);
    }
    
    /**
     * Attempts to parse <code>text</code>.
     * @param text the input String
     * @param patterns he user supplied set of patterns. Can be null, in which
     * case the same patters as {@link #maybeParseDate(String)} will be used.
     * @return null if no value is provided or if value could not be parsed as
     * date using one of the patterns provided.
     */
    public static Date maybeParseDate(String text, String... patterns) {

        if (text == null) { return null; }
        
        String[] patternKeys = ((!ArrayUtils.isNullOrEmpty(patterns)) ? patterns : DEFAULT_PATTERNS);
        
        for (String pattern : patternKeys) {

            DateFormat format = new SimpleDateFormat(pattern);
            Date parse = null;
            try {
                parse = format.parse(text);
                return parse;
            } catch (ParseException e) {
            }
        }
        return null;
    }
    
    /**
     * Attempts to parse <code>text</code> as Double.
     * @param text the input String
     * @return null if no value is provided or if value could not be parsed as
     * Double.
     */
    public static Double maybeParseDouble(String text) {
        if (text == null) {
            return null;
        }
        Double number = null;
        try {
            number = Double.valueOf(text);
        } catch (NumberFormatException e) {
        }
        return number;
    }
    
    /**
     * Attempts to parse <code>text</code> as Double.
     * @param text the input String
     * @return null if no value is provided or if value could not be parsed as
     * Long.
     */
    public static Long maybeParseLong(String text) {
        if (text == null) {
            return null;
        }
        Long number = null;
        try {
            number = Long.valueOf(text);
        } catch (NumberFormatException e) {
        }
        return number;
    }
    
    /**
     * Attempts to parse <code>text</code> as Double.
     * @param text the input String
     * @return null if value supplied is not 'true' or 'false', otherwise the
     * Boolean representation of this value.
     */
    public static Boolean maybeParseBoolean(String text) {
        if (text == null) {
            return null;
        }
        if ("true".equals(text) || "false".equals(text)) {
            return Boolean.valueOf(text);
        }
        return null;
    }
    
    /**
     * Attempt to parse text into an object using the following scheme:
     * <ul>
     * <li>if null then leave as such
     * <li>First attempt to parse the field as a date using one of the default date formats. See {@link #maybeParseDate(String)}.
     * <li>Then attempt to parse as {@link Long}
     * <li>Then attempt to parse as {@link Double}
     * <li>Then attempt to parse as {@link Boolean}
     * <li>Otherwise treat as string
     * </ul>
     */
    public static Object parseObject(String text) {
        if (text == null) return null;
        Date date = maybeParseDate(text);
        if (date != null) {
            return date;
        }
        
        Long longNumber = maybeParseLong(text);
        if (longNumber != null) {
            return longNumber;
        }
        
        Double doubleNumber = maybeParseDouble(text);
        if (doubleNumber != null) {
            return doubleNumber;
        }
        
        Boolean booleanValue = maybeParseBoolean(text);
        if (booleanValue != null) {
            return booleanValue;
        }
        
        return text;
    }
    
}
