package com.jdc.themis.dealer.utils;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.jdc.themis.dealer.domain.EnumValue;
import com.jdc.themis.dealer.service.RefDataQueryService;
import com.jdc.themis.dealer.web.domain.DurationDetail;

public class TestUtils {

	@Mock
	private RefDataQueryService refDataDAL;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks( this );
		refDataDAL = mock(RefDataQueryService.class);
	}
	
	@Test
	public void getCurrentTimestamp() {
		Assert.assertNotNull(Utils.currentTimestamp());
	}
	
	@Test
	public void getDurationDesc() {
		final DurationDetail duration = new DurationDetail();
		duration.setId(1);
		duration.setLowerBound(0);
		duration.setUpperBound(30);
		duration.setUnit(1);
		final EnumValue enumValue = new EnumValue();
		enumValue.setTypeID(1);
		enumValue.setValue(1);
		enumValue.setName("Days");
		when(refDataDAL.getEnumValue("DurationUnit", 1)).thenReturn(enumValue);
		Assert.assertEquals("0-30 Days", Utils.getDurationDesc(duration, refDataDAL));
	}
	
	@Test(expected=RuntimeException.class)
	public void getDurationDescFail() {
		final DurationDetail duration = new DurationDetail();
		duration.setId(1);
		duration.setLowerBound(0);
		duration.setUpperBound(30);
		duration.setUnit(1);
		final EnumValue enumValue = new EnumValue();
		enumValue.setTypeID(1);
		enumValue.setValue(1);
		enumValue.setName("Days");
		when(refDataDAL.getEnumValue("DurationUnit", 1)).thenReturn(null);
		Utils.getDurationDesc(duration, refDataDAL);
		Assert.fail("should not be successful");
	}
}
