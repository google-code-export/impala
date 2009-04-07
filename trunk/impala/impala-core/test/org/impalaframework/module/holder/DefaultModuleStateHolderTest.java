/*
 * Copyright 2007-2008 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.impalaframework.module.holder;

import junit.framework.TestCase;

public class DefaultModuleStateHolderTest extends TestCase {

	public void testLock() throws Exception {
		final DefaultModuleStateHolder holder = new DefaultModuleStateHolder();
		
		holder.lock();
		assertTrue(holder.isAvailable());
		
		//call a second time
		holder.lock();
		assertTrue(holder.isAvailable());
		
		checkAvailability(holder, false);
		holder.unlock();
		holder.unlock();
		
		checkAvailability(holder, true);
		assertTrue(holder.isAvailable());
	}

	private void checkAvailability(final DefaultModuleStateHolder holder, final boolean expectToBeAvailable) throws InterruptedException {
		Runnable r = new Runnable() {
			public void run() {
				if (expectToBeAvailable) assertTrue(holder.isAvailable());
				else assertFalse(holder.isAvailable());
				System.out.println("Proved available: " + expectToBeAvailable);
			}
		};
		
		final Thread thread = new Thread(r);
		thread.start();
		thread.join();
	}
	
}