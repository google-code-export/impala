package tapestry5.application.pages;

import interfaces.MessageService;

import java.util.Date;

import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.ioc.annotations.Inject;

import tapestry5.application.MessageHolder;

/**
 * Start page of application.
 */
public class Tapestry5Home {
    

    @Inject
    @Service("messageService")
    private MessageService messageService;  
    
    @Inject
    @Service("messageHolder")
    private MessageHolder messageHolder;
    
    public Date getCurrentTime() {
        return new Date();
    }
    
    public String getMessage(){
        messageHolder.getMessage();
        return messageService.getMessage();
    }
    
}
