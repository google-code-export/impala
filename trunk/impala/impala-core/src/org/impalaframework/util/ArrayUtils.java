package org.impalaframework.util;

import java.util.ArrayList;
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

import java.util.List;

import org.springframework.util.Assert;

public abstract class ArrayUtils {
	
	public static List<String> toList(String[] array) {
		Assert.notNull(array);
		java.util.List<String> list = new ArrayList<String>(array.length);
		for (String entry : array) {
			list.add(entry);
		}
		return list;
	}
}
