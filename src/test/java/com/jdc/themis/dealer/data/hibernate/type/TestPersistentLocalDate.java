package com.jdc.themis.dealer.data.hibernate.type;

import static org.mockito.Mockito.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.time.calendar.LocalDate;

import junit.framework.Assert;

import org.hibernate.HibernateException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.jdc.themis.dealer.data.hibernate.type.PersistentLocalDate;

public class TestPersistentLocalDate {
	
	private PersistentLocalDate localDate;
	@Mock
	private ResultSet rs;
	@Mock
	private PreparedStatement ps;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks( this );
		rs = mock(ResultSet.class);
		ps = mock(PreparedStatement.class);
		localDate = new PersistentLocalDate();
	}
	
	@Test
	public void checkSqlTypes() {
		Assert.assertEquals(Types.DATE, localDate.sqlTypes()[0]);
	}
	
	@Test
	public void checkEquals() {
		Assert.assertTrue(localDate.equals(LocalDate.of(2013, 8, 1), LocalDate.of(2013, 8, 1)));
		Assert.assertFalse(localDate.equals(LocalDate.of(2013, 8, 2), LocalDate.of(2013, 8, 1)));
		Assert.assertFalse(localDate.equals(null, LocalDate.of(2013, 8, 1)));
	}
	
	@Test
	public void safeGet() throws HibernateException, SQLException {
		final Date date = new Date(new java.util.Date().getTime());
		when(rs.getDate("validDate")).thenReturn(date);
		final Calendar c = new GregorianCalendar();
		c.setTime(date);
		final Object result = localDate.nullSafeGet(rs, new String[]{"validDate"}, null);
		Assert.assertEquals(LocalDate.of(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH)), result);
	}
	
	@Test
	public void safeGetNull() throws HibernateException, SQLException {
		when(rs.getDate("validDate")).thenReturn(null);
		final Object result = localDate.nullSafeGet(rs, new String[]{"validDate"}, null);
		Assert.assertEquals(null, result);
	}
	
	@Test
	public void safeGetWasNull() throws HibernateException, SQLException {
		when(rs.wasNull()).thenReturn(true);
		final Object result = localDate.nullSafeGet(rs, new String[]{"validDate"}, null);
		Assert.assertEquals(null, result);
	}
	
	@Test
	public void safeSet() throws HibernateException, SQLException {
		localDate.nullSafeSet(ps, LocalDate.of(2013, 8, 1), 1);
		verify(ps).setDate(eq(1), any(Date.class));
	}
	
	@Test
	public void safeSetNull() throws HibernateException, SQLException {
		localDate.nullSafeSet(ps, null, 1);
		verify(ps).setNull(1, Types.DATE);
	}
	
	@Test
	public void assemble() {
		Assert.assertEquals(LocalDate.of(2013, 8, 1), localDate.assemble(LocalDate.of(2013, 8, 1), null));
	}
	
	@Test
	public void deassemble() {
		Assert.assertEquals(LocalDate.of(2013, 8, 1), localDate.disassemble(LocalDate.of(2013, 8, 1)));
	}
	
	@Test
	public void toXMLString() {
		Assert.assertEquals("2013-08-01", localDate.toXMLString(LocalDate.of(2013, 8, 1)));
	}
	
	@Test
	public void fromXMLString() {
		Assert.assertEquals(LocalDate.of(2013, 8, 1), localDate.fromXMLString("2013-08-01"));
	}
}
