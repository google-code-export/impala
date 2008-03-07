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

import java.io.File;

/**
 * @author Phil Zoio
 */
public class PathUtils {
	private static final String FOLDER_SEPARATOR = "/";

	public static String getCurrentDirectoryName() {
		File file = new File(new File("").getAbsolutePath());
		return file.getName();
	}

	public static String getPath(String root, String suffix) {
		//FIXME test what happens with / and "" are passed in 
		if (root == null) {
			root = "";
		}
		if (root.endsWith(FOLDER_SEPARATOR)) {
			root = root.substring(0, root.length()-1);
		}
		if (suffix == null) {
			suffix = FOLDER_SEPARATOR;
		}
		if (!suffix.startsWith(FOLDER_SEPARATOR)) {
			suffix = FOLDER_SEPARATOR + suffix;
		}
		String path = root + suffix;
		return path;
	}

}
