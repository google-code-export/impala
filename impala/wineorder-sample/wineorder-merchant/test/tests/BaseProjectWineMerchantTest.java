package tests;


import interfaces.WineMerchant;

import org.impalaframework.testrun.DynamicContextHolder;

public abstract class BaseProjectWineMerchantTest extends BaseWineMerchantTest {

	public void baseClassOperation() {
		super.baseClassOperation();
		WineMerchant merchant = DynamicContextHolder.getBean("wineMerchant", WineMerchant.class);
		System.out.println(merchant);
	}

}