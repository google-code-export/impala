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

import java.io.File;

import org.impalaframework.constants.LocationConstants;
import org.impalaframework.web.StartJetty;
import org.impalaframework.web.WebConstants;

public class StartServer {
    public static void main(String[] args) {
        //you can add dynamic.properties into /tmp and edit it there
        System.setProperty("property.folder", "/tmp");
        System.setProperty("org.mortbay.log.class", "org.mortbay.log.StdErrLog");
        System.setProperty("example-host."+WebConstants.BOOTSTRAP_LOCATIONS_RESOURCE_PARAM, "classpath:impala-embedded.properties");
        
        //an example of a context path-specific system property
        System.setProperty("example-host.auto.reload.check.delay", "15");
        System.setProperty(LocationConstants.WORKSPACE_ROOT_PROPERTY, new File("../").getAbsolutePath());
        StartJetty.main(new String[]{"8080", "../example-host/context", "/example-host"});
    }
}
