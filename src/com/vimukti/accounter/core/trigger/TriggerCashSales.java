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
public class TriggerCashSales implements Trigger {

	@Override
	public void fire(Connection conn, Object[] oldRow, Object[] newRow)
			throws SQLException {

		// Required variables declaration and initialization
		Statement stat = conn.createStatement();

		Long newCashSaleId = (newRow != null) ? ((newRow[0] != null) ? (Long) newRow[0]
				: null)
				: null;
		@SuppressWarnings("unused")
		Long oldCashSaleId = (oldRow != null) ? ((oldRow[0] != null) ? (Long) oldRow[0]
				: null)
				: null;
		@SuppressWarnings("unused")
		Long newDepositInAccountId = (newRow != null) ? ((newRow[8] != null) ? (Long) newRow[8]
				: null)
				: null;
		Long oldDepositInAccountId = (oldRow != null) ? ((oldRow[8] != null) ? (Long) oldRow[8]
				: null)
				: null;
		Long newTaxGroupId = (newRow != null) ? ((newRow[14] != null) ? (Long) newRow[14]
				: null)
				: null;
		@SuppressWarnings("unused")
		Long oldTaxGroupId = (oldRow != null) ? ((oldRow[14] != null) ? (Long) oldRow[14]
				: null)
				: null;
		@SuppressWarnings("unused")
		Double newTotal = (newRow != null) ? ((newRow[17] != null) ? (Double) newRow[17]
				: null)
				: null;
		Double oldTotal = (oldRow != null) ? ((oldRow[17] != null) ? (Double) oldRow[17]
				: null)
				: null;
		@SuppressWarnings("unused")
		Double newAllLineTotal = (newRow != null) ? ((newRow[18] != null) ? (Double) newRow[18]
				: null)
				: null;
		@SuppressWarnings("unused")
		Double oldAllLineTotal = (oldRow != null) ? ((oldRow[18] != null) ? (Double) oldRow[18]
				: null)
				: null;
		Double newAllTaxableLineTotal = (newRow != null) ? ((newRow[19] != null) ? (Double) newRow[19]
				: null)
				: null;
		@SuppressWarnings("unused")
		Double oldAllTaxableLineTotal = (oldRow != null) ? ((oldRow[19] != null) ? (Double) oldRow[19]
				: null)
				: null;
		Boolean newIsVoid = (newRow != null) ? ((newRow[21] != null) ? (Boolean) newRow[21]
				: null)
				: null;
		Boolean oldIsVoid = (oldRow != null) ? ((oldRow[21] != null) ? (Boolean) oldRow[21]
				: null)
				: null;
		int accountType = 0;

		// Condition for checking whether this Trigger call is for new Row
		// Insertion

		if (newRow != null && oldRow == null) {

		}
		// Condition for checking whether this Trigger call is for Row Updation
		else if (newRow != null && oldRow != null) {

			// To check whether this updation is caused by Voiding any Cash Sale
			// or not.

			if (oldIsVoid != newIsVoid) {

				// Query for deleting corresponding transaction rows from
				// Account
				// Transaction table
				stat.execute(new StringBuilder().append(
						"DELETE FROM ACCOUNT_TRANSACTION WHERE T_ID =").append(
						newCashSaleId).toString());

				// Query to retrieve the type of the Deposit In Account
				accountType = getAccountType(stat, oldDepositInAccountId);

				if (accountType == Account.TYPE_CREDIT_CARD) {

					// To update the current and total balance of the Deposit in
					// account.
					updateCurrentAndTotalBalanceOfDepositInAccount(stat,
							oldTotal, oldDepositInAccountId,
							AccounterConstants.SYMBOL_PLUS);

				} else {
					// To update the current and total balance of the Deposit in
					// account.
					updateCurrentAndTotalBalanceOfDepositInAccount(stat,
							oldTotal, oldDepositInAccountId,
							AccounterConstants.SYMBOL_MINUS);

				}

				// ResultSet r1 = stat.executeQuery(new StringBuilder().append(
				// "SELECT I.TAXGROUP_ID FROM CASH_SALES I WHERE I.ID =")
				// .append(oldRow[0]).toString());
				if (!newTaxGroupId.equals(null)
						&& !(newAllTaxableLineTotal).equals(0.0)) {

					// To update the corresponding TaxAgency Accounts of the
					// selected TaxGroup.
					updateTaxAgenciesCurrentAndTotalBalances(stat,
							newCashSaleId, newAllTaxableLineTotal,
							AccounterConstants.SYMBOL_MINUS);

					// Reverse updating the balance of corresponding tax
					// agency
					updateTaxAgenciesBalances(stat, newCashSaleId,
							newAllTaxableLineTotal,
							AccounterConstants.SYMBOL_MINUS);

					// Deleting corresponding transaction rows from TaxRate
					// Calculation table
					stat
							.execute(new StringBuilder()
									.append(
											"DELETE FROM TAX_RATE_CALCULATION WHERE TRANSACTION_ID =")
									.append(newCashSaleId).toString());

				}

			}

		}

	}

	private void updateTaxAgenciesBalances(Statement stat, Long newCashSaleId,
			Double newAllTaxableLineTotal, String symbol) throws SQLException {
		stat
				.execute(new StringBuilder()
						.append("UPDATE TAXAGENCY A SET A.BALANCE = A.BALANCE ")
						.append(symbol)
						.append(" ")
						.append(newAllTaxableLineTotal)
						.append(
								"* (SELECT TRC.RATE FROM TAX_RATE_CALCULATION TRC WHERE TRC.TRANSACTION_ID=")
						.append(newCashSaleId)
						.append(
								" AND TRC.TAXAGENCY_ID = A.ID )/100 WHERE A.ID IN (SELECT TRC.TAXAGENCY_ID  FROM TAX_RATE_CALCULATION TRC WHERE TRC.TRANSACTION_ID=")
						.append(newCashSaleId).append(")").toString());

	}

	private void updateTaxAgenciesCurrentAndTotalBalances(Statement stat,
			Long cashSaleId, Double allTaxableLineTotal, String symbol)
			throws SQLException {
		stat
				.execute(new StringBuilder()
						.append(
								"UPDATE ACCOUNT A SET A.CURRENT_BALANCE = A.CURRENT_BALANCE ")
						.append(symbol)
						.append(" ")
						.append(allTaxableLineTotal)
						.append(
								"*(SELECT SUM(TRC.RATE) FROM TAX_RATE_CALCULATION TRC WHERE TRC.TRANSACTION_ID= ")
						.append(cashSaleId)
						.append(
								" AND TRC.TAXAGENCY_ACCOUNT_ID = A.ID GROUP BY TRC.TAXAGENCY_ACCOUNT_ID)/100 , A.TOTAL_BALANCE = A.TOTAL_BALANCE ")
						.append(symbol)
						.append(" ")
						.append(allTaxableLineTotal)
						.append(
								"*(SELECT SUM(TRC.RATE) FROM TAX_RATE_CALCULATION TRC WHERE TRC.TRANSACTION_ID= ")
						.append(cashSaleId)
						.append(
								" AND TRC.TAXAGENCY_ACCOUNT_ID = A.ID GROUP BY TRC.TAXAGENCY_ACCOUNT_ID)/100 WHERE A.ID IN (SELECT TRC.TAXAGENCY_ACCOUNT_ID  FROM TAX_RATE_CALCULATION TRC WHERE TRC.TRANSACTION_ID =")
						.append(cashSaleId).append(" )").toString());

	}

	private void updateCurrentAndTotalBalanceOfDepositInAccount(Statement stat,
			Double total, Long depositInAccountId, String symbol)
			throws SQLException {
		stat.execute(new StringBuilder().append(
				"UPDATE ACCOUNT A SET A.CURRENT_BALANCE = A.CURRENT_BALANCE ")
				.append(symbol).append(" ").append(total).append(
						", A.TOTAL_BALANCE = A.TOTAL_BALANCE ").append(symbol)
				.append(" ").append(total).append(" WHERE A.ID =").append(
						depositInAccountId).toString());
	}

	private int getAccountType(Statement stat, Long depositInAccountId)
			throws SQLException {
		int accountType = 0;

		ResultSet r = stat.executeQuery(new StringBuilder().append(
				"SELECT A.A_TYPE FROM ACCOUNT A WHERE A.ID =").append(
				depositInAccountId).toString());

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
