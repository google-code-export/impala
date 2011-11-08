package classes;

import java.lang.reflect.Method;

import net.sf.ehcache.Cache;

import org.impalaframework.util.ReflectionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * Demonstrates overriding the ehcache lib in the module example-optional, with the original ehcache-1.3.0.jar
 * @author Phil Zoio
 */
public class OverriddenSharedJarLoadingBean implements InitializingBean {

    public void afterPropertiesSet() throws Exception {
    
        Cache cache = new Cache("name", 100, true, true, 1000L, 1000L);
        System.out.println(this.getClass().getName() + ": " + cache);
        
        //this should fall over, as it is not present in local jar api
        final Method findMethod = ReflectionUtils.findMethod(cache.getClass(), "getCacheEventNotificationService", new Class[]{});
        Assert.notNull(findMethod, "Did not expect to find method 'getCacheEventNotificationService'");

        System.out.println(cache.getCacheEventNotificationService());
    }
    
}
