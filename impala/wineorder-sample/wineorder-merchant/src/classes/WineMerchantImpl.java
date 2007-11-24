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

package classes;

import interfaces.WineDAO;
import interfaces.WineMerchant;

import java.util.Collection;

import org.springframework.transaction.annotation.Transactional;

public class WineMerchantImpl implements WineMerchant {

	private WineDAO wineDAO;

	@Transactional()
	public void addWine(Wine wine) {
		wineDAO.save(wine);
	}

	public Collection<Wine> getWinesOfVintage(int vintage) {
		return wineDAO.getWinesOfVintage(vintage);
	}

	public void setWineDAO(WineDAO wineDAO) {
		this.wineDAO = wineDAO;
	}

}
