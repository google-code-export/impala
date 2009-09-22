package shared;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

@Service
public class WebComponentBean implements InitializingBean, BeanNameAware {
    
    private String beanName;

    public void afterPropertiesSet() throws Exception {
        System.out.println(">>>>>>>>>> Starting web bean: " + beanName);
    }
    
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }
    
}
