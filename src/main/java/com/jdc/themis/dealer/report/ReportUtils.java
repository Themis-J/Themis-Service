package com.jdc.themis.dealer.report;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import ch.lambdaj.Lambda;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public abstract class ReportUtils {

	public static Double calcReference(final Collection<Double> amounts) {
		Preconditions.checkArgument(
				amounts.size() >= 10,
				"size of list should be greater or equal to 10: "
						+ amounts.size());
		// find top 2 to 10 and get average amount
		final List<Double> list = Lists.newArrayList(amounts);// clone the list
		Collections.sort(list, AmountComparator.INSTANCE); // sort the list
		final List<Double> topAmounts = list.subList(1, 10); // get stop 2 to 10
		return Lambda.avg(topAmounts).doubleValue();
	}

	public static Double calcPercentage(Double current, Double previous) {
		if ( previous == BigDecimal.ZERO.doubleValue() ) {
			return BigDecimal.ZERO.doubleValue();
		}
		return (current - previous) / Math.abs(previous);
	}
	
}
