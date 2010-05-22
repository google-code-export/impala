package org.impalaframework.samples.teamplan.orm;

import org.impalaframework.samples.teamplan.main.MessageService;

public class MessageServiceImpl implements MessageService {

    private String message;
    
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
