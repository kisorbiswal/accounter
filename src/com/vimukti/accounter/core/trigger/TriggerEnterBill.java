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
public class TriggerEnterBill implements Trigger {

	@Override
	public void fire(Connection conn, Object[] oldRow, Object[] newRow)
			throws SQLException {

		// Required variables declaration and initialization

		Statement stat = conn.createStatement();

		Long newEnterBillId = (newRow != null) ? ((newRow[0] != null) ? (Long) newRow[0]
				: null)
				: null;
		Long oldEnterBillId = (oldRow != null) ? ((oldRow[0] != null) ? (Long) oldRow[0]
				: null)
				: null;
		@SuppressWarnings("unused")
		Long newVendorId = (newRow != null) ? ((newRow[1] != null) ? (Long) newRow[1]
				: null)
				: null;
		Long oldVendorId = (oldRow != null) ? ((oldRow[1] != null) ? (Long) oldRow[1]
				: null)
				: null;
		@SuppressWarnings("unused")
		Double newTotal = (newRow != null) ? ((newRow[10] != null) ? (Double) newRow[10]
				: null)
				: null;
		Double oldTotal = (oldRow != null) ? ((oldRow[10] != null) ? (Double) oldRow[10]
				: null)
				: null;
		Boolean newIsVoid = (newRow != null) ? ((newRow[11] != null) ? (Boolean) newRow[11]
				: null)
				: null;
		Boolean oldIsVoid = (oldRow != null) ? ((oldRow[11] != null) ? (Boolean) oldRow[11]
				: null)
				: null;
		Long newAccountsPayableId = (newRow != null) ? ((newRow[15] != null) ? (Long) newRow[15]
				: null)
				: null;
		@SuppressWarnings("unused")
		Long oldAccountsPayableId = (oldRow != null) ? ((oldRow[15] != null) ? (Long) oldRow[15]
				: null)
				: null;

		// Condition for checking whether this Trigger call is for new Row
		// Insertion
		if (newRow != null && oldRow == null) {
		}

		// Condition for checking whether this Trigger call is for Row Updation

		else if (newRow != null && oldRow != null) {
			// To check whether this updation is caused by Voiding the Enter
			// Bill
			// or not.
			if (oldIsVoid != newIsVoid) {

				// Deleting corresponding transaction rows from Account
				// Transaction table
				stat.execute(new StringBuilder()
						.append("DELETE FROM ACCOUNT_TRANSACTION WHERE T_ID =")
						.append(newEnterBillId).toString());

				// Reverse update the Accounts Payable Account with the
				// EnterBill amount
				updateAccountsPayableAccount(stat, oldTotal,
						newAccountsPayableId, AccounterConstants.SYMBOL_MINUS);

				// Reverse update the Vendor Balance by the EnterBill amount
				updateVendorBalance(stat, oldTotal, oldVendorId,
						AccounterConstants.SYMBOL_MINUS);

				int count = 0;
				ResultSet r = stat
						.executeQuery(new StringBuilder()
								.append("SELECT COUNT(*) FROM TRANSACTION_PAYBILL TP WHERE TP.ENTER_BILL_ID =  ")
								.append(oldEnterBillId).toString());

				if (r.next()) {
					count = r.getInt(1);
				}

				// check do we have any Pay bill for this enterbill

				if (count > 0) {

					// Increasing the Unused Payments amount of Pay Bill with
					// the EnterBill amount in which it's being paid.
					stat.execute(new StringBuilder()
							.append("UPDATE PAY_BILL PB SET PB.UNUSED_AMOUNT = PB.UNUSED_AMOUNT + (SELECT TRP.PAYMENT FROM TRANSACTION_PAYBILL TRP  WHERE TRP.TRANSACTION_ID = PB.ID AND TRP.ENTER_BILL_ID= ")
							.append(oldEnterBillId)
							.append(") WHERE PB.ID IN (SELECT TRP1.TRANSACTION_ID FROM TRANSACTION_PAYBILL TRP1 WHERE TRP1.ENTER_BILL_ID = ")
							.append(oldEnterBillId).append(")").toString());

					// Increase the Credits and payments balance by the amount
					// used in this enter bill
					stat.execute(new StringBuilder()
							.append("UPDATE CREDITS_AND_PAYMENTS CP SET CP.BALANCE = CP.BALANCE + (CASE WHEN (SELECT TRP1.APPLIED_CREDITS FROM TRANSACTION_PAYBILL TRP1 JOIN TRANSACTION_CREDITS_AND_PAYMENTS TCP1 ON TCP1.TRANSACTION_PAYBILL_ID = TRP1.ID JOIN CREDITS_AND_PAYMENTS CP1 ON TCP1.CREDITS_AND_PAYMENTS_ID= CP1.ID WHERE CP1.ID = CP.ID AND TRP1.ENTER_BILL_ID = ")
							.append(oldEnterBillId)
							.append(")  > 0 THEN SELECT  TCP2.AMOUNT_TO_USE  FROM TRANSACTION_CREDITS_AND_PAYMENTS TCP2  JOIN CREDITS_AND_PAYMENTS CP2 ON TCP2.CREDITS_AND_PAYMENTS_ID = CP2.ID JOIN TRANSACTION_PAYBILL TRP2 ON TRP2.ID = TCP2.TRANSACTION_PAYBILL_ID WHERE CP2.ID = CP.ID AND TRP2.ENTER_BILL_ID =")
							.append(oldEnterBillId)
							.append(" ELSE 0 END) WHERE CP.ID IN (SELECT CP1.ID FROM CREDITS_AND_PAYMENTS CP1 JOIN TRANSACTION_CREDITS_AND_PAYMENTS TCP ON CP1.ID = TCP.CREDITS_AND_PAYMENTS_ID JOIN TRANSACTION_PAYBILL TRP1 ON TCP.TRANSACTION_PAYBILL_ID = TRP1.ID WHERE TRP1.ENTER_BILL_ID = ")
							.append(oldEnterBillId).append(")").toString());

					// Updating the Paybill to make the payments and the applied
					// credits amounts as zero.
					stat.execute(new StringBuilder()
							.append("UPDATE TRANSACTION_PAYBILL TRP SET TRP.PAYMENT = 0.0,TRP.APPLIED_CREDITS = 0.0 WHERE TRP.ENTER_BILL_ID = ")
							.append(oldEnterBillId).toString());

					// Update the corresponding TransactionCreditsAndPayments
					// amount to use as zero.
					stat.execute(new StringBuilder()
							.append("UPDATE TRANSACTION_CREDITS_AND_PAYMENTS TCP SET TCP.AMOUNT_TO_USE = 0.0 WHERE TCP.ID IN (SELECT TCP.ID FROM TRANSACTION_CREDITS_AND_PAYMENTS TCP JOIN TRANSACTION_PAYBILL TRP ON TCP.TRANSACTION_PAYBILL_ID = TRP.ID WHERE TRP.ENTER_BILL_ID = ")
							.append(oldEnterBillId).append(")").toString());

				}

				// Update the Enter Bill to make the it as paid.
				stat.execute(new StringBuilder()
						.append("UPDATE ENTER_BILL EB SET EB.PAYMENTS = EB.TOTAL,EB.BALANCE_DUE = 0.0 WHERE EB.ID =")
						.append(oldEnterBillId).toString());

			}

		}

	}

	private void updateVendorBalance(Statement stat, Double total,
			Long vendorId, String symbol) throws SQLException {
		stat.execute(new StringBuilder()
				.append("UPDATE VENDOR V SET V.BALANCE = V.BALANCE ")
				.append(symbol).append(" ").append(total)
				.append(" WHERE V.ID = ").append(vendorId).toString());
	}

	private void updateAccountsPayableAccount(Statement stat, Double total,
			Long accountsPayableId, String symbol) throws SQLException {
		stat.execute(new StringBuilder()
				.append("UPDATE ACCOUNT A SET A.CURRENT_BALANCE = A.CURRENT_BALANCE ")
				.append(symbol).append(" ").append(total)
				.append(", A.TOTAL_BALANCE = A.TOTAL_BALANCE ").append(symbol)
				.append(" ").append(total).append(" WHERE A.ID = ")
				.append(accountsPayableId).toString());
	}

	@Override
	public void init(Connection arg0, String arg1, String arg2, String arg3,
			boolean arg4, int arg5) throws SQLException {
		// currently not using anywhere in the project.

	}

}
