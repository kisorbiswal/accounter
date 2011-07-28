package com.vimukti.accounter.core.trigger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.h2.api.Trigger;

import com.vimukti.accounter.core.AccounterConstants;
import com.vimukti.accounter.core.TransactionMakeDeposit;

/**
 * 
 * @author Devesh Satwani
 * 
 */
public class TriggerTransactionMakeDeposit implements Trigger {

	@Override
	public void fire(Connection conn, Object[] oldRow, Object[] newRow)
			throws SQLException {

		// Required variables declaration and initialization

		Statement stat = conn.createStatement();

		Long newTransactionMakeDepositId = (newRow != null) ? ((newRow[0] != null) ? (Long) newRow[0]
				: null)
				: null;
		Long oldTransactionMakeDepositId = (oldRow != null) ? ((oldRow[0] != null) ? (Long) oldRow[0]
				: null)
				: null;

		
		Long newPaymentMethodId = (newRow != null) ? ((newRow[4] != null) ? (Long) newRow[4]
				: null)
				: null;
		
		Long oldPaymentMethodId = (oldRow != null) ? ((oldRow[4] != null) ? (Long) oldRow[4]
				: null)
				: null;

		Integer newEntryType = (newRow != null) ? ((newRow[5] != null) ? (Integer) newRow[5]
				: null)
				: null;
		
		Integer oldEntryType = (oldRow != null) ? ((oldRow[5] != null) ? (Integer) oldRow[5]
				: null)
				: null;

		Long newAccountId = (newRow != null) ? ((newRow[6] != null) ? (Long) newRow[6]
				: null)
				: null;
		
		Long oldAccountId = (oldRow != null) ? ((oldRow[6] != null) ? (Long) oldRow[6]
				: null)
				: null;

		Long newVendorId = (newRow != null) ? ((newRow[7] != null) ? (Long) newRow[7]
				: null)
				: null;
		
		Long oldVendorId = (oldRow != null) ? ((oldRow[7] != null) ? (Long) oldRow[7]
				: null)
				: null;

		Long newCustomerId = (newRow != null) ? ((newRow[8] != null) ? (Long) newRow[8]
				: null)
				: null;
		
		Long oldCustomerId = (oldRow != null) ? ((oldRow[8] != null) ? (Long) oldRow[8]
				: null)
				: null;

		Double newAmount = (newRow != null) ? ((newRow[10] != null) ? (Double) newRow[10]
				: null)
				: null;
		
		Double oldAmount = (oldRow != null) ? ((oldRow[10] != null) ? (Double) oldRow[10]
				: null)
				: null;

		Boolean newIsNewEntry = (newRow != null) ? ((newRow[11] != null) ? (Boolean) newRow[11]
				: null)
				: null;
		
		Boolean oldIsNewEntry = (oldRow != null) ? ((oldRow[11] != null) ? (Boolean) oldRow[11]
				: null)
				: null;

		
		Long newCashAccountId = (newRow != null) ? ((newRow[12] != null) ? (Long) newRow[12]
				: null)
				: null;
		
		Long oldCashDiscountId = (oldRow != null) ? ((oldRow[12] != null) ? (Long) oldRow[12]
				: null)
				: null;

		Long newTransactionId = (newRow != null) ? ((newRow[13] != null) ? (Long) newRow[13]
				: null)
				: null;
		Long oldTransactionId = (oldRow != null) ? ((oldRow[13] != null) ? (Long) oldRow[13]
				: null)
				: null;

		Long newAccountsReceivableId = (newRow != null) ? ((newRow[16] != null) ? (Long) newRow[16]
				: null)
				: null;
		
		Long oldAccountsReceivableId = (oldRow != null) ? ((oldRow[16] != null) ? (Long) oldRow[16]
				: null)
				: null;

		Long newAccountsPayableId = (newRow != null) ? ((newRow[17] != null) ? (Long) newRow[17]
				: null)
				: null;
		
		Long oldAccountsPayableId = (oldRow != null) ? ((oldRow[17] != null) ? (Long) oldRow[17]
				: null)
				: null;

		
		Long newDepositedTransactionId = (newRow != null) ? ((newRow[18] != null) ? (Long) newRow[18]
				: null)
				: null;
		
		Long oldDepositedTransactionId = (oldRow != null) ? ((oldRow[18] != null) ? (Long) oldRow[18]
				: null)
				: null;

		Integer newIndex = (newRow != null) ? ((newRow[20] != null) ? (Integer) newRow[20]
				: null)
				: null;
		Integer oldIndex = (oldRow != null) ? ((oldRow[20] != null) ? (Integer) oldRow[20]
				: null)
				: null;

		int count = 0;
		
		Long depositNumber = null;

		// Condition for checking whether this Trigger call is for new Row
		// Insertion
		if (newRow != null && oldRow == null) {
			// if(newTransactionId != null){
			// doInCreatePart(newIsNewEntry, stat, newAmount, newCashAccountId,
			// newTransactionId, newEntryType, newDepositedTransactionId,
			// newAccountId, newPaymentMethodId, newAccountsReceivableId,
			// newCustomerId, depositNumber, newAccountsPayableId, newVendorId);
			// }
		}
		// Condition for checking whether this Trigger call is for Row Updation
		else if (newRow != null && oldRow != null) {

			// This Trigger Updation call is for Updation TransactionID and
			// Index. Hence do the create part
			if (oldIndex == null && newIndex != null) {
				// doInCreatePart(newIsNewEntry, stat, newAmount,
				// newCashAccountId, newTransactionId, newEntryType,
				// newDepositedTransactionId, newAccountId, newPaymentMethodId,
				// newAccountsReceivableId, newCustomerId, depositNumber,
				// newAccountsPayableId, newVendorId);
			}
			// Actual Updation part of Trigger Call
			else if (!(oldIndex == null && newIndex != null)) {
				Boolean isMakeDepositVoided = Boolean.FALSE;
				// To check whether this updation is caused by Voiding any Make
				// Deposit
				// or not.
				ResultSet r = stat
						.executeQuery(new StringBuilder()
								.append("SELECT M.IS_VOID FROM MAKE_DEPOSIT M WHERE M.ID =")
								.append(newTransactionId).toString());
				if (r.next()) {
					isMakeDepositVoided = r.getBoolean(1);
				}
				if (isMakeDepositVoided) {

					// Deleting corresponding transaction rows from Account
					// Transaction table
					stat.execute(new StringBuilder()
							.append("DELETE FROM ACCOUNT_TRANSACTION WHERE T_ID =")
							.append(newTransactionId).toString());

					// check whether the row is New Entry or not
					if (!newIsNewEntry) {

						/*
						 * Here the amount is negative for the Financial Account
						 * Entries with transfer of fund from Undeposited Funds
						 * A/C(Cash Account) to Other A/c and for Vendor
						 * Entries.
						 */

						// Reverse Update the Undeposited Funds Account of the
						// Entry
						updateCurrentAndTotalBalancesOfUnDepositedFundsAccount(
								stat, newAmount, AccounterConstants.SYMBOL_PLUS);

					} else {

						int entryType = (newEntryType).intValue();
						switch (entryType) {

						case TransactionMakeDeposit.TYPE_FINANCIAL_ACCOUNT:

							Boolean isIncrease = getIsIncreaseValue(stat,
									newAccountId);

							if (isIncrease) {
								// updating the current and total balances of
								// the corresponding Account

								updateCurrentAndTotalBalancesOfCorrespondingAccount(
										stat, newAmount, newAccountId,
										AccounterConstants.SYMBOL_PLUS);

							} else {
								// updating the current and total balances of
								// the corresponding Account

								updateCurrentAndTotalBalancesOfCorrespondingAccount(
										stat, newAmount, newAccountId,
										AccounterConstants.SYMBOL_MINUS);

							}

							break;

						// Switch to this case if the Transaction Make deposit
						// is of type Customer
						case TransactionMakeDeposit.TYPE_CUSTOMER:

							// Reverse updating the current and total balances
							// of the Accounts receivable Account

							updateCurrentAndTotalBalancesOfCorrespondingAccount(
									stat, newAmount, newAccountsReceivableId,
									AccounterConstants.SYMBOL_PLUS);

							// Updating the customer balance.
							updateCustomerBalance(stat, newAmount,
									newCustomerId,
									AccounterConstants.SYMBOL_PLUS);

							// Retrieving the count of the Transactions which
							// are being paid with this Transaction Make
							// Deposit, as a Credits and payments.
							depositNumber = getTransactionNumber(stat,
									oldTransactionId);
							ResultSet rs2 = stat
									.executeQuery(new StringBuilder()
											.append("SELECT COUNT(*) FROM TRANSACTION_CREDITS_AND_PAYMENTS TCP WHERE TCP.CREDITS_AND_PAYMENTS_ID = SELECT CP.ID FROM CREDTIS_AND_PAYMENTS WHERE CP.TRANSACTION_ID = ")
											.append(newTransactionMakeDepositId)
											.append(" AND TCP.AMOUNT_TO_USE > 0.0")
											.toString());

							if (rs2.next()) {
								count = rs2.getInt(1);
							}

							if (count > 0) {

								// Updating the Balance due of the invoices by
								// the amount used from credits and payments of
								// this Transacton Make Deposit.
								stat.execute(new StringBuilder()
										.append("UPDATE INVOICE I SET I.BALANCE_DUE = I.BALANCE_DUE + (SELECT SUM(TCP1.AMOUNT_TO_USE) FROM TRANSACTION_CREDITS_AND_PAYMENTS TCP1 JOIN TRANSACTION_RECEIVE_PAYMENT TRP1 ON TCP1.TRANSACTION_RECEIVE_PAYMENT_ID = TRP1.ID JOIN INVOICE I1 ON TRP1.INVOICE_ID = I1.ID WHERE I1.ID = I.ID AND TCP1.CREDITS_AND_PAYMENTS_ID = SELECT CP.ID FROM CREDTIS_AND_PAYMENTS WHERE CP.TRANSACTION_ID = ")
										.append(newTransactionMakeDepositId)
										.append("),I.PAYMENTS = I.PAYMENTS - (SELECT SUM(TCP1.AMOUNT_TO_USE) FROM TRANSACTION_CREDITS_AND_PAYMENTS TCP1 JOIN TRANSACTION_RECEIVE_PAYMENT TRP1 ON TCP1.TRANSACTION_RECEIVE_PAYMENT_ID = TRP1.ID JOIN INVOICE I1 ON TRP1.INVOICE_ID = I1.ID WHERE I1.ID = I.ID AND TCP1.CREDITS_AND_PAYMENTS_ID = SELECT CP.ID FROM CREDTIS_AND_PAYMENTS WHERE CP.TRANSACTION_ID = ")
										.append(newTransactionMakeDepositId)
										.append(") WHERE I.ID IN (SELECT I.ID FROM INVOICE I JOIN TRANSACTION_RECEIVE_PAYMENT TRP ON I.ID = TRP.INVOICE_ID JOIN TRANSACTION_CREDITS_AND_PAYMENTS TCP ON TRP.ID = TCP.TRANSACTION_RECEIVE_PAYMENT_ID WHERE TCP.CREDITS_AND_PAYMENTS_ID = SELECT CP.ID FROM CREDTIS_AND_PAYMENTS WHERE CP.TRANSACTION_ID = ")
										.append(newTransactionMakeDepositId)
										.append(")").toString());
								// Updating the Balance due of the Customer
								// Refund by the amount used from credits and
								// payments of this Transacton Make Deposit.
								stat.execute(new StringBuilder()
										.append("UPDATE CUSTOMER_REFUND CR SET CR.BALANCE_DUE = CR.BALANCE_DUE + (SELECT SUM(TCP1.AMOUNT_TO_USE) FROM TRANSACTION_CREDITS_AND_PAYMENTS TCP1 JOIN TRANSACTION_RECEIVE_PAYMENT TRP1 ON TCP1.TRANSACTION_RECEIVE_PAYMENT_ID = TRP1.ID JOIN CUSTOMER_REFUND CR1 ON TRP1.CUSTOMER_REFUND_ID = CR1.ID WHERE CR1.ID = CR.ID AND TCP1.CREDITS_AND_PAYMENTS_ID = SELECT CP.ID FROM CREDTIS_AND_PAYMENTS WHERE CP.TRANSACTION_ID = ")
										.append(newTransactionMakeDepositId)
										.append("),CR.PAYMENTS = CR.PAYMENTS - (SELECT SUM(TCP1.AMOUNT_TO_USE) FROM TRANSACTION_CREDITS_AND_PAYMENTS TCP1 JOIN TRANSACTION_RECEIVE_PAYMENT TRP1 ON TCP1.TRANSACTION_RECEIVE_PAYMENT_ID = TRP1.ID JOIN CUSTOMER_REFUND CR1 ON TRP1.CUSTOMER_REFUND_ID = CR1.ID WHERE CR1.ID = CR.ID AND TCP1.CREDITS_AND_PAYMENTS_ID = SELECT CP.ID FROM CREDTIS_AND_PAYMENTS WHERE CP.TRANSACTION_ID = ")
										.append(newTransactionMakeDepositId)
										.append(") WHERE CR.ID IN (SELECT CR.ID FROM CUSTOMER_REFUND CR JOIN TRANSACTION_RECEIVE_PAYMENT TRP ON CR.ID = TRP.CUSTOMER_REFUND_ID JOIN TRANSACTION_CREDITS_AND_PAYMENTS TCP ON TRP.ID = TCP.TRANSACTION_RECEIVE_PAYMENT_ID WHERE TCP.CREDITS_AND_PAYMENTS_ID = SELECT CP.ID FROM CREDTIS_AND_PAYMENTS WHERE CP.TRANSACTION_ID = ")
										.append(newTransactionMakeDepositId)
										.append(")").toString());

								// Decrease the applied credits amount of the
								// Transacton receive payments by the amount
								// used from the credtis and payments
								stat.execute(new StringBuilder()
										.append("UPDATE TRANSACTION_RECEIVE_PAYMENT TRP SET TRP.APPLIED_CREDITS = TRP.APPLIED_CREDITS - (SELECT TCP1.AMOUNT_TO_USE FROM TRANSACTION_CREDITS_AND_PAYMENTS TCP1 JOIN TRANSACTION_RECEIVE_PAYMENT TRP1 ON TCP1.TRANSACTION_RECEIVE_PAYMENT_ID  = TRP1.ID WHERE TRP1.ID = TRP.ID AND TCP1.CREDITS_AND_PAYMENTS_ID = SELECT CP.ID FROM CREDTIS_AND_PAYMENTS WHERE CP.TRANSACTION_ID = ")
										.append(newTransactionMakeDepositId)
										.append(") WHERE TRP.ID IN (SELECT TRP.ID FROM TRANSACTION_RECEIVE_PAYMENT TRP JOIN TRANSACTION_CREDITS_AND_PAYMENTS TCP ON TRP.ID = TCP.TRANSACTION_RECEIVE_PAYMENT_ID WHERE TCP.CREDITS_AND_PAYMENTS_ID = SELECT CP.ID FROM CREDTIS_AND_PAYMENTS WHERE CP.TRANSACTION_ID = ")
										.append(newTransactionMakeDepositId)
										.append(")").toString());

								// Update the amount to use of Transaction
								// credits and payments to make it as zero.
								stat.execute(new StringBuilder()
										.append("UPDATE TRANSACTION_CREDITS_AND_PAYMENTS TCP SET TCP.AMOUNT_TO_USE = 0.0 WHERE TCP.CREDITS_AND_PAYMENTS_ID = SELECT CP.ID FROM CREDTIS_AND_PAYMENTS WHERE CP.TRANSACTION_ID = ")
										.append(newTransactionMakeDepositId)
										.toString());

							}

							// Deleting the corresponding entries of this
							// Transaction make deposit in the
							// CreditsAndPayments table.
							stat.execute(new StringBuilder()
									.append("DELETE FROM CREDITS_AND_PAYMENTS CP WHERE CP.ID = SELECT CP1.ID FROM CREDITS_AND_PAYMENTS CP1 WHERE CP1.TRANSACTION_ID = ")
									.append(newTransactionMakeDepositId)
									.toString());

							break;

						// Switch to this case if the Transaction Make deposit
						// is of type Vendor
						case TransactionMakeDeposit.TYPE_VENDOR:

							// Updating the Corresponding Accounts

							// Reverse Updating the current and total balances
							// of Accounts Payable account.
							updateCurrentAndTotalBalancesOfCorrespondingAccount(
									stat, newAmount, newAccountsPayableId,
									AccounterConstants.SYMBOL_MINUS);

							// Update Vendor Balance
							updateVendorBalance(stat, newAmount, newVendorId,
									AccounterConstants.SYMBOL_MINUS);

							// Checking the count of the Transaction PayBills in
							// which this Transactin Make deposit being paid.

							ResultSet rs = stat
									.executeQuery(new StringBuilder()
											.append("SELECT COUNT(*) FROM TRANSACTION_PAYBILL TRP WHERE TRP.TRASACTION_MAKE_DEPOSIT_ID =  ")
											.append(oldTransactionMakeDepositId)
											.toString());

							if (rs.next()) {
								count = rs.getInt(1);
							}

							// check do we have any receive payment for this
							// invoice

							if (count > 0) {

								// increase the PayBill's unused amount by the
								// payments of all the corresponding Transaction
								// Pay bills in which the Transaction Make
								// Deposit is getting paid.
								stat.execute(new StringBuilder()
										.append("UPDATE PAY_BILL PB SET PB.UNUSED_AMOUNT = PB.UNUSED_AMOUNT + (SELECT TRP.PAYMENT FROM TRANSACTION_PAYBILL TRP  WHERE TRP.TRANSACTION_ID = PB.ID AND TRP.TRASACTION_MAKE_DEPOSIT_ID= ")
										.append(oldTransactionMakeDepositId)
										.append(") WHERE PB.ID IN (SELECT TRP1.TRANSACTION_ID FROM TRANSACTION_PAYBILL TRP1 WHERE TRP1.TRASACTION_MAKE_DEPOSIT_ID = ")
										.append(oldTransactionMakeDepositId)
										.append(")").toString());

								// Increasing the Balance of the Credits and
								// payments with the applied credits amount of
								// the Transaction PayBills in which this
								// Transaction make deposit is getting paid.
								stat.execute(new StringBuilder()
										.append("UPDATE CREDITS_AND_PAYMENTS CP SET CP.BALANCE = CP.BALANCE + (CASE WHEN (SELECT TRP1.APPLIED_CREDITS FROM TRANSACTION_PAYBILL TRP1 JOIN TRANSACTION_CREDITS_AND_PAYMENTS TCP1 ON TCP1.TRANSACTION_PAYBILL_ID = TRP1.ID JOIN CREDITS_AND_PAYMENTS CP1 ON TCP1.CREDITS_AND_PAYMENTS_ID = CP1.ID WHERE CP1.ID = CP.ID AND TRP1.TRASACTION_MAKE_DEPOSIT_ID = ")
										.append(oldTransactionMakeDepositId)
										.append(")  > 0 THEN SELECT  TCP2.AMOUNT_TO_USE  FROM TRANSACTION_CREDITS_AND_PAYMENTS TCP2  JOIN CREDITS_AND_PAYMENTS CP2 ON TCP2.CREDITS_AND_PAYMENTS_ID = CP2.ID JOIN TRANSACTION_PAYBILL TRP2 ON TRP2.ID = TCP2.TRANSACTION_PAYBILL_ID WHERE CP2.ID = CP.ID AND TRP2.TRASACTION_MAKE_DEPOSIT_ID =")
										.append(oldTransactionMakeDepositId)
										.append(" ELSE 0 END) WHERE CP.ID IN (SELECT CP1.ID FROM CREDITS_AND_PAYMENTS CP1 JOIN TRANSACTION_CREDITS_AND_PAYMENTS TCP ON CP1.ID = TCP.CREDITS_AND_PAYMENTS_ID JOIN TRANSACTION_PAYBILL TRP1 ON TCP.TRANSACTION_PAYBILL_ID = TRP1.ID WHERE TRP1.TRASACTION_MAKE_DEPOSIT_ID = ")
										.append(oldTransactionMakeDepositId)
										.append(")").toString());

								// Making the Transacton paybill payment as zero
								stat.execute(new StringBuilder()
										.append("UPDATE TRANSACTION_PAYBILL TRP SET TRP.PAYMENT = 0.0,TRP.APPLIED_CREDITS = 0.0 WHERE TRP.TRASACTION_MAKE_DEPOSIT_ID = ")
										.append(oldTransactionMakeDepositId)
										.toString());

								// To make the transaction credits and payments
								// amount to use as zero.
								stat.execute(new StringBuilder()
										.append("UPDATE TRANSACTION_CREDITS_AND_PAYMENTS TCP SET TCP.AMOUNT_TO_USE = 0.0 WHERE TCP.ID IN (SELECT TCP.ID FROM TRANSACTION_CREDITS_AND_PAYMENTS TCP JOIN TRANSACTION_PAYBILL TRP ON TCP.TRANSACTION_PAYBILL_ID = TRP.ID WHERE TRP.TRASACTION_MAKE_DEPOSIT_ID = ")
										.append(oldTransactionMakeDepositId)
										.append(")").toString());

							}

							// Update Transaction Make deposit to make it's
							// payment is equal to the amount and the balance
							// due is zero.
							stat.execute(new StringBuilder()
									.append("UPDATE TRASACTION_MAKE_DEPOSIT TMD SET TMD.PAYMENTS = TMD.AMOUNT,TMD.BALANCE_DUE = 0.0 WHERE TMD.ID =")
									.append(oldTransactionMakeDepositId)
									.toString());

							break;

						}

					}

				}
			}

		}

	}

	private void updateCurrentAndTotalBalancesOfUnDepositedFundsAccount(
			Statement stat, Double amount, String symbol) throws SQLException {

		stat.execute(new StringBuilder()
				.append("UPDATE ACCOUNT A SET A.CURRENT_BALANCE = A.CURRENT_BALANCE ")
				.append(symbol).append(" ").append(amount)
				.append(", A.TOTAL_BALANCE = A.TOTAL_BALANCE ").append(symbol)
				.append(" ").append(amount).append(" WHERE A.NAME = '")
				.append(AccounterConstants.UN_DEPOSITED_FUNDS).append("'")
				.toString());
	}

	private void updateVendorBalance(Statement stat, Double amount,
			Long vendorId, String symbol) throws SQLException {

		stat.execute(new StringBuilder()
				.append("UPDATE VENDOR V SET V.BALANCE = V.BALANCE ")
				.append(symbol).append(" ").append(amount)
				.append(" WHERE V.ID = ").append(vendorId).toString());

	}

	private void updateCustomerBalance(Statement stat, Double amount,
			Long customerId, String symbol) throws SQLException {

		stat.execute(new StringBuilder()
				.append("UPDATE CUSTOMER C SET C.BALANCE = C.BALANCE ")
				.append(symbol).append(" ").append(amount)
				.append(" WHERE C.ID = ").append(customerId).toString());
	}

	private Long getTransactionNumber(Statement stat, Long transactionId)
			throws SQLException {
		Long depositNumber = null;
		ResultSet r1 = stat.executeQuery(new StringBuilder()
				.append("SELECT T.NUMBER FROM TRANSACTION T WHERE T.ID =")
				.append(transactionId).toString());
		if (r1.next()) {
			depositNumber = r1.getLong(1);
		}
		return depositNumber;
	}

	private Boolean getIsIncreaseValue(Statement stat, Long accountId)
			throws SQLException {
		Boolean isIncrease = Boolean.FALSE;
		ResultSet rs1 = stat.executeQuery(new StringBuilder()
				.append("SELECT A.IS_INCREASE FROM ACCOUNT A WHERE A.ID =")
				.append(accountId).toString());
		if (rs1.next()) {
			isIncrease = rs1.getBoolean(1);
		}
		return isIncrease;
	}

	private void updateCurrentAndTotalBalancesOfCorrespondingAccount(
			Statement stat, Double amount, Long accountId, String symbol)
			throws SQLException {

		stat.execute(new StringBuilder()
				.append("UPDATE ACCOUNT A SET A.CURRENT_BALANCE = A.CURRENT_BALANCE ")
				.append(symbol).append(" ").append(amount)
				.append(", A.TOTAL_BALANCE = A.TOTAL_BALANCE ").append(symbol)
				.append(" ").append(amount).append(" WHERE A.ID = ")
				.append(accountId).toString());
	}

	@Override
	public void init(Connection arg0, String arg1, String arg2, String arg3,
			boolean arg4, int arg5) throws SQLException {
		// currently not using anywhere in the project.

	}

}
