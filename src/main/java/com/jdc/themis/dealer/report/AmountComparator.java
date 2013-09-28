package com.jdc.themis.dealer.report;

import java.util.Comparator;

public class AmountComparator implements Comparator<Double> {
	private AmountComparator() {
	}

	public static final AmountComparator INSTANCE = new AmountComparator();

	@Override
	public int compare(Double arg0, Double arg1) {
		return -1 * arg0.compareTo(arg1);
	}
}
