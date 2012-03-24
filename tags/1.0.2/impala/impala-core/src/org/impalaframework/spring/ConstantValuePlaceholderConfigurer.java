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

package org.impalaframework.spring;

import java.lang.reflect.Field;

import org.impalaframework.exception.ConfigurationException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionVisitor;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.util.StringValueResolver;

public class ConstantValuePlaceholderConfigurer implements BeanFactoryPostProcessor, BeanNameAware, BeanFactoryAware, BeanClassLoaderAware {

    private BeanFactory beanFactory;
    private String beanName;
    private String constantPrefix = "constant:[";
    private String constantSuffix = "]";
    private ClassLoader classLoader;

    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        
        StringValueResolver valueResolver = new ConstantStringValueResolver();
        BeanDefinitionVisitor visitor = new BeanDefinitionVisitor(valueResolver);

        String[] beanNames = beanFactory.getBeanDefinitionNames();
        for (int i = 0; i < beanNames.length; i++) {
            
            // Check that we're not parsing our own bean definition
            if (!(beanNames[i].equals(this.beanName) && beanFactory.equals(this.beanFactory))) {
                BeanDefinition bd = beanFactory.getBeanDefinition(beanNames[i]);
                try {
                    visitor.visitBeanDefinition(bd);
                }
                catch (BeanDefinitionStoreException ex) {
                    throw new BeanDefinitionStoreException(bd.getResourceDescription(), beanNames[i], ex.getMessage());
                }
            }
        }
    }   
    
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void setBeanName(String name) {
        this.beanName = name;
    }
    
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }
    
    class ConstantStringValueResolver implements StringValueResolver {
        
        ConstantStringValueResolver() {
            super();
        }

        public String resolveStringValue(String strVal) {
            if (strVal == null) return null;
            
            String trim = strVal.trim();
            if (trim.startsWith(constantPrefix) && trim.endsWith(constantSuffix)) {
                
                String expression = trim.substring(constantPrefix.length(), trim.length() - constantSuffix.length()).trim();
                
                int lastDotIndex = expression.lastIndexOf('.');
                
                if (lastDotIndex < 0) {
                    throw new ConfigurationException("Invalid expression '" + expression + "' in expression '" + strVal + "'. Must evaluate to constant (e.g. 'constant:[org.impalframework.constants.LocationConstant.MODULE_CLASS_DIR_PROPERTY]'");
                }
                
                String className = expression.substring(0, lastDotIndex);
                String fieldName = expression.substring(lastDotIndex + 1);
                
                try {
                    Class<?> clazz = Class.forName(className, true, classLoader);
                    try {
                        Field field = clazz.getField(fieldName);
                        Object value = field.get(null);
                        if (value != null) {
                            return value.toString();
                        } else {
                            throw new ConfigurationException("Field '" + fieldName + "' in class " + className + " in expression '" + strVal + "' cannot evaluate to null");
                        }
                    }
                    catch (ConfigurationException e) {
                        throw e;
                    }
                    catch (Exception e) {
                        throw new ConfigurationException("Field '" + fieldName + "' in class " + className + " in expression '" + strVal + "' could not be evaluated. Could not evaluate constant in bean '" + beanName +"'", e);
                    }
                }
                catch (ClassNotFoundException e) {
                    throw new ConfigurationException("Class '" + className + "' in expression '" + strVal + "' could not be found. Could not evaluate constant in bean '" + beanName +"'" );
                }
            }
            
            return strVal;
        }
        
    }


}
