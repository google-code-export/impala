package optional;

import net.sf.ehcache.Cache;

import org.impalaframework.util.ReflectionUtils;
import org.springframework.beans.factory.InitializingBean;

public class LocalJarLoadingBean implements InitializingBean {

    public void afterPropertiesSet() throws Exception {
    
        Cache cache = new Cache("name", 100, true, true, 1000L, 1000L);
        System.out.println(cache);
        
        //this should fall over, as it is not present in local jar api
        System.out.println(ReflectionUtils.invokeMethod(cache, "getCacheEventNotificationService", new Object[]{}));
    }
}

