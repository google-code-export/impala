package org.impalaframework.samples.maven.web;

import java.util.Map;

import org.impalaframework.samples.maven.MessageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@SuppressWarnings("unchecked")
public class MessageController {
    private MessageService messageService;

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    @RequestMapping("/message.htm")
    public void viewMessage(Map model) {
        model.put("message", messageService.getMessage());
    }
}
