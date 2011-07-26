package org.hibernate.dialect;

import java.sql.Types;

import org.hibernate.dialect.MySQLInnoDBDialect;

public class AccounterMySQLDialect extends MySQLInnoDBDialect {

	@Override
	public String getTableTypeString() {
		StringBuilder sb = new StringBuilder(super.getTableTypeString());
		sb.append(" DEFAULT CHARSET=utf8");
		return sb.toString();
	}

	public AccounterMySQLDialect() {
		registerColumnType(Types.BIGINT, "bigint(20)");
		registerHibernateType(Types.LONGVARCHAR, "longvarchar");
	}
}
