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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class HelloWorldAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        
        HttpSession session = request.getSession();
        
        incrementSerializedValue(request, session);
        
        incrementValue(request, session);
        
        request.setAttribute("from", "Phil");
        return mapping.findForward("success");
    }

    private void incrementSerializedValue(HttpServletRequest request,
            HttpSession session) {
        
        SerializableValueHolder serializableValueHolder = (SerializableValueHolder) session.getAttribute("serializedSessionValue");
        if (serializableValueHolder == null) {
            serializableValueHolder = new SerializableValueHolder();
            session.setAttribute("serializedSessionValue",
                    serializableValueHolder);
        }
        serializableValueHolder.increment();
        request.setAttribute("serializedSessionValue", serializableValueHolder.getCount());
    }

    private void incrementValue(HttpServletRequest request, HttpSession session) {
        
        ValueHolder valueHolder = (ValueHolder) session.getAttribute("sessionValue");
        if (valueHolder == null) {
            valueHolder = new ValueHolder();
            session.setAttribute("sessionValue",
                    valueHolder);
        }
        valueHolder.increment();
        request.setAttribute("sessionValue", valueHolder.getCount());
    }

}
