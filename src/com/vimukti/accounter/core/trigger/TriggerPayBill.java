package com.vimukti.accounter.core.trigger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import org.h2.api.Trigger;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.AccounterConstants;
import com.vimukti.accounter.core.PayBill;

/**
 * 
 * @author Devesh Satwani
 * 
 */
public class TriggerPayBill implements Trigger {

	public void fire(Connection conn, Object[] oldRow, Object[] newRow)
			throws SQLException {

		// Required variables declaration and initialization

		Statement stat = conn.createStatement();

		Long newPayBillId = (newRow != null) ? ((newRow[0] != null) ? (Long) newRow[0]
				: null)
				: null;
		Long oldPayBillId = (oldRow != null) ? ((oldRow[0] != null) ? (Long) oldRow[0]
				: null)
				: null;
		@SuppressWarnings("unused")
		Long newPayFromAccountId = (newRow != null) ? ((newRow[1] != null) ? (Long) newRow[1]
				: null)
				: null;
		Long oldPayFromAccountId = (oldRow != null) ? ((oldRow[1] != null) ? (Long) oldRow[1]
				: null)
				: null;

		Long newVendorId = (newRow != null) ? ((newRow[4] != null) ? (Long) newRow[4]
				: null)
				: null;
		@SuppressWarnings("unused")
		Long oldVendorId = (oldRow != null) ? ((oldRow[4] != null) ? (Long) oldRow[4]
				: null)
				: null;
		Double newAmount = (newRow != null) ? ((newRow[6] != null) ? (Double) newRow[6]
				: null)
				: null;
		Double oldAmount = (oldRow != null) ? ((oldRow[6] != null) ? (Double) oldRow[6]
				: null)
				: null;

		Boolean newIsVoid = (newRow != null) ? ((newRow[8] != null) ? (Boolean) newRow[8]
				: null)
				: null;
		Boolean oldIsVoid = (oldRow != null) ? ((oldRow[8] != null) ? (Boolean) oldRow[8]
				: null)
				: null;

		Long newAccountsPayableId = (newRow != null) ? ((newRow[9] != null) ? (Long) newRow[9]
				: null)
				: null;
		@SuppressWarnings("unused")
		Long oldAccountsPayableId = (oldRow != null) ? ((oldRow[9] != null) ? (Long) oldRow[9]
				: null)
				: null;
		Double newUnUsedAmount = (newRow != null) ? ((newRow[10] != null) ? (Double) newRow[10]
				: null)
				: null;
		Double oldUnUsedAmount = (oldRow != null) ? ((oldRow[10] != null) ? (Double) oldRow[10]
				: null)
				: null;
		Integer newPayBillType = (newRow != null) ? ((newRow[11] != null) ? (Integer) newRow[11]
				: null)
				: null;
		@SuppressWarnings("unused")
		Integer oldPayBillType = (oldRow != null) ? ((oldRow[11] != null) ? (Integer) oldRow[11]
				: null)
				: null;

		// Condition for checking whether this Trigger call is for new Row
		// Insertion

		if (newRow != null && oldRow == null) {
		}
		// Condition for checking whether this Trigger call is for Row Updation

		else if (newRow != null && oldRow != null) {
			// To check whether this updation is caused by Voiding the PayBill
			// or Vendor Payment
			// or not.
			if (oldIsVoid != newIsVoid) {

				// Deleting corresponding transaction rows from Account
				// Transaction table
				stat.execute(new StringBuilder().append(
						"DELETE FROM ACCOUNT_TRANSACTION WHERE T_ID =").append(
						newPayBillId).toString());

				// Check whether this updated PayBill is vendor Payment or
				// PayBill
				if (newPayBillType == PayBill.TYPE_VENDOR_PAYMENT) {

					// Reverse Updating the Current and total balances of
					// Accounts Payable account.
					updateAccountsPayableAccount(stat, newAmount,
							newAccountsPayableId,
							AccounterConstants.SYMBOL_PLUS);

					// Reverse Update the Vendor Balance
					updateVendorBalance(stat, newAmount, newVendorId,
							AccounterConstants.SYMBOL_PLUS);

				}

				int accountType = getAccountType(stat, oldPayFromAccountId);

				// Checking the PayFrom account type
				if (accountType == Account.TYPE_CREDIT_CARD
						|| accountType == Account.TYPE_OTHER_CURRENT_LIABILITY
						|| accountType == Account.TYPE_LONG_TERM_LIABILITY) {

					// Reverse Updating the corresponding Pay From account.
					updateCurrentAndTotalBalancesOfPayFromAccount(stat,
							oldAmount, oldPayFromAccountId,
							AccounterConstants.SYMBOL_MINUS);

				} else {
					// Reverse Updating the corresponding Pay From Account.
					updateCurrentAndTotalBalancesOfPayFromAccount(stat,
							oldAmount, oldPayFromAccountId,
							AccounterConstants.SYMBOL_PLUS);

				}

				// Checking whether the unused amount is not equal to zero or
				// not
				if (oldUnUsedAmount != 0D) {

					// Query for retrieving the number of credits and payments
					// used in this paybill
					int count = 0;

					ResultSet r2 = stat
							.executeQuery(new StringBuilder()
									.append(
											"SELECT COUNT(*) FROM TRANSACTION_CREDITS_AND_PAYMENTS TCP WHERE TCP.CREDITS_AND_PAYMENTS_ID = SELECT CP.ID FROM CREDTIS_AND_PAYMENTS WHERE CP.TRANSACTION_ID = ")
									.append(newPayBillId).append(
											"' AND TCP.AMOUNT_TO_USE > 0.0 ")
									.toString());

					if (r2.next()) {
						count = r2.getInt(1);
					}

					// If the count greater than zero then we will do the
					// following operations.
					if (count > 0) {

						// Increase the Balance due of the EnterBils by the used
						// amount of this PayBill as Credit and Payment
						stat
								.execute(new StringBuilder()
										.append(
												"UPDATE ENTER_BILL EB SET EB.BALANCE_DUE = EB.BALANCE_DUE + (SELECT TCP1.AMOUNT_TO_USE FROM TRANSACTION_CREDITS_AND_PAYMENTS TCP1 JOIN TRANSACTION_PAYBILL TRP1 ON TCP1.TRANSACTION_PAYBILL_ID = TRP1.ID JOIN ENTER_BILL EB1 ON TRP1.ENTER_BILL_ID = EB1.ID WHERE EB1.ID = EB.ID AND TCP1.CREDITS_AND_PAYMENTS_ID = SELECT CP.ID FROM CREDTIS_AND_PAYMENTS WHERE CP.TRANSACTION_ID = ")
										.append(newPayBillId)
										.append(
												"),EB.PAYMENTS = EB.PAYMENTS - (SELECT TCP1.AMOUNT_TO_USE FROM TRANSACTION_CREDITS_AND_PAYMENTS TCP1 JOIN TRANSACTION_PAYBILL TRP1 ON TCP1.TRANSACTION_PAYBILL_ID = TRP1.ID JOIN ENTER_BILL EB1 ON TRP1.ENTER_BILL_ID = EB1.ID WHERE EB1.ID = EB.ID AND TCP1.CREDITS_AND_PAYMENTS_ID = SELECT CP.ID FROM CREDTIS_AND_PAYMENTS WHERE CP.TRANSACTION_ID = ")
										.append(newPayBillId)
										.append(
												") WHERE EB.ID IN (SELECT EB.ID FROM ENTER_BILL EB JOIN TRANSACTION_PAYBILL TRP ON EB.ID = TRP.ENTER_BILL_ID JOIN TRANSACTION_CREDITS_AND_PAYMENTS TCP ON TRP.ID = TCP.TRANSACTION_PAYBILL_ID WHERE TCP.CREDITS_AND_PAYMENTS_ID = SELECT CP.ID FROM CREDTIS_AND_PAYMENTS WHERE CP.TRANSACTION_ID = ")
										.append(newPayBillId).append(")")
										.toString());

						// Decreasing the Transaction PayBills applied credits
						// amounts by the used amount of this PayBill as Credit
						// and Payment
						stat
								.execute(new StringBuilder()
										.append(
												"UPDATE TRANSACTION_PAYBILL TRP SET TRP.APPLIED_CREDITS = TRP.APPLIED_CREDITS - (SELECT TCP1.AMOUNT_TO_USE FROM TRANSACTION_CREDITS_AND_PAYMENTS TCP1 JOIN TRANSACTION_PAYBILL TRP1 WHERE TRP1.ID = TRP.ID AND TCP1.CREDITS_AND_PAYMENTS_ID = SELECT CP.ID FROM CREDTIS_AND_PAYMENTS WHERE CP.TRANSACTION_ID = ")
										.append(newPayBillId)
										.append(
												") WHERE TRP.ID IN (SELECT TRP.ID FROM TRANSACTION_PAYBILL TRP JOIN TRANSACTION_CREDITS_AND_PAYMENTS TCP ON TRP.ID = TCP.TRANSACTION_PAYBILL_ID WHERE TCP.CREDITS_AND_PAYMENTS_ID = SELECT CP.ID FROM CREDTIS_AND_PAYMENTS WHERE CP.TRANSACTION_ID = ")
										.append(newPayBillId).append(")")
										.toString());

						// Updating the Transaction Credits and Payments to make
						// the amount to use as zero.
						stat
								.execute(new StringBuilder()
										.append(
												"UPDATE TRANSACTION_CREDITS_AND_PAYMENTS TCP SET TCP.AMOUNT_TO_USE = 0.0 WHERE TCP.CREDITS_AND_PAYMENTS_ID = SELECT CP.ID FROM CREDTIS_AND_PAYMENTS WHERE CP.TRANSACTION_ID = ")
										.append(newPayBillId).toString());

					}

					// Delete the corresponding entries from the credtis and
					// payments table.
					stat
							.execute(new StringBuilder()
									.append(
											"DELETE FROM CREDITS_AND_PAYMENTS CP WHERE CP.CREDITS_AND_PAYMENTS_ID = SELECT CP.ID FROM CREDTIS_AND_PAYMENTS WHERE CP.TRANSACTION_ID = ")
									.append(newPayBillId).toString());

					// Updaing the Pay Bill to make all the PayBill amount as un
					// used amount.

					stat
							.execute(new StringBuilder()
									.append(
											"UPDATE PAY_BILL PB SET PB.UNUSED_AMOUNT = PB.AMOUNT WHERE PB.ID = ")
									.append(oldPayBillId).toString());

				}

			}

			// when we void the enter bill the unused amount of payBill will
			// increase if
			// old unused amount is 0 then we need to insert new row in credits
			// and payments else we need to update credits and payments by
			// difference of old and new amount

			else if (!newIsVoid) {

				Long transactionNumber = null;
				Date transactionDate = null;
				Long companyId = null;

				ResultSet r1 = stat
						.executeQuery(new StringBuilder()
								.append(
										"SELECT T.NUMBER,T.T_DATE,T.COMPANY_ID FROM TRANSACTION T WHERE T.ID =")
								.append(newPayBillId).toString());

				if (r1.next()) {
					transactionNumber = r1.getLong(1);
					transactionDate = r1.getDate(2);
					companyId = r1.getLong(3);
				}
				// checking whether the unused amount is equal to zero or not
				if (oldUnUsedAmount == 0D) {

					// Inserting a new row into credits and payments table with
					// the Pay Bill Unused amount because it's not exist.

					stat
							.execute(new StringBuilder()
									.append(
											"INSERT INTO CREDITS_AND_PAYMENTS (DATE,COMPANY_ID,TYPE,MEMO,CREDIT_AMOUNT,BALANCE,TRANSACTION_ID,CUSTOMER_OR_VENDOR_ID) VALUES('")
									.append(transactionDate).append("',")
									.append(companyId).append(",4,'").append(
											transactionNumber).append(
											"Vendor Payment").append("',")
									.append(newAmount).append(",").append(
											newUnUsedAmount).append(",")
									.append(newPayBillId).append(",").append(
											newVendorId).append(")").toString());

				} else {

					// Updating the existing Entry for this paybill in Credits
					// and Payments table.
					stat
							.execute(new StringBuilder()
									.append(
											"UPDATE CREDITS_AND_PAYMENTS CP SET CP.BALANCE = CP.BALANCE + ")
									.append(newUnUsedAmount)
									.append("-")
									.append(oldUnUsedAmount)
									.append(
											"WHERE CP.ID = SELECT CP1.ID FROM CREDITS_AND_PAYMENTS CP1 WHERE CP1.TRANSACTION_ID = ")
									.append(newPayBillId).toString());

				}

			}

		}
	}

	@SuppressWarnings("unused")
	private void updatePayBillStatus(Statement stat, int status,
			Long newPayBillId) throws SQLException {
		stat.execute(new StringBuilder().append(
				"UPDATE TRANSACTION T SET T.STATUS = ").append(status).append(
				" WHERE T.ID = ").append(newPayBillId).toString());
	}

	private void updateCurrentAndTotalBalancesOfPayFromAccount(Statement stat,
			Double amount, Long payFromAccountId, String symbol)
			throws SQLException {
		stat.execute(new StringBuilder().append(
				"UPDATE ACCOUNT A SET A.CURRENT_BALANCE = A.CURRENT_BALANCE ")
				.append(symbol).append(" ").append(amount).append(
						", A.TOTAL_BALANCE = A.TOTAL_BALANCE ").append(symbol)
				.append(" ").append(amount).append(" WHERE A.ID =").append(
						payFromAccountId).toString());
	}

	private void updateAccountsPayableAccount(Statement stat, Double amount,
			Long accountsPayableId, String symbol) throws SQLException {
		stat.execute(new StringBuilder().append(
				"UPDATE ACCOUNT A SET A.CURRENT_BALANCE = A.CURRENT_BALANCE ")
				.append(symbol).append(" ").append(amount).append(
						", A.TOTAL_BALANCE = A.TOTAL_BALANCE ").append(symbol)
				.append(" ").append(amount).append(" WHERE A.ID = ").append(
						accountsPayableId).toString());
	}

	private void updateVendorBalance(Statement stat, Double amount,
			Long vendorId, String symbol) throws SQLException {
		stat.execute(new StringBuilder().append(
				"UPDATE VENDOR V SET V.BALANCE = V.BALANCE ").append(symbol)
				.append(" ").append(amount).append(" WHERE V.ID = ").append(
						vendorId).toString());
	}

	@SuppressWarnings("unused")
	private void insertVendorPaymentIntoCreditsAndPayments(Statement stat,
			Date transactionDate, Long companyId, Long transactionNumber,
			Double unUsedAmount, Long payBillId, Long vendorId)
			throws SQLException {
		stat
				.execute(new StringBuilder()
						.append(
								"INSERT INTO CREDITS_AND_PAYMENTS (DATE,COMPANY_ID,TYPE,MEMO,CREDIT_AMOUNT,BALANCE,TRANSACTION_ID,CUSTOMER_OR_VENDOR_ID) VALUES('")
						.append(transactionDate).append("',").append(companyId)
						.append(",4,'").append(transactionNumber).append(
								AccounterConstants.VENDOR_PAYMENT).append("',")
						.append(unUsedAmount).append(",").append(unUsedAmount)
						.append(",").append(payBillId).append(",").append(
								vendorId).append(")").toString());
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
