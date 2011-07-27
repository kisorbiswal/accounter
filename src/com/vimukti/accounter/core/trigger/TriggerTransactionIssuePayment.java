package com.vimukti.accounter.core.trigger;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.h2.api.Trigger;

import com.vimukti.accounter.core.Transaction;

/**
 * 
 * @author Devesh Satwani
 * 
 */
public class TriggerTransactionIssuePayment implements Trigger {

	@Override
	public void fire(Connection conn, Object[] oldRow, Object[] newRow)
			throws SQLException {

		// Required variables declaration and initialization

		Statement stat = conn.createStatement();

		Long newWriteCheckId = (newRow != null) ? ((newRow[3] != null) ? (Long) newRow[3]
				: null)
				: null;
		@SuppressWarnings("unused")
		Long oldWriteCheckId = (oldRow != null) ? ((oldRow[3] != null) ? (Long) oldRow[3]
				: null)
				: null;

		Long newCustomerRefundId = (newRow != null) ? ((newRow[10] != null) ? (Long) newRow[10]
				: null)
				: null;
		@SuppressWarnings("unused")
		Long oldCustomerRefundId = (oldRow != null) ? ((oldRow[10] != null) ? (Long) oldRow[10]
				: null)
				: null;

		Integer newIndex = (newRow != null) ? ((newRow[11] != null) ? (Integer) newRow[11]
				: null)
				: null;
		Integer oldIndex = (oldRow != null) ? ((oldRow[11] != null) ? (Integer) oldRow[11]
				: null)
				: null;

		// Condition for checking whether this Trigger call is for new Row
		// Insertion

		if (newRow != null && oldRow == null) {

		}
		// Condition for checking whether this Trigger call is for Row Updation
		else if (newRow != null && oldRow != null) {

			// This Trigger Updation call is for Updation TransactionID and
			// Index. Hence do the create part
			if (oldIndex == null && newIndex != null) {
				doInCreatePart(stat, newWriteCheckId, newCustomerRefundId);

			}

		}

	}

	private void doInCreatePart(Statement stat, Long newWriteCheckId,
			Long newCustomerRefundId) throws SQLException {

		if (newWriteCheckId != null) {

			// Update the Status of Write Check as Issued
			stat.execute(new StringBuilder()
					.append("UPDATE TRANSACTION T SET T.STATUS = ")
					.append(Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED)
					.append(" WHERE T.ID = ").append(newWriteCheckId)
					.toString());

		} else if (newCustomerRefundId != null) {

			// Update the Status of Customer Refund as Issued
			stat.execute(new StringBuilder()
					.append("UPDATE TRANSACTION T SET T.STATUS = ")
					.append(Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED)
					.append(" WHERE T.ID = ").append(newCustomerRefundId)
					.toString());
		}
	}

	@Override
	public void init(Connection arg0, String arg1, String arg2, String arg3,
			boolean arg4, int arg5) throws SQLException {
		// currently not using anywhere in the project.

	}

}
