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

package org.impalaframework.module.spi;


/**
 * Functionality for coarse grained framework locking, which is invoked when
 * module reload operations are invoked.
 * 
 * @author Phil Zoio
 */
public interface FrameworkLockHolder {
		
	/**
	 * Invoked when module operations are invoked
	 */
	void lock();
	
	/**
	 * Invoked when module operations are completed
	 */
	void unlock();
	
	/**
	 * Returns true if framework operations is not locked by another thread. 
	 */
	boolean isAvailable();
	
}