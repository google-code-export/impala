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

package struts;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.config.ModuleConfig;

/**
 * Extension of Struts <code>ActionServlet</code> which allows it to be reloadable.
 * The <code>destroy</code> method simply removes any <code>RequestProcessor</code> instances
 * held as <code>ServletContext</code> keys
 * @author Phil Zoio
 */
public class ReloadableActionServlet extends ActionServlet {

    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unchecked")
    @Override
    public void destroy() {

        List <String>values = new ArrayList<String>();
        Enumeration<String> names = getServletContext().getAttributeNames();
        while (names.hasMoreElements()) {
            values.add(names.nextElement());
        }

        Iterator<String> keys = values.iterator();
        while (keys.hasNext()) {
            String name = keys.next();
            Object value = getServletContext().getAttribute(name);

            if (!(value instanceof ModuleConfig)) {
                continue;
            }

            ModuleConfig config = (ModuleConfig) value;
            getServletContext().removeAttribute(
                    Globals.REQUEST_PROCESSOR_KEY + config.getPrefix());
        }

        super.destroy();
    }

}
