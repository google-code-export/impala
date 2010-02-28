/*
 * Copyright 2007-2008 the original author or authors.
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
import java.io.Reader;
import java.io.Writer;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.exception.ExecutionException;
import org.impalaframework.xml.SimpleSaxErrorHandler;
import org.springframework.beans.factory.xml.DefaultDocumentLoader;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class XMLDomUtils {

    private static Log logger = LogFactory.getLog(XMLDomUtils.class);
    
    public static String readOptionalElementText(Element element, String childName) {
        Element childElement = DomUtils.getChildElementByTagName(element, childName);
        String text = null;
        if (childElement != null)
            text = DomUtils.getTextValue(childElement);
        return text;
    }
    
    public static Document newDocument() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = null;
        try {
            docBuilder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new ExecutionException("Error in parser configuration", e);
        }
        Document doc = docBuilder.newDocument();
        return doc;
    }

    public static void output(Writer writer, Document document) {
        try {
            Source source = new DOMSource(document);
            Result result = new StreamResult(writer);
            Transformer xformer = TransformerFactory.newInstance().newTransformer();
            xformer.transform(source, result);
          } catch (Exception e) {
            throw new ExecutionException("Failed outputting XML document", e);
          }

    }

    public static Document loadDocument(Resource resource) {

        Assert.notNull(resource);
        Reader reader = ResourceUtils.getReaderForResource(resource);
        
        return loadDocument(reader, resource.getDescription());
    }

    public static Document loadDocument(Reader reader, String description) {
        Document document = null;
        DefaultDocumentLoader loader = new DefaultDocumentLoader();
        try {
            InputSource inputSource = new InputSource(reader);
            document = loader.loadDocument(inputSource, null, new SimpleSaxErrorHandler(logger),
                    XmlBeanDefinitionReader.VALIDATION_NONE, true);
        }
        catch (Exception e) {
            throw new ExecutionException("Unable to load XML document from resource " + description, e);
        }
        finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
            }
        }
        return document;
    }

    /**
     * Validates document of given description using w3c.org schema validation
     * @param document the DOM document instance
     * @param description a description of the document, typically name or path
     * @param xsdLocation the location of the schema on the classpath
     * @param xsdClassLoader the class loader to use to locate the document
     */
    public static void validateDocument(Document document, String description,
            String xsdLocation, ClassLoader xsdClassLoader) {
        
        ClassPathResource xsdResource = new ClassPathResource(xsdLocation, xsdClassLoader);
        validateDocument(document, description, xsdResource);
    }

    /**
     * Validates document of given description using w3c.org schema validation
     * @param document the DOM document instance
     * @param description a description of the document, typically name or path
     * @param xsdResource the schema resource used for validation
     */
    public static void validateDocument(
            Document document,
            String description, 
            Resource xsdResource) {
        
        Assert.notNull(xsdResource, "xsdResource cannot be null");
        
        if (!xsdResource.exists()) {
            throw new ExecutionException("Cannot validate document as xsdResource '" + xsdResource + "' does not exist");
        } else {
            System.out.println("Validating using schema resource " + xsdResource.getDescription());
            logger.debug("Validating using schema resource " + xsdResource.getDescription());
        }
        
        SchemaFactory factory = 
            SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    
        try {
            InputStream inputStream = xsdResource.getInputStream();
            Source source = new StreamSource(inputStream);
    
            Schema schema = factory.newSchema(source);
            Validator validator = schema.newValidator();
            validator.validate(new DOMSource(document));
        }
        catch (SAXParseException e) {
            throw new ExecutionException("Error on " + e.getLineNumber() + ", column " + e.getColumnNumber() + " in " + description + ": " + e.getMessage(), e);
        }
        catch (SAXException e) {
            throw new ExecutionException("Error parsing " + description + ": " + e.getMessage(), e);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
}
