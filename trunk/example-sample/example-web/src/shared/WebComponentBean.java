package shared;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

@Service
public class WebComponentBean implements InitializingBean {

    public void afterPropertiesSet() throws Exception {
        System.out.println(">>>>>>>>>> Starting web bean");
    }
    
}
