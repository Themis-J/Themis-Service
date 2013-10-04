package com.jdc.themis.dealer.service.impl;

import com.google.common.base.Function;
import com.jdc.themis.dealer.domain.DealerIncomeRevenueFact;

enum GetDealerIDFromRevenueFunction implements
		Function<DealerIncomeRevenueFact, Integer> {
	INSTANCE;

	@Override
	public Integer apply(final DealerIncomeRevenueFact item) {
		return item.getDealerID();
	}
}