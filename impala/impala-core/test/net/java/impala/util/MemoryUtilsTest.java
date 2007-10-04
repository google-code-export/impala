package net.java.impala.util;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class MemoryUtilsTest extends TestCase {

	public void testUsedMemory() {

		List<Object> list = new ArrayList<Object>();

		for (int i = 0; i < 500000; i++) {
			list.add(new Object());
		}
		MemoryUtils.printMemoryInfo();
		assert (MemoryUtils.usedMemory() > 0);
		assert (MemoryUtils.maxMemory(Runtime.getRuntime()) > 0);
		assert (MemoryUtils.usedMemory(Runtime.getRuntime()) > 0);

	}

}
