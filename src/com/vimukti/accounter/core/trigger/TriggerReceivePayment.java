package com.vimukti.accounter.core.trigger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import org.h2.api.Trigger;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.AccounterConstants;
import com.vimukti.accounter.core.Transaction;

/**
 * 
 * @author Devesh Satwani
 * 
 */
public class TriggerReceivePayment implements Trigger {

	public void fire(Connection conn, Object[] oldRow, Object[] newRow)
			throws SQLException {

		// Required variables declaration and initialization

		Statement stat = conn.createStatement();

		Long newReceivePaymentId = (newRow != null) ? ((newRow[0] != null) ? (Long) newRow[0]
				: null)
				: null;
		Long oldReceivePaymentId = (oldRow != null) ? ((oldRow[0] != null) ? (Long) oldRow[0]
				: null)
				: null;

		Long newCustomerId = (newRow != null) ? ((newRow[1] != null) ? (Long) newRow[1]
				: null)
				: null;
		@SuppressWarnings("unused")
		Long oldCustomerId = (oldRow != null) ? ((oldRow[1] != null) ? (Long) oldRow[1]
				: null)
				: null;
		Double newAmount = (newRow != null) ? ((newRow[2] != null) ? (Double) newRow[2]
				: null)
				: null;
		Double oldAmount = (oldRow != null) ? ((oldRow[2] != null) ? (Double) oldRow[2]
				: null)
				: null;
		@SuppressWarnings("unused")
		Long newDepositInAccountId = (newRow != null) ? ((newRow[7] != null) ? (Long) newRow[7]
				: null)
				: null;
		Long oldDepositInAccountId = (oldRow != null) ? ((oldRow[7] != null) ? (Long) oldRow[7]
				: null)
				: null;

		Double newUnUsedPayments = (newRow != null) ? ((newRow[9] != null) ? (Double) newRow[9]
				: null)
				: null;
		Double oldUnUsedPayments = (oldRow != null) ? ((oldRow[9] != null) ? (Double) oldRow[9]
				: null)
				: null;

		Boolean newIsVoid = (newRow != null) ? ((newRow[10] != null) ? (Boolean) newRow[10]
				: null)
				: null;
		Boolean oldIsVoid = (oldRow != null) ? ((oldRow[10] != null) ? (Boolean) oldRow[10]
				: null)
				: null;

		Double newTotalCashDiscount = (newRow != null) ? ((newRow[11] != null) ? (Double) newRow[11]
				: null)
				: null;
		@SuppressWarnings("unused")
		Double oldTotalCashDiscount = (oldRow != null) ? ((oldRow[11] != null) ? (Double) oldRow[11]
				: null)
				: null;
		Double newTotalWriteOff = (newRow != null) ? ((newRow[12] != null) ? (Double) newRow[12]
				: null)
				: null;
		@SuppressWarnings("unused")
		Double oldTotalWriteOff = (oldRow != null) ? ((oldRow[12] != null) ? (Double) oldRow[12]
				: null)
				: null;
		Long newAccountsReceivableId = (newRow != null) ? ((newRow[14] != null) ? (Long) newRow[14]
				: null)
				: null;
		@SuppressWarnings("unused")
		Long oldAccountsReceivableId = (oldRow != null) ? ((oldRow[14] != null) ? (Long) oldRow[14]
				: null)
				: null;
		@SuppressWarnings("unused")
		Double newTotalAppliedCredits = (newRow != null) ? ((newRow[15] != null) ? (Double) newRow[15]
				: null)
				: null;
		@SuppressWarnings("unused")
		Double oldTotalAppliedCredits = (oldRow != null) ? ((oldRow[15] != null) ? (Double) oldRow[15]
				: null)
				: null;

		// Condition for checking whether this Trigger call is for new Row
		// Insertion

		if (newRow != null && oldRow == null) {
		}
		// Condition for checking whether this Trigger call is for Row Updation

		else if (newRow != null && oldRow != null) {
			// To check whether this updation is caused by Voiding the Receive
			// Payment
			// or not.
			if (oldIsVoid != newIsVoid) {

				// Deleting corresponding transaction rows from Account
				// Transaction table
				stat.execute(new StringBuilder().append(
						"DELETE FROM ACCOUNT_TRANSACTION WHERE T_ID =").append(
						newReceivePaymentId).toString());

				// Reverse Update Accounts Receivable Account.

				updateAccountsReceivableAccount(stat, newAmount,
						newAccountsReceivableId, AccounterConstants.SYMBOL_PLUS);

				int accountType = getAccountType(stat, oldDepositInAccountId);

				if (accountType == Account.TYPE_CREDIT_CARD) {

					// Reverse Update the Current and total balance of the
					// Deposit in Account.
					updateCurrentAndTotalBalancesOfDepositInAccount(stat,
							oldAmount, oldDepositInAccountId,
							AccounterConstants.SYMBOL_PLUS);

				} else {

					// Reverse Update the Current and total balance of the
					// Deposit in Account.
					updateCurrentAndTotalBalancesOfDepositInAccount(stat,
							oldAmount, oldDepositInAccountId,
							AccounterConstants.SYMBOL_MINUS);

				}
				// Reverse Update the Corresponding Customer Balance
				updateCustomerBalance(stat, oldAmount, newCustomerId,
						AccounterConstants.SYMBOL_PLUS);

				if (oldUnUsedPayments != 0D) {

					// Query to find whether any Credits of this Receive Payment
					// had applied in any Invoices or Customer Refudns.
					int count = 0;

					ResultSet r2 = stat
							.executeQuery(new StringBuilder()
									.append(
											"SELECT COUNT(*) FROM TRANSACTION_CREDITS_AND_PAYMENTS TCP WHERE TCP.CREDITS_AND_PAYMENTS_ID = SELECT CP.ID FROM CREDITS_AND_PAYMENTS CP WHERE CP.TRANSACTION_ID = ")
									.append(newReceivePaymentId).append(
											" AND TCP.AMOUNT_TO_USE > 0.0 ")
									.toString());

					if (r2.next()) {
						count = r2.getInt(1);
					}

					if (count > 0) {

						// Increase the Balances of the Invoices by the amount
						// used from this Receive Payment credits.
						stat
								.execute(new StringBuilder()
										.append(
												"UPDATE INVOICE I SET I.BALANCE_DUE = I.BALANCE_DUE + (SELECT SUM(TCP1.AMOUNT_TO_USE) FROM TRANSACTION_CREDITS_AND_PAYMENTS TCP1 JOIN TRANSACTION_RECEIVE_PAYMENT TRP1 ON TCP1.TRANSACTION_RECEIVE_PAYMENT_ID = TRP1.ID JOIN INVOICE I1 ON TRP1.INVOICE_ID = I1.ID WHERE I1.ID = I.ID AND TCP1.CREDITS_AND_PAYMENTS_ID = SELECT CP.ID FROM CREDITS_AND_PAYMENTS CP WHERE CP.TRANSACTION_ID = ")
										.append(newReceivePaymentId)
										.append(
												"),I.PAYMENTS = I.PAYMENTS - (SELECT SUM(TCP1.AMOUNT_TO_USE) FROM TRANSACTION_CREDITS_AND_PAYMENTS TCP1 JOIN TRANSACTION_RECEIVE_PAYMENT TRP1 ON TCP1.TRANSACTION_RECEIVE_PAYMENT_ID = TRP1.ID JOIN INVOICE I1 ON TRP1.INVOICE_ID = I1.ID WHERE I1.ID = I.ID AND TCP1.CREDITS_AND_PAYMENTS_ID = SELECT CP.ID FROM CREDITS_AND_PAYMENTS CP WHERE CP.TRANSACTION_ID = ")
										.append(newReceivePaymentId)
										.append(
												") WHERE I.ID IN (SELECT I.ID FROM INVOICE I JOIN TRANSACTION_RECEIVE_PAYMENT TRP ON I.ID = TRP.INVOICE_ID JOIN TRANSACTION_CREDITS_AND_PAYMENTS TCP ON TRP.ID = TCP.TRANSACTION_RECEIVE_PAYMENT_ID WHERE TCP.CREDITS_AND_PAYMENTS_ID = SELECT CP.ID FROM CREDITS_AND_PAYMENTS CP WHERE CP.TRANSACTION_ID = ")
										.append(newReceivePaymentId)
										.append(")").toString());

						// Increase the Balances of the Customer Refunds by the
						// amount used from this Receive Payment credits.
						stat
								.execute(new StringBuilder()
										.append(
												"UPDATE CUSTOMER_REFUND CR SET CR.BALANCE_DUE = CR.BALANCE_DUE + (SELECT SUM(TCP1.AMOUNT_TO_USE) FROM TRANSACTION_CREDITS_AND_PAYMENTS TCP1 JOIN TRANSACTION_RECEIVE_PAYMENT TRP1 ON TCP1.TRANSACTION_RECEIVE_PAYMENT_ID = TRP1.ID JOIN CUSTOMER_REFUND CR1 ON TRP1.CUSTOMER_REFUND_ID = CR1.ID WHERE CR1.ID = CR.ID AND TCP1.CREDITS_AND_PAYMENTS_ID = SELECT CP.ID FROM CREDITS_AND_PAYMENTS CP WHERE CP.TRANSACTION_ID = ")
										.append(newReceivePaymentId)
										.append(
												"),CR.PAYMENTS = CR.PAYMENTS - (SELECT SUM(TCP1.AMOUNT_TO_USE) FROM TRANSACTION_CREDITS_AND_PAYMENTS TCP1 JOIN TRANSACTION_RECEIVE_PAYMENT TRP1 ON TCP1.TRANSACTION_RECEIVE_PAYMENT_ID = TRP1.ID JOIN CUSTOMER_REFUND CR1 ON TRP1.CUSTOMER_REFUND_ID = CR1.ID WHERE CR1.ID = CR.ID AND TCP1.CREDITS_AND_PAYMENTS_ID = SELECT CP.ID FROM CREDITS_AND_PAYMENTS CP WHERE CP.TRANSACTION_ID = ")
										.append(newReceivePaymentId)
										.append(
												") WHERE CR.ID IN (SELECT CR.ID FROM CUSTOMER_REFUND CR JOIN TRANSACTION_RECEIVE_PAYMENT TRP ON CR.ID = TRP.CUSTOMER_REFUND_ID JOIN TRANSACTION_CREDITS_AND_PAYMENTS TCP ON TRP.ID = TCP.TRANSACTION_RECEIVE_PAYMENT_ID WHERE TCP.CREDITS_AND_PAYMENTS_ID = SELECT CP.ID FROM CREDITS_AND_PAYMENTS CP WHERE CP.TRANSACTION_ID = ")
										.append(newReceivePaymentId)
										.append(")").toString());

						// Decrease the Applied Credits amount of the
						// Transaction Receive Payments by the amount used from
						// this Receive Payment Credits.
						stat
								.execute(new StringBuilder()
										.append(
												"UPDATE TRANSACTION_RECEIVE_PAYMENT TRP SET TRP.APPLIED_CREDITS = TRP.APPLIED_CREDITS - (SELECT TCP1.AMOUNT_TO_USE FROM TRANSACTION_CREDITS_AND_PAYMENTS TCP1 JOIN TRANSACTION_RECEIVE_PAYMENT TRP1 ON TCP1.TRANSACTION_RECEIVE_PAYMENT_ID = TRP1.ID WHERE TRP1.ID = TRP.ID AND TCP1.CREDITS_AND_PAYMENTS_ID = SELECT CP.ID FROM CREDITS_AND_PAYMENTS CP WHERE CP.TRANSACTION_ID = ")
										.append(newReceivePaymentId)
										.append(
												") WHERE TRP.ID IN (SELECT TRP.ID FROM TRANSACTION_RECEIVE_PAYMENT TRP JOIN TRANSACTION_CREDITS_AND_PAYMENTS TCP ON TRP.ID = TCP.TRANSACTION_RECEIVE_PAYMENT_ID WHERE TCP.CREDITS_AND_PAYMENTS_ID = SELECT CP.ID FROM CREDITS_AND_PAYMENTS CP WHERE CP.TRANSACTION_ID = ")
										.append(newReceivePaymentId)
										.append(")").toString());

						// Update the amount to use of the Corresponding
						// Transaction Credits and payments of this Receive
						// Payment as zero
						stat
								.execute(new StringBuilder()
										.append(
												"UPDATE TRANSACTION_CREDITS_AND_PAYMENTS TCP SET TCP.AMOUNT_TO_USE = 0.0 WHERE TCP.CREDITS_AND_PAYMENTS_ID = SELECT CP.ID FROM CREDITS_AND_PAYMENTS CP WHERE CP.TRANSACTION_ID = ")
										.append(newReceivePaymentId).toString());

					}

					// Delete the Corresponding Credits and payments of this
					// Receive Payment from Credits and Payments table.
					stat
							.execute(new StringBuilder()
									.append(
											"DELETE FROM CREDITS_AND_PAYMENTS CP WHERE CP.TRANSACTION_ID = ")
									.append(newReceivePaymentId).toString());

					// Update the Receive Payment to make the receive payment
					// amount as un used payment
					stat
							.execute(new StringBuilder()
									.append(
											"UPDATE RECEIVE_PAYMENT RP SET RP.UNUSED_PAYMENTS = RP.AMOUNT WHERE RP.ID = ")
									.append(oldReceivePaymentId).toString());

				}

			}

			// when we void the invoice the unused amount of rec pay will
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
								.append(newReceivePaymentId).toString());

				if (r1.next()) {
					transactionNumber = r1.getLong(1);
					transactionDate = r1.getDate(2);
					companyId = r1.getLong(3);
				}
				Double creditAmount = (newTotalCashDiscount).doubleValue()
						+ (newTotalWriteOff).doubleValue()
						+ (newAmount).doubleValue();

				if (oldUnUsedPayments == 0D) {

					// Insert an entry into Credits and Payments table for the
					// un used amount of this receive payment, because no
					// previous entry is there for this receive payment
					insertReceivePaymentIntoCreditsAndPayments(stat,
							transactionDate, companyId, transactionNumber,
							creditAmount, newUnUsedPayments,
							newReceivePaymentId, newCustomerId);

				} else {

					// update the balance of the existing entry for this receive
					// payment in credits and payments table with this un used
					// amount.
					stat
							.execute(new StringBuilder()
									.append(
											"UPDATE CREDITS_AND_PAYMENTS CP SET CP.BALANCE = CP.BALANCE + ")
									.append(newUnUsedPayments)
									.append("-")
									.append(oldUnUsedPayments)
									.append(
											"WHERE CP.ID = SELECT CP1.ID FROM CREDITS_PAYMENTS CP1 WHERE CP1.TRANSACTION_ID = ")
									.append(newReceivePaymentId).toString());

				}

			}

		}
	}

	@SuppressWarnings("unused")
	private void updateReceivePaymentStatus(Statement stat,
			Double newUnUsedPayments, Double newAmount,
			Double newTotalCashDiscount, Double newTotalWriteOff,
			Double newTotalAppliedCredits, Long newReceivePaymentId)
			throws SQLException {

		// To check whether Unused payment is equal to amount or not and any
		// Cash discount or Write off or Applied Credit are applied or not
		if ((newUnUsedPayments).equals(newAmount)
				&& (newTotalCashDiscount > 0D || newTotalWriteOff > 0D)
				|| newTotalAppliedCredits > 0D) {
			stat.execute(new StringBuilder().append(
					"UPDATE TRANSACTION T SET T.STATUS = ").append(
					Transaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED)
					.append(" WHERE T.ID = ").append(newReceivePaymentId)
					.toString());
		}
		// To check whether Unused payment is equal to amount or not
		else if ((newUnUsedPayments).equals(newAmount)) {
			stat.execute(new StringBuilder().append(
					"UPDATE TRANSACTION T SET T.STATUS = ").append(
					Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED)
					.append(" WHERE T.ID = ").append(newReceivePaymentId)
					.toString());
		}
		// To check whether Unused payment is greater than zero and not equal to
		// amount or not
		else if (newUnUsedPayments > 0D
				&& !(newUnUsedPayments).equals(newAmount)) {
			stat.execute(new StringBuilder().append(
					"UPDATE TRANSACTION T SET T.STATUS = ").append(
					Transaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED)
					.append(" WHERE T.ID = ").append(newReceivePaymentId)
					.toString());
		}
		// To check whether Unused payment is equal to zero or not
		else if ((newUnUsedPayments).equals(0D)) {
			stat.execute(new StringBuilder().append(
					"UPDATE TRANSACTION T SET T.STATUS = ").append(
					Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED).append(
					" WHERE T.ID = ").append(newReceivePaymentId).toString());
		}
	}

	private void updateCustomerBalance(Statement stat, Double amount,
			Long customerId, String symbol) throws SQLException {
		stat.execute(new StringBuilder().append(
				"UPDATE CUSTOMER C SET C.BALANCE = C.BALANCE ").append(symbol)
				.append(" ").append(amount).append(" WHERE C.ID = ").append(
						customerId).toString());
	}

	private void updateCurrentAndTotalBalancesOfDepositInAccount(
			Statement stat, Double amount, Long depositInAccountId,
			String symbol) throws SQLException {
		stat.execute(new StringBuilder().append(
				"UPDATE ACCOUNT A SET A.CURRENT_BALANCE = A.CURRENT_BALANCE ")
				.append(symbol).append(" ").append(amount).append(
						", A.TOTAL_BALANCE = A.TOTAL_BALANCE ").append(symbol)
				.append(" ").append(amount).append(" WHERE A.ID =").append(
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

	private void insertReceivePaymentIntoCreditsAndPayments(Statement stat,
			Date transactionDate, Long companyId, Long transactionNumber,
			Double creditAmount, Double newUnUsedPayments,
			Long newReceivePaymentId, Long newCustomerId) throws SQLException {

		stat
				.execute(new StringBuilder()
						.append(
								"INSERT INTO CREDITS_AND_PAYMENTS (DATE,COMPANY_ID,TYPE,MEMO,CREDIT_AMOUNT,BALANCE,TRANSACTION_ID,CUSTOMER_OR_VENDOR_ID) VALUES('")
						.append(transactionDate).append("',").append(companyId)
						.append(",1,'").append(transactionNumber).append(
								AccounterConstants.CUSTOMER_PAYMENT).append(
								"',").append(creditAmount).append(",").append(
								newUnUsedPayments).append(",").append(
								newReceivePaymentId).append(",").append(
								newCustomerId).append(")").toString());

	}

	private void updateAccountsReceivableAccount(Statement stat, Double amount,
			Long accountsReceivableId, String symbol) throws SQLException {
		stat.execute(new StringBuilder().append(
				"UPDATE ACCOUNT A SET A.CURRENT_BALANCE = A.CURRENT_BALANCE ")
				.append(symbol).append(" ").append(amount).append(
						", A.TOTAL_BALANCE = A.TOTAL_BALANCE ").append(symbol)
				.append(" ").append(amount).append(" WHERE A.ID = ").append(
						accountsReceivableId).toString());
	}

	@Override
	public void init(Connection arg0, String arg1, String arg2, String arg3,
			boolean arg4, int arg5) throws SQLException {
		// currently not using anywhere in the project.

	}

}
