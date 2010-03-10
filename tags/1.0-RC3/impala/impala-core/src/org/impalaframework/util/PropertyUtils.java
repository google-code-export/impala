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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.impalaframework.exception.ExecutionException;
import org.springframework.core.io.Resource;

/**
 * @author Phil Zoio
 */
public class PropertyUtils {

    public static Properties loadProperties(URL resource) {
        InputStream inputStream = null;

        String description = resource.toString();
        try {
            inputStream = resource.openStream();
        }
        catch (IOException e) {
            throw new ExecutionException("Unable to load properties file from resource: " + description, e);
        }
        return loadProperties(inputStream, description);
    }

    public static Properties loadProperties(Resource resource) {
        InputStream inputStream = null;

        String description = resource.getDescription();
        try {
            inputStream = resource.getInputStream();
        }
        catch (IOException e) {
            throw new ExecutionException("Unable to load properties file " + description, e);
        }

        return loadProperties(inputStream, description);
    }

    public static Properties loadProperties(InputStream inputStream, String description) {
        Properties props = new Properties();
        try {
            props.load(inputStream);
        }
        catch (IOException e) {
            throw new ExecutionException("Unable to load properties file " + description, e);
        }
        finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                }
                catch (IOException e) {
                }
            }
        }
        return props;
    }

}
