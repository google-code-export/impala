/*
 * Copyright 2007 the original author or authors.
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

package net.java.impala.spring.resolver;

import java.io.File;
import java.util.Properties;

import org.springframework.util.StringUtils;

import net.java.impala.location.PropertyClassLocationResolver;

public class WebPropertyClassLocationResolver extends PropertyClassLocationResolver implements WebClassLocationResolver {

	public WebPropertyClassLocationResolver() {
		super();
	}	
	
	public WebPropertyClassLocationResolver(Properties properties) {
		super(properties);
	}

	public File getParentWebClassLocation(String parentName, String servletName) {
		String suffix = StringUtils.cleanPath(getProperty(PLUGIN_CLASS_DIR_PROPERTY));
		String path = getPath(getRootDirectoryPath(), parentName + "-" + servletName);
		path = getPath(path, suffix);
		return new File(path);
	}
}
