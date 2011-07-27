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
public class TriggerVendor implements Trigger {

	@Override
	public void fire(Connection conn, Object[] oldRow, Object[] newRow)
			throws SQLException {

		// Required variables declaration and initialization

		Statement stat = conn.createStatement();
		Long newAccountsPayableId = (newRow != null) ? ((newRow[28] != null) ? (Long) newRow[28]
				: null)
				: null;
		Double newBalance = (newRow != null) ? ((newRow[8] != null) ? (Double) newRow[8]
				: null)
				: null;

		// Condition for checking whether this Trigger call is for new Row
		// Insertion

		if (newRow != null && oldRow == null) {

			if (newBalance != 0.0) {
				// Create Journal Entry for this Vendor

			}
			stat.execute(new StringBuilder()
					.append("UPDATE ACCOUNT A SET A.OPENING_BALANCE = A.OPENING_BALANCE + ")
					.append(newBalance)
					.append(",A.CURRENT_BALANCE = A.CURRENT_BALANCE + ")
					.append(newBalance)
					.append(",A.TOTAL_BALANCE = A.TOTAL_BALANCE + ")
					.append(newBalance).append(" WHERE A.ID = ")
					.append(newAccountsPayableId).toString());
		} else if (newRow != null && oldRow != null) {

		}

	}

	@Override
	public void init(Connection arg0, String arg1, String arg2, String arg3,
			boolean arg4, int arg5) throws SQLException {
		// currently not using

	}

}
