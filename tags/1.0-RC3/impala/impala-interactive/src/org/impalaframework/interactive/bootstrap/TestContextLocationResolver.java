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

package org.impalaframework.interactive.bootstrap;

import org.impalaframework.bootstrap.ConfigurationSettings;
import org.impalaframework.bootstrap.SimpleContextLocationResolver;
import org.impalaframework.config.PropertySource;
import org.impalaframework.config.StringPropertyValue;

public class TestContextLocationResolver extends SimpleContextLocationResolver {

    @Override
    public void addCustomLocations(ConfigurationSettings configSettings,
            PropertySource propertySource) {
        super.addCustomLocations(configSettings, propertySource);
        
        StringPropertyValue testClassDirectory = new StringPropertyValue(propertySource, TestBootstrapProperties.MODULE_TEST_DIRECTORY, "bin");
        configSettings.addProperty(TestBootstrapProperties.MODULE_TEST_DIRECTORY, testClassDirectory);
        
        configSettings.add("META-INF/impala-test-bootstrap.xml");
    }

}
