package org.impalaframework.samples.maven.module;

import org.impalaframework.samples.maven.MessageService;

public class MessageServiceImpl implements MessageService {

    public String getMessage() {
        return "Hello World, Maven style";
    }

}
