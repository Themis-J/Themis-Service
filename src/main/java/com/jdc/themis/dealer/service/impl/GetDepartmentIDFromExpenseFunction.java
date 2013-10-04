package com.jdc.themis.dealer.service.impl;

import com.google.common.base.Function;
import com.jdc.themis.dealer.domain.DealerIncomeExpenseFact;

enum GetDepartmentIDFromExpenseFunction implements
		Function<DealerIncomeExpenseFact, Integer> {
	INSTANCE;

	@Override
	public Integer apply(final DealerIncomeExpenseFact item) {
		return item.getDepartmentID();
	}
}