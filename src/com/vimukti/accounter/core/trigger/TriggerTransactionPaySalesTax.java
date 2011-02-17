package com.vimukti.accounter.core.trigger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.h2.api.Trigger;

import com.vimukti.accounter.core.AccounterConstants;

public class TriggerTransactionPaySalesTax implements Trigger {

	public void fire(Connection conn, Object[] oldRow, Object[] newRow)
			throws SQLException {

		Statement stat = conn.createStatement();

		Long newTransactionPaySalesTaxId = (newRow != null) ? ((newRow[0] != null) ? (Long) newRow[0]
				: null)
				: null;

		@SuppressWarnings("unused")
		Long oldTransactionPaySalesTaxId = (oldRow != null) ? ((oldRow[0] != null) ? (Long) oldRow[0]
				: null)
				: null;

		Long newTaxAgencyId = (newRow != null) ? ((newRow[2] != null) ? (Long) newRow[2]
				: null)
				: null;

		@SuppressWarnings("unused")
		Long oldTaxAgencyId = (newRow != null) ? ((newRow[2] != null) ? (Long) newRow[2]
				: null)
				: null;

		@SuppressWarnings("unused")
		Long newTaxCodeId = (newRow != null) ? ((newRow[3] != null) ? (Long) newRow[3]
				: null)
				: null;

		@SuppressWarnings("unused")
		Long oldTaxCodeId = (newRow != null) ? ((newRow[3] != null) ? (Long) newRow[3]
				: null)
				: null;

		Double newAmountToPay = (newRow != null) ? ((newRow[4] != null) ? (Double) newRow[4]
				: null)
				: null;

		@SuppressWarnings("unused")
		Double oldAmountToPay = (newRow != null) ? ((newRow[4] != null) ? (Double) newRow[4]
				: null)
				: null;

		@SuppressWarnings("unused")
		Double newTaxDue = (newRow != null) ? ((newRow[5] != null) ? (Double) newRow[5]
				: null)
				: null;

		@SuppressWarnings("unused")
		Double oldTaxDue = (newRow != null) ? ((newRow[5] != null) ? (Double) newRow[5]
				: null)
				: null;

		Long newTransactionId = (newRow != null) ? ((newRow[6] != null) ? (Long) newRow[6]
				: null)
				: null;
		@SuppressWarnings("unused")
		Long oldTransactionId = (oldRow != null) ? ((oldRow[6] != null) ? (Long) oldRow[6]
				: null)
				: null;

		Integer newIndex = (newRow != null) ? ((newRow[7] != null) ? (Integer) newRow[7]
				: null)
				: null;

		Integer oldIndex = (oldRow != null) ? ((oldRow[7] != null) ? (Integer) oldRow[7]
				: null)
				: null;

		Boolean isIncrease = false;

		Long liabilityAccountId = null;

		if (newRow != null && oldRow == null) {

		}

		// Condition for checking whether this Trigger call is for Row Updation
		else if (newRow != null && oldRow != null) {

			// This Trigger Updation call is for Updation TransactionID and
			// Index. Hence do the create part
			if (oldIndex == null && newIndex != null) {
				doInCreatePart(stat, isIncrease, liabilityAccountId,
						newTaxAgencyId, newAmountToPay);
			}
			// Actual Updation part of Trigger Call
			else if (!(oldIndex == null && newIndex != null)) {

				ResultSet r = stat.executeQuery(new StringBuilder().append(
						"SELECT M.IS_VOID FROM PAY_SALES_TAX M WHERE M.ID =")
						.append(newTransactionId).toString());
				r.next();

				if (r.getBoolean(1)) {

					// Deleting corresponding transaction rows from Account
					// Transaction
					// table
					stat.execute(new StringBuilder().append(
							"DELETE FROM ACCOUNT_TRANSACTION WHERE T_ID =")
							.append(newTransactionPaySalesTaxId).toString());

					ResultSet rs = stat
							.executeQuery(new StringBuilder()
									.append(
											"SELECT TA.ACCOUNT_ID FROM TAXAGENCY TA WHERE TA.ID= ")
									.append(newTaxAgencyId).toString());

					if (rs.next()) {
						liabilityAccountId = rs.getLong(1);
					}

					isIncrease = getAccountIsIncrease(stat, liabilityAccountId);

					if (isIncrease)
						updateTaxAgenciesAccountCurrentAndTotalBalances(stat,
								newAmountToPay, liabilityAccountId,
								AccounterConstants.SYMBOL_PLUS);

					else
						updateTaxAgenciesAccountCurrentAndTotalBalances(stat,
								newAmountToPay, liabilityAccountId,
								AccounterConstants.SYMBOL_MINUS);

					updateTaxAgenciesBalances(stat, newAmountToPay,
							AccounterConstants.SYMBOL_PLUS);
				}

			}

		}
	}

	private void updateTaxAgenciesAccountCurrentAndTotalBalances(
			Statement stat, Double amountToPay, Long liabilityAccountId,
			String symbol) throws SQLException {
		stat.execute(new StringBuilder().append(
				"UPDATE ACCOUNT A SET A.CURRENT_BALANCE = A.CURRENT_BALANCE ")
				.append(symbol).append(" ").append(amountToPay).append(
						", A.TOTAL_BALANCE = A.TOTAL_BALANCE ").append(symbol)
				.append(" ").append(amountToPay).append(" WHERE A.ID =")
				.append(liabilityAccountId).toString());
	}

	private boolean getAccountIsIncrease(Statement stat, Long depositInAccountId)
			throws SQLException {
		boolean accountType = false;

		ResultSet r = stat.executeQuery(new StringBuilder().append(
				"SELECT A.IS_INCREASE FROM ACCOUNT A WHERE A.ID =").append(
				depositInAccountId).toString());

		if (r.next()) {
			accountType = r.getBoolean(1);
		}
		return accountType;
	}

	private void updateTaxAgenciesBalances(Statement stat,
			Double newAllTaxableLineTotal, String symbol) throws SQLException {
		stat.execute(new StringBuilder().append(
				"UPDATE TAXAGENCY A SET A.BALANCE = A.BALANCE ").append(symbol)
				.append(" ").append(newAllTaxableLineTotal).toString());

	}

	private void doInCreatePart(Statement stat, Boolean isIncrease,
			Long liabilityAccountId, Long newTaxAgencyId, Double newAmountToPay)
			throws SQLException {

		ResultSet rs = stat.executeQuery(new StringBuilder().append(
				"SELECT TA.ACCOUNT_ID FROM TAXAGENCY TA WHERE TA.ID= ").append(
				newTaxAgencyId).toString());

		if (rs.next()) {
			liabilityAccountId = rs.getLong(1);
		}

		isIncrease = getAccountIsIncrease(stat, liabilityAccountId);

		if (isIncrease)

			updateTaxAgenciesAccountCurrentAndTotalBalances(stat,
					newAmountToPay, liabilityAccountId,
					AccounterConstants.SYMBOL_MINUS);

		else

			updateTaxAgenciesAccountCurrentAndTotalBalances(stat,
					newAmountToPay, liabilityAccountId,
					AccounterConstants.SYMBOL_PLUS);

		updateTaxAgenciesBalances(stat, newAmountToPay,
				AccounterConstants.SYMBOL_MINUS);

		stat
				.execute(new StringBuilder()
						.append(
								"UPDATE PAY_SALES_TAX_ENTRIES PSE SET PSE.BALANCE = PSE.BALANCE - ")
						.append(newAmountToPay).toString());
	}

	public void init(Connection arg0, String arg1, String arg2, String arg3,
			boolean arg4, int arg5) throws SQLException {
		// TODO Auto-generated method stub

	}

}
