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

import java.util.Collection;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import interfaces.WineDAO;

public class WineDAOImpl extends HibernateDaoSupport implements WineDAO {

	public void save(Wine wine) {
		getHibernateTemplate().save(wine);
	}

	@SuppressWarnings("unchecked")
	public Collection<Wine> getWinesOfVintage(int vintage) {
		return getHibernateTemplate().find("from Wine wine where wine.vintage = ?", vintage);
	}

	public void update(Wine wine) {
		getHibernateTemplate().update(wine);
	}

	public Wine findById(long id) {
		return (Wine) getHibernateTemplate().get(Wine.class, id);
	}

}
