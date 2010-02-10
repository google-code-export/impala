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

import interfaces.EntryService;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.impalaframework.config.FloatPropertyValue;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import classes.ConcreteService;

import shared.ValueHolder;

public class TestController1 extends MultiActionController {

    private ConcreteService concreteService;
    
    private EntryService entryService;
    
    private FloatPropertyValue magicNumber;

    public ModelAndView test(HttpServletRequest request, HttpServletResponse response) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("staticparam", ""+magicNumber.getValue());
        map.put("dynamicparam", "" + entryService.getEntriesOfCount(1996).size());

        setSessionValue(request, map, "shared:intvalue", "shared_intvalue");
        setSessionValue(request, map, "intvalue", "intvalue");

        ModelAndView mav = new ModelAndView("test", map);
        return mav;
    }

    private void setSessionValue(HttpServletRequest request,
            HashMap<String, String> map, 
            final String sessionAttributeName,
            final String moduleAttributeName) {
        
        HttpSession session = request.getSession();
        ValueHolder valueHolder = (ValueHolder) session.getAttribute(sessionAttributeName);
        if (valueHolder == null) {
            valueHolder = new ValueHolder();
            session.setAttribute(sessionAttributeName, valueHolder);
        }
        valueHolder.increment();
        map.put(moduleAttributeName, ""+valueHolder.getCount());
        
        concreteService.doSomething();
    }

    public void setEntryService(EntryService entryService) {
        this.entryService = entryService;
    }

    public void setMagicNumber(FloatPropertyValue magicNumber) {
        this.magicNumber = magicNumber;
    }
    
    public void setConcreteService(ConcreteService concreteService) {
        this.concreteService = concreteService;
    }

}
