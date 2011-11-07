package classes;

import net.sf.ehcache.Cache;

import org.springframework.beans.factory.InitializingBean;

public class SharedJarLoadingBean implements InitializingBean {

    public void afterPropertiesSet() throws Exception {
    
        Cache cache = new Cache("name", 100, true, true, 1000L, 1000L);
        System.out.println(cache);
        
        System.out.println(cache.getCacheEventNotificationService());
    }
    
}
