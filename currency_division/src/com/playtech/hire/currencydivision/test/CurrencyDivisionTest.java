package com.playtech.hire.currencydivision.test;

import com.playtech.hire.currencydivision.CurrencyDivision;

public class CurrencyDivisionTest {

	public static void main(String[] args) {
		System.out.println("Test1: " + CurrencyDivision.divideCurrency(4, 2));
		System.out.println("Test2: " + CurrencyDivision.divideCurrency(10, 7));
		System.out.println("Test3: " + CurrencyDivision.divideCurrency(11, 7));
		System.out.println("Test4: " + CurrencyDivision.divideCurrency(12, 7));
		System.out.println("Test5: " + CurrencyDivision.divideCurrency(100, 3));
		System.out.println("Test6: " + CurrencyDivision.divideCurrency(15, 4));
		System.out.println("Test7: " + CurrencyDivision.divideCurrency(13, 4));
		System.out.println("Test8: " + CurrencyDivision.divideCurrency(Integer.MAX_VALUE, 3));

		try {
			CurrencyDivision.divideCurrency(-1, 3);
			System.exit(1);
		} catch (Exception e) {
			System.out.println("Test9: test for negative money value ok");
		}
		
		try {
			CurrencyDivision.divideCurrency(1, 101);
			System.exit(1);
		} catch (Exception e) {
			System.out.println("Test10: test non divisible money amount ok");
		}
	}
}
