package org.impalaframework.urlmapping.web;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MessageController {
    
    @SuppressWarnings("unchecked")
    @RequestMapping("/notes.htm")
    public void viewMessage(Map model) {
    }
}
