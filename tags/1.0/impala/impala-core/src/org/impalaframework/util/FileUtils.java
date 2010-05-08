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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.impalaframework.exception.ExecutionException;
import org.impalaframework.exception.InvalidStateException;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

/**
 * @author Phil Zoio
 */
public class FileUtils {

    public static byte[] getBytes(File file) throws IOException {
        if (file == null)
            throw new IllegalArgumentException("File is null");

        if (!file.exists())
            throw new InvalidStateException("File " + file + " does not exist");

        return FileCopyUtils.copyToByteArray(file);
    }

    public static byte[] getBytes(Resource resource) throws IOException {
        InputStream inputStream = null;
        try {
             inputStream = resource.getInputStream();
            return FileCopyUtils.copyToByteArray(inputStream);
        }
        finally {
            try {
                inputStream.close();
            }
            catch (Exception e) {
            }
        }
    }
    
    public static List<String> readLines(Reader reader) {
        try {
            List<String> lines = new ArrayList<String>();
            BufferedReader bufferedReader = new BufferedReader(reader);
            String readLine = null;
            while ((readLine = bufferedReader.readLine()) != null) {
                lines.add(readLine);
            }
            return lines;
        }
        catch (IOException e) {
            throw new ExecutionException("Error reading lines using reader " + reader, e);
        }
    }

}
