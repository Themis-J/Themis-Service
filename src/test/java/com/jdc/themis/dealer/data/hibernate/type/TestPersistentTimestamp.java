package com.jdc.themis.dealer.data.hibernate.type;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.time.calendar.LocalDateTime;
import javax.time.calendar.TimeZone;

import junit.framework.Assert;

import org.hibernate.HibernateException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TestPersistentTimestamp {

	private PersistentTimestamp timestamp;
	@Mock
	private ResultSet rs;
	@Mock
	private PreparedStatement ps;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		rs = mock(ResultSet.class);
		ps = mock(PreparedStatement.class);
		timestamp = new PersistentTimestamp();
	}

	@Test
	public void checkSqlTypes() {
		Assert.assertEquals(Types.TIMESTAMP, timestamp.sqlTypes()[0]);
	}

	@Test
	public void checkEquals() {
		Assert.assertTrue(timestamp.equals(LocalDateTime.of(2013, 8, 1, 11, 23)
				.atZone(TimeZone.UTC).toInstant(),
				LocalDateTime.of(2013, 8, 1, 11, 23).atZone(TimeZone.UTC)
						.toInstant()));
		Assert.assertFalse(timestamp.equals(LocalDateTime
				.of(2013, 8, 1, 11, 23).atZone(TimeZone.UTC).toInstant(),
				LocalDateTime.of(2013, 9, 1, 11, 23).atZone(TimeZone.UTC)
						.toInstant()));
		Assert.assertFalse(timestamp.equals(null,
				LocalDateTime.of(2013, 9, 1, 11, 23).atZone(TimeZone.UTC)
						.toInstant()));
	}

	@Test
	public void safeGet() throws HibernateException, SQLException {
		final Date date = new Date(new java.util.Date().getTime());
		when(rs.getTimestamp("timestamp")).thenReturn(
				new java.sql.Timestamp(date.getTime()));
		final Calendar c = new GregorianCalendar();
		c.setTime(date);
		final Object result = timestamp.nullSafeGet(rs,
				new String[] { "timestamp" }, null);
		Assert.assertNotNull(result);
	}

	@Test
	public void safeGetNull() throws HibernateException, SQLException {
		when(rs.getTimestamp("timestamp")).thenReturn(null);
		final Object result = timestamp.nullSafeGet(rs,
				new String[] { "timestamp" }, null);
		Assert.assertEquals(null, result);
	}

	@Test
	public void safeGetWasNull() throws HibernateException, SQLException {
		when(rs.wasNull()).thenReturn(true);
		final Object result = timestamp.nullSafeGet(rs,
				new String[] { "timestamp" }, null);
		Assert.assertEquals(null, result);
	}

	@Test
	public void safeSet() throws HibernateException, SQLException {
		timestamp.nullSafeSet(ps,
				LocalDateTime.of(2013, 8, 1, 11, 23).atZone(TimeZone.UTC)
						.toInstant(), 1);
		verify(ps).setTimestamp(eq(1), any(java.sql.Timestamp.class));
	}

	@Test
	public void safeSetNull() throws HibernateException, SQLException {
		timestamp.nullSafeSet(ps, null, 1);
		verify(ps).setNull(1, Types.TIMESTAMP);
	}
}
