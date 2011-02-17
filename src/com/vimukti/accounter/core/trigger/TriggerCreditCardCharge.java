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
public class TriggerCreditCardCharge implements Trigger {

	@Override
	public void fire(Connection conn, Object[] oldRow, Object[] newRow)
			throws SQLException {

		// Required variables declaration and initialization

		Statement stat = conn.createStatement();

		Long newCreditCardChargeId = (newRow != null) ? ((newRow[0] != null) ? (Long) newRow[0]
				: null)
				: null;
		@SuppressWarnings("unused")
		Long oldCreditCardChargeId = (oldRow != null) ? ((oldRow[0] != null) ? (Long) oldRow[0]
				: null)
				: null;
		@SuppressWarnings("unused")
		Long newPayFromAccountId = (newRow != null) ? ((newRow[6] != null) ? (Long) newRow[6]
				: null)
				: null;
		Long oldPayFromAccountId = (oldRow != null) ? ((oldRow[6] != null) ? (Long) oldRow[6]
				: null)
				: null;
		@SuppressWarnings("unused")
		Double newTotal = (newRow != null) ? ((newRow[11] != null) ? (Double) newRow[11]
				: null)
				: null;
		Double oldTotal = (oldRow != null) ? ((oldRow[11] != null) ? (Double) oldRow[11]
				: null)
				: null;
		Boolean newIsVoid = (newRow != null) ? ((newRow[12] != null) ? (Boolean) newRow[12]
				: null)
				: null;
		Boolean oldIsVoid = (oldRow != null) ? ((oldRow[12] != null) ? (Boolean) oldRow[12]
				: null)
				: null;
		int accountType = 0;

		// Condition for checking whether this Trigger call is for new Row
		// Insertion

		if (newRow != null && oldRow == null) {

		}
		// Condition for checking whether this Trigger call is for Row Updation
		else if (newRow != null && oldRow != null) {

			// Deleting corresponding transaction rows from Account Transaction
			// table
			stat.execute(new StringBuilder().append(
					"DELETE FROM ACCOUNT_TRANSACTION WHERE T_ID =").append(
					newCreditCardChargeId).toString());

			// To check whether this updation is caused by Voiding any Credit
			// Card Charge
			// or not.

			if (oldIsVoid != newIsVoid) {

				// retrieving the account type of pay from account.
				accountType = getAccountType(stat, oldPayFromAccountId);

				// Checking for the Pay From account type
				if (accountType == Account.TYPE_CREDIT_CARD
						|| accountType == Account.TYPE_LONG_TERM_LIABILITY
						|| accountType == Account.TYPE_OTHER_CURRENT_LIABILITY) {

					// Reverse updating the Corresponding Payfrom account.
					updateCurrentAndTotalBalanceOfPayFromAccount(stat,
							oldTotal, oldPayFromAccountId,
							AccounterConstants.SYMBOL_MINUS);

				} else {

					// Reverse updating the Corresponding Payfrom account.
					updateCurrentAndTotalBalanceOfPayFromAccount(stat,
							oldTotal, oldPayFromAccountId,
							AccounterConstants.SYMBOL_PLUS);

				}

			}

		}

	}

	// private void
	// insertCorrespondingRowsForTheOperationIntoAccountTransactionTable(
	// Statement stat, Long newCreditCardChargeId,
	// Long newPayFromAccountId, Double newTotal) throws SQLException {
	// stat
	// .execute(new StringBuilder()
	// .append(
	// "INSERT INTO ACCOUNT_TRANSACTION (T_NUMBER,TYPE,USER_ID,T_ID,T_TYPE,T_DATE,AMOUNT) VALUES ")
	// .append(
	// "((SELECT T.NUMBER FROM TRANSACTION T WHERE T.ID=")
	// .append(newCreditCardChargeId)
	// .append("),")
	// .append(AccountTransaction.TYPE_ACCOUNT)
	// .append(",")
	// .append(newPayFromAccountId)
	// .append(",")
	// .append(newCreditCardChargeId)
	// .append(
	// ",(SELECT T.T_TYPE FROM TRANSACTION T WHERE T.ID=")
	// .append(newCreditCardChargeId)
	// .append(
	// "),(SELECT T.T_DATE FROM TRANSACTION T WHERE T.ID=")
	// .append(newCreditCardChargeId)
	// .append(
	// "),(CASE WHEN (SELECT A.A_TYPE FROM ACCOUNT A WHERE A.ID =")
	// .append(newPayFromAccountId)
	// .append(")=")
	// .append(Account.TYPE_CREDIT_CARD)
	// .append(
	// " OR (SELECT A.A_TYPE FROM ACCOUNT A WHERE A.ID =")
	// .append(newPayFromAccountId)
	// .append(")=")
	// .append(Account.TYPE_LONG_TERM_LIABILITY)
	// .append(
	// " OR (SELECT A.A_TYPE FROM ACCOUNT A WHERE A.ID =")
	// .append(newPayFromAccountId).append(")=").append(
	// Account.TYPE_OTHER_CURRENT_LIABILITY).append(
	// " THEN ").append(newTotal).append(" ELSE -1*")
	// .append(newTotal).append(" END ))").toString());
	// }

	private void updateCurrentAndTotalBalanceOfPayFromAccount(Statement stat,
			Double total, Long payFromAccountId, String symbol)
			throws SQLException {
		stat.execute(new StringBuilder().append(
				"UPDATE ACCOUNT A SET A.CURRENT_BALANCE = A.CURRENT_BALANCE ")
				.append(symbol).append(" ").append(total).append(
						", A.TOTAL_BALANCE = A.TOTAL_BALANCE ").append(symbol)
				.append(" ").append(total).append(" WHERE A.ID =").append(
						payFromAccountId).toString());
	}

	private int getAccountType(Statement stat, Long payFromAccountId)
			throws SQLException {
		int accountType = 0;

		ResultSet r = stat.executeQuery(new StringBuilder().append(
				"SELECT A.A_TYPE FROM ACCOUNT A WHERE A.ID =").append(
				payFromAccountId).toString());

		if (r.next()) {
			accountType = r.getInt(1);
		}
		return accountType;
	}

	@Override
	public void init(Connection arg0, String arg1, String arg2, String arg3,
			boolean arg4, int arg5) throws SQLException {
		// TODO Auto-generated method stub

	}

}
