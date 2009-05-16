package org.impalaframework.module.loader;

import java.util.List;

import junit.framework.Assert;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;

public class ModuleTestUtils {

    @SuppressWarnings("unchecked")
    public static void checkHasPostProcessor(boolean expectPostProcessor,
            ConfigurableApplicationContext context, Class processorClass) {
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) context.getBeanFactory();
        List<BeanPostProcessor> beanPostProcessors = beanFactory.getBeanPostProcessors();
        
        boolean hasPostProcessor = false;
        for (BeanPostProcessor processor : beanPostProcessors) {
            if (processorClass.isAssignableFrom(processor.getClass())) {
                hasPostProcessor = true;
            }
        }
        Assert.assertTrue(hasPostProcessor == expectPostProcessor);
    }

}
