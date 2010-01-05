

import interfaces.MessageService;

/**
 * Autowired class
 * @author Phil Zoio
 */
public class AutowiredClass {

    private MessageService messageService;

    public String useMessage() {
        System.out.println(messageService);
        if (messageService == null) {
            return "autowiring not supported";
        }
        return messageService.getMessage();
    }

    //@Autowired
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }
}
