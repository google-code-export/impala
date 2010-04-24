package com.application.jsf;

import java.util.Date;

public class SayHelloController {
    
    private String result;
    
    public void sayHello() {
        System.out.println("Hello World from Phil again");
        result = "Phil said 'Hi There' on date: " + new Date() + " from class " + this;
    }
    
    public String getResult() {
        return result;
    }
}
