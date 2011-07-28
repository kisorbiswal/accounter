package com.vimukti.accounter.core.trigger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.h2.api.Trigger;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.AccounterConstants;

/**
 * 
 * @author Devesh Satwani
 * 
 */
public class TriggerWriteCheck implements Trigger {

	@Override
	public void fire(Connection conn, Object[] oldRow, Object[] newRow)
			throws SQLException {

		// Required variables declaration and initialization

		Long newWriteCheckId = (newRow != null) ? ((newRow[0] != null) ? (Long) newRow[0]
				: null)
				: null;
		
		Long oldWriteCheckId = (oldRow != null) ? ((oldRow[0] != null) ? (Long) oldRow[0]
				: null)
				: null;

		Long newAccountId = (newRow != null) ? ((newRow[2] != null) ? (Long) newRow[2]
				: null)
				: null;
		
		Long oldAccountId = (oldRow != null) ? ((oldRow[2] != null) ? (Long) oldRow[2]
				: null)
				: null;

		Double newAmount = (newRow != null) ? ((newRow[8] != null) ? (Double) newRow[8]
				: null)
				: null;
		
		Double oldAmount = (oldRow != null) ? ((oldRow[8] != null) ? (Double) oldRow[8]
				: null)
				: null;

		Boolean newIsVoid = (newRow != null) ? ((newRow[12] != null) ? (Boolean) newRow[12]
				: null)
				: null;
		Boolean oldIsVoid = (oldRow != null) ? ((oldRow[12] != null) ? (Boolean) oldRow[12]
				: null)
				: null;

		Statement stat = conn.createStatement();

		int accountType = 0;
		// Condition for checking whether this Trigger call is for new Row
		// Insertion

		if (newRow != null && oldRow == null) {
		}
		// Condition for checking whether this Trigger call is for Row Updation
		else if (newRow != null && oldRow != null) {
			// To check whether this updation is caused by Voiding any Write
			// Check
			// or not.
			if (oldIsVoid != newIsVoid) {

				// Deleting corresponding transaction rows from Account
				// Transaction table
				stat.execute(new StringBuilder()
						.append("DELETE FROM ACCOUNT_TRANSACTION WHERE T_ID =")
						.append(newWriteCheckId).toString());

				accountType = getAccountType(stat, newAccountId);

				if (accountType == Account.TYPE_CREDIT_CARD
						|| accountType == Account.TYPE_LONG_TERM_LIABILITY
						|| accountType == Account.TYPE_OTHER_CURRENT_LIABILITY) {

					// Reverse Updating the current and total balances of the
					// Corresponding account.
					updateCurrentAndTotalBalancesOfCorrespondingAccount(stat,
							newAmount, newAccountId,
							AccounterConstants.SYMBOL_MINUS);

				} else if (accountType == Account.TYPE_CASH
						|| accountType == Account.TYPE_BANK) {

					// Reverse Updating the current and total balances of the
					// Corresponding account.
					updateCurrentAndTotalBalancesOfCorrespondingAccount(stat,
							newAmount, newAccountId,
							AccounterConstants.SYMBOL_PLUS);
				}

			}
		}

	}

	private void updateCurrentAndTotalBalancesOfCorrespondingAccount(
			Statement stat, Double amount, Long accountId, String symbol)
			throws SQLException {

		stat.execute(new StringBuilder()
				.append("UPDATE ACCOUNT A SET A.CURRENT_BALANCE = A.CURRENT_BALANCE ")
				.append(symbol).append(" ").append(amount)
				.append(", A.TOTAL_BALANCE = A.TOTAL_BALANCE ").append(symbol)
				.append(" ").append(amount).append(" WHERE A.ID =")
				.append(accountId).toString());
	}

	private int getAccountType(Statement stat, Long newAccountId)
			throws SQLException {

		int accountType = 0;
		ResultSet r = stat.executeQuery(new StringBuilder()
				.append("SELECT A.A_TYPE FROM ACCOUNT A WHERE A.ID =")
				.append(newAccountId).toString());
		if (r.next()) {
			accountType = r.getInt(1);
		}
		return accountType;
	}

	@Override
	public void init(Connection arg0, String arg1, String arg2, String arg3,
			boolean arg4, int arg5) throws SQLException {
		// currently not using anywhere

	}

}
