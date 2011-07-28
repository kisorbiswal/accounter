package com.vimukti.accounter.core.trigger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.h2.api.Trigger;

import com.vimukti.accounter.core.AccounterConstants;

/**
 * 
 * @author Devesh Satwani
 * 
 */
public class TriggerTransferFund implements Trigger {

	@Override
	public void fire(Connection conn, Object[] oldRow, Object[] newRow)
			throws SQLException {

		// Required variables declaration and initialization

		Statement stat = conn.createStatement();

		Long newTransferFundId = (newRow != null) ? ((newRow[0] != null) ? (Long) newRow[0]
				: null)
				: null;
		
		Long oldTransferFundId = (oldRow != null) ? ((oldRow[0] != null) ? (Long) oldRow[0]
				: null)
				: null;

		Long newTransferFromAccountId = (newRow != null) ? ((newRow[2] != null) ? (Long) newRow[2]
				: null)
				: null;
		
		Long oldTransferFromAccountId = (oldRow != null) ? ((oldRow[2] != null) ? (Long) oldRow[2]
				: null)
				: null;

		Long newTransferToAccountId = (newRow != null) ? ((newRow[3] != null) ? (Long) newRow[3]
				: null)
				: null;
		
		Long oldTransferToAccountId = (oldRow != null) ? ((oldRow[3] != null) ? (Long) oldRow[3]
				: null)
				: null;

		Double newTransferAmount = (newRow != null) ? ((newRow[4] != null) ? (Double) newRow[4]
				: null)
				: null;
		
		Double oldTransferAmount = (oldRow != null) ? ((oldRow[4] != null) ? (Double) oldRow[4]
				: null)
				: null;

		Boolean newIsVoid = (newRow != null) ? ((newRow[6] != null) ? (Boolean) newRow[6]
				: null)
				: null;
		Boolean oldIsVoid = (oldRow != null) ? ((oldRow[6] != null) ? (Boolean) oldRow[6]
				: null)
				: null;

		Boolean isIncrease = Boolean.FALSE;

		// Condition for checking whether this Trigger call is for new Row
		// Insertion
		if (newRow != null && oldRow == null) {
		}
		// Condition for checking whether this Trigger call is for Row Updation

		else if (newRow != null && oldRow != null) {

			// To check whether this updation is caused by Voiding any Transfer
			// Fund
			// or not.
			if (oldIsVoid != newIsVoid) {

				// Deleting corresponding transaction rows from Account
				// Transaction table
				stat.execute(new StringBuilder().append(
						"DELETE FROM ACCOUNT_TRANSACTION WHERE T_ID =").append(
						newTransferFundId).toString());

				isIncrease = getIsIncreaseValuesOfAccount(stat,
						newTransferFromAccountId);

				if (isIncrease) {
					// Update the current and total balances of the
					// corresponding Transfer From account
					updateCurrentAndTotalBalancesOfCorrespondingAccount(stat,
							newTransferAmount, newTransferFromAccountId,
							AccounterConstants.SYMBOL_MINUS);

				} else {
					// Update the current and total balances of the
					// corresponding Transfer From account
					updateCurrentAndTotalBalancesOfCorrespondingAccount(stat,
							newTransferAmount, newTransferFromAccountId,
							AccounterConstants.SYMBOL_PLUS);

				}

				isIncrease = getIsIncreaseValuesOfAccount(stat,
						newTransferToAccountId);

				if (isIncrease) {
					// Update the current and total balances of the
					// corresponding Transfer To account
					updateCurrentAndTotalBalancesOfCorrespondingAccount(stat,
							newTransferAmount, newTransferToAccountId,
							AccounterConstants.SYMBOL_PLUS);

				} else {
					// Update the current and total balances of the
					// corresponding Transfer From account
					updateCurrentAndTotalBalancesOfCorrespondingAccount(stat,
							newTransferAmount, newTransferToAccountId,
							AccounterConstants.SYMBOL_MINUS);

				}

			}

		}

	}

	private void updateCurrentAndTotalBalancesOfCorrespondingAccount(
			Statement stat, Double amount, Long accountId, String symbol)
			throws SQLException {

		stat.execute(new StringBuilder().append(
				"UPDATE ACCOUNT A SET A.CURRENT_BALANCE = A.CURRENT_BALANCE ")
				.append(symbol).append(" ").append(amount).append(
						", A.TOTAL_BALANCE = A.TOTAL_BALANCE ").append(symbol)
				.append(" ").append(amount).append(" WHERE A.ID=").append(
						accountId).toString());
	}

	private Boolean getIsIncreaseValuesOfAccount(Statement stat, Long accountId)
			throws SQLException {

		Boolean isIncrease = Boolean.FALSE;
		ResultSet r = stat.executeQuery(new StringBuilder().append(
				"SELECT A.IS_INCREASE FROM ACCOUNT A WHERE A.ID =").append(
				accountId).toString());
		if (r.next()) {
			isIncrease = r.getBoolean(1);
		}

		return isIncrease;
	}

	@Override
	public void init(Connection arg0, String arg1, String arg2, String arg3,
			boolean arg4, int arg5) throws SQLException {
		// currently not using anywhere in the project.

	}
}
