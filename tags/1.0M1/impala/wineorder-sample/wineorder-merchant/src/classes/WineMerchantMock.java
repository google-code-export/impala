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

package classes;

import interfaces.WineMerchant;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class WineMerchantMock implements WineMerchant {
	private Map<Long, Wine> wines = new HashMap<Long, Wine>();

	public void addWine(Wine wine) {
		wines.put(wine.getId(), wine);
	}

	public Collection<Wine> getWinesOfVintage(int vintage) {
		ArrayList<Wine> list = new ArrayList<Wine>();

		Collection<Wine> values = wines.values();
		for (Wine wine : values) {
			if (wine.getVintage() == vintage) {
				list.add(wine);
			}
		}

		return list;
	}
}
