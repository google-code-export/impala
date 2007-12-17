package org.impalaframework.module.beanset;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.impalaframework.module.beanset.BeanSetMapReader;
import org.springframework.beans.FatalBeanException;

public class BeanSetMapReaderTest extends TestCase {

	public final void testNull() {
		BeanSetMapReader reader = new BeanSetMapReader();
		final Map<String, Set<String>> map = reader.readBeanSetSpec(null);
		assertEquals(0, map.size());
	}	
	
	public final void testReadBeanSetSpec() {
		BeanSetMapReader reader = new BeanSetMapReader();
		final Map<String, Set<String>> map = reader.readBeanSetSpec("null: set1, set2; mock: set3, ");
		assertEquals(2, map.size());
		
		final Set<String> setNull = map.get("null");
		assertEquals(2, setNull.size());
		final Iterator<String> iteratorNull = setNull.iterator();
		
		assertEquals("set1", iteratorNull.next());
		assertEquals("set2", iteratorNull.next());
		
		final Set<String> setMock = map.get("mock");
		assertEquals(1, setMock.size());
		final Iterator<String> iteratorMock = setMock.iterator();
		
		assertEquals("set3", iteratorMock.next());
	}
	
	public final void testNothingAtEnd() {
		BeanSetMapReader reader = new BeanSetMapReader();
		final Map<String, Set<String>> map = reader.readBeanSetSpec("null:, set1; mock:");
		assertEquals(1, map.size());
		
		final Set<String> setNull = map.get("null");
		assertEquals(1, setNull.size());
		final Iterator<String> iteratorNull = setNull.iterator();
		assertEquals("set1", iteratorNull.next());
	}
	
	public void testMissingColonIndex() {
		try {
			new BeanSetMapReader().readBeanSetSpec("null: set1, set2; mock set3");
			fail("Expected missing colon index failure");
		}
		catch (FatalBeanException e) {
			assertEquals(
					"Invalid beanset specification. Missing ':' from string ' mock set3' in 'null: set1, set2; mock set3'",
					e.getMessage());
		}
	}

}