package com.vimukti.accounter.core.trigger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.h2.api.Trigger;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.AccounterConstants;
import com.vimukti.accounter.core.Transaction;

/**
 * 
 * @author Devesh Satwani
 * 
 */
public class TriggerCustomerRefund implements Trigger {

	@Override
	public void fire(Connection conn, Object[] oldRow, Object[] newRow)
			throws SQLException {

		// Required variables declaration and initialization

		Statement stat = conn.createStatement();

		Long newCustomerRefundId = (newRow != null) ? ((newRow[0] != null) ? (Long) newRow[0]
				: null)
				: null;
		Long oldCustomerRefundId = (oldRow != null) ? ((oldRow[0] != null) ? (Long) oldRow[0]
				: null)
				: null;
		Long newCustomerId = (newRow != null) ? ((newRow[2] != null) ? (Long) newRow[2]
				: null)
				: null;
		@SuppressWarnings("unused")
		Long oldCustomerId = (oldRow != null) ? ((oldRow[2] != null) ? (Long) oldRow[2]
				: null)
				: null;
		Long newPayFromAccountId = (newRow != null) ? ((newRow[4] != null) ? (Long) newRow[4]
				: null)
				: null;
		@SuppressWarnings("unused")
		Long oldPayFromAccountId = (oldRow != null) ? ((oldRow[4] != null) ? (Long) oldRow[4]
				: null)
				: null;
		Double newAmount = (newRow != null) ? ((newRow[5] != null) ? (Double) newRow[5]
				: null)
				: null;
		@SuppressWarnings("unused")
		Double oldAmount = (oldRow != null) ? ((oldRow[5] != null) ? (Double) oldRow[5]
				: null)
				: null;
		@SuppressWarnings("unused")
		Long newPaymentMethodId = (newRow != null) ? ((newRow[6] != null) ? (Long) newRow[6]
				: null)
				: null;
		@SuppressWarnings("unused")
		Long oldPaymentMethodId = (oldRow != null) ? ((oldRow[6] != null) ? (Long) oldRow[6]
				: null)
				: null;
		Boolean newIsVoid = (newRow != null) ? ((newRow[13] != null) ? (Boolean) newRow[13]
				: null)
				: null;
		Boolean oldIsVoid = (oldRow != null) ? ((oldRow[13] != null) ? (Boolean) oldRow[13]
				: null)
				: null;
		Long newAccountsReceivableId = (newRow != null) ? ((newRow[15] != null) ? (Long) newRow[15]
				: null)
				: null;
		@SuppressWarnings("unused")
		Long oldAccountsReceivableId = (oldRow != null) ? ((oldRow[15] != null) ? (Long) oldRow[15]
				: null)
				: null;

		int accountType = 0;

		// Condition for checking whether this Trigger call is for new Row
		// Insertion
		if (newRow != null && oldRow == null) {
		}
		// Condition for checking whether this Trigger call is for Row Updation
		else if (newRow != null && oldRow != null) {
			// To check whether this updation is caused by Voiding any Customer
			// Refund
			// or not.
			if (oldIsVoid != newIsVoid) {

				// Deleting corresponding transaction rows from Account
				// Transaction table
				stat.execute(new StringBuilder()
						.append("DELETE FROM ACCOUNT_TRANSACTION WHERE T_ID =")
						.append(newCustomerRefundId).toString());

				// Reverse Updating the PayFrom Account balance with Refund
				// Amount according to the IsIncrease value of payFrom Account

				accountType = getAccountType(stat, newPayFromAccountId);

				if (accountType == Account.TYPE_CREDIT_CARD
						|| accountType == Account.TYPE_LONG_TERM_LIABILITY
						|| accountType == Account.TYPE_OTHER_CURRENT_LIABILITY) {

					// Reverse Updating the Pay From Account
					updateCurrentAndTotalBalanceOfPayFromAccount(stat,
							newAmount, newPayFromAccountId,
							AccounterConstants.SYMBOL_MINUS);

				} else {

					// Reverse Updating the Pay From Account
					updateCurrentAndTotalBalanceOfPayFromAccount(stat,
							newAmount, newPayFromAccountId,
							AccounterConstants.SYMBOL_PLUS);

				}

				// Reverse Updating the AccountsReceivable account with Refund
				// amount.

				updateAccountsReceivableAccount(stat, newAmount,
						newAccountsReceivableId,
						AccounterConstants.SYMBOL_MINUS);

				// Reverse Updating the Corresponding Customer Balance with
				// Refund amount

				updateCustomerBalance(stat, newAmount, newCustomerId,
						AccounterConstants.SYMBOL_MINUS);

				// Query to retrieve the number of places it's being paid.
				int count = 0;

				ResultSet r1 = stat
						.executeQuery(new StringBuilder()
								.append("SELECT COUNT(*) FROM TRANSACTION_RECEIVE_PAYMENT TRP WHERE TRP.CUSTOMER_REFUND_ID =  ")
								.append(oldCustomerRefundId).toString());

				if (r1.next()) {
					count = r1.getInt(1);
				}

				// check do we have any receive payment for this invoice

				if (count > 0) {

					// Increasing the Unused payment of the ReceivePayment by
					// the CustomerRefund amount in which this customer refund
					// is being paid.
					stat.execute(new StringBuilder()
							.append("UPDATE RECEIVE_PAYMENT RP SET RP.UNUSED_PAYMENTS = RP.UNUSED_PAYMENTS + (SELECT TRP.PAYMENT FROM TRANSACTION_RECEIVE_PAYMENT TRP  WHERE TRP.TRANSACTION_ID = RP.ID AND TRP.CUSTOMER_REFUND_ID= ")
							.append(oldCustomerRefundId)
							.append(") WHERE RP.ID IN (SELECT TRP1.TRANSACTION_ID FROM TRANSACTION_RECEIVE_PAYMENT TRP1 WHERE TRP1.CUSTOMER_REFUND_ID = ")
							.append(oldCustomerRefundId).append(")").toString());

					// Increasing the Balance if the CreditsAndPayments which we
					// have used in this Customer Refund
					stat.execute(new StringBuilder()
							.append("UPDATE CREDITS_AND_PAYMENTS CP SET CP.BALANCE = CP.BALANCE + (CASE WHEN (SELECT SUM(TRP1.APPLIED_CREDITS) FROM TRANSACTION_RECEIVE_PAYMENT TRP1 JOIN TRANSACTION_CREDITS_AND_PAYMENTS TCP1 ON TCP1.TRANSACTION_RECEIVE_PAYMENT_ID = TRP1.ID JOIN CREDITS_AND_PAYMENTS CP1 ON TCP1.CREDITS_AND_PAYMENTS_ID = CP1.ID WHERE CP1.ID = CP.ID AND TRP1.CUSTOMER_REFUND_ID = ")
							.append(oldCustomerRefundId)
							.append(")  > 0 THEN SELECT  SUM(TCP2.AMOUNT_TO_USE)  FROM TRANSACTION_CREDITS_AND_PAYMENTS TCP2  JOIN CREDITS_AND_PAYMENTS CP2 ON TCP2.CREDITS_AND_PAYMENTS_ID = CP2.ID JOIN TRANSACTION_RECEIVE_PAYMENT TRP2 ON TRP2.ID = TCP2.TRANSACTION_RECEIVE_PAYMENT_ID WHERE CP2.ID = CP.ID AND TRP2.CUSTOMER_REFUND_ID =")
							.append(oldCustomerRefundId)
							.append(" ELSE 0 END) WHERE CP.ID IN (SELECT CP1.ID FROM CREDITS_AND_PAYMENTS CP1 JOIN TRANSACTION_CREDITS_AND_PAYMENTS TCP ON CP1.ID = TCP.CREDITS_AND_PAYMENTS_ID JOIN TRANSACTION_RECEIVE_PAYMENT TRP1 ON TCP.TRANSACTION_RECEIVE_PAYMENT_ID = TRP1.ID WHERE TRP1.CUSTOMER_REFUND_ID = ")
							.append(oldCustomerRefundId).append(")").toString());

					// Update Transactin ReceivePayment corresponding to this
					// customer Refund to make the payments amount and the
					// applied credits amount as zero.

					stat.execute(new StringBuilder()
							.append("UPDATE TRANSACTION_RECEIVE_PAYMENT TRP SET TRP.PAYMENT = 0.0,TRP.APPLIED_CREDITS = 0.0 WHERE TRP.CUSTOMER_REFUND_ID = ")
							.append(oldCustomerRefundId).toString());

					// Updating the Transaction Credits and Payments
					// corresponding to this customer refund to make the amount
					// to use as zero.

					stat.execute(new StringBuilder()
							.append("UPDATE TRANSACTION_CREDITS_AND_PAYMENTS TCP SET TCP.AMOUNT_TO_USE = 0.0 WHERE TCP.ID IN (SELECT TCP.ID FROM TRANSACTION_CREDITS_AND_PAYMENTS TCP JOIN TRANSACTION_RECEIVE_PAYMENT TRP ON TCP.TRANSACTION_RECEIVE_PAYMENT_ID = TRP.ID WHERE TRP.CUSTOMER_REFUND_ID = ")
							.append(oldCustomerRefundId).append(")").toString());

					// To Update the Status of the ReveivePayments as Un
					// applied, in which this Customer Refund is participated.
					updateStatusOfOldCustomerRefund(stat, oldCustomerRefundId);

				}

				// updating the customer refund to make it as completly paid.
				stat.execute(new StringBuilder()
						.append("UPDATE CUSTOMER_REFUND CR SET CR.PAYMENTS = CR.AMOUNT,CR.BALANCE_DUE = 0.0 WHERE CR.ID =")
						.append(oldCustomerRefundId).toString());

			}

		}

	}

	private void updateStatusOfOldCustomerRefund(Statement stat,
			Long oldCustomerRefundId) throws SQLException {
		stat.execute(new StringBuilder()
				.append("UPDATE TRANSACTION T SET T.STATUS = ")
				.append(Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED)
				.append(" WHERE T.ID IN (SELECT TRP1.TRANSACTION_ID FROM TRANSACTION_RECEIVE_PAYMENT TRP1 WHERE TRP1.CUSTOMER_REFUND_ID = ")
				.append(oldCustomerRefundId).toString());
	}

	@SuppressWarnings("unused")
	private void updateStatusOfNewCustomerRefund(Statement stat,
			Long newPaymentMethodId, Long newCustomerRefundId)
			throws SQLException {

		Integer paymentMethodType = 0;
		ResultSet rs = stat.executeQuery(new StringBuilder()
				.append("SELECT PM.TYPE FROM PAYMENTMETHOD PM WHERE PM.ID = ")
				.append(newPaymentMethodId).toString());
		if (rs.next()) {
			paymentMethodType = rs.getInt(1);
		}
		if ((paymentMethodType).equals(AccounterConstants.PAYMENT_METHOD_CHECK)) {
			stat.execute(new StringBuilder()
					.append("UPDATE TRANSACTION T SET T.STATUS = ")
					.append(Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED)
					.append(" WHERE T.ID = ").append(newCustomerRefundId)
					.toString());
		} else {
			stat.execute(new StringBuilder()
					.append("UPDATE TRANSACTION T SET T.STATUS = ")
					.append(Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED)
					.append(" WHERE T.ID = ").append(newCustomerRefundId)
					.toString());

		}
	}

	private void updateCustomerBalance(Statement stat, Double amount,
			Long customerId, String symbol) throws SQLException {
		stat.execute(new StringBuilder()
				.append("UPDATE CUSTOMER C SET C.BALANCE = C.BALANCE ")
				.append(symbol).append(" ").append(amount)
				.append(" WHERE C.ID = ").append(customerId).toString());
	}

	private void updateAccountsReceivableAccount(Statement stat, Double amount,
			Long accountsReceivableId, String symbol) throws SQLException {
		stat.execute(new StringBuilder()
				.append("UPDATE ACCOUNT A SET A.CURRENT_BALANCE = A.CURRENT_BALANCE ")
				.append(symbol).append(" ").append(amount)
				.append(", A.TOTAL_BALANCE = A.TOTAL_BALANCE ").append(symbol)
				.append(" ").append(amount).append(" WHERE A.ID = ")
				.append(accountsReceivableId).toString());
	}

	private void updateCurrentAndTotalBalanceOfPayFromAccount(Statement stat,
			Double amount, Long payFromAccountId, String symbol)
			throws SQLException {
		stat.execute(new StringBuilder()
				.append("UPDATE ACCOUNT A SET A.CURRENT_BALANCE = A.CURRENT_BALANCE ")
				.append(symbol).append(" ").append(amount)
				.append(", A.TOTAL_BALANCE = A.TOTAL_BALANCE ").append(symbol)
				.append(" ").append(amount).append(" WHERE A.ID =")
				.append(payFromAccountId).toString());
	}

	private int getAccountType(Statement stat, Long payFromAccountId)
			throws SQLException {
		int accountType = 0;
		ResultSet r = stat.executeQuery(new StringBuilder()
				.append("SELECT A.A_TYPE FROM ACCOUNT A WHERE A.ID =")
				.append(payFromAccountId).toString());
		if (r.next()) {
			accountType = r.getInt(1);
		}
		return accountType;

	}

	@Override
	public void init(Connection arg0, String arg1, String arg2, String arg3,
			boolean arg4, int arg5) throws SQLException {
		// currently not using anywhere in the project.

	}

}
