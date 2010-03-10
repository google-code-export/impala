/*
 * Copyright 2007-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.impalaframework.xml.schema;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Implementation of {@link EntityResolver} used to resolve a schema location to a named resource on the classpath.
 * 
 * @author Phil Zoio
 */
public class ClassPathSchemaResolver implements EntityResolver {

    private static final Log logger = LogFactory.getLog(ClassPathSchemaResolver.class);
    
    private String resourceLocation;

    private ClassLoader classLoader;

    public ClassPathSchemaResolver(String schemaLocation, ClassLoader classLoader) {
        super();
        this.resourceLocation = schemaLocation;
        this.classLoader = classLoader;
    }

    public InputSource resolveEntity(String publicId, String systemId)
            throws SAXException, IOException {

        if (systemId != null) {
            if (resourceLocation != null) {
                Resource resource = new ClassPathResource(resourceLocation, this.classLoader);
                InputSource source = new InputSource(resource.getInputStream());
                source.setPublicId(publicId);
                source.setSystemId(systemId);
                
                if (logger.isDebugEnabled()) {
                    logger.debug("Found XML schema for system id in '" + systemId
                            + "' in classpath: " + resourceLocation);
                }
                
                return source;
            }
        }
        return null;
    }

}
