package com.vimukti.accounter.core.trigger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.h2.api.Trigger;

import com.vimukti.accounter.core.AccounterConstants;
import com.vimukti.accounter.core.Transaction;

/**
 * 
 * @author Devesh Satwani
 * 
 */
public class TriggerTransactionReceivePayment implements Trigger {

	@Override
	public void fire(Connection conn, Object[] oldRow, Object[] newRow)
			throws SQLException {
		// Required variables declaration and initialization

		Statement stat = conn.createStatement();

		Long newTransactionReceivePaymentId = (newRow != null) ? ((newRow[0] != null) ? (Long) newRow[0]
				: null)
				: null;
		Long oldTransactionReceivePaymentId = (oldRow != null) ? ((oldRow[0] != null) ? (Long) oldRow[0]
				: null)
				: null;

		Long newInvoiceId = (newRow != null) ? ((newRow[3] != null) ? (Long) newRow[3]
				: null)
				: null;
		@SuppressWarnings("unused")
		Long oldInvoiceId = (oldRow != null) ? ((oldRow[3] != null) ? (Long) oldRow[3]
				: null)
				: null;

		Long newDiscountAccountId = (newRow != null) ? ((newRow[6] != null) ? (Long) newRow[6]
				: null)
				: null;
		@SuppressWarnings("unused")
		Long oldDiscountAccountId = (oldRow != null) ? ((oldRow[6] != null) ? (Long) oldRow[6]
				: null)
				: null;

		Double newCashDiscountAmount = (newRow != null) ? ((newRow[7] != null) ? (Double) newRow[7]
				: null)
				: null;
		Double oldCashDiscountAmount = (oldRow != null) ? ((oldRow[7] != null) ? (Double) oldRow[7]
				: null)
				: null;

		Long newWriteOffAccountId = (newRow != null) ? ((newRow[8] != null) ? (Long) newRow[8]
				: null)
				: null;
		@SuppressWarnings("unused")
		Long oldWriteOffAccountId = (oldRow != null) ? ((oldRow[8] != null) ? (Long) oldRow[8]
				: null)
				: null;

		Double newWriteOffAmount = (newRow != null) ? ((newRow[9] != null) ? (Double) newRow[9]
				: null)
				: null;
		Double oldWriteOffAmount = (oldRow != null) ? ((oldRow[9] != null) ? (Double) oldRow[9]
				: null)
				: null;

		@SuppressWarnings("unused")
		Double newAppliedCreditsAmount = (newRow != null) ? ((newRow[10] != null) ? (Double) newRow[10]
				: null)
				: null;
		Double oldAppliedCreditsAmount = (oldRow != null) ? ((oldRow[10] != null) ? (Double) oldRow[10]
				: null)
				: null;

		@SuppressWarnings("unused")
		Double newPaymentAmount = (newRow != null) ? ((newRow[11] != null) ? (Double) newRow[11]
				: null)
				: null;
		Double oldPaymentAmount = (oldRow != null) ? ((oldRow[11] != null) ? (Double) oldRow[11]
				: null)
				: null;

		Long newTransactionId = (newRow != null) ? ((newRow[12] != null) ? (Long) newRow[12]
				: null)
				: null;
		Long oldTransactionId = (oldRow != null) ? ((oldRow[12] != null) ? (Long) oldRow[12]
				: null)
				: null;

		Boolean newIsVoid = (newRow != null) ? ((newRow[13] != null) ? (Boolean) newRow[13]
				: null)
				: null;
		@SuppressWarnings("unused")
		Boolean oldIsVoid = (oldRow != null) ? ((oldRow[13] != null) ? (Boolean) oldRow[13]
				: null)
				: null;

		Long newCustomerRefundId = (newRow != null) ? ((newRow[14] != null) ? (Long) newRow[14]
				: null)
				: null;
		@SuppressWarnings("unused")
		Long oldCustomerRefundId = (oldRow != null) ? ((oldRow[14] != null) ? (Long) oldRow[14]
				: null)
				: null;

		// Long newAccountsReceivableId = (newRow != null)?((newRow[15] !=
		// null)?(Long) newRow[15]:null):null;
		// Long oldAccountsReceivableId = (oldRow != null)?((oldRow[15] !=
		// null)?(Long) oldRow[15]:null):null;

		Integer newIndex = (newRow != null) ? ((newRow[16] != null) ? (Integer) newRow[16]
				: null)
				: null;
		Integer oldIndex = (oldRow != null) ? ((oldRow[16] != null) ? (Integer) oldRow[16]
				: null)
				: null;

		Long newAccountsReceivableId = null;
		@SuppressWarnings("unused")
		Long oldAccountsReceivableId = null;
		Boolean isIncrease = Boolean.FALSE;

		newAccountsReceivableId = getAccountsReceivableId(stat,
				newTransactionId);
		oldAccountsReceivableId = getAccountsReceivableId(stat,
				oldTransactionId);
		// Condition for checking whether this Trigger call is for new Row
		// Insertion
		if (newRow != null && oldRow == null) {

			// if(newTransactionId != null){
			// doInCreatePart(stat, newDiscountAccountId, newCashDiscountAmount,
			// newAccountsReceivableId, newTransactionId, isIncrease,
			// newWriteOffAccountId, newWriteOffAmount, newInvoiceId,
			// newAppliedCreditsAmount, newPaymentAmount, newCustomerRefundId);
			// }

		}

		// Condition for checking whether this Trigger call is for Row Updation
		else if (newRow != null && oldRow != null) {

			// This Trigger Updation call is for Updation TransactionID and
			// Index. Hence do the create part
			if (oldIndex == null && newIndex != null) {
				// doInCreatePart(stat, newDiscountAccountId,
				// newCashDiscountAmount, newAccountsReceivableId,
				// newTransactionId, isIncrease, newWriteOffAccountId,
				// newWriteOffAmount, newInvoiceId, newAppliedCreditsAmount,
				// newPaymentAmount, newCustomerRefundId);
			}
			// Actual Updation part of Trigger Call
			else if (!(oldIndex == null && newIndex != null)) {

				newAccountsReceivableId = getAccountsReceivableId(stat,
						oldTransactionId);

				// To check whether this updation is caused by Voiding any
				// Receive Payment
				// or not.
				ResultSet r = stat.executeQuery(new StringBuilder().append(
						"SELECT M.IS_VOID FROM RECEIVE_PAYMENT M WHERE M.ID =")
						.append(newTransactionId).toString());
				r.next();
				if (r.getBoolean(1) && !newIsVoid) {

					// Deleting corresponding transaction rows from Account
					// Transaction
					// table
					stat.execute(new StringBuilder().append(
							"DELETE FROM ACCOUNT_TRANSACTION WHERE T_ID =")
							.append(newTransactionId).toString());

					if (newDiscountAccountId != null) {

						// Increase the Accounts Receivable A/c By the amount of
						// Discount
						updateCurrentAndTotalBalancesOfCorrespondingAccount(
								stat, newCashDiscountAmount,
								newAccountsReceivableId,
								AccounterConstants.SYMBOL_PLUS);

						// Increase the Customer Opening Balance by the amount
						// of
						// Discount

						updateCustomerBalance(stat, newCashDiscountAmount,
								newTransactionId,
								AccounterConstants.SYMBOL_PLUS);

						// Reverse Updating the Discount Account with the
						// discount
						// Amount
						isIncrease = getIsIncreaseValueOfAccount(stat,
								newDiscountAccountId);

						if (isIncrease) {

							// Update the corresponding Discount account with
							// discount amount

							updateCurrentAndTotalBalancesOfCorrespondingAccount(
									stat, newCashDiscountAmount,
									newDiscountAccountId,
									AccounterConstants.SYMBOL_PLUS);

						} else {
							// Update the corresponding Discount account with
							// discount amount

							updateCurrentAndTotalBalancesOfCorrespondingAccount(
									stat, newCashDiscountAmount,
									newDiscountAccountId,
									AccounterConstants.SYMBOL_MINUS);

						}
					}
					// Reverse Updating the selected Write off Account with
					// Write
					// off balance

					if (newWriteOffAccountId != null) {

						// Increase the Accounts Receivable A/c By the amount of
						// Write off

						updateCurrentAndTotalBalancesOfCorrespondingAccount(
								stat, newWriteOffAmount,
								newAccountsReceivableId,
								AccounterConstants.SYMBOL_PLUS);

						// Increase the Customer Opening Balance by the amount
						// of
						// Write off

						updateCustomerBalance(stat, newWriteOffAmount,
								newTransactionId,
								AccounterConstants.SYMBOL_PLUS);

						// Reverse Updating the Discount Account with the Write
						// off
						isIncrease = getIsIncreaseValueOfAccount(stat,
								newWriteOffAccountId);

						if (isIncrease) {

							// Reverse Updating the corresponding write off
							// account with write off amount
							updateCurrentAndTotalBalancesOfCorrespondingAccount(
									stat, newWriteOffAmount,
									newWriteOffAccountId,
									AccounterConstants.SYMBOL_PLUS);

						} else {
							// Reverse Updating the corresponding write off
							// account with write off amount
							updateCurrentAndTotalBalancesOfCorrespondingAccount(
									stat, newWriteOffAmount,
									newWriteOffAccountId,
									AccounterConstants.SYMBOL_MINUS);

						}

					}

					// To update Credits and Paymets entries balances those
					// participated in the Applied credits of that Transaction
					// Receive payment.

					if ((oldAppliedCreditsAmount).doubleValue() > 0.0) {
						stat
								.execute(new StringBuilder()
										.append(
												"UPDATE CREDITS_AND_PAYMENTS CP SET CP.BALANCE = CP.BALANCE +  (SELECT TCP2.AMOUNT_TO_USE FROM TRANSACTION_CREDITS_AND_PAYMENTS TCP2 WHERE TCP2.CREDITS_AND_PAYMENTS_ID = CP.ID AND TCP2.TRANSACTION_RECEIVE_PAYMENT_ID=")
										.append(oldTransactionReceivePaymentId)
										.append(
												")  WHERE CP.ID IN (SELECT CP1.ID FROM  CREDITS_AND_PAYMENTS CP1 JOIN TRANSACTION_CREDITS_AND_PAYMENTS TCP ON CP1.ID = TCP.CREDITS_AND_PAYMENTS_ID WHERE TCP.TRANSACTION_RECEIVE_PAYMENT_ID = ")
										.append(oldTransactionReceivePaymentId)
										.append(")").toString());

						// // To update the Amount_to_use to 0 in Transaction
						// Credits
						// // and
						// // Pyaments for the entries to which the
						// // TransactionReceivePayment is match.
						//
						// stat
						// .execute(new StringBuilder()
						// .append(
						// "UPDATE TRANSACTION_CREDITS_AND_PAYMENTS TCP SET TCP.AMOUNT_TO_USE = 0.0 WHERE TCP.TRANSACTION_RECEIVE_PAYMENT_ID = ")
						// .append(newTransactionReceivePaymentId).toString());
						//
						//						

						// To Update the Status of the Customer Credit Memo
						ResultSet rs = stat
								.executeQuery(new StringBuilder()
										.append(
												"SELECT CP.BALANCE, CP.CREDIT_AMOUNT, T.T_TYPE FROM CREDITS_AND_PAYMENTS CP JOIN TRANSACTION_CREDITS_AND_PAYMENTS TCP ON CP.ID = TCP.CREDITS_AND_PAYMENTS_ID JOIN TRANSACTION T ON T.ID = CP.TRANSACTION_ID WHERE TCP.TRANSACTION_RECEIVE_PAYMENT_ID = ")
										.append(newTransactionReceivePaymentId)
										.toString());
						rs.next();
						Double balance = rs.getDouble(1);
						Double creditAmount = rs.getDouble(2);
						if (((Integer) rs.getInt(3))
								.equals(Transaction.TYPE_CUSTOMER_CREDIT_MEMO)) {
							if (balance > 0D && balance < creditAmount) {
								stat
										.execute(new StringBuilder()
												.append(
														"UPDATE TRANSACTION T SET T.STATUS = ")
												.append(
														Transaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED)
												.append(" WHERE T.ID = ")

												.append(newTransactionId)
												.toString());
							} else if (balance == 0D) {
								stat
										.execute(new StringBuilder()
												.append(
														"UPDATE TRANSACTION T SET T.STATUS = ")
												.append(
														Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED)
												.append(" WHERE T.ID = ")
												.append(newTransactionId)
												.toString());
							} else if (balance.equals(creditAmount)) {
								stat
										.execute(new StringBuilder()
												.append(
														"UPDATE TRANSACTION T SET T.STATUS = ")
												.append(
														Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED)
												.append(" WHERE T.ID = ")
												.append(newTransactionId)
												.toString());
							}
						}
					}
					// To update the BalanceDue by increase it by the Payments &
					// Applied credits of the corresponding
					// TransactionREceivePayment and decrease Payments too with
					// the
					// same in Invoice for which of the invoice we are using in
					// this
					// Transaction ReceivePayment.

					Double amount = (oldCashDiscountAmount).doubleValue()
							+ (oldWriteOffAmount).doubleValue()
							+ (oldAppliedCreditsAmount).doubleValue()
							+ (oldPaymentAmount).doubleValue();

					if (newInvoiceId != null) {

						// Reverse Update the Payments and the balance due of
						// the corresponding customer refund
						updatePaymentsAndBalanceDueOfCorrespondingTable(stat,
								amount, newInvoiceId,
								AccounterConstants.SYMBOL_MINUS,
								AccounterConstants.TABLE_INVOICE);

						// To Change the Status of the Invoice which we paid in
						// this
						// Receive Payment

						ResultSet rs = stat
								.executeQuery(new StringBuilder()
										.append(
												"SELECT I.BALANCE_DUE, I.TOTAL FROM INVOICE I WHERE I.ID = ")
										.append(newInvoiceId).toString());
						rs.next();
						Double balanceDue = rs.getDouble(1);
						Double total = rs.getDouble(2);

						if (balanceDue > 0D && balanceDue < total) {
							stat
									.execute(new StringBuilder()
											.append(
													"UPDATE TRANSACTION T SET T.STATUS = ")
											.append(
													Transaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED)
											.append(" WHERE T.ID = ").append(
													newInvoiceId).toString());
						} else if (balanceDue == 0D) {
							stat
									.execute(new StringBuilder()
											.append(
													"UPDATE TRANSACTION T SET T.STATUS = ")
											.append(
													Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED)
											.append(" WHERE T.ID = ").append(
													newInvoiceId).toString());
						} else if (balanceDue.equals(total)) {
							stat
									.execute(new StringBuilder()
											.append(
													"UPDATE TRANSACTION T SET T.STATUS = ")
											.append(
													Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED)
											.append(" WHERE T.ID = ").append(
													newInvoiceId).toString());
						}

					} else {
						// Reverse Update the Payments and the balance due of
						// the corresponding customer refund
						updatePaymentsAndBalanceDueOfCorrespondingTable(stat,
								amount, newCustomerRefundId,
								AccounterConstants.SYMBOL_MINUS,
								AccounterConstants.TABLE_CUSTOMER_REFUND);

					}
					// To update Applycredits and payment of the corresponding
					// TransactionReceivepayment to zero.

					stat
							.execute(new StringBuilder()
									.append(
											"UPDATE TRANSACTION_RECEIVE_PAYMENT TRP SET TRP.APPLIED_CREDITS = 0.0, TRP.PAYMENT = 0.0,TRP.CASH_DISCOUNT = 0.0,TRP.WRITE_OFF = 0.0,TRP.IS_VOID = 'TRUE' WHERE TRP.ID=")
									.append(newTransactionReceivePaymentId)
									.toString());

				}
			}

		}

	}

	private Long getAccountsReceivableId(Statement stat, Long transactionId)
			throws SQLException {

		Long id = null;
		ResultSet rs = stat
				.executeQuery(new StringBuilder()
						.append(
								"SELECT RP.ACCOUNTS_RECEIVABLE_ID FROM RECEIVE_PAYMENT RP WHERE RP.ID = ")
						.append(transactionId).toString());
		if (rs.next()) {
			id = rs.getLong(1);
		}
		return id;
	}

	private void updatePaymentsAndBalanceDueOfCorrespondingTable(
			Statement stat, Double amount, Long invoiceOrCustomerRefundId,
			String symbol, String table) throws SQLException {

		String sign = "";
		if (symbol.equals(AccounterConstants.SYMBOL_PLUS)) {
			sign = AccounterConstants.SYMBOL_MINUS;
		} else
			sign = AccounterConstants.SYMBOL_PLUS;

		stat.execute(new StringBuilder().append("UPDATE ").append(table)
				.append(" I SET I.PAYMENTS = I.PAYMENTS ").append(symbol)
				.append(" ").append(amount).append(
						",I.BALANCE_DUE = I.BALANCE_DUE ").append(sign).append(
						" ").append(amount).append(" WHERE I.ID = ").append(
						invoiceOrCustomerRefundId).toString());
	}

	private void updateCustomerBalance(Statement stat,
			Double newCashDiscountAmount, Long newTransactionId, String symbol)
			throws SQLException {

		stat
				.execute(new StringBuilder()
						.append("UPDATE CUSTOMER C SET C.BALANCE = C.BALANCE ")
						.append(symbol)
						.append(" ")
						.append(newCashDiscountAmount)
						.append(
								" WHERE C.ID = (SELECT RP.CUSTOMER_ID FROM RECEIVE_PAYMENT RP WHERE RP.ID =")
						.append(newTransactionId).append(")").toString());
	}

	private void updateCurrentAndTotalBalancesOfCorrespondingAccount(
			Statement stat, Double cashDiscountAmount,
			Long accountsReceivableId, String symbol) throws SQLException {
		stat.execute(new StringBuilder().append(
				"UPDATE ACCOUNT A SET A.CURRENT_BALANCE = A.CURRENT_BALANCE ")
				.append(symbol).append(" ").append(cashDiscountAmount).append(
						", A.TOTAL_BALANCE = A.TOTAL_BALANCE ").append(symbol)
				.append(" ").append(cashDiscountAmount)
				.append(" WHERE A.ID = ").append(accountsReceivableId)
				.toString());

	}

	private Boolean getIsIncreaseValueOfAccount(Statement stat,
			Long discountAccountId) throws SQLException {

		Boolean isIncrease = Boolean.FALSE;

		ResultSet rs1 = stat.executeQuery(new StringBuilder().append(
				"SELECT A.IS_INCREASE FROM ACCOUNT A WHERE A.ID=").append(
				discountAccountId).toString());
		if (rs1.next()) {
			isIncrease = rs1.getBoolean(1);
		}
		return isIncrease;
	}

	@Override
	public void init(Connection arg0, String arg1, String arg2, String arg3,
			boolean arg4, int arg5) throws SQLException {
		// TODO Auto-generated method stub

	}

}
