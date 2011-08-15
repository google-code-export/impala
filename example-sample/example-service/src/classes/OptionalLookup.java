package classes;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class OptionalLookup implements ApplicationContextAware, InitializingBean {

    private ApplicationContext applicationContext;
    
    public void afterPropertiesSet() throws Exception {
        try {
            final Object bean = applicationContext.getBean("optionalBean");
            System.out.println("Optional bean " + bean);
        }
        catch (Exception e) {
            System.out.println("Not able to find optional bean: " + e.getClass().getName());
        }
    }
    
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.applicationContext = applicationContext;
    }
    
}
