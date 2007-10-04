package net.java.impala.ant;

import java.net.MalformedURLException;
import java.net.URL;

import junit.framework.TestCase;

public class GetTaskResultTest extends TestCase {

	public void testResultConstructor() throws MalformedURLException {
		construct(false, 0, null, "");
		construct(false, 1, null, "");
		construct(false, 2, new URL("http://location"), "");

		construct(true, -1, null, "result must be between 0 and 2 (inclusive)");
		construct(true, 3, null, "result must be between 0 and 2 (inclusive)");
		construct(true, 2, null, "success location required for successful result");
	}

	public void testToString() throws MalformedURLException {
		toString(0, null, "archive not modified");
		toString(1, null, "archive could not be downloaded from any location");
		toString(2, new URL("http://location"), "archive resolved from http://location");
	}

	private void construct(boolean fail, int result, URL successLocation, String expected) {
		try {
			new Result("archive", result, successLocation);
			if (fail)
				fail("Success not expected");
		}
		catch (IllegalArgumentException e) {
			if (fail)
				assertEquals(expected, e.getMessage());
			else
				fail("Failure not expected");
		}
	}

	private void toString(int result, URL successLocation, String expected) {
		assertEquals(expected, new Result("archive", result, successLocation).toString());
	}
}
