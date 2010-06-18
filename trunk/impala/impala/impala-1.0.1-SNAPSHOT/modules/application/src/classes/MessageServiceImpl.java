package @project.package.name@.@module.project.name@;

import @project.package.name@.@main.project.name@.MessageService;

public class MessageServiceImpl implements MessageService {

    private String message;
    
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
