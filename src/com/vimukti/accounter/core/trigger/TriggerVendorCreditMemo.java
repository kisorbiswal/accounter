package com.vimukti.accounter.core.trigger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import org.h2.api.Trigger;

import com.vimukti.accounter.core.AccounterConstants;

/**
 * 
 * @author Devesh Satwani
 * 
 */
public class TriggerVendorCreditMemo implements Trigger {

	@Override
	public void fire(Connection conn, Object[] oldRow, Object[] newRow)
			throws SQLException {

		// Required variables declaration and initialization

		Long newVendorCreditMemoId = (newRow != null) ? ((newRow[0] != null) ? (Long) newRow[0]
				: null)
				: null;
		@SuppressWarnings("unused")
		Long oldVendorCreditMemoId = (oldRow != null) ? ((oldRow[0] != null) ? (Long) oldRow[0]
				: null)
				: null;

		Long newVendorId = (newRow != null) ? ((newRow[4] != null) ? (Long) newRow[4]
				: null)
				: null;
		@SuppressWarnings("unused")
		Long oldVendorId = (oldRow != null) ? ((oldRow[4] != null) ? (Long) oldRow[4]
				: null)
				: null;

		@SuppressWarnings("unused")
		Double newTotalAmount = (newRow != null) ? ((newRow[3] != null) ? (Double) newRow[3]
				: null)
				: null;
		Double oldTotalAmount = (oldRow != null) ? ((oldRow[3] != null) ? (Double) oldRow[3]
				: null)
				: null;

		Boolean newIsVoid = (newRow != null) ? ((newRow[7] != null) ? (Boolean) newRow[7]
				: null)
				: null;
		Boolean oldIsVoid = (oldRow != null) ? ((oldRow[7] != null) ? (Boolean) oldRow[7]
				: null)
				: null;

		Long newAccountsPayableId = (newRow != null) ? ((newRow[8] != null) ? (Long) newRow[8]
				: null)
				: null;
		@SuppressWarnings("unused")
		Long oldAccountsPayableId = (oldRow != null) ? ((oldRow[8] != null) ? (Long) oldRow[8]
				: null)
				: null;

		Statement stat = conn.createStatement();

		@SuppressWarnings("unused")
		Long transactionId = null;
		@SuppressWarnings("unused")
		Date transactionDate = null;
		@SuppressWarnings("unused")
		Long companyId = null;

		// Condition for checking whether this Trigger call is for new Row
		// Insertion

		if (newRow != null && oldRow == null) {
		}
		// Condition for checking whether this Trigger call is for Row Updation
		else if (newRow != null && oldRow != null) {

			// To check whether this updation is caused by Voiding any Vendor
			// Credit Memo
			// or not.
			if (oldIsVoid != newIsVoid) {

				// Deleting corresponding transaction rows from Account
				// Transaction table
				stat.execute(new StringBuilder().append(
						"DELETE FROM ACCOUNT_TRANSACTION WHERE T_ID =").append(
						newVendorCreditMemoId).toString());

				// Updating the current and total balances of the corresponding
				// account
				updateCurrentAndTotalBalancesOfCorrespondingAccount(stat,
						oldTotalAmount, newAccountsPayableId,
						AccounterConstants.SYMBOL_PLUS);

				// Update the corresponding Vendor Balance
				updateVendorBalance(stat, oldTotalAmount, newVendorId,
						AccounterConstants.SYMBOL_PLUS);

				int count = 0;
				ResultSet r2 = stat
						.executeQuery(new StringBuilder()
								.append(
										"SELECT COUNT(*) FROM TRANSACTION_CREDITS_AND_PAYMENTS TCP WHERE TCP.ID = SELECT CP1.ID FROM CREDITS_PAYMENTS CP1 WHERE CP1.TRANSACTION_ID = ")
								.append(newVendorCreditMemoId).append(
										" AND TCP.AMOUNT_TO_USE > 0.0 ")
								.toString());

				if (r2.next()) {
					count = r2.getInt(1);
				}

				if (count > 0) {

					// Update the enter bill balance due with the used amount
					// from this vendor credit memo as Credit and payment
					stat
							.execute(new StringBuilder()
									.append(
											"UPDATE ENTER_BILL EB SET EB.BALANCE_DUE = EB.BALANCE_DUE + (SELECT TCP1.AMOUNT_TO_USE FROM TRANSACTION_CREDITS_AND_PAYMENTS TCP1 JOIN TRANSACTION_PAYBILL TRP1 ON TCP1.TRANSACTION_PAYBILL_ID = TRP1.ID JOIN ENTER_BILL EB1 ON TRP1.ENTER_BILL_ID = EB1.ID WHERE EB1.ID = EB.ID AND TCP1.CREDITS_AND_PAYMENTS_ID = SELECT CP.ID FROM CREDTIS_AND_PAYMENTS WHERE CP.TRANSACTION_ID = ")
									.append(newVendorCreditMemoId)
									.append(
											"),EB.PAYMENTS = EB.PAYMENTS - (SELECT TCP1.AMOUNT_TO_USE FROM TRANSACTION_CREDITS_AND_PAYMENTS TCP1 JOIN TRANSACTION_PAYBILL TRP1 ON TCP1.TRANSACTION_PAYBILL_ID = TRP1.ID JOIN ENTER_BILL EB1 ON TRP1.ENTER_BILL_ID = EB1.ID WHERE EB1.ID = EB.ID AND TCP1.CREDITS_AND_PAYMENTS_ID = SELECT CP.ID FROM CREDTIS_AND_PAYMENTS WHERE CP.TRANSACTION_ID = ")
									.append(newVendorCreditMemoId)
									.append(
											") WHERE EB.ID IN (SELECT EB.ID FROM ENTER_BILL EB JOIN TRANSACTION_PAYBILL TRP ON EB.ID = TRP.ENTER_BILL_ID JOIN TRANSACTION_CREDITS_AND_PAYMENTS TCP ON TRP.ID = TCP.TRANSACTION_PAYBILL_ID WHERE TCP.CREDITS_AND_PAYMENTS_ID = SELECT CP.ID FROM CREDTIS_AND_PAYMENTS WHERE CP.TRANSACTION_ID = ")
									.append(newVendorCreditMemoId).append(")")
									.toString());

					// Updating the applied credit amount of corresponding
					// transaction paybills by the amount to used for this
					// Vendor Credit memo
					stat
							.execute(new StringBuilder()
									.append(
											"UPDATE TRANSACTION_PAYBILL TRP SET TRP.APPLIED_CREDITS = TRP.APPLIED_CREDITS - SELECT(TCP1.AMOUNT_TO_USE FROM TRANSACTION_CREDITS_AND_PAYMENTS TCP1 JOIN TRANSACTION_PAYBILL TRP1 WHERE TRP1.ID = TRP.ID) WHERE TRP.ID IN (SELECT TRP.ID FROM TRANSACTION_PAYBILL TRP JOIN TRANSACTION_CREDITS_AND_PAYMENTS TCP ON TRP.ID = TCP.TRANSACTION_PAYBILL_ID WHERE TCP.CREDITS_AND_PAYMENTS_ID = SELECT CP.ID FROM CREDTIS_AND_PAYMENTS WHERE CP.TRANSACTION_ID = ")
									.append(newVendorCreditMemoId).append(")")
									.toString());

					// Update the Transaction credits and payments to make the
					// amount to use as zero for the corresponding entries
					stat
							.execute(new StringBuilder()
									.append(
											"UPDATE TRANSACTION_CREDITS_AND_PAYMENTS TCP SET TCP.AMOUNT_TO_USE = 0.0 WHERE TCP.CREDITS_AND_PAYMENTS_ID = SELECT CP.ID FROM CREDTIS_AND_PAYMENTS WHERE CP.TRANSACTION_ID = ")
									.append(newVendorCreditMemoId).toString());

				}

				// Deleting this Credit Memo from CREDITS_AND_PAYMENTS table.
				stat
						.execute(new StringBuilder()
								.append(
										"DELETE FROM CREDITS_AND_PAYMENTS CP WHERE CP.ID = SELECT CP1.ID FROM CREDITS_AND_PAYMENTS CP1 WHERE CP1.TRANSACTION_ID = ")
								.append(newVendorCreditMemoId).toString());

			}

		}

	}

	private void updateVendorBalance(Statement stat, Double totalAmount,
			Long vendorId, String symbol) throws SQLException {

		stat.execute(new StringBuilder().append(
				"UPDATE VENDOR V SET V.BALANCE = V.BALANCE ").append(symbol)
				.append(" ").append(totalAmount).append(" WHERE V.ID = ")
				.append(vendorId).toString());

	}

	private void updateCurrentAndTotalBalancesOfCorrespondingAccount(
			Statement stat, Double amount, Long accountId, String symbol)
			throws SQLException {

		stat.execute(new StringBuilder().append(
				"UPDATE ACCOUNT A SET A.CURRENT_BALANCE = A.CURRENT_BALANCE ")
				.append(symbol).append(" ").append(amount).append(
						", A.TOTAL_BALANCE = A.TOTAL_BALANCE ").append(symbol)
				.append(" ").append(amount).append(" WHERE A.ID = ").append(
						accountId).toString());
	}

	@Override
	public void init(Connection arg0, String arg1, String arg2, String arg3,
			boolean arg4, int arg5) throws SQLException {
		// TODO Auto-generated method stub

	}

}
