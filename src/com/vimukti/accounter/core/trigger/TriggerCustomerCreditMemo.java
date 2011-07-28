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
public class TriggerCustomerCreditMemo implements Trigger {

	@Override
	public void fire(Connection conn, Object[] oldRow, Object[] newRow)
			throws SQLException {

		// Required variables declaration and initialization

		Statement stat = conn.createStatement();

		Long newCustomerCreditMemoId = (newRow != null) ? ((newRow[0] != null) ? (Long) newRow[0]
				: null)
				: null;
		
		Long oldCustomerCreditMemoId = (oldRow != null) ? ((oldRow[0] != null) ? (Long) oldRow[0]
				: null)
				: null;
		
		Long newCustomerId = (newRow != null) ? ((newRow[1] != null) ? (Long) newRow[1]
				: null)
				: null;
		Long oldCustomerId = (oldRow != null) ? ((oldRow[1] != null) ? (Long) oldRow[1]
				: null)
				: null;
		
		Long newTaxGroupId = (newRow != null) ? ((newRow[9] != null) ? (Long) newRow[9]
				: null)
				: null;
		Long oldTaxGroupId = (oldRow != null) ? ((oldRow[9] != null) ? (Long) oldRow[9]
				: null)
				: null;

		
		Double newTotal = (newRow != null) ? ((newRow[12] != null) ? (Double) newRow[12]
				: null)
				: null;
		Double oldTotal = (oldRow != null) ? ((oldRow[12] != null) ? (Double) oldRow[12]
				: null)
				: null;
		
		Double newAllLineTotal = (newRow != null) ? ((newRow[13] != null) ? (Double) newRow[13]
				: null)
				: null;
		
		Double oldAllLineTotal = (oldRow != null) ? ((oldRow[13] != null) ? (Double) oldRow[13]
				: null)
				: null;
		Double newAllTaxableLineTotal = (newRow != null) ? ((newRow[14] != null) ? (Double) newRow[14]
				: null)
				: null;
		Double oldAllTaxableLineTotal = (oldRow != null) ? ((oldRow[14] != null) ? (Double) oldRow[14]
				: null)
				: null;
		Boolean newIsVoid = (newRow != null) ? ((newRow[16] != null) ? (Boolean) newRow[16]
				: null)
				: null;
		Boolean oldIsVoid = (oldRow != null) ? ((oldRow[16] != null) ? (Boolean) oldRow[16]
				: null)
				: null;
		
		Long newAccountsReceivableId = (newRow != null) ? ((newRow[17] != null) ? (Long) newRow[17]
				: null)
				: null;
		Long oldAccountsReceivableId = (oldRow != null) ? ((oldRow[17] != null) ? (Long) oldRow[17]
				: null)
				: null;

		// Condition for checking whether this Trigger call is for new Row
		// Insertion

		if (newRow != null && oldRow == null) {
		}
		// Condition for checking whether this Trigger call is for Row Updation
		else if (newRow != null && oldRow != null) {

			// To check whether this updation is caused by Voiding any Customer
			// Credit Memo
			// or not.

			if (oldIsVoid != newIsVoid) {

				// Deleting corresponding transaction rows from Account
				// Transaction table
				stat.execute(new StringBuilder()
						.append("DELETE FROM ACCOUNT_TRANSACTION WHERE T_ID =")
						.append(newCustomerCreditMemoId).toString());

				// Reverse updating AccountsReceivable Account

				updateAccountsReceivableAccount(stat, oldTotal,
						oldAccountsReceivableId, AccounterConstants.SYMBOL_PLUS);

				// Reverse Updating Customer Opening Balance

				updateCustomerBalance(stat, oldTotal, oldCustomerId,
						AccounterConstants.SYMBOL_PLUS);

				if (!oldTaxGroupId.equals(null)
						&& !(oldAllTaxableLineTotal).equals(0.0)) {

					// Reverse Updating the TaxAgency Accounts with the Sales
					// Tax amount.
					updateCorrespondingTaxAgencyAccountsWithSalesTax(stat,
							newAllTaxableLineTotal, newCustomerCreditMemoId,
							AccounterConstants.SYMBOL_PLUS);

					// Reverse updating the balance of corresponding tax
					// agency

					updateCorrespondingTaxAgencyBalancesWithSalesTax(stat,
							newAllTaxableLineTotal, newCustomerCreditMemoId,
							AccounterConstants.SYMBOL_PLUS);

					// Deleting corresponding transaction rows from TaxRate
					// Calculation table
					stat.execute(new StringBuilder()
							.append("DELETE FROM TAX_RATE_CALCULATION WHERE TRANSACTION_ID =")
							.append(newCustomerCreditMemoId).toString());

				}

				// Query to retrieve the number of places it's being used.
				int count = 0;

				ResultSet r2 = stat
						.executeQuery(new StringBuilder()
								.append("SELECT COUNT(*) FROM TRANSACTION_CREDITS_AND_PAYMENTS TCP WHERE TCP.CREDITS_AND_PAYMENTS_ID = SELECT CP.ID FROM CREDTIS_AND_PAYMENTS CP WHERE CP.TRANSACTION_ID = ")
								.append(newCustomerCreditMemoId)
								.append(" AND TCP.AMOUNT_TO_USE > 0.0 ")
								.toString());

				if (r2.next()) {
					count = r2.getInt(1);
				}

				if (count > 0) {

					// Increase the Balance of the Invoices which are being paid
					// with this Customer Credit Memo
					stat.execute(new StringBuilder()
							.append("UPDATE INVOICE I SET I.BALANCE_DUE = I.BALANCE_DUE + (SELECT SUM(TCP1.AMOUNT_TO_USE) FROM TRANSACTION_CREDITS_AND_PAYMENTS TCP1 JOIN TRANSACTION_RECEIVE_PAYMENT TRP1 ON TCP1.TRANSACTION_RECEIVE_PAYMENT_ID = TRP1.ID JOIN INVOICE I1 ON TRP1.INVOICE_ID = I1.ID WHERE I1.ID = I.ID AND TCP1.CREDITS_AND_PAYMENTS_ID = SELECT CP.ID FROM CREDTIS_AND_PAYMENTS CP WHERE CP.TRANSACTION_ID = ")
							.append(newCustomerCreditMemoId)
							.append("),I.PAYMENTS = I.PAYMENTS - (SELECT SUM(TCP1.AMOUNT_TO_USE) FROM TRANSACTION_CREDITS_AND_PAYMENTS TCP1 JOIN TRANSACTION_RECEIVE_PAYMENT TRP1 ON TCP1.TRANSACTION_RECEIVE_PAYMENT_ID = TRP1.ID JOIN INVOICE I1 ON TRP1.INVOICE_ID = I1.ID WHERE I1.ID = I.ID AND TCP1.CREDITS_AND_PAYMENTS_ID = SELECT CP.ID FROM CREDTIS_AND_PAYMENTS CP WHERE CP.TRANSACTION_ID = ")
							.append(newCustomerCreditMemoId)
							.append(") WHERE I.ID IN (SELECT I.ID FROM INVOICE I JOIN TRANSACTION_RECEIVE_PAYMENT TRP ON I.ID = TRP.INVOICE_ID JOIN TRANSACTION_CREDITS_AND_PAYMENTS TCP ON TRP.ID = TCP.TRANSACTION_RECEIVE_PAYMENT_ID WHERE TCP.CREDITS_AND_PAYMENTS_ID = SELECT CP.ID FROM CREDTIS_AND_PAYMENTS CP WHERE CP.TRANSACTION_ID = ")
							.append(newCustomerCreditMemoId).append(")")
							.toString());

					// Increase the Balance of the Customer Refunds which are
					// being paid with this Customer Credit Memo
					stat.execute(new StringBuilder()
							.append("UPDATE CUSTOMER_REFUND CR SET CR.BALANCE_DUE = CR.BALANCE_DUE + (SELECT SUM(TCP1.AMOUNT_TO_USE) FROM TRANSACTION_CREDITS_AND_PAYMENTS TCP1 JOIN TRANSACTION_RECEIVE_PAYMENT TRP1 ON TCP1.TRANSACTION_RECEIVE_PAYMENT_ID = TRP1.ID JOIN CUSTOMER_REFUND CR1 ON TRP1.CUSTOMER_REFUND_ID = CR1.ID WHERE CR1.ID = CR.ID AND TCP1.CREDITS_AND_PAYMENTS_ID = SELECT CP.ID FROM CREDTIS_AND_PAYMENTS CP WHERE CP.TRANSACTION_ID = ")
							.append(newCustomerCreditMemoId)
							.append("),CR.PAYMENTS = CR.PAYMENTS - (SELECT SUM(TCP1.AMOUNT_TO_USE) FROM TRANSACTION_CREDITS_AND_PAYMENTS TCP1 JOIN TRANSACTION_RECEIVE_PAYMENT TRP1 ON TCP1.TRANSACTION_RECEIVE_PAYMENT_ID = TRP1.ID JOIN CUSTOMER_REFUND CR1 ON TRP1.CUSTOMER_REFUND_ID = CR1.ID WHERE CR1.ID = CR.ID AND TCP1.CREDITS_AND_PAYMENTS_ID = SELECT CP.ID FROM CREDTIS_AND_PAYMENTS CP WHERE CP.TRANSACTION_ID = ")
							.append(newCustomerCreditMemoId)
							.append(") WHERE CR.ID IN (SELECT CR.ID FROM CUSTOMER_REFUND CR JOIN TRANSACTION_RECEIVE_PAYMENT TRP ON CR.ID = TRP.CUSTOMER_REFUND_ID JOIN TRANSACTION_CREDITS_AND_PAYMENTS TCP ON TRP.ID = TCP.TRANSACTION_RECEIVE_PAYMENT_ID WHERE TCP.CREDITS_AND_PAYMENTS_ID = SELECT CP.ID FROM CREDTIS_AND_PAYMENTS CP WHERE CP.TRANSACTION_ID = ")
							.append(newCustomerCreditMemoId).append(")")
							.toString());

					// Reduce the applied credits amount of the Transaction
					// Receive Payment by the amount used from this Customer
					// Credit Memo as Credit and Payment

					stat.execute(new StringBuilder()
							.append("UPDATE TRANSACTION_RECEIVE_PAYMENT TRP SET TRP.APPLIED_CREDITS = TRP.APPLIED_CREDITS - (SELECT TCP1.AMOUNT_TO_USE FROM TRANSACTION_CREDITS_AND_PAYMENTS TCP1 JOIN TRANSACTION_RECEIVE_PAYMENT TRP1 ON TCP1.TRANSACTION_RECEIVE_PAYMENT_ID  = TRP1.ID WHERE TRP1.ID = TRP.ID  AND TCP1.CREDITS_AND_PAYMENTS_ID = SELECT CP.ID FROM CREDTIS_AND_PAYMENTS CP WHERE CP.TRANSACTION_ID = ")
							.append(newCustomerCreditMemoId)
							.append(") WHERE TRP.ID IN (SELECT TRP.ID FROM TRANSACTION_RECEIVE_PAYMENT TRP JOIN TRANSACTION_CREDITS_AND_PAYMENTS TCP ON TRP.ID = TCP.TRANSACTION_RECEIVE_PAYMENT_ID WHERE TCP.CREDITS_AND_PAYMENTS_ID = SELECT CP.ID FROM CREDTIS_AND_PAYMENTS CP WHERE CP.TRANSACTION_ID = ")
							.append(newCustomerCreditMemoId).append(")")
							.toString());

					// Updating the amount to use as zero for the Transaction
					// Credits and payments created for this Customer Credit
					// Memo.
					stat.execute(new StringBuilder()
							.append("UPDATE TRANSACTION_CREDITS_AND_PAYMENTS TCP SET TCP.AMOUNT_TO_USE = 0.0 WHERE TCP.CREDITS_AND_PAYMENTS_ID = SELECT CP.ID FROM CREDTIS_AND_PAYMENTS CP WHERE CP.TRANSACTION_ID = ")
							.append(newCustomerCreditMemoId).toString());

				}

				// Deleting this Credit Memo from CREDITS_AND_PAYMENTS table.
				stat.execute(new StringBuilder()
						.append("DELETE FROM CREDITS_AND_PAYMENTS CP WHERE CP.CREDITS_AND_PAYMENTS_ID = SELECT CP.ID FROM CREDTIS_AND_PAYMENTS CP WHERE CP.TRANSACTION_ID = ")
						.append(newCustomerCreditMemoId).toString());

			}

		}

	}

	private void updateCorrespondingTaxAgencyBalancesWithSalesTax(
			Statement stat, Double allTaxableLineTotal,
			Long customerCreditMemoId, String symbol) throws SQLException {
		stat.execute(new StringBuilder()
				.append("UPDATE TAXAGENCY A SET A.BALANCE = A.BALANCE ")
				.append(symbol)
				.append(" ")
				.append(allTaxableLineTotal)
				.append("* (SELECT TRC.RATE FROM TAX_RATE_CALCULATION TRC WHERE TRC.TRANSACTION_ID=")
				.append(customerCreditMemoId)
				.append(" AND TRC.TAXAGENCY_ID = A.ID )/100 WHERE A.ID IN (SELECT TRC.TAXAGENCY_ID  FROM TAX_RATE_CALCULATION TRC WHERE TRC.TRANSACTION_ID=")
				.append(customerCreditMemoId).append(")").toString());

	}

	private void updateCorrespondingTaxAgencyAccountsWithSalesTax(
			Statement stat, Double allTaxableLineTotal,
			Long customerCreditMemoId, String symbol) throws SQLException {
		stat.execute(new StringBuilder()
				.append("UPDATE ACCOUNT A SET A.CURRENT_BALANCE = A.CURRENT_BALANCE ")
				.append(symbol)
				.append(" ")
				.append(allTaxableLineTotal)
				.append("*(SELECT SUM(TRC.RATE) FROM TAX_RATE_CALCULATION TRC WHERE TRC.TRANSACTION_ID= ")
				.append(customerCreditMemoId)
				.append(" AND TRC.TAXAGENCY_ACCOUNT_ID = A.ID GROUP BY TRC.TAXAGENCY_ACCOUNT_ID)/100 , A.TOTAL_BALANCE = A.TOTAL_BALANCE ")
				.append(symbol)
				.append(" ")
				.append(allTaxableLineTotal)
				.append("*(SELECT SUM(TRC.RATE) FROM TAX_RATE_CALCULATION TRC WHERE TRC.TRANSACTION_ID= ")
				.append(customerCreditMemoId)
				.append(" AND TRC.TAXAGENCY_ACCOUNT_ID = A.ID GROUP BY TRC.TAXAGENCY_ACCOUNT_ID)/100 WHERE A.ID IN (SELECT TRC.TAXAGENCY_ACCOUNT_ID  FROM TAX_RATE_CALCULATION TRC WHERE TRC.TRANSACTION_ID =")
				.append(customerCreditMemoId).append(" )").toString());

	}

	
	private void insertTaxGroupDetailsIntoTaxRateCalculationTable(
			Statement stat, Long newCustomerCreditMemoId, Long newTaxGroupId,
			Double newTotal, Double newAllTaxableLineTotal,
			Double newAllLineTotal) throws SQLException {
		stat.execute(new StringBuilder()
				.append("INSERT INTO TAX_RATE_CALCULATION  (TAXAGENCY_ACCOUNT_ID,RATE,TRANSACTION_ID,TRANSACTION_TYPE,TAX_CODE_ID,TOTAL,TAXABLE_AMOUNT,NON_TAXABLE_AMOUNT,TAX_COLLECTED,TAXAGENCY_ID)")
				.append("(SELECT TA.ACCOUNT_ID,TR.RATE,")
				.append(newCustomerCreditMemoId)
				.append(", 4 , ")
				.append("TC.ID")
				.append(" , ")
				.append(newTotal)
				.append(" , ")
				.append(newAllTaxableLineTotal)
				.append(" , ")
				.append(newAllLineTotal - newAllTaxableLineTotal)
				.append(" , ")
				.append("-1 * ")
				.append(newAllTaxableLineTotal)
				.append(" * (TR.RATE /100)")
				.append(",TA.ID  FROM TAXRATES TR JOIN TAXGROUP_TAXCODE TGTC ON TR.TAXCODE_ID = TGTC.TAXCODE_ID JOIN TAXGROUP TG ")
				.append(" ON TGTC.TAXGROUP_ID = TG.ID JOIN TAXCODE TC ON TC.ID=TR.TAXCODE_ID JOIN TAXAGENCY TA ON TA.ID = TC.TAXAGENCY_ID WHERE ")
				.append(" TG.ID = ")
				.append(newTaxGroupId)
				.append(" AND TR.AS_OF IN (SELECT MAX(TR1.AS_OF) FROM TAXRATES TR1 WHERE TR1.ID IN (SELECT (TR.ID) FROM TAXRATES TR JOIN ")
				.append(" TAXGROUP_TAXCODE TGTC ON TR.TAXCODE_ID = TGTC.TAXCODE_ID JOIN TAXGROUP TG ON TGTC.TAXGROUP_ID = TG.ID) AND ")
				.append(" TR1.AS_OF <= (SELECT T.T_DATE FROM TRANSACTION T WHERE T.ID=")
				.append(newCustomerCreditMemoId)
				.append(") GROUP BY TR1.TAXCODE_ID))").toString());
	}

	
	private void insertCustomerCreditMemoIntoCreditsAndPayments(Statement stat,
			Date transactionDate, Long companyId, Long transactionNumber,
			Double newTotal, Long newCustomerCreditMemoId, Long newCustomerId)
			throws SQLException {
		stat.execute(new StringBuilder()
				.append("INSERT INTO CREDITS_AND_PAYMENTS (DATE,COMPANY_ID,TYPE,MEMO,CREDIT_AMOUNT,BALANCE,TRANSACTION_ID,CUSTOMER_OR_VENDOR_ID) VALUES('")
				.append(transactionDate).append("',").append(companyId)
				.append(",2,'").append(transactionNumber)
				.append(AccounterConstants.CUSTOMER_CREDIT_MEMO).append("',")
				.append(newTotal).append(",").append(newTotal).append(",")
				.append(newCustomerCreditMemoId).append(",")
				.append(newCustomerId).append(")").toString());
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
