package example3.servlet;

import java.io.Serializable;

public class SessionValueHolder implements Serializable {

    private static final long serialVersionUID = 1L;
    int value;
    private String message;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
    
    public void increment() {
        value++;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String title) {
        this.message = title;
    }

}
