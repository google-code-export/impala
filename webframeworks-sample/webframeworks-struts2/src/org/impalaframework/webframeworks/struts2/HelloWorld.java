package org.impalaframework.webframeworks.struts2;

import interfaces.MessageService;

import com.opensymphony.xwork2.ActionSupport;

public class HelloWorld extends ActionSupport {

    private static final long serialVersionUID = 1L;
    
    public static final String MESSAGE = "Struts 2 is up and running now ...";
    
    private MessageService messageService;
    
    public HelloWorld() {
        super();
    }
     
    /**
     * Autowired setter for MessageService
     */
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    public String execute() throws Exception {
        setMessage(MESSAGE + " with message from Impala message service: " + messageService.getMessage());
        return SUCCESS;
    }

    private String message;

    public void setMessage(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}