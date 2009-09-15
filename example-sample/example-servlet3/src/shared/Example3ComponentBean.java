package shared;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

@Service
public class Example3ComponentBean implements InitializingBean {

    public void afterPropertiesSet() throws Exception {
        System.out.println(">>>>>>>>>> Starting example 3 bean");
    }
    
}
