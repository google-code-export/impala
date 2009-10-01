package org.impalaframework.webframeworks.struts2;

import com.opensymphony.xwork2.ActionSupport;

public class HelloWorld extends ActionSupport {

    private static final long serialVersionUID = 1L;
    
    public static final String MESSAGE = "Struts 2 is up and running now ...";

    public String execute() throws Exception {
        setMessage(MESSAGE);
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