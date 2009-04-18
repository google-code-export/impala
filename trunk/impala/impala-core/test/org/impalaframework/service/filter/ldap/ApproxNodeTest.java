package org.impalaframework.service.filter.ldap;

import junit.framework.TestCase;

public class ApproxNodeTest extends TestCase {

    public void testApprox() {
        assertEquals("", ApproxNode.approx(""));
        assertEquals("abc", ApproxNode.approx("ABC"));
        assertEquals("abcdef", ApproxNode.approx("abc \n def \n"));
        assertEquals("abcdef", ApproxNode.approx("\nABC \n def"));
    }

}
