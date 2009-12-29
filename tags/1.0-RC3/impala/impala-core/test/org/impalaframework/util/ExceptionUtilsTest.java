package org.impalaframework.util;

import junit.framework.TestCase;

public class ExceptionUtilsTest extends TestCase {
    public void testReturnException() {
        Exception e = new Exception("Some message", new RuntimeException("Runtime message"));
        String returnException = ExceptionUtils.returnException(e);
        System.out.println(returnException);
        
        assertTrue(returnException.startsWith("Runtime message (java.lang.RuntimeException)"));
    }
    
    public void testPrintException() {
        Exception e = new Exception("Some message", new RuntimeException("Runtime message"));
        Throwable t = ExceptionUtils.getRootCause(e);
        
        String exceptionString = ExceptionUtils.getStackTraceString(t, "<br/>", 3);
        System.out.println(exceptionString);
    }
    
    public void testGetRootCause() throws Exception {
        Exception root = new Exception();
        Exception e2 = new Exception(root);
        Exception e3 = new Exception(e2);
        Exception e4 = new Exception(e3);
        
        assertSame(root, ExceptionUtils.getRootCause(e4));
        assertSame(root, ExceptionUtils.getRootCause(e3));
        assertSame(root, ExceptionUtils.getRootCause(e2));
        assertSame(root, ExceptionUtils.getRootCause(root));
    }

}
