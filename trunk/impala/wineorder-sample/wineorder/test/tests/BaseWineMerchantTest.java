/*
 * Copyright 2007 the original author or authors.
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

package tests;

import interfaces.WineMerchant;

import org.impalaframework.testrun.DynamicContextHolder;

import test.BaseDataTest;

public abstract class BaseWineMerchantTest extends BaseDataTest {

	public void baseClassOperation() {
		WineMerchant merchant = DynamicContextHolder.getBean("wineMerchant", WineMerchant.class);
		System.out.println(merchant);
	}

}