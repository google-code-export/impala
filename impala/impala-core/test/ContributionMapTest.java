import org.impalaframework.service.registry.contribution.ContributionMap;

import junit.framework.TestCase;


public class ContributionMapTest extends TestCase {

	private ContributionMap<String, String> map;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		map = new ContributionMap<String, String>();
	}
	
	public void testPlain() throws Exception {
		assertFalse(map.containsKey("key"));
		assertFalse(map.containsKey("value"));
		assertTrue(map.entrySet().isEmpty());
		assertTrue(map.keySet().isEmpty());
		assertTrue(map.isEmpty());
		assertTrue(map.isEmpty());
	}
	
}
