package com.vimukti.accounter.core.trigger;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.h2.api.Trigger;

/**
 * 
 * @author Devesh Satwani
 * 
 */
public class TriggerIssuePayment implements Trigger {

	@Override
	public void fire(Connection conn, Object[] oldRow, Object[] newRow)
			throws SQLException {

		@SuppressWarnings("unused")
		Statement stat = conn.createStatement();

		if (newRow != null && oldRow == null) {

		} else if (newRow != null && oldRow != null) {

		}

	}

	@Override
	public void init(Connection arg0, String arg1, String arg2, String arg3,
			boolean arg4, int arg5) throws SQLException {
		// TODO Auto-generated method stub

	}

}
