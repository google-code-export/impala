package classes;

import org.springframework.beans.factory.InitializingBean;

public class HostBean implements InitializingBean {

    public String getMessage() {
        return "Hello from host bean";
    }

    public void afterPropertiesSet() throws Exception {
        System.out.println("********************* initializing host bean ********************");
    }
    
}
