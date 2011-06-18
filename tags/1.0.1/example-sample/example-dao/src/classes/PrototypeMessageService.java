package classes;

import interfaces.MessageService;

/**
 * Prototype service which will show a new message each time getMessage is called
 * @author Phil Zoio
 */
public class PrototypeMessageService implements MessageService {

    private long time;

    public PrototypeMessageService() {
        super();
        time = System.currentTimeMillis();
    }

    public String getMessage() {
        return "Hello at " + time + " from " + this;
    }

}
