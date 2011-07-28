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
public class TriggerMakeDeposit implements Trigger {

	@Override
	public void fire(Connection conn, Object[] oldRow, Object[] newRow)
			throws SQLException {

		// Required variables declaration and initialization

		Statement stat = conn.createStatement();

		Long newMakeDepositId = (newRow != null) ? ((newRow[0] != null) ? (Long) newRow[0]
				: null)
				: null;
		
		Long oldMakeDepositId = (oldRow != null) ? ((oldRow[0] != null) ? (Long) oldRow[0]
				: null)
				: null;
		Long newDepositInAccountId = (newRow != null) ? ((newRow[1] != null) ? (Long) newRow[1]
				: null)
				: null;
		
		Long oldDepositInAccountId = (oldRow != null) ? ((oldRow[1] != null) ? (Long) oldRow[1]
				: null)
				: null;

		Long newCashBackAccountId = (newRow != null) ? ((newRow[4] != null) ? (Long) newRow[4]
				: null)
				: null;
		
		Long oldCashBackAccountId = (oldRow != null) ? ((oldRow[4] != null) ? (Long) oldRow[4]
				: null)
				: null;
		Double newCashBackAmount = (newRow != null) ? ((newRow[6] != null) ? (Double) newRow[6]
				: null)
				: null;
		
		Double oldCashBackAmount = (oldRow != null) ? ((oldRow[6] != null) ? (Double) oldRow[6]
				: null)
				: null;
		Double newTotal = (newRow != null) ? ((newRow[7] != null) ? (Double) newRow[7]
				: null)
				: null;
		
		Double oldTotal = (oldRow != null) ? ((oldRow[7] != null) ? (Double) oldRow[7]
				: null)
				: null;
		Boolean newIsVoid = (newRow != null) ? ((newRow[8] != null) ? (Boolean) newRow[8]
				: null)
				: null;
		Boolean oldIsVoid = (oldRow != null) ? ((oldRow[8] != null) ? (Boolean) oldRow[8]
				: null)
				: null;

		// Condition for checking whether this Trigger call is for new Row
		// Insertion

		if (newRow != null && oldRow == null) {
		}
		// Condition for checking whether this Trigger call is for Row Updation
		else if (newRow != null && oldRow != null) {

			// To check whether this updation is caused by Voiding the Make
			// Deposit
			// or not.
			if (oldIsVoid != newIsVoid) {

				// Deleting corresponding transaction rows from Account
				// Transaction table
				stat.execute(new StringBuilder()
						.append("DELETE FROM ACCOUNT_TRANSACTION WHERE T_ID =")
						.append(newMakeDepositId).toString());

				// Updating the Bank Account

				updateCurrentAndTotalBalanceOfDepositInAccount(stat, newTotal,
						newDepositInAccountId, AccounterConstants.SYMBOL_MINUS);

				// Reverse Updating the Cash Back Account

				if (newCashBackAccountId != null) {
					Boolean increase = getAccountIsIncreaseValue(stat,
							newCashBackAccountId);

					if (increase) {

						// Reverse update Current and Total balance of Cash Back
						// Account.
						updateCurrentAndTotalBalancesOfCashBackAccount(stat,
								newCashBackAmount, newCashBackAccountId,
								AccounterConstants.SYMBOL_PLUS);

					} else {
						// Reverse update Current and Total balance of Cash Back
						// Account.
						updateCurrentAndTotalBalancesOfCashBackAccount(stat,
								newCashBackAmount, newCashBackAccountId,
								AccounterConstants.SYMBOL_MINUS);

					}

				}
			}
		}
	}

	private void updateCurrentAndTotalBalancesOfCashBackAccount(Statement stat,
			Double cashBackAmount, Long cashBackAccountId, String symbol)
			throws SQLException {
		stat.execute(new StringBuilder()
				.append("UPDATE ACCOUNT A SET CURRENT_BALANCE= CURRENT_BALANCE ")
				.append(symbol).append(" ").append(cashBackAmount)
				.append(", A.TOTAL_BALANCE = A.TOTAL_BALANCE ").append(symbol)
				.append(" ").append(cashBackAmount).append(" WHERE ID = ")
				.append(cashBackAccountId).toString());

	}

	private Boolean getAccountIsIncreaseValue(Statement stat,
			Long cashBackAccountId) throws SQLException {

		Boolean increase = Boolean.FALSE;
		ResultSet rs1 = stat.executeQuery(new StringBuilder()
				.append("SELECT A.IS_INCREASE FROM ACCOUNT A WHERE A.ID =")
				.append(cashBackAccountId).toString());

		if (rs1.next()) {
			increase = rs1.getBoolean(1);
		}
		return increase;
	}

	private void updateCurrentAndTotalBalanceOfDepositInAccount(Statement stat,
			Double total, Long depositInAccountId, String symbol)
			throws SQLException {

		stat.execute(new StringBuilder()
				.append("UPDATE ACCOUNT A SET CURRENT_BALANCE= CURRENT_BALANCE ")
				.append(symbol).append(" ").append(total)
				.append(", A.TOTAL_BALANCE = A.TOTAL_BALANCE ").append(symbol)
				.append(" ").append(total).append(" WHERE ID = ")
				.append(depositInAccountId).toString());

	}

	@Override
	public void init(Connection arg0, String arg1, String arg2, String arg3,
			boolean arg4, int arg5) throws SQLException {
		// currently not using anywhere in the project.

	}

}
