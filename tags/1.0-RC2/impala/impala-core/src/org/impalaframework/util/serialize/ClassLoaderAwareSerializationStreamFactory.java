package org.impalaframework.util.serialize;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

/**
 * Custom serialization implementation mechanism which allows for {@link Serializable}
 * object loaded using a different class loader to be cloned using a new class loader.
 * The new object will have the same state as the original. The only difference will be the
 * identity of the object's class loader.
 * 
 * Used for maintaining {@link javax.servlet.http.HttpSession} attributes across module reloads.
 * 
 * Based on http://blog.araneaframework.org/2006/11/21/zero-turn-around-in-java/
 * and http://weblogs.java.net/blog/emcmanus/archive/2007/04/cloning_java_ob.html
 * @author Phil Zoio
 */
public class ClassLoaderAwareSerializationStreamFactory implements SerializationStreamFactory {

    private static final Log logger = LogFactory.getLog(ClassLoaderAwareSerializationStreamFactory.class);
     
    private ClassLoader classLoader;
    private AnnotatedObjectOutputStream objectOutputStream;

    public ClassLoaderAwareSerializationStreamFactory(ClassLoader classLoader) {
        super();
        Assert.notNull(classLoader);
        this.classLoader = classLoader;
    }

    public ObjectOutputStream getOutputStream(OutputStream output)
            throws IOException {
        AnnotatedObjectOutputStream cloneOutput = new AnnotatedObjectOutputStream(output);
        
        //store for use by input
        this.objectOutputStream = cloneOutput;
        return cloneOutput;
    }
    
    public ObjectInputStream getInputStream(InputStream input)
            throws IOException {
        return new ClassLoaderAwareInputStream(objectOutputStream, input, classLoader);
    }
    
    private static class AnnotatedObjectOutputStream extends ObjectOutputStream {
        Queue<String> classQueue = new LinkedList<String>();

        AnnotatedObjectOutputStream(OutputStream out) throws IOException {
            super(out);
        }

        @Override
        protected void annotateClass(Class<?> c) {
            if (logger.isDebugEnabled()) 
                logger.debug("annotateClass: " + c.getName());
            
            classQueue.add(c.getName());
        }

        @Override
        protected void annotateProxyClass(Class<?> c) {
            if (logger.isDebugEnabled()) 
                logger.debug("annotateProxyClass: " + c.getName());
            
            classQueue.add(c.getName());
        }
    }
    
    private static class ClassLoaderAwareInputStream extends ObjectInputStream {
        
        private final AnnotatedObjectOutputStream output;
        private ClassLoader classLoader;
        
        ClassLoaderAwareInputStream(AnnotatedObjectOutputStream output, InputStream in, ClassLoader classLoader) throws IOException {
            super(in);
            this.output = output;
            this.classLoader = classLoader;
        }

        @Override
        protected Class<?> resolveClass(ObjectStreamClass osc)
                throws IOException, ClassNotFoundException {
            String className = output.classQueue.poll();
            if (logger.isDebugEnabled()) logger.debug("About to attempt to load class " + className);
            
            final Class<?> c = Class.forName(className, true, classLoader);
            return c;
        }
    }
}
