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

/**
 * Class with utility methods for manipulating {@link StringBuffer}s.
 * @author Phil Zoio
 */
public class StringBufferUtils {
    
    /**
     * Lobs the last <code>chars</code> off the {@link StringBuffer}
     * @param buffer the current StringBuffer, which may be modified
     * @param chars number of characters to remove
     */
    public static void chop(StringBuffer buffer, int chars) {
        int numberToChop = Math.min(buffer.length(), chars);
        buffer.delete(buffer.length()-numberToChop, buffer.length());
    }
}
