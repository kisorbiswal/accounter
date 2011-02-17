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
public class TriggerTransactionPayBill implements Trigger {

	@Override
	public void fire(Connection conn, Object[] oldRow, Object[] newRow)
			throws SQLException {
		// Required variables declaration and initialization

		Statement stat = conn.createStatement();

		Long newTransactionPayBillId = (newRow != null) ? ((newRow[0] != null) ? (Long) newRow[0]
				: null)
				: null;
		Long oldTransactionPayBillId = (oldRow != null) ? ((oldRow[0] != null) ? (Long) oldRow[0]
				: null)
				: null;

		Long newEnterBillId = (newRow != null) ? ((newRow[3] != null) ? (Long) newRow[3]
				: null)
				: null;
		@SuppressWarnings("unused")
		Long oldEnterBillId = (oldRow != null) ? ((oldRow[3] != null) ? (Long) oldRow[3]
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

		@SuppressWarnings("unused")
		Double newAppliedCreditsAmount = (newRow != null) ? ((newRow[8] != null) ? (Double) newRow[8]
				: null)
				: null;
		Double oldAppliedCreditsAmount = (oldRow != null) ? ((oldRow[8] != null) ? (Double) oldRow[8]
				: null)
				: null;

		Double newPaymentAmount = (newRow != null) ? ((newRow[9] != null) ? (Double) newRow[9]
				: null)
				: null;
		Double oldPaymentAmount = (oldRow != null) ? ((oldRow[9] != null) ? (Double) oldRow[9]
				: null)
				: null;

		Long newTransactionId = (newRow != null) ? ((newRow[10] != null) ? (Long) newRow[10]
				: null)
				: null;
		Long oldTransactionId = (oldRow != null) ? ((oldRow[10] != null) ? (Long) oldRow[10]
				: null)
				: null;

		Boolean newIsVoid = (newRow != null) ? ((newRow[11] != null) ? (Boolean) newRow[11]
				: null)
				: null;
		@SuppressWarnings("unused")
		Boolean oldIsVoid = (oldRow != null) ? ((oldRow[11] != null) ? (Boolean) oldRow[11]
				: null)
				: null;

		Long newTransactionMakeDepositId = (newRow != null) ? ((newRow[12] != null) ? (Long) newRow[12]
				: null)
				: null;
		@SuppressWarnings("unused")
		Long oldTransactionMakeDepositId = (oldRow != null) ? ((oldRow[12] != null) ? (Long) oldRow[12]
				: null)
				: null;

		// Long newAccountsPayableId = (newRow != null)?((newRow[13] !=
		// null)?(Long) newRow[13]:null):null;
		// Long oldAccountsPayableId = (oldRow != null)?((oldRow[13] !=
		// null)?(Long) oldRow[13]:null):null;
		//		
		Long newVendorId = (newRow != null) ? ((newRow[14] != null) ? (Long) newRow[14]
				: null)
				: null;
		@SuppressWarnings("unused")
		Long oldVendorId = (oldRow != null) ? ((oldRow[14] != null) ? (Long) oldRow[14]
				: null)
				: null;

		Integer newIndex = (newRow != null) ? ((newRow[16] != null) ? (Integer) newRow[16]
				: null)
				: null;
		Integer oldIndex = (oldRow != null) ? ((oldRow[16] != null) ? (Integer) oldRow[16]
				: null)
				: null;

		Long newAccountsPayableId = null;
		@SuppressWarnings("unused")
		Long oldAccountsPayableId = null;

		Boolean isIncrease = Boolean.FALSE;

		newAccountsPayableId = getAccountsPayableId(stat, newTransactionId);
		oldAccountsPayableId = getAccountsPayableId(stat, oldTransactionId);

		// Condition for checking whether this Trigger call is for new Row
		// Insertion

		if (newRow != null && oldRow == null) {

			// if(newTransactionId != null){
			// doInCreatePart(stat, newPaymentAmount, newDiscountAccountId,
			// newCashDiscountAmount, newVendorId, newTransactionId,
			// newAccountsPayableId, isIncrease, newEnterBillId,
			// newAppliedCreditsAmount, newTransactionMakeDepositId);
			// }
		}
		// Condition for checking whether this Trigger call is for Row Updation

		else if (newRow != null && oldRow != null) {

			// This Trigger Updation call is for Updation TransactionID and
			// Index. Hence do the create part
			if (oldIndex == null && newIndex != null) {
				// doInCreatePart(stat, newPaymentAmount, newDiscountAccountId,
				// newCashDiscountAmount, newVendorId, newTransactionId,
				// newAccountsPayableId, isIncrease, newEnterBillId,
				// newAppliedCreditsAmount, newTransactionMakeDepositId);
			}
			// Actual Updation part of Trigger Call
			else if (!(oldIndex == null && newIndex != null)) {

				ResultSet r = stat.executeQuery(new StringBuilder().append(
						"SELECT M.IS_VOID FROM PAY_BILL M WHERE M.ID =")
						.append(newTransactionId).toString());
				r.next();
				// To check whether this updation is caused by Voiding any
				// PayBill
				// or not.
				if (r.getBoolean(1) && !newIsVoid) {

					// Deleting corresponding transaction rows from Account
					// Transaction table
					stat.execute(new StringBuilder().append(
							"DELETE FROM ACCOUNT_TRANSACTION WHERE T_ID =")
							.append(newTransactionMakeDepositId).toString());

					// Reverse Updating the Corresponding Vendor's Balance
					updateCorrespondingVendorBalance(stat, newPaymentAmount,
							newDiscountAccountId, newCashDiscountAmount,
							newVendorId, AccounterConstants.SYMBOL_PLUS);

					// Increase the Accounts Payable A/c By the amount of
					// Discount and payment

					StringBuilder sb2 = new StringBuilder();
					sb2
							.append(
									"UPDATE ACCOUNT A SET A.CURRENT_BALANCE = A.CURRENT_BALANCE + ")
							.append(newPaymentAmount);
					if (newDiscountAccountId != null) {
						sb2.append("+");
						sb2.append(newCashDiscountAmount);
					}
					sb2.append(", A.TOTAL_BALANCE = A.TOTAL_BALANCE + ")
							.append(newPaymentAmount);
					if (newDiscountAccountId != null) {
						sb2.append("+");
						sb2.append(newCashDiscountAmount);
					}
					sb2.append(" WHERE A.ID = ").append(newAccountsPayableId);
					stat.execute(sb2.toString());

					if (newDiscountAccountId != null) {

						// Reverse Updating the Discount Account with the
						// discount
						// Amount
						isIncrease = getIsIncreaseValueOfAccount(stat,
								newDiscountAccountId);

						if (isIncrease) {

							// Update the corresponding Cash Discount Account By
							// the amount of Discount
							updateCurrentAndTotalBalancesOfCorrespondingAccount(
									stat, newCashDiscountAmount,
									newDiscountAccountId,
									AccounterConstants.SYMBOL_MINUS);

						} else {

							// Update the corresponding Cash Discount Account By
							// the amount of Discount
							updateCurrentAndTotalBalancesOfCorrespondingAccount(
									stat, newCashDiscountAmount,
									newDiscountAccountId,
									AccounterConstants.SYMBOL_PLUS);

						}
					}

					// To update Credits and Paymets entries balances those
					// participated in the Applied credits of that Transaction
					// Pay Bill

					if ((oldAppliedCreditsAmount).doubleValue() > 0.0) {
						stat
								.execute(new StringBuilder()
										.append(
												"UPDATE CREDITS_AND_PAYMENTS CP SET CP.BALANCE = CP.BALANCE +  (SELECT TCP2.AMOUNT_TO_USE FROM TRANSACTION_CREDITS_AND_PAYMENTS TCP2 WHERE TCP2.CREDITS_AND_PAYMENTS_ID = CP.ID AND TCP2.TRANSACTION_PAYBILL_ID=")
										.append(oldTransactionPayBillId)
										.append(
												")  WHERE CP.ID IN (SELECT CP1.ID FROM  CREDITS_AND_PAYMENTS CP1 JOIN TRANSACTION_CREDITS_AND_PAYMENTS TCP ON CP1.ID = TCP.CREDITS_AND_PAYMENTS_ID WHERE TCP.TRANSACTION_PAYBILL_ID = ")
										.append(oldTransactionPayBillId)
										.append(")").toString());

						// To update the Amount_to_use to 0 in Transaction
						// Credits
						// and
						// Pyaments for the entries to which the
						// TransactionReceivePayment is match.

						stat
								.execute(new StringBuilder()
										.append(
												"UPDATE TRANSACTION_CREDITS_AND_PAYMENTS TCP SET TCP.AMOUNT_TO_USE = 0.0 WHERE TCP.TRANSACTION_PAYBILL_ID = ")
										.append(newTransactionPayBillId)
										.toString());

						// To Update the Status of the Customer Credit Memo
						ResultSet rs = stat
								.executeQuery(new StringBuilder()
										.append(
												"SELECT CP.BALANCE, CP.CREDIT_AMOUNT, T.T_TYPE FROM CREDITS_AND_PAYMENTS CP JOIN TRANSACTION_CREDITS_AND_PAYMENTS TCP ON CP.ID = TCP.CREDITS_AND_PAYMENTS_ID JOIN TRANSACTION T ON T.ID = CP.TRANSACTION_ID WHERE TCP.TRANSACTION_PAYBILL_ID = ")
										.append(newTransactionPayBillId)
										.toString());
						rs.next();
						Double balance = rs.getDouble(1);
						Double creditAmount = rs.getDouble(2);
						if (((Integer) rs.getInt(3))
								.equals(Transaction.TYPE_VENDOR_CREDIT_MEMO)) {
							if (balance > 0D && balance < creditAmount) {
								stat
										.execute(new StringBuilder()
												.append(
														"UPDATE TRANSACTION T SET T.STATUS = ")
												.append(
														Transaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED)
												.append(" WHERE T.ID = ")

												.append(newEnterBillId)
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
							+ (oldAppliedCreditsAmount).doubleValue()
							+ (oldPaymentAmount).doubleValue();

					if (newEnterBillId != null) {

						// Reverse Updating the Enter Bill payment amount and
						// the Balance due amount.
						updatePaymentsAndBalanceDueOfCorrespondingTable(stat,
								amount, newEnterBillId,
								AccounterConstants.SYMBOL_MINUS,
								AccounterConstants.TABLE_ENTER_BILL);

						// To Change the Status of the Enter Bill which we paid
						// in this Receive Payment

						ResultSet rs = stat
								.executeQuery(new StringBuilder()
										.append(
												"SELECT EB.BALANCE_DUE, EB.TOTAL FROM ENTER_BILL EB WEHRE EB.ID = ")
										.append(newEnterBillId).toString());
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
													newEnterBillId).toString());
						} else if (balanceDue == 0D) {
							stat
									.execute(new StringBuilder()
											.append(
													"UPDATE TRANSACTION T SET T.STATUS = ")
											.append(
													Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED)
											.append(" WHERE T.ID = ").append(
													newEnterBillId).toString());
						} else if (balanceDue.equals(total)) {
							stat
									.execute(new StringBuilder()
											.append(
													"UPDATE TRANSACTION T SET T.STATUS = ")
											.append(
													Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED)
											.append(" WHERE T.ID = ").append(
													newEnterBillId).toString());
						}

					} else {

						// Reverse Updating the Enter Bill payment amount and
						// the Balance due amount.
						updatePaymentsAndBalanceDueOfCorrespondingTable(
								stat,
								amount,
								newTransactionMakeDepositId,
								AccounterConstants.SYMBOL_MINUS,
								AccounterConstants.TABLE_TRANSACTION_MAKE_DEPOSIT);

					}
					// To update Applycredits and payment of the corresponding
					// TransactionReceivepayment to zero.

					stat
							.execute(new StringBuilder()
									.append(
											"UPDATE TRANSACTION_PAYBILL TRP SET TRP.APPLIED_CREDITS = 0.0, TRP.PAYMENT = 0.0,TRP.CASH_DISCOUNT = 0.0,TRP.IS_VOID = 'TRUE' WHERE TRP.ID=")
									.append(newTransactionPayBillId).toString());

				}

			}

		}

	}

	private Long getAccountsPayableId(Statement stat, Long transactionId)
			throws SQLException {

		Long id = null;
		ResultSet rs = stat
				.executeQuery(new StringBuilder()
						.append(
								"SELECT PB.ACCOUNTS_PAYABLE_ID FROM PAY_BILL PB WHERE PB.ID = ")
						.append(transactionId).toString());
		if (rs.next()) {
			id = rs.getLong(1);
		}
		return id;
	}

	private void updatePaymentsAndBalanceDueOfCorrespondingTable(
			Statement stat, Double amount,
			Long enterBillOrTransactionMakeDepositId, String symbol,
			String table) throws SQLException {

		String sign = "";
		if (symbol.equals(AccounterConstants.SYMBOL_PLUS))
			sign = AccounterConstants.SYMBOL_MINUS;
		else
			sign = AccounterConstants.SYMBOL_PLUS;
		stat.execute(new StringBuilder().append("UPDATE ").append(table)
				.append(" EB SET EB.PAYMENTS = EB.PAYMENTS ").append(symbol)
				.append(" ").append(amount).append(
						",EB.BALANCE_DUE = EB.BALANCE_DUE ").append(sign)
				.append(" ").append(amount).append(" WHERE EB.ID = ").append(
						enterBillOrTransactionMakeDepositId).toString());
	}

	private void updateCurrentAndTotalBalancesOfCorrespondingAccount(
			Statement stat, Double cashDiscountAmount, Long accountsPayableId,
			String symbol) throws SQLException {

		StringBuilder sb2 = new StringBuilder();
		sb2.append(
				"UPDATE ACCOUNT A SET A.CURRENT_BALANCE = A.CURRENT_BALANCE ")
				.append(symbol).append(" ").append(cashDiscountAmount).append(
						", A.TOTAL_BALANCE = A.TOTAL_BALANCE ").append(symbol)
				.append(" ").append(cashDiscountAmount)
				.append(" WHERE A.ID = ").append(accountsPayableId);
		stat.execute(sb2.toString());
	}

	private void updateCorrespondingVendorBalance(Statement stat,
			Double paymentAmount, Long discountAccountId,
			Double cashDiscountAmount, Long vendorId, String symbol)
			throws SQLException {

		StringBuilder sb = new StringBuilder(
				"UPDATE VENDOR V SET V.BALANCE = V.BALANCE ").append(symbol)
				.append(" ");
		sb.append(paymentAmount);
		if (discountAccountId != null) {
			sb.append(symbol);
			sb.append(cashDiscountAmount);
		}
		sb.append(" WHERE V.ID = ").append(vendorId);
		stat.execute(sb.toString());
	}

	private Boolean getIsIncreaseValueOfAccount(Statement stat,
			Long discountAccountId) throws SQLException {
		Boolean isIncrease = Boolean.FALSE;
		// Updating the Discount Account with the discount Amount
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