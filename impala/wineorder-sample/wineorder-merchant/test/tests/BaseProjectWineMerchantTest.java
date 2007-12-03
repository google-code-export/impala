package tests;


import interfaces.WineMerchant;

import org.impalaframework.testrun.DynamicContextHolder;

import test.BaseDataTest;

public abstract class BaseProjectWineMerchantTest extends BaseDataTest {

	public void baseClassOperation() {
		WineMerchant merchant = DynamicContextHolder.getBean(this, "wineMerchant", WineMerchant.class);
		System.out.println(merchant);
	}

}