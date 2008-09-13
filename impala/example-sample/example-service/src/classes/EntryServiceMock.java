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

import interfaces.EntryService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class EntryServiceMock implements EntryService {
	private Map<Long, Entry> wines = new HashMap<Long, Entry>();

	public void addEntry(Entry wine) {
		wines.put(wine.getId(), wine);
	}

	public Collection<Entry> getEntriesOfCount(int vintage) {
		ArrayList<Entry> list = new ArrayList<Entry>();

		Collection<Entry> values = wines.values();
		for (Entry wine : values) {
			if (wine.getCount() == vintage) {
				list.add(wine);
			}
		}

		return list;
	}
}
