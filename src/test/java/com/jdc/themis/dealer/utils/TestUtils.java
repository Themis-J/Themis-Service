package com.jdc.themis.dealer.utils;

import static org.mockito.Mockito.mock;
import junit.framework.Assert;

import static org.mockito.Mockito.when;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.jdc.themis.dealer.data.dao.RefDataDAO;
import com.jdc.themis.dealer.domain.Duration;
import com.jdc.themis.dealer.domain.EnumValue;

import fj.data.Option;

public class TestUtils {

	@Mock
	private RefDataDAO refDataDAL;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks( this );
		refDataDAL = mock(RefDataDAO.class);
	}
	
	@Test
	public void getCurrentTimestamp() {
		Assert.assertNotNull(Utils.currentTimestamp());
	}
	
	@Test
	public void getDurationDesc() {
		final Duration duration = new Duration();
		duration.setId(1);
		duration.setLowerBound(0);
		duration.setUpperBound(30);
		duration.setUnit(1);
		final EnumValue enumValue = new EnumValue();
		enumValue.setTypeID(1);
		enumValue.setValue(1);
		enumValue.setName("Days");
		when(refDataDAL.getEnumValue("DurationUnit", 1)).thenReturn(Option.<EnumValue>some(enumValue));
		Assert.assertEquals("0-30 Days", Utils.getDurationDesc(duration, refDataDAL));
	}
	
	@Test(expected=RuntimeException.class)
	public void getDurationDescFail() {
		final Duration duration = new Duration();
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
