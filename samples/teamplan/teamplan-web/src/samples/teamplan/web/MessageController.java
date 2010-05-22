package samples.teamplan.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import samples.teamplan.main.MessageService;

import java.util.Map;

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
