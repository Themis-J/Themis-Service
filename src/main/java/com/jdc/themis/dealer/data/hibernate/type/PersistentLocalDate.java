package com.jdc.themis.dealer.data.hibernate.type;

import java.io.Serializable;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.time.calendar.LocalDate;

import org.hibernate.HibernateException;
import org.hibernate.usertype.EnhancedUserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jdc.themis.dealer.data.dao.hibernate.RefDataDAOImpl;

public class PersistentLocalDate implements EnhancedUserType, Serializable {

	private final static Logger logger = LoggerFactory.getLogger(RefDataDAOImpl.class);
	private static final long serialVersionUID = 1L;

	@Override
	public int[] sqlTypes() {
		return new int[]{Types.DATE};
	}

	@Override
	public Class<LocalDate> returnedClass() {
		return LocalDate.class;
	}

	@Override
	public boolean equals(Object x, Object y) throws HibernateException {
		return x==y || x != null && y != null && x.equals(y);
	}

	@Override
	public int hashCode(Object x) throws HibernateException {
		return x.hashCode();
	}

	@Override
	public Object nullSafeGet(ResultSet rs, String[] names, Object owner)
			throws HibernateException, SQLException {
		final String name = names[0];
		if ( rs.wasNull() ) {
			return null;
		}
		final Date date = rs.getDate(name);
		if ( date == null ) {
			return null;
		}
		final Calendar c = new GregorianCalendar();
		c.setTime(date);
		final Object value = LocalDate.of(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
		logger.debug("Get date {} for field {}", value, name);
		return value;
	}

	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index)
			throws HibernateException, SQLException {
		if ( value == null ) {
			st.setNull(index, Types.DATE);
		} else {
			final LocalDate date = (LocalDate) value;
			final Calendar c = new GregorianCalendar();
			c.set(date.getYear(), date.getMonthOfYear().getValue() - 1, date.getDayOfMonth());
			logger.debug("Set date {} for field {}", c, index);
			st.setDate(index, new Date(c.getTimeInMillis()));
		}
	}

	@Override
	public Object deepCopy(Object value) throws HibernateException {
		// instant is immutable so we can return it directly
		return (LocalDate) value;
	}

	@Override
	public boolean isMutable() {
		return false;
	}

	@Override
	public Serializable disassemble(Object value) throws HibernateException {
		return (Serializable) value;
	}

	@Override
	public Object assemble(Serializable cached, Object owner)
			throws HibernateException {
		return cached;
	}

	@Override
	public Object replace(Object original, Object target, Object owner)
			throws HibernateException {
		return original;
	}

	@Override
	public String objectToSQLString(Object value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String toXMLString(Object value) {
		return value.toString();
	}

	@Override
	public Object fromXMLString(String xmlValue) {
		return LocalDate.parse(xmlValue);
	}

}
