package classes;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;

import net.sf.ehcache.Cache;

import org.impalaframework.util.ReflectionUtils;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;

/**
 * Demonstrates overriding the ehcache lib in the module example-optional, with the original ehcache-1.3.0.jar
 * @author Phil Zoio
 */
public class OverriddenSharedJarLoadingBean implements InitializingBean, BeanClassLoaderAware {
    
    private ClassLoader classLoader;

    public void afterPropertiesSet() throws Exception {
    
        Cache cache = new Cache("name", 100, true, true, 1000L, 1000L);
        System.out.println(this.getClass().getName() + ": " + cache);
        
        //this should fall over, as it is not present in local jar api
        final Method findMethod = ReflectionUtils.findMethod(cache.getClass(), "getCacheEventNotificationService", new Class[]{});
        Assert.notNull(findMethod, "Did not expect to find method 'getCacheEventNotificationService'");

        System.out.println(cache.getCacheEventNotificationService());
        
        InputStream stream = null;
        
        try {
            stream = classLoader.getResourceAsStream("ehcache-failsafe.xml");
            final String string = FileCopyUtils.copyToString(new InputStreamReader(stream));
            //using ehcache-1.3.0, so this string is present
            Assert.isTrue(string.contains("diskExpiryThreadIntervalSeconds"));
        }
        finally {
            try {
                stream.close();
            }
            catch (Exception e) {
            }
        }
    }
    
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }
    
}
