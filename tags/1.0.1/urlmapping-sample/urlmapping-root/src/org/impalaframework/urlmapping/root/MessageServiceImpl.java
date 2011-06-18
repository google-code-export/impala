package org.impalaframework.urlmapping.root;

public class MessageServiceImpl implements UpdatableMessageService {

    private String message = "Hello world";
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getMessage() {
        return message;
    }

}
