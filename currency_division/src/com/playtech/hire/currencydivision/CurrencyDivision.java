package com.playtech.hire.currencydivision;

import java.util.ArrayList;
import java.util.List;

public class CurrencyDivision {
	
	public static List<Double> divideCurrency(int money, int people) {
		List<Double> list = new ArrayList<Double>();
		long totalCents = (long)money * 100;
		long centsPerPerson;
		
		if (money <= 0 || people <= 0)
			throw new IllegalArgumentException("Illegal input value");			
		
		for (int p = people; totalCents > 0; p--) {
			centsPerPerson = totalCents / p;
			if (centsPerPerson == 0)
				throw new IllegalArgumentException("Too many people for given amount");
			
			totalCents -= centsPerPerson;
			list.add((double)centsPerPerson / 100);
		}

		return list;
	} 
}
