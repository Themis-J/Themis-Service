package com.jdc.themis.dealer.utils;

import java.util.Date;

import javax.time.Instant;

import com.google.common.base.Preconditions;
import com.jdc.themis.dealer.service.RefDataQueryService;
import com.jdc.themis.dealer.web.domain.DurationDetail;

public abstract class Utils {

	public static Instant currentTimestamp() {
		return Instant.millis(new Date().getTime());
	}
	
	public static String getDurationDesc(final DurationDetail duration, final RefDataQueryService refDataDAL) {
		Preconditions.checkNotNull(refDataDAL.getEnumValue("DurationUnit", duration.getUnit()), "unknown unit id " + duration.getUnit());
		return new StringBuffer()
			.append(duration.getLowerBound()).append("-")
			.append(duration.getUpperBound() != null ? duration.getUpperBound() : "MAX").append(" ")
			.append(refDataDAL.getEnumValue("DurationUnit", duration.getUnit()).some().getName())
			.toString();
	}
}
