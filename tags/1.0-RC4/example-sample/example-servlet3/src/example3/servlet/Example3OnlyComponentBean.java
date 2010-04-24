package example3.servlet;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

@Service
public class Example3OnlyComponentBean implements InitializingBean, BeanNameAware {

    private String beanName;

    public void afterPropertiesSet() throws Exception {
        System.out.println(">>>>>>>>>> Starting example 3 only component bean updated: " + beanName);
    }

    public void setBeanName(String name) {
        this.beanName = name;
    }
    
}
