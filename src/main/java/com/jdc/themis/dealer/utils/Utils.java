package com.jdc.themis.dealer.utils;

import java.util.Date;

import javax.time.Instant;

public abstract class Utils {

	public static Instant currentTimestamp() {
		return Instant.millis(new Date().getTime());
	}
	
}
