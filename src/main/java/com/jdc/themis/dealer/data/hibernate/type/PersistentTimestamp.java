package com.jdc.themis.dealer.data.hibernate.type;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;

import javax.time.Instant;

import org.hibernate.HibernateException;
import org.hibernate.usertype.EnhancedUserType;

public class PersistentTimestamp implements EnhancedUserType, Serializable {

	private static final long serialVersionUID = 1L;

	@Override
	public int[] sqlTypes() {
		return new int[]{Types.TIMESTAMP};
	}

	@Override
	public Class<Instant> returnedClass() {
		return Instant.class;
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
		final Timestamp timestamp = rs.getTimestamp(name);
		if ( timestamp == null ) {
			return null;
		}
		final Object value = Instant.millis(timestamp.getTime());
		return value;
	}

	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index)
			throws HibernateException, SQLException {
		if ( value == null ) {
			st.setNull(index, Types.TIMESTAMP);
		} else {
			st.setTimestamp(index, new Timestamp(((Instant) value).toEpochMillisLong()));
		}
	}

	@Override
	public Object deepCopy(Object value) throws HibernateException {
		// instant is immutable so we can return it directly
		return (Instant) value;
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
		return Instant.parse(xmlValue);
	}

}
