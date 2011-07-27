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
public class TriggerInvoice implements Trigger {

	@Override
	public void fire(Connection conn, Object[] oldRow, Object[] newRow)
			throws SQLException {

		// Required variables declaration and initialization

		Statement stat = conn.createStatement();

		Long newInvoiceId = (newRow != null) ? ((newRow[0] != null) ? (Long) newRow[0]
				: null)
				: null;
		Long oldInvoiceId = (oldRow != null) ? ((oldRow[0] != null) ? (Long) oldRow[0]
				: null)
				: null;
		@SuppressWarnings("unused")
		Long newCustomerId = (newRow != null) ? ((newRow[1] != null) ? (Long) newRow[1]
				: null)
				: null;
		Long oldCustomerId = (oldRow != null) ? ((oldRow[1] != null) ? (Long) oldRow[1]
				: null)
				: null;
		@SuppressWarnings("unused")
		Double newAllLineTotal = (newRow != null) ? ((newRow[12] != null) ? (Double) newRow[12]
				: null)
				: null;
		@SuppressWarnings("unused")
		Double oldAllLineTotal = (oldRow != null) ? ((oldRow[12] != null) ? (Double) oldRow[12]
				: null)
				: null;
		Double newAllTaxableLineTotal = (newRow != null) ? ((newRow[13] != null) ? (Double) newRow[13]
				: null)
				: null;
		@SuppressWarnings("unused")
		Double oldAllTaxableLineTotal = (oldRow != null) ? ((oldRow[13] != null) ? (Double) oldRow[13]
				: null)
				: null;
		Long newTaxGroupId = (newRow != null) ? ((newRow[17] != null) ? (Long) newRow[17]
				: null)
				: null;
		@SuppressWarnings("unused")
		Long oldTaxGroupId = (oldRow != null) ? ((oldRow[17] != null) ? (Long) oldRow[17]
				: null)
				: null;
		Double newTotal = (newRow != null) ? ((newRow[20] != null) ? (Double) newRow[20]
				: null)
				: null;
		Double oldTotal = (oldRow != null) ? ((oldRow[20] != null) ? (Double) oldRow[20]
				: null)
				: null;
		Boolean newIsVoid = (newRow != null) ? ((newRow[23] != null) ? (Boolean) newRow[23]
				: null)
				: null;
		Boolean oldIsVoid = (oldRow != null) ? ((oldRow[23] != null) ? (Boolean) oldRow[23]
				: null)
				: null;
		Long newAccountsReceivableId = (newRow != null) ? ((newRow[25] != null) ? (Long) newRow[25]
				: null)
				: null;
		@SuppressWarnings("unused")
		Long oldAccountsReceivableId = (oldRow != null) ? ((oldRow[25] != null) ? (Long) oldRow[25]
				: null)
				: null;
		Long newEstimateId = (newRow != null) ? ((newRow[27] != null) ? (Long) newRow[27]
				: null)
				: null;
		Long oldEstimateId = (oldRow != null) ? ((oldRow[27] != null) ? (Long) oldRow[27]
				: null)
				: null;

		// Condition for checking whether this Trigger call is for new Row
		// Insertion

		if (newRow != null && oldRow == null) {
		}
		// Condition for checking whether this Trigger call is for Row Updation
		else if (newRow != null && oldRow != null) {

			// To check whether this updation is caused by Voiding the Invoice
			// or not.
			if (oldIsVoid != newIsVoid) {

				// Deleting corresponding transaction rows from Account
				// Transaction table
				stat.execute(new StringBuilder()
						.append("DELETE FROM ACCOUNT_TRANSACTION WHERE T_ID =")
						.append(newInvoiceId).toString());

				// Reverse Updating the AccountsReceivable account balance with
				// Invoice amount.

				updateAccountsReceivableAccount(stat, newTotal,
						newAccountsReceivableId,
						AccounterConstants.SYMBOL_MINUS);

				// Reverse Updating the Corresponding Customer Balance with
				// Invoice amount.

				updateCustomerBalance(stat, oldTotal, oldCustomerId,
						AccounterConstants.SYMBOL_MINUS);

				// Reverse Updating the Corresponding TaxGroup's TaxCode's
				// TaxAgency Account Balance with allTaxable Line total if we
				// select any TaxGroup.

				if (newTaxGroupId != null
						&& !(newAllTaxableLineTotal).equals(0.0)) {

					// Updating the TaxAgency Accounts with the Sales Tax
					// amount.
					updateCorrespondingTaxAgencyAccountsWithSalesTax(stat,
							newAllTaxableLineTotal, newInvoiceId,
							AccounterConstants.SYMBOL_MINUS);

					// Reverse updating the balance of corresponding tax
					// agency

					updateCorrespondingTaxAgencyBalancesWithSalesTax(stat,
							newAllTaxableLineTotal, newInvoiceId,
							AccounterConstants.SYMBOL_MINUS);

					// Deleting corresponding transaction rows from TaxRate
					// Calculation table
					stat.execute(new StringBuilder()
							.append("DELETE FROM TAX_RATE_CALCULATION WHERE TRANSACTION_ID =")
							.append(newInvoiceId).toString());

				}

				int count = 0;
				ResultSet r = stat
						.executeQuery(new StringBuilder()
								.append("SELECT COUNT(*) FROM TRANSACTION_RECEIVE_PAYMENT TRP WHERE TRP.INVOICE_ID =  ")
								.append(oldInvoiceId).toString());

				if (r.next()) {
					count = r.getInt(1);
				}

				// check do we have any receive payment for this invoice

				if (count > 0) {

					// Increasing the Unused payment of the ReceivePayment by
					// the Invoice amount in which this Invoice is being paid.
					stat.execute(new StringBuilder()
							.append("UPDATE RECEIVE_PAYMENT RP SET RP.UNUSED_PAYMENTS = RP.UNUSED_PAYMENTS + (SELECT TRP.PAYMENT FROM TRANSACTION_RECEIVE_PAYMENT TRP  WHERE TRP.TRANSACTION_ID = RP.ID AND TRP.INVOICE_ID= ")
							.append(oldInvoiceId)
							.append(") WHERE RP.ID IN (SELECT TRP1.TRANSACTION_ID FROM TRANSACTION_RECEIVE_PAYMENT TRP1 WHERE TRP1.INVOICE_ID = ")
							.append(oldInvoiceId).append(")").toString());

					// Increasing the Balance if the CreditsAndPayments which we
					// have used in this Invoice
					stat.execute(new StringBuilder()
							.append("UPDATE CREDITS_AND_PAYMENTS CP SET CP.BALANCE = CP.BALANCE + (CASE WHEN (SELECT SUM(TRP1.APPLIED_CREDITS) FROM TRANSACTION_RECEIVE_PAYMENT TRP1 JOIN TRANSACTION_CREDITS_AND_PAYMENTS TCP1 ON TCP1.TRANSACTION_RECEIVE_PAYMENT_ID = TRP1.ID JOIN CREDITS_AND_PAYMENTS CP1 ON TCP1.CREDITS_AND_PAYMENTS_ID= CP1.ID WHERE CP1.ID = CP.ID AND TRP1.INVOICE_ID = ")
							.append(oldInvoiceId)
							.append(")  > 0 THEN SELECT  SUM(TCP2.AMOUNT_TO_USE)  FROM TRANSACTION_CREDITS_AND_PAYMENTS TCP2  JOIN CREDITS_AND_PAYMENTS CP2 ON TCP2.CREDITS_AND_PAYMENTS_ID = CP2.ID JOIN TRANSACTION_RECEIVE_PAYMENT TRP2 ON TRP2.ID = TCP2.TRANSACTION_RECEIVE_PAYMENT_ID WHERE CP2.ID = CP.ID AND TRP2.INVOICE_ID =")
							.append(oldInvoiceId)
							.append(" ELSE 0 END) WHERE CP.ID IN (SELECT CP1.ID FROM CREDITS_AND_PAYMENTS CP1 JOIN TRANSACTION_CREDITS_AND_PAYMENTS TCP ON CP1.ID = TCP.CREDITS_AND_PAYMENTS_ID JOIN TRANSACTION_RECEIVE_PAYMENT TRP1 ON TCP.TRANSACTION_RECEIVE_PAYMENT_ID = TRP1.ID WHERE TRP1.INVOICE_ID = ")
							.append(oldInvoiceId).append(")").toString());

					// Update Transactin ReceivePayment corresponding to this
					// Invoice to make the payments amount and the applied
					// credits amount as zero.
					stat.execute(new StringBuilder()
							.append("UPDATE TRANSACTION_RECEIVE_PAYMENT TRP SET TRP.PAYMENT = 0.0,TRP.APPLIED_CREDITS = 0.0 WHERE TRP.INVOICE_ID = ")
							.append(oldInvoiceId).toString());

					// Updating the Transaction Credits and Payments
					// corresponding to this Invoice to make the amount to use
					// as zero.
					stat.execute(new StringBuilder()
							.append("UPDATE TRANSACTION_CREDITS_AND_PAYMENTS TCP SET TCP.AMOUNT_TO_USE = 0.0 WHERE TCP.ID IN (SELECT TCP.ID FROM TRANSACTION_CREDITS_AND_PAYMENTS TCP JOIN TRANSACTION_RECEIVE_PAYMENT TRP ON TCP.TRANSACTION_RECEIVE_PAYMENT_ID = TRP.ID WHERE TRP.INVOICE_ID = ")
							.append(oldInvoiceId).append(")").toString());

					// To Update the Status of the ReveivePayments as Un
					// applied, in which this Invoice is participated.

					stat.execute(new StringBuilder()
							.append("UPDATE TRANSACTION T SET T.STATUS = ")
							.append(Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED)
							.append(" WHERE T.ID IN (SELECT TRP1.TRANSACTION_ID FROM TRANSACTION_RECEIVE_PAYMENT TRP1 WHERE TRP1.INVOICE_ID = ")
							.append(oldInvoiceId).append(")").toString());

				}
				// updating the Invoice to make it as completly paid.
				stat.execute(new StringBuilder()
						.append("UPDATE INVOICE I SET I.PAYMENTS = I.TOTAL,I.BALANCE_DUE = 0.0 WHERE I.ID =")
						.append(oldInvoiceId).toString());

			}

			if (oldEstimateId != null) {

				// To Mark the Estimate as Not Invoiced.
				stat.execute(new StringBuilder()
						.append("UPDATE ESTIMATE E SET E.IS_TURNED_TO_INVOICE = 'FALSE' WHERE E.ID = ")
						.append(oldEstimateId).toString());

				// To update the status of the Estimate as Open.
				updateStatusOfEstimate(stat,
						Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED,
						newEstimateId);

			}

		}
	}

	private void updateStatusOfEstimate(Statement stat, int status,
			Long estimateId) throws SQLException {
		stat.execute(new StringBuilder()
				.append("UPDATE TRANSACTION T SET T.STATUS = ").append(status)
				.append(" WHERE T.ID = ").append(estimateId).toString());
	}

	private void updateCorrespondingTaxAgencyBalancesWithSalesTax(
			Statement stat, Double allTaxableLineTotal, Long invoiceId,
			String symbol) throws SQLException {
		stat.execute(new StringBuilder()
				.append("UPDATE TAXAGENCY A SET A.BALANCE = A.BALANCE ")
				.append(symbol)
				.append(" ")
				.append(allTaxableLineTotal)
				.append("* (SELECT TRC.RATE FROM TAX_RATE_CALCULATION TRC WHERE TRC.TRANSACTION_ID=")
				.append(invoiceId)
				.append(" AND TRC.TAXAGENCY_ID = A.ID )/100 WHERE A.ID IN (SELECT TRC.TAXAGENCY_ID  FROM TAX_RATE_CALCULATION TRC WHERE TRC.TRANSACTION_ID=")
				.append(invoiceId).append(")").toString());
	}

	private void updateCorrespondingTaxAgencyAccountsWithSalesTax(
			Statement stat, Double allTaxableLineTotal, Long invoiceId,
			String symbol) throws SQLException {
		stat.execute(new StringBuilder()
				.append("UPDATE ACCOUNT A SET A.CURRENT_BALANCE = A.CURRENT_BALANCE ")
				.append(symbol)
				.append(" ")
				.append(allTaxableLineTotal)
				.append("*(SELECT SUM(TRC.RATE) FROM TAX_RATE_CALCULATION TRC WHERE TRC.TRANSACTION_ID= ")
				.append(invoiceId)
				.append(" AND TRC.TAXAGENCY_ACCOUNT_ID = A.ID GROUP BY TRC.TAXAGENCY_ACCOUNT_ID)/100 , A.TOTAL_BALANCE = A.TOTAL_BALANCE ")
				.append(symbol)
				.append(" ")
				.append(allTaxableLineTotal)
				.append("*(SELECT SUM(TRC.RATE) FROM TAX_RATE_CALCULATION TRC WHERE TRC.TRANSACTION_ID= ")
				.append(invoiceId)
				.append(" AND TRC.TAXAGENCY_ACCOUNT_ID = A.ID GROUP BY TRC.TAXAGENCY_ACCOUNT_ID)/100 WHERE A.ID IN (SELECT TRC.TAXAGENCY_ACCOUNT_ID  FROM TAX_RATE_CALCULATION TRC WHERE TRC.TRANSACTION_ID =")
				.append(invoiceId).append(" )").toString());
	}

	@SuppressWarnings("unused")
	private void insertTaxGroupDetailsIntoTaxRateCalculationTable(
			Statement stat, Long newInvoiceId, Long newTaxGroupId,
			Double newTotal, Double newAllTaxableLineTotal,
			Double newAllLineTotal) throws SQLException {
		stat.execute(new StringBuilder()
				.append("INSERT INTO TAX_RATE_CALCULATION  (TAXAGENCY_ACCOUNT_ID,RATE,TRANSACTION_ID,TRANSACTION_TYPE,TAX_CODE_ID,TOTAL,TAXABLE_AMOUNT,NON_TAXABLE_AMOUNT,TAX_COLLECTED,TAXAGENCY_ID)")
				.append("(SELECT TA.ACCOUNT_ID,TR.RATE,")
				.append(newInvoiceId)
				.append(", 8 , ")
				.append("TC.ID")
				.append(" , ")
				.append(newTotal)
				.append(" , ")
				.append(newAllTaxableLineTotal)
				.append(" , ")
				.append(newAllLineTotal - newAllTaxableLineTotal)
				.append(" , ")
				.append(newAllTaxableLineTotal)
				.append(" * (TR.RATE /100)")
				.append(",TA.ID  FROM TAXRATES TR JOIN TAXGROUP_TAXCODE TGTC ON TR.TAXCODE_ID = TGTC.TAXCODE_ID JOIN TAXGROUP TG ")
				.append(" ON TGTC.TAXGROUP_ID = TG.ID JOIN TAXCODE TC ON TC.ID=TR.TAXCODE_ID JOIN TAXAGENCY TA ON TA.ID = TC.TAXAGENCY_ID WHERE ")
				.append(" TG.ID = ")
				.append(newTaxGroupId)
				.append(" AND TR.AS_OF IN (SELECT MAX(TR1.AS_OF) FROM TAXRATES TR1 WHERE TR1.ID IN (SELECT (TR.ID) FROM TAXRATES TR JOIN ")
				.append(" TAXGROUP_TAXCODE TGTC ON TR.TAXCODE_ID = TGTC.TAXCODE_ID JOIN TAXGROUP TG ON TGTC.TAXGROUP_ID = TG.ID) AND ")
				.append(" TR1.AS_OF <= (SELECT T.T_DATE FROM TRANSACTION T WHERE T.ID=")
				.append(newInvoiceId).append(") GROUP BY TR1.TAXCODE_ID) )")
				.toString());

	}

	private void updateCustomerBalance(Statement stat, Double total,
			Long customerId, String symbol) throws SQLException {
		stat.execute(new StringBuilder()
				.append("UPDATE CUSTOMER C SET C.BALANCE = C.BALANCE ")
				.append(symbol).append(" ").append(total)
				.append(" WHERE C.ID = ").append(customerId).toString());

	}

	private void updateAccountsReceivableAccount(Statement stat, Double total,
			Long accountsReceivableId, String symbol) throws SQLException {
		stat.execute(new StringBuilder()
				.append("UPDATE ACCOUNT A SET A.CURRENT_BALANCE = A.CURRENT_BALANCE ")
				.append(symbol).append(" ").append(total)
				.append(", A.TOTAL_BALANCE = A.TOTAL_BALANCE ").append(symbol)
				.append(" ").append(total).append(" WHERE A.ID = ")
				.append(accountsReceivableId).toString());

	}

	@Override
	public void init(Connection arg0, String arg1, String arg2, String arg3,
			boolean arg4, int arg5) throws SQLException {
		// currently not using anywhere in the project.

	}

}
