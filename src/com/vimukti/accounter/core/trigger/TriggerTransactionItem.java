//package com.vimukti.accounter.core.trigger;
//
//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//
//import org.h2.api.Trigger;
//
//import com.vimukti.accounter.web.client.core.AccountTransaction;
//import com.vimukti.accounter.web.client.core.AccounterConstants;
//import com.vimukti.accounter.web.client.core.Transaction;
//import com.vimukti.accounter.web.client.core.TransactionItem;
//
///**
// * 
// * @author Devesh Satwani
// * 
// */
//public class TriggerTransactionItem implements Trigger {
//
//	public void fire(Connection conn, Object[] oldRow, Object[] newRow)
//			throws SQLException {
//
//		// Required variables declaration and initialization
//
//		Statement stat = conn.createStatement();
//
//		Integer newType = (newRow != null) ? ((newRow[3] != null) ? (Integer) newRow[3]
//				: null)
//				: null;
//		Integer oldType = (oldRow != null) ? ((oldRow[3] != null) ? (Integer) oldRow[3]
//				: null)
//				: null;
//
//		Long newItemId = (newRow != null) ? ((newRow[4] != null) ? (Long) newRow[4]
//				: null)
//				: null;
//		Long oldItemId = (oldRow != null) ? ((oldRow[4] != null) ? (Long) oldRow[4]
//				: null)
//				: null;
//
//		Long newTaxCodeId = (newRow != null) ? ((newRow[5] != null) ? (Long) newRow[5]
//				: null)
//				: null;
//		Long oldTaxCodeId = (oldRow != null) ? ((oldRow[5] != null) ? (Long) oldRow[5]
//				: null)
//				: null;
//
//		Long newAccountId = (newRow != null) ? ((newRow[6] != null) ? (Long) newRow[6]
//				: null)
//				: null;
//		Long oldAccountId = (oldRow != null) ? ((oldRow[6] != null) ? (Long) oldRow[6]
//				: null)
//				: null;
//
//		Double newLineTotal = (newRow != null) ? ((newRow[11] != null) ? (Double) newRow[11]
//				: null)
//				: null;
//		Double oldLineTotal = (oldRow != null) ? ((oldRow[11] != null) ? (Double) oldRow[11]
//				: null)
//				: null;
//
//		Long newTransactionId = (newRow != null) ? ((newRow[13] != null) ? (Long) newRow[13]
//				: null)
//				: null;
//		Long oldTransactionId = (oldRow != null) ? ((oldRow[13] != null) ? (Long) oldRow[13]
//				: null)
//				: null;
//		Integer newIndex = (newRow != null) ? ((newRow[14] != null) ? (Integer) newRow[14]
//				: null)
//				: null;
//		Integer oldIndex = (oldRow != null) ? ((oldRow[14] != null) ? (Integer) oldRow[14]
//				: null)
//				: null;
//
//		// Condition for checking whether this Trigger call is for new Row
//		// Insertion
//
//		if (newRow != null && oldRow == null) {
//
//			// if (newRow[17] == null) {
//			// doInCreatePart(stat, newTransactionId, newType, newItemId,
//			// newTaxCodeId, newAccountId, newLineTotal);
//			// }
//
//		}
//		// Condition for checking whether this Trigger call is for Row Updation
//		else if (newRow != null && oldRow != null) {
//
//			// Checking for whether this updation caused by the Transaction Id
//			// updation while creating Transaction Item.
//
//			if (oldIndex == null && newIndex != null) {
//				doInCreatePart(stat, newTransactionId, newType, newItemId,
//						newTaxCodeId, newAccountId, newLineTotal);
//			}
//
//			else if (!(oldIndex == null && newIndex != null)) {
//				// Getting the Transaction type
//				int transactionType = getTransactionType(stat, newTransactionId);
//
//				Boolean isVoid = Boolean.FALSE;
//
//				switch (transactionType) {
//
//				case Transaction.TYPE_INVOICE:
//
//					isVoid = isVoidedTransaction(stat, newTransactionId,
//							AccounterConstants.TABLE_INVOICE);
//
//					if (isVoid) {
//
//						getReverseUpdateAccountForInvoiceAndCashSales(stat,
//								newType, newItemId, newTaxCodeId, newAccountId,
//								newLineTotal, newTransactionId);
//					}
//
//					break;
//
//				case Transaction.TYPE_ENTER_BILL:
//
//					isVoid = isVoidedTransaction(stat, newTransactionId,
//							AccounterConstants.TABLE_ENTER_BILL);
//
//					if (isVoid) {
//
//						getReverseUpdateAccountForEnterBillAndCashPurchaseAndCreditCardCharge(
//								stat, newType, newItemId, newTaxCodeId,
//								newAccountId, newLineTotal, newTransactionId);
//					}
//
//					break;
//
//				case Transaction.TYPE_CASH_SALES:
//
//					isVoid = isVoidedTransaction(stat, newTransactionId,
//							AccounterConstants.TABLE_CASH_SALES);
//
//					if (isVoid) {
//
//						getReverseUpdateAccountForInvoiceAndCashSales(stat,
//								newType, newItemId, newTaxCodeId, newAccountId,
//								newLineTotal, newTransactionId);
//					}
//
//					break;
//
//				case Transaction.TYPE_CASH_PURCHASE:
//
//					isVoid = isVoidedTransaction(stat, newTransactionId,
//							AccounterConstants.TABLE_CASH_PURCHASE);
//
//					if (isVoid) {
//
//						getReverseUpdateAccountForEnterBillAndCashPurchaseAndCreditCardCharge(
//								stat, newType, newItemId, newTaxCodeId,
//								newAccountId, newLineTotal, newTransactionId);
//					}
//
//					break;
//
//				case Transaction.TYPE_CREDIT_CARD_CHARGE:
//
//					isVoid = isVoidedTransaction(stat, newTransactionId,
//							AccounterConstants.TABLE_CREDIT_CARD_CHARGES);
//
//					if (isVoid) {
//
//						getReverseUpdateAccountForEnterBillAndCashPurchaseAndCreditCardCharge(
//								stat, newType, newItemId, newTaxCodeId,
//								newAccountId, newLineTotal, newTransactionId);
//					}
//
//					break;
//
//				case Transaction.TYPE_CUSTOMER_CREDIT_MEMO:
//
//					isVoid = isVoidedTransaction(stat, newTransactionId,
//							AccounterConstants.TABLE_CUSTOMER_CREDIT_MEMO);
//
//					if (isVoid) {
//
//						getReverseUpdateAccountForCustomerCreditMemoAndWriteChecks(
//								stat, newType, newItemId, newTaxCodeId,
//								newAccountId, newLineTotal, newTransactionId);
//					}
//
//					break;
//
//				case Transaction.TYPE_ESTIMATE:
//					break;
//
//				case Transaction.TYPE_VENDOR_CREDIT_MEMO:
//
//					isVoid = isVoidedTransaction(stat, newTransactionId,
//							AccounterConstants.TABLE_VENDOR_CREDIT_MEMO);
//
//					if (isVoid) {
//
//						getReverseUpdateAccountForVendorCreditMemo(stat,
//								newType, newItemId, newAccountId, newLineTotal,
//								newTransactionId);
//					}
//
//					break;
//
//				case Transaction.TYPE_WRITE_CHECK:
//
//					isVoid = isVoidedTransaction(stat, newTransactionId,
//							AccounterConstants.TABLE_WRITE_CHECKS);
//
//					if (isVoid) {
//
//						getReverseUpdateAccountForCustomerCreditMemoAndWriteChecks(
//								stat, newType, newItemId, newTaxCodeId,
//								newAccountId, newLineTotal, newTransactionId);
//					}
//					break;
//
//				default:
//
//					break;
//
//				}
//			}
//
//		}
//		// This Block is For Deletion of Transaction Item
//		else if (newRow == null && oldRow != null) {
//
//			// Getting the Transaction type
//			int transactionType = getTransactionType(stat, oldTransactionId);
//
//			switch (transactionType) {
//
//			case Transaction.TYPE_INVOICE:
//
//				getReverseUpdateAccountForInvoiceAndCashSales(stat, oldType,
//						newItemId, newTaxCodeId, newAccountId, newLineTotal,
//						newTransactionId);
//
//				break;
//
//			case Transaction.TYPE_ENTER_BILL:
//
//				getReverseUpdateAccountForEnterBillAndCashPurchaseAndCreditCardCharge(
//						stat, oldType, newItemId, newTaxCodeId, newAccountId,
//						newLineTotal, newTransactionId);
//
//				break;
//
//			case Transaction.TYPE_CASH_SALES:
//
//				getReverseUpdateAccountForInvoiceAndCashSales(stat, oldType,
//						newItemId, newTaxCodeId, newAccountId, newLineTotal,
//						newTransactionId);
//
//				break;
//
//			case Transaction.TYPE_CASH_PURCHASE:
//
//				getReverseUpdateAccountForEnterBillAndCashPurchaseAndCreditCardCharge(
//						stat, oldType, newItemId, newTaxCodeId, newAccountId,
//						newLineTotal, newTransactionId);
//
//				break;
//
//			case Transaction.TYPE_CREDIT_CARD_CHARGE:
//
//				getReverseUpdateAccountForEnterBillAndCashPurchaseAndCreditCardCharge(
//						stat, oldType, newItemId, newTaxCodeId, newAccountId,
//						newLineTotal, newTransactionId);
//
//				break;
//
//			case Transaction.TYPE_CUSTOMER_CREDIT_MEMO:
//
//				getReverseUpdateAccountForCustomerCreditMemoAndWriteChecks(
//						stat, oldType, newItemId, newTaxCodeId, newAccountId,
//						newLineTotal, newTransactionId);
//
//				break;
//
//			case Transaction.TYPE_ESTIMATE:
//				break;
//
//			case Transaction.TYPE_VENDOR_CREDIT_MEMO:
//
//				getReverseUpdateAccountForVendorCreditMemo(stat, oldType,
//						newItemId, newAccountId, newLineTotal, newTransactionId);
//
//				break;
//
//			case Transaction.TYPE_WRITE_CHECK:
//
//				getReverseUpdateAccountForCustomerCreditMemoAndWriteChecks(
//						stat, oldType, newItemId, newTaxCodeId, newAccountId,
//						newLineTotal, newTransactionId);
//				break;
//
//			default:
//
//				break;
//
//			}
//
//		}
//
//	}
//
//	private void doInCreatePart(Statement stat, Long newTransactionId,
//			Integer newType, Long newItemId, Long newTaxCodeId,
//			Long newAccountId, Double newLineTotal) throws SQLException {
//
//		// Getting the Transaction type
//		int transactionType = getTransactionType(stat, newTransactionId);
//
//		switch (transactionType) {
//
//		case Transaction.TYPE_INVOICE:
//
//			getUpdateAccountForInvoiceAndCashSales(stat, newType, newItemId,
//					newTaxCodeId, newAccountId, newLineTotal, newTransactionId);
//			break;
//
//		case Transaction.TYPE_ENTER_BILL:
//
//			getUpdateAccountForEnterBillAndCashPurchaseAndCreditCardCharge(
//					stat, newType, newItemId, newTaxCodeId, newAccountId,
//					newLineTotal, newTransactionId);
//			break;
//
//		case Transaction.TYPE_CASH_SALES:
//
//			getUpdateAccountForInvoiceAndCashSales(stat, newType, newItemId,
//					newTaxCodeId, newAccountId, newLineTotal, newTransactionId);
//			break;
//
//		case Transaction.TYPE_CASH_PURCHASE:
//
//			getUpdateAccountForEnterBillAndCashPurchaseAndCreditCardCharge(
//					stat, newType, newItemId, newTaxCodeId, newAccountId,
//					newLineTotal, newTransactionId);
//			break;
//
//		case Transaction.TYPE_CREDIT_CARD_CHARGE:
//
//			getUpdateAccountForEnterBillAndCashPurchaseAndCreditCardCharge(
//					stat, newType, newItemId, newTaxCodeId, newAccountId,
//					newLineTotal, newTransactionId);
//			break;
//
//		case Transaction.TYPE_CUSTOMER_CREDIT_MEMO:
//
//			getUpdateAccountForCustomerCreditMemoAndWriteChecks(stat, newType,
//					newItemId, newTaxCodeId, newAccountId, newLineTotal,
//					newTransactionId);
//			break;
//
//		case Transaction.TYPE_ESTIMATE:
//			break;
//
//		case Transaction.TYPE_VENDOR_CREDIT_MEMO:
//
//			getUpdateAccountForVendorCreditMemo(stat, newType, newItemId,
//					newAccountId, newLineTotal, newTransactionId);
//			break;
//		case Transaction.TYPE_WRITE_CHECK:
//
//			getUpdateAccountForCustomerCreditMemoAndWriteChecks(stat, newType,
//					newItemId, newTaxCodeId, newAccountId, newLineTotal,
//					newTransactionId);
//			break;
//
//		default:
//
//			break;
//
//		}
//
//	}
//
//	private Boolean isVoidedTransaction(Statement stat, Long transactionId,
//			String table) throws SQLException {
//
//		Boolean isVoid = Boolean.FALSE;
//		ResultSet r = stat.executeQuery(new StringBuilder().append(
//				"SELECT I.IS_VOID FROM ").append(table).append(
//				" I WHERE I.ID =").append(transactionId).toString());
//		if (r.next()) {
//			isVoid = r.getBoolean(1);
//		}
//		return isVoid;
//	}
//
//	private int getTransactionType(Statement stat, Long transactionId)
//			throws SQLException {
//		int transactionType = 0;
//		ResultSet rs = stat.executeQuery(new StringBuilder().append(
//				"SELECT T.T_TYPE FROM TRANSACTION T WHERE T.ID =").append(
//				transactionId).toString());
//		if (rs.next()) {
//			transactionType = rs.getInt(1);
//		}
//		return transactionType;
//	}
//
//	private void updateCorrespondingTaxAgencyBalance(Statement stat,
//			Double lineTotal, Long taxCodeId, String symbol)
//			throws SQLException {
//
//		stat
//				.execute(new StringBuilder()
//						.append("UPDATE TAXAGENCY T SET T.BALANCE = T.BALANCE ")
//						.append(symbol)
//						.append(" ")
//						.append(lineTotal)
//						.append(
//								" WHERE T.ID = (SELECT TA.ID FROM TAXAGENCY TA JOIN TAXCODE TC ON TC.TAXAGENCY_ID = TA.ID WHERE TC.ID =")
//						.append(taxCodeId).append(")").toString());
//
//	}
//
//	private void updateCurrentAndTotalBalancesOfCorrespondingTaxAgencyAccount(
//			Statement stat, Double lineTotal, Long taxCodeId, String symbol)
//			throws SQLException {
//		stat
//				.execute(new StringBuilder()
//						.append(
//								"UPDATE ACCOUNT A SET A.CURRENT_BALANCE = A.CURRENT_BALANCE ")
//						.append(symbol)
//						.append(" ")
//						.append(lineTotal)
//						.append(", A.TOTAL_BALANCE = A.TOTAL_BALANCE ")
//						.append(symbol)
//						.append(" ")
//						.append(lineTotal)
//						.append(
//								" WHERE A.ID = (SELECT A.ID FROM ACCOUNT A JOIN TAXAGENCY TA ON A.ID = TA.ACCOUNT_ID JOIN TAXCODE TC ON TC.TAXAGENCY_ID = TA.ID WHERE TC.ID =")
//						.append(taxCodeId).append(")").toString());
//	}
//
//	private void updateCurrentAndTotalBalancesOfGridAccountOrItemAccount(
//			Statement stat, Double lineTotal, Long accountId, String symbol)
//			throws SQLException {
//		stat.execute(new StringBuilder().append(
//				"UPDATE ACCOUNT A SET A.CURRENT_BALANCE = A.CURRENT_BALANCE ")
//				.append(symbol).append(" ").append(lineTotal).append(
//						", A.TOTAL_BALANCE = A.TOTAL_BALANCE ").append(symbol)
//				.append(" ").append(lineTotal).append(" WHERE A.ID =").append(
//						accountId).toString());
//	}
//
//	private void getUpdateAccountForInvoiceAndCashSales(Statement stat,
//			Integer type, Long newItemId, Long newTaxCodeId, Long newAccountId,
//			Double newLineTotal, Long newTransactionId) throws SQLException {
//
//		Boolean isIncrease = Boolean.FALSE;
//
//		switch (type) {
//
//		case TransactionItem.TYPE_ITEM:
//
//			Long incomeAccountId = null;
//
//			ResultSet rs1 = stat
//					.executeQuery(new StringBuilder()
//							.append(
//									"SELECT A.ID,A.IS_INCREASE FROM ACCOUNT A JOIN ITEM I ON A.ID = I.INCOME_ACCOUNT_ID WHERE I.ID =")
//							.append(newItemId).toString());
//			if (rs1.next()) {
//				incomeAccountId = rs1.getLong(1);
//				isIncrease = rs1.getBoolean(2);
//			}
//
//			if (isIncrease) {
//
//				// Update the Current and total Balances of the Item Income
//				// Account.
//				updateCurrentAndTotalBalancesOfGridAccountOrItemAccount(stat,
//						newLineTotal, incomeAccountId,
//						AccounterConstants.SYMBOL_PLUS);
//
//			} else {
//				// Update the Current and total Balances of the Item Income
//				// Account.
//				updateCurrentAndTotalBalancesOfGridAccountOrItemAccount(stat,
//						newLineTotal, incomeAccountId,
//						AccounterConstants.SYMBOL_MINUS);
//			}
//
//			// inserting the corresponding rows into Account Transaction table
//			stat
//					.execute(new StringBuilder()
//							.append(
//									"INSERT INTO ACCOUNT_TRANSACTION (T_NUMBER,TYPE,USER_ID,T_ID,T_TYPE,T_DATE,AMOUNT) ")
//							.append(
//									"(SELECT (SELECT T.NUMBER FROM TRANSACTION T WHERE T.ID=")
//							.append(newTransactionId)
//							.append("),")
//							.append(AccountTransaction.TYPE_ACCOUNT)
//							.append(",A.ID,")
//							.append(newTransactionId)
//							.append(
//									",(SELECT T.T_TYPE FROM TRANSACTION T WHERE T.ID=")
//							.append(newTransactionId)
//							.append(
//									"),(SELECT T.T_DATE FROM TRANSACTION T WHERE T.ID=")
//							.append(newTransactionId)
//							.append("),(CASE WHEN A.IS_INCREASE ='TRUE' THEN ")
//							.append(newLineTotal)
//							.append(" ELSE -1*")
//							.append(newLineTotal)
//							.append(
//									" END ) FROM ACCOUNT A WHERE A.ID =(SELECT A.ID FROM ACCOUNT A JOIN ITEM I ON A.ID = I.INCOME_ACCOUNT_ID WHERE I.ID =")
//							.append(newItemId).append("))").toString());
//
//			break;
//
//		case TransactionItem.TYPE_SALESTAX:
//
//			ResultSet rs3 = stat
//					.executeQuery(new StringBuilder()
//							.append(
//									"SELECT A.IS_INCREASE FROM ACCOUNT A WHERE A.ID=(SELECT A.ID FROM ACCOUNT A JOIN TAXAGENCY TA ON A.ID = TA.ACCOUNT_ID JOIN TAXCODE TC ON TC.TAXAGENCY_ID = TA.ID WHERE TC.ID =")
//							.append(newTaxCodeId).append(")").toString());
//			if (rs3.next()) {
//				isIncrease = rs3.getBoolean(1);
//			}
//
//			if (isIncrease) {
//
//				// Updating the Current and Total Balances of corresponding
//				// TaxCode's Tax Agency Account.
//				updateCurrentAndTotalBalancesOfCorrespondingTaxAgencyAccount(
//						stat, newLineTotal, newTaxCodeId,
//						AccounterConstants.SYMBOL_PLUS);
//
//			} else {
//				// Updating the Current and Total Balances of corresponding
//				// TaxCode's Tax Agency Account.
//				updateCurrentAndTotalBalancesOfCorrespondingTaxAgencyAccount(
//						stat, newLineTotal, newTaxCodeId,
//						AccounterConstants.SYMBOL_MINUS);
//
//			}
//
//			// inserting the corresponding rows into Account Transaction table
//			stat
//					.execute(new StringBuilder()
//							.append(
//									"INSERT INTO ACCOUNT_TRANSACTION (T_NUMBER,TYPE,USER_ID,T_ID,T_TYPE,T_DATE,AMOUNT) ")
//							.append(
//									"(SELECT (SELECT T.NUMBER FROM TRANSACTION T WHERE T.ID=")
//							.append(newTransactionId)
//							.append("),")
//							.append(AccountTransaction.TYPE_ACCOUNT)
//							.append(",A.ID,")
//							.append(newTransactionId)
//							.append(
//									",(SELECT T.T_TYPE FROM TRANSACTION T WHERE T.ID=")
//							.append(newTransactionId)
//							.append(
//									"),(SELECT T.T_DATE FROM TRANSACTION T WHERE T.ID=")
//							.append(newTransactionId)
//							.append("),(CASE WHEN A.IS_INCREASE = 'TRUE' THEN ")
//							.append(newLineTotal)
//							.append(" ELSE -1*")
//							.append(newLineTotal)
//							.append(
//									" END ) FROM ACCOUNT A WHERE A.ID =(SELECT A.ID FROM ACCOUNT A JOIN TAXAGENCY TA ON A.ID = TA.ACCOUNT_ID JOIN TAXCODE TC ON TC.TAXAGENCY_ID = TA.ID WHERE TC.ID =")
//							.append(newTaxCodeId).append("))").toString());
//
//			// Need to Update the TaxAgencies Opening Balances to Increase
//
//			updateCorrespondingTaxAgencyBalance(stat, newLineTotal,
//					newTaxCodeId, AccounterConstants.SYMBOL_PLUS);
//
//			// inserting the corresponding rows into Account Transaction table
//			stat
//					.execute(new StringBuilder()
//							.append(
//									"INSERT INTO ACCOUNT_TRANSACTION (T_NUMBER,TYPE,USER_ID,T_ID,T_TYPE,T_DATE,AMOUNT) ")
//							.append(
//									"(SELECT (SELECT T.NUMBER FROM TRANSACTION T WHERE T.ID=")
//							.append(newTransactionId)
//							.append("),")
//							.append(AccountTransaction.TYPE_TAX_AGENCY)
//							.append(",TT.ID,")
//							.append(newTransactionId)
//							.append(
//									",(SELECT T.T_TYPE FROM TRANSACTION T WHERE T.ID=")
//							.append(newTransactionId)
//							.append(
//									"),(SELECT T.T_DATE FROM TRANSACTION T WHERE T.ID=")
//							.append(newTransactionId)
//							.append("),")
//							.append(newLineTotal)
//							.append(
//									" FROM TAXAGENCY TT WHERE TT.ID = (SELECT TA.ID FROM TAXAGENCY TA JOIN TAXCODE TC ON TC.TAXAGENCY_ID = TA.ID WHERE TC.ID =")
//							.append(newTaxCodeId).append("))").toString());
//
//			// Inserting the corresponding rows into PaySalesTaxEntries table.
//
//			stat
//					.execute(new StringBuilder()
//							.append(
//									"INSERT INTO PAY_SALES_TAX_ENTRIES (T_ID,T_DATE,TAXCODE_ID,TAXAGENCY_ID,AMOUNT,BALANCE) VALUES (")
//							.append(newTransactionId)
//							.append(
//									",(SELECT T.T_DATE FROM TRANSACTION T WHERE T.ID=")
//							.append(newTransactionId)
//							.append("),")
//							.append(newTaxCodeId)
//							.append(
//									",(SELECT TC.TAXAGENCY_ID FROM TAXCODE TC WHERE TC.ID=")
//							.append(newTaxCodeId).append("),").append(
//									newLineTotal).append(",").append(
//									newLineTotal).append(")").toString());
//
//			break;
//
//		case TransactionItem.TYPE_ACCOUNT:
//
//			ResultSet rs2 = stat.executeQuery(new StringBuilder().append(
//					"SELECT A.IS_INCREASE FROM ACCOUNT A WHERE A.ID=").append(
//					newAccountId).toString());
//			if (rs2.next()) {
//				isIncrease = rs2.getBoolean(1);
//			}
//
//			if (isIncrease) {
//
//				// Update the Current and total Balances of the selected Grid
//				// Account.
//				updateCurrentAndTotalBalancesOfGridAccountOrItemAccount(stat,
//						newLineTotal, newAccountId,
//						AccounterConstants.SYMBOL_PLUS);
//
//			} else {
//				// Update the Current and total Balances of the selected Grid
//				// Account.
//				updateCurrentAndTotalBalancesOfGridAccountOrItemAccount(stat,
//						newLineTotal, newAccountId,
//						AccounterConstants.SYMBOL_MINUS);
//
//			}
//
//			// inserting the corresponding rows into Account Transaction table
//			stat
//					.execute(new StringBuilder()
//							.append(
//									"INSERT INTO ACCOUNT_TRANSACTION (T_NUMBER,TYPE,USER_ID,T_ID,T_TYPE,T_DATE,AMOUNT) VALUES ")
//							.append(
//									"((SELECT T.NUMBER FROM TRANSACTION T WHERE T.ID=")
//							.append(newTransactionId)
//							.append("),")
//							.append(AccountTransaction.TYPE_ACCOUNT)
//							.append(",")
//							.append(newAccountId)
//							.append(",")
//							.append(newTransactionId)
//							.append(
//									",(SELECT T.T_TYPE FROM TRANSACTION T WHERE T.ID=")
//							.append(newTransactionId)
//							.append(
//									"),(SELECT T.T_DATE FROM TRANSACTION T WHERE T.ID=")
//							.append(newTransactionId)
//							.append(
//									"),(CASE WHEN (SELECT A.IS_INCREASE FROM ACCOUNT A WHERE A.ID =")
//							.append(newAccountId).append(")='TRUE' THEN ")
//							.append(newLineTotal).append(" ELSE -1*").append(
//									newLineTotal).append(" END ))").toString());
//
//			break;
//
//		}
//
//	}
//
//	private void getUpdateAccountForVendorCreditMemo(Statement stat,
//			Integer type, Long newItemId, Long newAccountId,
//			Double newLineTotal, Long newTransactionId) throws SQLException {
//
//		Boolean isIncrease = Boolean.FALSE;
//
//		switch (type) {
//		case TransactionItem.TYPE_ITEM:
//			Long expeseAccountId = null;
//			ResultSet rs1 = stat
//					.executeQuery(new StringBuilder()
//							.append(
//									"SELECT A.ID,A.IS_INCREASE FROM ACCOUNT A JOIN ITEM I ON A.ID = I.EXPENSE_ACCOUNT_ID WHERE I.ID =")
//							.append(newItemId).toString());
//			if (rs1.next()) {
//				expeseAccountId = rs1.getLong(1);
//				isIncrease = rs1.getBoolean(2);
//			}
//
//			if (isIncrease) {
//
//				// Update the Current and total Balances of the Item Expense
//				// Account.
//				updateCurrentAndTotalBalancesOfGridAccountOrItemAccount(stat,
//						newLineTotal, expeseAccountId,
//						AccounterConstants.SYMBOL_PLUS);
//
//			} else {
//				// Update the Current and total Balances of the Item Expense
//				// Account.
//				updateCurrentAndTotalBalancesOfGridAccountOrItemAccount(stat,
//						newLineTotal, expeseAccountId,
//						AccounterConstants.SYMBOL_MINUS);
//
//			}
//
//			// inserting the corresponding rows into Account Transaction table
//			stat
//					.execute(new StringBuilder()
//							.append(
//									"INSERT INTO ACCOUNT_TRANSACTION (T_NUMBER,TYPE,USER_ID,T_ID,T_TYPE,T_DATE,AMOUNT) ")
//							.append(
//									"(SELECT (SELECT T.NUMBER FROM TRANSACTION T WHERE T.ID=")
//							.append(newTransactionId)
//							.append("),")
//							.append(AccountTransaction.TYPE_ACCOUNT)
//							.append(",A.ID,")
//							.append(newTransactionId)
//							.append(
//									",(SELECT T.T_TYPE FROM TRANSACTION T WHERE T.ID=")
//							.append(newTransactionId)
//							.append(
//									"),(SELECT T.T_DATE FROM TRANSACTION T WHERE T.ID=")
//							.append(newTransactionId)
//							.append("),(CASE WHEN A.IS_INCREASE = 'TRUE' THEN ")
//							.append(newLineTotal)
//							.append(" ELSE -1*")
//							.append(newLineTotal)
//							.append(
//									" END ) FROM ACCOUNT A WHERE A.ID =(SELECT A.ID FROM ACCOUNT A JOIN ITEM I ON A.ID = I.EXPENSE_ACCOUNT_ID WHERE I.ID =")
//							.append(newItemId).append("))").toString());
//
//			break;
//
//		case TransactionItem.TYPE_ACCOUNT:
//
//			ResultSet rs2 = stat.executeQuery(new StringBuilder().append(
//					"SELECT A.IS_INCREASE FROM ACCOUNT A WHERE A.ID=").append(
//					newAccountId).toString());
//			if (rs2.next()) {
//				isIncrease = rs2.getBoolean(1);
//			}
//
//			if (isIncrease) {
//
//				// Update the Current and total Balances of the selected Grid
//				// Account.
//				updateCurrentAndTotalBalancesOfGridAccountOrItemAccount(stat,
//						newLineTotal, newAccountId,
//						AccounterConstants.SYMBOL_PLUS);
//
//			} else {
//				// Update the Current and total Balances of the selected Grid
//				// Account.
//				updateCurrentAndTotalBalancesOfGridAccountOrItemAccount(stat,
//						newLineTotal, newAccountId,
//						AccounterConstants.SYMBOL_MINUS);
//
//			}
//
//			// inserting the corresponding rows into Account Transaction table
//			stat
//					.execute(new StringBuilder()
//							.append(
//									"INSERT INTO ACCOUNT_TRANSACTION (T_NUMBER,TYPE,USER_ID,T_ID,T_TYPE,T_DATE,AMOUNT) VALUES ")
//							.append(
//									"((SELECT T.NUMBER FROM TRANSACTION T WHERE T.ID=")
//							.append(newTransactionId)
//							.append("),")
//							.append(AccountTransaction.TYPE_ACCOUNT)
//							.append(",")
//							.append(newAccountId)
//							.append(",")
//							.append(newTransactionId)
//							.append(
//									",(SELECT T.T_TYPE FROM TRANSACTION T WHERE T.ID=")
//							.append(newTransactionId)
//							.append(
//									"),(SELECT T.T_DATE FROM TRANSACTION T WHERE T.ID=")
//							.append(newTransactionId)
//							.append(
//									"),(CASE WHEN (SELECT A.IS_INCREASE FROM ACCOUNT A WHERE A.ID =")
//							.append(newAccountId).append(")='TRUE' THEN ")
//							.append(newLineTotal).append(" ELSE -1*").append(
//									newLineTotal).append(" END ))").toString());
//
//			break;
//
//		}
//
//	}
//
//	private void getUpdateAccountForEnterBillAndCashPurchaseAndCreditCardCharge(
//			Statement stat, Integer type, Long newItemId, Long newTaxCodeId,
//			Long newAccountId, Double newLineTotal, Long newTransactionId)
//			throws SQLException {
//
//		Boolean isIncrease = Boolean.FALSE;
//
//		switch (type) {
//		case TransactionItem.TYPE_ITEM:
//			Long expeseAccountId = null;
//
//			ResultSet rs1 = stat
//					.executeQuery(new StringBuilder()
//							.append(
//									"SELECT A.ID,A.IS_INCREASE FROM ACCOUNT A JOIN ITEM I ON A.ID = I.EXPENSE_ACCOUNT_ID WHERE I.ID =")
//							.append(newItemId).toString());
//			if (rs1.next()) {
//				expeseAccountId = rs1.getLong(1);
//				isIncrease = rs1.getBoolean(2);
//			}
//
//			if (isIncrease) {
//
//				// Update the Current and total Balances of the selected Item
//				// Expense Account.
//				updateCurrentAndTotalBalancesOfGridAccountOrItemAccount(stat,
//						newLineTotal, expeseAccountId,
//						AccounterConstants.SYMBOL_MINUS);
//
//			} else {
//				// Update the Current and total Balances of the selected Item
//				// Expense Account.
//				updateCurrentAndTotalBalancesOfGridAccountOrItemAccount(stat,
//						newLineTotal, expeseAccountId,
//						AccounterConstants.SYMBOL_PLUS);
//
//			}
//
//			// inserting the corresponding rows into Account Transaction table
//			stat
//					.execute(new StringBuilder()
//							.append(
//									"INSERT INTO ACCOUNT_TRANSACTION (T_NUMBER,TYPE,USER_ID,T_ID,T_TYPE,T_DATE,AMOUNT) ")
//							.append(
//									"(SELECT (SELECT T.NUMBER FROM TRANSACTION T WHERE T.ID=")
//							.append(newTransactionId)
//							.append("),")
//							.append(AccountTransaction.TYPE_ACCOUNT)
//							.append(",A.ID,")
//							.append(newTransactionId)
//							.append(
//									",(SELECT T.T_TYPE FROM TRANSACTION T WHERE T.ID=")
//							.append(newTransactionId)
//							.append(
//									"),(SELECT T.T_DATE FROM TRANSACTION T WHERE T.ID=")
//							.append(newTransactionId)
//							.append(
//									"),(CASE WHEN A.IS_INCREASE = 'TRUE' THEN -1*")
//							.append(newLineTotal)
//							.append(" ELSE ")
//							.append(newLineTotal)
//							.append(
//									" END ) FROM ACCOUNT A WHERE A.ID =(SELECT A.ID FROM ACCOUNT A JOIN ITEM I ON A.ID = I.EXPENSE_ACCOUNT_ID WHERE I.ID =")
//							.append(newItemId).append("))").toString());
//
//			break;
//
//		case TransactionItem.TYPE_ACCOUNT:
//
//			ResultSet rs2 = stat.executeQuery(new StringBuilder().append(
//					"SELECT A.IS_INCREASE FROM ACCOUNT A WHERE A.ID=").append(
//					newAccountId).toString());
//			if (rs2.next()) {
//				isIncrease = rs2.getBoolean(1);
//			}
//
//			if (isIncrease) {
//				// Update the Current and total Balances of the selected Grid
//				// Account.
//				updateCurrentAndTotalBalancesOfGridAccountOrItemAccount(stat,
//						newLineTotal, newAccountId,
//						AccounterConstants.SYMBOL_MINUS);
//
//			} else {
//				// Update the Current and total Balances of the selected Grid
//				// Account.
//				updateCurrentAndTotalBalancesOfGridAccountOrItemAccount(stat,
//						newLineTotal, newAccountId,
//						AccounterConstants.SYMBOL_PLUS);
//
//			}
//
//			// inserting the corresponding rows into Account Transaction table
//			stat
//					.execute(new StringBuilder()
//							.append(
//									"INSERT INTO ACCOUNT_TRANSACTION (T_NUMBER,TYPE,USER_ID,T_ID,T_TYPE,T_DATE,AMOUNT) VALUES ")
//							.append(
//									"((SELECT T.NUMBER FROM TRANSACTION T WHERE T.ID=")
//							.append(newTransactionId)
//							.append("),")
//							.append(AccountTransaction.TYPE_ACCOUNT)
//							.append(",")
//							.append(newAccountId)
//							.append(",")
//							.append(newTransactionId)
//							.append(
//									",(SELECT T.T_TYPE FROM TRANSACTION T WHERE T.ID=")
//							.append(newTransactionId)
//							.append(
//									"),(SELECT T.T_DATE FROM TRANSACTION T WHERE T.ID=")
//							.append(newTransactionId)
//							.append(
//									"),(CASE WHEN (SELECT A.IS_INCREASE FROM ACCOUNT A WHERE A.ID =")
//							.append(newAccountId).append(")='TRUE' THEN -1*")
//							.append(newLineTotal).append(" ELSE ").append(
//									newLineTotal).append(" END ))").toString());
//
//			break;
//
//		// This case is for the purpose of WriteChecks & Customer Credit Memo
//		// only
//
//		case TransactionItem.TYPE_SALESTAX:
//
//			ResultSet rs3 = stat
//					.executeQuery(new StringBuilder()
//							.append(
//									"SELECT A.IS_INCREASE FROM ACCOUNT A WHERE A.ID=(SELECT A.ID FROM ACCOUNT A JOIN TAXAGENCY TA ON A.ID = TA.ACCOUNT_ID JOIN TAXCODE TC ON TC.TAXAGENCY_ID = TA.ID WHERE TC.ID =")
//							.append(newTaxCodeId).append(")").toString());
//			if (rs3.next()) {
//				isIncrease = rs3.getBoolean(1);
//			}
//
//			if (isIncrease) {
//
//				// Updating the Current and Total Balances of corresponding
//				// TaxCode's Tax Agency Account.
//				updateCurrentAndTotalBalancesOfCorrespondingTaxAgencyAccount(
//						stat, newLineTotal, newTaxCodeId,
//						AccounterConstants.SYMBOL_MINUS);
//
//			} else {
//				// Updating the Current and Total Balances of corresponding
//				// TaxCode's Tax Agency Account.
//				updateCurrentAndTotalBalancesOfCorrespondingTaxAgencyAccount(
//						stat, newLineTotal, newTaxCodeId,
//						AccounterConstants.SYMBOL_PLUS);
//
//			}
//
//			// inserting the corresponding rows into Account Transaction table
//			stat
//					.execute(new StringBuilder()
//							.append(
//									"INSERT INTO ACCOUNT_TRANSACTION (T_NUMBER,TYPE,USER_ID,T_ID,T_TYPE,T_DATE,AMOUNT) ")
//							.append(
//									"(SELECT (SELECT T.NUMBER FROM TRANSACTION T WHERE T.ID=")
//							.append(newTransactionId)
//							.append("),")
//							.append(AccountTransaction.TYPE_ACCOUNT)
//							.append(",A.ID,")
//							.append(newTransactionId)
//							.append(
//									",(SELECT T.T_TYPE FROM TRANSACTION T WHERE T.ID=")
//							.append(newTransactionId)
//							.append(
//									"),(SELECT T.T_DATE FROM TRANSACTION T WHERE T.ID=")
//							.append(newTransactionId)
//							.append(
//									"),(CASE WHEN A.IS_INCREASE = 'TRUE' THEN -1*")
//							.append(newLineTotal)
//							.append(" ELSE ")
//							.append(newLineTotal)
//							.append(
//									" END ) FROM ACCOUNT A WHERE A.ID =(SELECT A.ID FROM ACCOUNT A JOIN TAXAGENCY TA ON A.ID = TA.ACCOUNT_ID JOIN TAXCODE TC ON TC.TAXAGENCY_ID = TA.ID WHERE TC.ID =")
//							.append(newTaxCodeId).append("))").toString());
//
//			// Need to Update the TaxAgencies Opening Balances to Decrease
//
//			updateCorrespondingTaxAgencyBalance(stat, newLineTotal,
//					newTaxCodeId, AccounterConstants.SYMBOL_MINUS);
//
//			// inserting the corresponding rows into Account Transaction table
//			stat
//					.execute(new StringBuilder()
//							.append(
//									"INSERT INTO ACCOUNT_TRANSACTION (T_NUMBER,TYPE,USER_ID,T_ID,T_TYPE,T_DATE,AMOUNT) ")
//							.append(
//									"(SELECT (SELECT T.NUMBER FROM TRANSACTION T WHERE T.ID=")
//							.append(newTransactionId)
//							.append("),")
//							.append(AccountTransaction.TYPE_TAX_AGENCY)
//							.append(",TT.ID,")
//							.append(newTransactionId)
//							.append(
//									",(SELECT T.T_TYPE FROM TRANSACTION T WHERE T.ID=")
//							.append(newTransactionId)
//							.append(
//									"),(SELECT T.T_DATE FROM TRANSACTION T WHERE T.ID=")
//							.append(newTransactionId)
//							.append("),-1*")
//							.append(newLineTotal)
//							.append(
//									" FROM TAXAGENCY TT WHERE TT.ID = (SELECT TA.ID FROM TAXAGENCY TA JOIN TAXCODE TC ON TC.TAXAGENCY_ID = TA.ID WHERE TC.ID =")
//							.append(newTaxCodeId).append("))").toString());
//
//			break;
//
//		}
//
//	}
//
//	private void getUpdateAccountForCustomerCreditMemoAndWriteChecks(
//			Statement stat, Integer type, Long newItemId, Long newTaxCodeId,
//			Long newAccountId, Double newLineTotal, Long newTransactionId)
//			throws SQLException {
//
//		Boolean isIncrease = Boolean.FALSE;
//
//		switch (type) {
//		case TransactionItem.TYPE_ITEM:
//			Long incomeAccountId = null;
//
//			ResultSet rs1 = stat
//					.executeQuery(new StringBuilder()
//							.append(
//									"SELECT A.ID,A.IS_INCREASE FROM ACCOUNT A JOIN ITEM I ON A.ID = I.INCOME_ACCOUNT_ID WHERE I.ID =")
//							.append(newItemId).toString());
//			if (rs1.next()) {
//				incomeAccountId = rs1.getLong(1);
//				isIncrease = rs1.getBoolean(2);
//			}
//
//			if (isIncrease) {
//				// Update the Current and total Balances of the selected Item
//				// Income Account.
//				updateCurrentAndTotalBalancesOfGridAccountOrItemAccount(stat,
//						newLineTotal, incomeAccountId,
//						AccounterConstants.SYMBOL_MINUS);
//
//			} else {
//				// Update the Current and total Balances of the selected Item
//				// Income Account.
//				updateCurrentAndTotalBalancesOfGridAccountOrItemAccount(stat,
//						newLineTotal, incomeAccountId,
//						AccounterConstants.SYMBOL_PLUS);
//
//			}
//
//			// inserting the corresponding rows into Account Transaction table
//			stat
//					.execute(new StringBuilder()
//							.append(
//									"INSERT INTO ACCOUNT_TRANSACTION (T_NUMBER,TYPE,USER_ID,T_ID,T_TYPE,T_DATE,AMOUNT) ")
//							.append(
//									"(SELECT (SELECT T.NUMBER FROM TRANSACTION T WHERE T.ID=")
//							.append(newTransactionId)
//							.append("),")
//							.append(AccountTransaction.TYPE_ACCOUNT)
//							.append(",A.ID,")
//							.append(newTransactionId)
//							.append(
//									",(SELECT T.T_TYPE FROM TRANSACTION T WHERE T.ID=")
//							.append(newTransactionId)
//							.append(
//									"),(SELECT T.T_DATE FROM TRANSACTION T WHERE T.ID=")
//							.append(newTransactionId)
//							.append(
//									"),(CASE WHEN A.IS_INCREASE = 'TRUE' THEN -1*")
//							.append(newLineTotal)
//							.append(" ELSE ")
//							.append(newLineTotal)
//							.append(
//									" END ) FROM ACCOUNT A WHERE A.ID =(SELECT A.ID FROM ACCOUNT A JOIN ITEM I ON A.ID = I.INCOME_ACCOUNT_ID WHERE I.ID =")
//							.append(newItemId).append("))").toString());
//
//			break;
//
//		case TransactionItem.TYPE_ACCOUNT:
//
//			ResultSet rs2 = stat.executeQuery(new StringBuilder().append(
//					"SELECT A.IS_INCREASE FROM ACCOUNT A WHERE A.ID=").append(
//					newAccountId).toString());
//			if (rs2.next()) {
//				isIncrease = rs2.getBoolean(1);
//			}
//
//			if (isIncrease) {
//
//				// Update the Current and total Balances of the selected Grid
//				// Account.
//				updateCurrentAndTotalBalancesOfGridAccountOrItemAccount(stat,
//						newLineTotal, newAccountId,
//						AccounterConstants.SYMBOL_MINUS);
//
//			} else {
//				// Update the Current and total Balances of the selected Grid
//				// Account.
//				updateCurrentAndTotalBalancesOfGridAccountOrItemAccount(stat,
//						newLineTotal, newAccountId,
//						AccounterConstants.SYMBOL_PLUS);
//
//			}
//
//			// inserting the corresponding rows into Account Transaction table
//			stat
//					.execute(new StringBuilder()
//							.append(
//									"INSERT INTO ACCOUNT_TRANSACTION (T_NUMBER,TYPE,USER_ID,T_ID,T_TYPE,T_DATE,AMOUNT) VALUES ")
//							.append(
//									"((SELECT T.NUMBER FROM TRANSACTION T WHERE T.ID=")
//							.append(newTransactionId)
//							.append("),")
//							.append(AccountTransaction.TYPE_ACCOUNT)
//							.append(",")
//							.append(newAccountId)
//							.append(",")
//							.append(newTransactionId)
//							.append(
//									",(SELECT T.T_TYPE FROM TRANSACTION T WHERE T.ID=")
//							.append(newTransactionId)
//							.append(
//									"),(SELECT T.T_DATE FROM TRANSACTION T WHERE T.ID=")
//							.append(newTransactionId)
//							.append(
//									"),(CASE WHEN (SELECT A.IS_INCREASE FROM ACCOUNT A WHERE A.ID =")
//							.append(newAccountId).append(")='TRUE' THEN -1*")
//							.append(newLineTotal).append(" ELSE ").append(
//									newLineTotal).append(" END ))").toString());
//
//			break;
//
//		// This case is for the purpose of WriteChecks & Customer Credit Memo
//		// only
//
//		case TransactionItem.TYPE_SALESTAX:
//
//			ResultSet rs3 = stat
//					.executeQuery(new StringBuilder()
//							.append(
//									"SELECT A.IS_INCREASE FROM ACCOUNT A WHERE A.ID=(SELECT A.ID FROM ACCOUNT A JOIN TAXAGENCY TA ON A.ID = TA.ACCOUNT_ID JOIN TAXCODE TC ON TC.TAXAGENCY_ID = TA.ID WHERE TC.ID =")
//							.append(newTaxCodeId).append(")").toString());
//			if (rs3.next()) {
//				isIncrease = rs3.getBoolean(1);
//			}
//
//			if (rs3.getBoolean(1)) {
//
//				// Updating the Current and Total Balances of corresponding
//				// TaxCode's Tax Agency Account.
//				updateCurrentAndTotalBalancesOfCorrespondingTaxAgencyAccount(
//						stat, newLineTotal, newTaxCodeId,
//						AccounterConstants.SYMBOL_MINUS);
//
//			} else {
//				// Updating the Current and Total Balances of corresponding
//				// TaxCode's Tax Agency Account.
//				updateCurrentAndTotalBalancesOfCorrespondingTaxAgencyAccount(
//						stat, newLineTotal, newTaxCodeId,
//						AccounterConstants.SYMBOL_PLUS);
//
//			}
//
//			// inserting the corresponding rows into Account Transaction table
//			stat
//					.execute(new StringBuilder()
//							.append(
//									"INSERT INTO ACCOUNT_TRANSACTION (T_NUMBER,TYPE,USER_ID,T_ID,T_TYPE,T_DATE,AMOUNT) ")
//							.append(
//									"(SELECT (SELECT T.NUMBER FROM TRANSACTION T WHERE T.ID=")
//							.append(newTransactionId)
//							.append("),")
//							.append(AccountTransaction.TYPE_ACCOUNT)
//							.append(",A.ID,")
//							.append(newTransactionId)
//							.append(
//									",(SELECT T.T_TYPE FROM TRANSACTION T WHERE T.ID=")
//							.append(newTransactionId)
//							.append(
//									"),(SELECT T.T_DATE FROM TRANSACTION T WHERE T.ID=")
//							.append(newTransactionId)
//							.append(
//									"),(CASE WHEN A.IS_INCREASE = 'TRUE' THEN -1*")
//							.append(newLineTotal)
//							.append(" ELSE ")
//							.append(newLineTotal)
//							.append(
//									" END ) FROM ACCOUNT A WHERE A.ID =(SELECT A.ID FROM ACCOUNT A JOIN TAXAGENCY TA ON A.ID = TA.ACCOUNT_ID JOIN TAXCODE TC ON TC.TAXAGENCY_ID = TA.ID WHERE TC.ID =")
//							.append(newTaxCodeId).append("))").toString());
//
//			// Need to Update the TaxAgencies Opening Balances to Decrease
//
//			updateCorrespondingTaxAgencyBalance(stat, newLineTotal,
//					newTaxCodeId, AccounterConstants.SYMBOL_MINUS);
//
//			// inserting the corresponding rows into Account Transaction table
//			stat
//					.execute(new StringBuilder()
//							.append(
//									"INSERT INTO ACCOUNT_TRANSACTION (T_NUMBER,TYPE,USER_ID,T_ID,T_TYPE,T_DATE,AMOUNT) ")
//							.append(
//									"(SELECT (SELECT T.NUMBER FROM TRANSACTION T WHERE T.ID=")
//							.append(newTransactionId)
//							.append("),")
//							.append(AccountTransaction.TYPE_TAX_AGENCY)
//							.append(",TT.ID,")
//							.append(newTransactionId)
//							.append(
//									",(SELECT T.T_TYPE FROM TRANSACTION T WHERE T.ID=")
//							.append(newTransactionId)
//							.append(
//									"),(SELECT T.T_DATE FROM TRANSACTION T WHERE T.ID=")
//							.append(newTransactionId)
//							.append("),-1*")
//							.append(newLineTotal)
//							.append(
//									" FROM TAXAGENCY TT WHERE TT.ID = (SELECT TA.ID FROM TAXAGENCY TA JOIN TAXCODE TC ON TC.TAXAGENCY_ID = TA.ID WHERE TC.ID =")
//							.append(newTaxCodeId).append("))").toString());
//
//			// Inserting the corresponding rows into PaySalesTaxEntries table.
//
//			stat
//					.execute(new StringBuilder()
//							.append(
//									"INSERT INTO PAY_SALES_TAX_ENTRIES (T_ID,T_DATE,TAXCODE_ID,TAXAGENCY_ID,AMOUNT,BALANCE) VALUES (")
//							.append(newTransactionId)
//							.append(
//									",(SELECT T.T_DATE FROM TRANSACTION T WHERE T.ID=")
//							.append(newTransactionId)
//							.append("),")
//							.append(newTaxCodeId)
//							.append(
//									",(SELECT TC.TAXAGENCY_ID FROM TAXCODE TC WHERE TC.ID=")
//							.append(newTaxCodeId).append("),").append(
//									newLineTotal).append(",").append(
//									newLineTotal).append(")").toString());
//
//			break;
//
//		}
//
//	}
//
//	private void getReverseUpdateAccountForInvoiceAndCashSales(Statement stat,
//			Integer type, Long newItemId, Long newTaxCodeId, Long newAccountId,
//			Double newLineTotal, Long newTransactionId) throws SQLException {
//
//		// Deleting corresponding transaction rows from Account Transaction
//		// table
//		stat.execute(new StringBuilder().append(
//				"DELETE FROM ACCOUNT_TRANSACTION WHERE T_ID =").append(
//				newTransactionId).toString());
//
//		Boolean isIncrease = Boolean.FALSE;
//
//		switch (type) {
//		case TransactionItem.TYPE_ITEM:
//
//			Long incomeAccountId = null;
//
//			ResultSet rs1 = stat
//					.executeQuery(new StringBuilder()
//							.append(
//									"SELECT A.ID,A.IS_INCREASE FROM ACCOUNT A JOIN ITEM I ON A.ID = I.INCOME_ACCOUNT_ID WHERE I.ID =")
//							.append(newItemId).toString());
//			if (rs1.next()) {
//				incomeAccountId = rs1.getLong(1);
//				isIncrease = rs1.getBoolean(2);
//			}
//
//			if (isIncrease) {
//
//				// Update the Current and total Balances of the selected Item
//				// Income Account.
//				updateCurrentAndTotalBalancesOfGridAccountOrItemAccount(stat,
//						newLineTotal, incomeAccountId,
//						AccounterConstants.SYMBOL_MINUS);
//
//			} else {
//				// Update the Current and total Balances of the selected Item
//				// Income Account.
//				updateCurrentAndTotalBalancesOfGridAccountOrItemAccount(stat,
//						newLineTotal, incomeAccountId,
//						AccounterConstants.SYMBOL_PLUS);
//
//			}
//
//			break;
//
//		case TransactionItem.TYPE_SALESTAX:
//
//			ResultSet rs3 = stat
//					.executeQuery(new StringBuilder()
//							.append(
//									"SELECT A.IS_INCREASE FROM ACCOUNT A WHERE A.ID=(SELECT A.ID FROM ACCOUNT A JOIN TAXAGENCY TA ON A.ID = TA.ACCOUNT_ID JOIN TAXCODE TC ON TC.TAXAGENCY_ID = TA.ID WHERE TC.ID =")
//							.append(newTaxCodeId).append(")").toString());
//			if (rs3.next()) {
//				isIncrease = rs3.getBoolean(1);
//			}
//
//			if (rs3.getBoolean(1)) {
//
//				// Updating the Current and Total Balances of corresponding
//				// TaxCode's Tax Agency Account.
//				updateCurrentAndTotalBalancesOfCorrespondingTaxAgencyAccount(
//						stat, newLineTotal, newTaxCodeId,
//						AccounterConstants.SYMBOL_MINUS);
//
//			} else {
//				// Updating the Current and Total Balances of corresponding
//				// TaxCode's Tax Agency Account.
//				updateCurrentAndTotalBalancesOfCorrespondingTaxAgencyAccount(
//						stat, newLineTotal, newTaxCodeId,
//						AccounterConstants.SYMBOL_PLUS);
//			}
//
//			// Neet to Update the TaxAgencies Opening Balances to Decrease
//			updateCorrespondingTaxAgencyBalance(stat, newLineTotal,
//					newTaxCodeId, AccounterConstants.SYMBOL_MINUS);
//			break;
//
//		case TransactionItem.TYPE_ACCOUNT:
//
//			ResultSet rs2 = stat.executeQuery(new StringBuilder().append(
//					"SELECT A.IS_INCREASE FROM ACCOUNT A WHERE A.ID=").append(
//					newAccountId).toString());
//			if (rs2.next()) {
//				isIncrease = rs2.getBoolean(1);
//			}
//
//			if (isIncrease) {
//
//				// Update the Current and total Balances of the selected Grid
//				// Account.
//				updateCurrentAndTotalBalancesOfGridAccountOrItemAccount(stat,
//						newLineTotal, newAccountId,
//						AccounterConstants.SYMBOL_MINUS);
//
//			} else {
//				// Update the Current and total Balances of the selected Grid
//				// Account.
//				updateCurrentAndTotalBalancesOfGridAccountOrItemAccount(stat,
//						newLineTotal, newAccountId,
//						AccounterConstants.SYMBOL_PLUS);
//
//			}
//
//			break;
//
//		}
//
//	}
//
//	private void getReverseUpdateAccountForVendorCreditMemo(Statement stat,
//			Integer type, Long newItemId, Long newAccountId,
//			Double newLineTotal, Long newTransactionId) throws SQLException {
//
//		// Deleting corresponding transaction rows from Account Transaction
//		// table
//		stat.execute(new StringBuilder().append(
//				"DELETE FROM ACCOUNT_TRANSACTION WHERE T_ID =").append(
//				newTransactionId).toString());
//		Boolean isIncrease = Boolean.FALSE;
//
//		switch (type) {
//		case TransactionItem.TYPE_ITEM:
//			Long expenseAccountId = null;
//			ResultSet rs1 = stat
//					.executeQuery(new StringBuilder()
//							.append(
//									"SELECT A.ID,A.IS_INCREASE FROM ACCOUNT A JOIN ITEM I ON A.ID = I.EXPENSE_ACCOUNT_ID WHERE I.ID =")
//							.append(newItemId).toString());
//			if (rs1.next()) {
//				expenseAccountId = rs1.getLong(1);
//				isIncrease = rs1.getBoolean(2);
//			}
//
//			if (isIncrease) {
//				// Update the Current and total Balances of the selected Item
//				// Expense Account.
//				updateCurrentAndTotalBalancesOfGridAccountOrItemAccount(stat,
//						newLineTotal, expenseAccountId,
//						AccounterConstants.SYMBOL_MINUS);
//
//			} else {
//				// Update the Current and total Balances of the selected Item
//				// Expense Account.
//				updateCurrentAndTotalBalancesOfGridAccountOrItemAccount(stat,
//						newLineTotal, expenseAccountId,
//						AccounterConstants.SYMBOL_PLUS);
//
//			}
//
//			break;
//
//		case TransactionItem.TYPE_ACCOUNT:
//
//			ResultSet rs2 = stat.executeQuery(new StringBuilder().append(
//					"SELECT A.IS_INCREASE FROM ACCOUNT A WHERE A.ID=").append(
//					newAccountId).toString());
//			if (rs2.next()) {
//				isIncrease = rs2.getBoolean(1);
//			}
//
//			if (isIncrease) {
//
//				// Update the Current and total Balances of the selected Grid
//				// Account.
//				updateCurrentAndTotalBalancesOfGridAccountOrItemAccount(stat,
//						newLineTotal, newAccountId,
//						AccounterConstants.SYMBOL_MINUS);
//
//			} else {
//				// Update the Current and total Balances of the selected Grid
//				// Account.
//				updateCurrentAndTotalBalancesOfGridAccountOrItemAccount(stat,
//						newLineTotal, newAccountId,
//						AccounterConstants.SYMBOL_PLUS);
//
//			}
//
//			break;
//
//		}
//
//	}
//
//	private void getReverseUpdateAccountForEnterBillAndCashPurchaseAndCreditCardCharge(
//			Statement stat, Integer type, Long newItemId, Long newTaxCodeId,
//			Long newAccountId, Double newLineTotal, Long newTransactionId)
//			throws SQLException {
//
//		// Deleting corresponding transaction rows from Account Transaction
//		// table
//		stat.execute(new StringBuilder().append(
//				"DELETE FROM ACCOUNT_TRANSACTION WHERE T_ID =").append(
//				newTransactionId).toString());
//		Boolean isIncrease = Boolean.FALSE;
//
//		switch (type) {
//		case TransactionItem.TYPE_ITEM:
//			Long expenseAccountId = null;
//
//			ResultSet rs1 = stat
//					.executeQuery(new StringBuilder()
//							.append(
//									"SELECT A.ID,A.IS_INCREASE FROM ACCOUNT A JOIN ITEM I ON A.ID = I.EXPENSE_ACCOUNT_ID WHERE I.ID =")
//							.append(newItemId).toString());
//			if (rs1.next()) {
//				expenseAccountId = rs1.getLong(1);
//				isIncrease = rs1.getBoolean(2);
//			}
//
//			if (isIncrease) {
//				// Update the Current and total Balances of the selected Item
//				// Expense Account.
//				updateCurrentAndTotalBalancesOfGridAccountOrItemAccount(stat,
//						newLineTotal, expenseAccountId,
//						AccounterConstants.SYMBOL_PLUS);
//
//			} else {
//				// Update the Current and total Balances of the selected Item
//				// Expense Account.
//				updateCurrentAndTotalBalancesOfGridAccountOrItemAccount(stat,
//						newLineTotal, expenseAccountId,
//						AccounterConstants.SYMBOL_MINUS);
//
//			}
//
//			break;
//
//		case TransactionItem.TYPE_ACCOUNT:
//
//			ResultSet rs2 = stat.executeQuery(new StringBuilder().append(
//					"SELECT A.IS_INCREASE FROM ACCOUNT A WHERE A.ID=").append(
//					newAccountId).toString());
//			if (rs2.next()) {
//				isIncrease = rs2.getBoolean(1);
//			}
//
//			if (isIncrease) {
//
//				// Update the Current and total Balances of the selected Grid
//				// Account.
//				updateCurrentAndTotalBalancesOfGridAccountOrItemAccount(stat,
//						newLineTotal, newAccountId,
//						AccounterConstants.SYMBOL_PLUS);
//
//			} else {
//
//				// Update the Current and total Balances of the selected Grid
//				// Account.
//				updateCurrentAndTotalBalancesOfGridAccountOrItemAccount(stat,
//						newLineTotal, newAccountId,
//						AccounterConstants.SYMBOL_MINUS);
//
//			}
//
//			break;
//
//		// This case is for the purpose of WriteChecks & Customer Credit Memo
//		// only
//
//		case TransactionItem.TYPE_SALESTAX:
//
//			ResultSet rs3 = stat
//					.executeQuery(new StringBuilder()
//							.append(
//									"SELECT A.IS_INCREASE FROM ACCOUNT A WHERE A.ID=(SELECT A.ID FROM ACCOUNT A JOIN TAXAGENCY TA ON A.ID = TA.ACCOUNT_ID JOIN TAXCODE TC ON TC.TAXAGENCY_ID = TA.ID WHERE TC.ID =")
//							.append(newTaxCodeId).append(")").toString());
//			if (rs3.next()) {
//				isIncrease = rs3.getBoolean(1);
//			}
//
//			if (isIncrease) {
//
//				// Updating the Current and Total Balances of corresponding
//				// TaxCode's Tax Agency Account.
//				updateCurrentAndTotalBalancesOfCorrespondingTaxAgencyAccount(
//						stat, newLineTotal, newTaxCodeId,
//						AccounterConstants.SYMBOL_PLUS);
//
//			} else {
//				// Updating the Current and Total Balances of corresponding
//				// TaxCode's Tax Agency Account.
//				updateCurrentAndTotalBalancesOfCorrespondingTaxAgencyAccount(
//						stat, newLineTotal, newTaxCodeId,
//						AccounterConstants.SYMBOL_MINUS);
//			}
//
//			// Need to Update the TaxAgencies Opening Balances to Increase
//			updateCorrespondingTaxAgencyBalance(stat, newLineTotal,
//					newTaxCodeId, AccounterConstants.SYMBOL_PLUS);
//
//			break;
//
//		}
//
//	}
//
//	private void getReverseUpdateAccountForCustomerCreditMemoAndWriteChecks(
//			Statement stat, Integer type, Long newItemId, Long newTaxCodeId,
//			Long newAccountId, Double newLineTotal, Long newTransactionId)
//			throws SQLException {
//
//		// Deleting corresponding transaction rows from Account Transaction
//		// table
//		stat.execute(new StringBuilder().append(
//				"DELETE FROM ACCOUNT_TRANSACTION WHERE T_ID =").append(
//				newTransactionId).toString());
//		Boolean isIncrease = Boolean.FALSE;
//
//		switch (type) {
//		case TransactionItem.TYPE_ITEM:
//			Long incomeAccountId = null;
//			ResultSet rs1 = stat
//					.executeQuery(new StringBuilder()
//							.append(
//									"SELECT A.ID,A.IS_INCREASE FROM ACCOUNT A JOIN ITEM I ON A.ID = I.INCOME_ACCOUNT_ID WHERE I.ID =")
//							.append(newItemId).toString());
//			if (rs1.next()) {
//				incomeAccountId = rs1.getLong(1);
//				isIncrease = rs1.getBoolean(2);
//			}
//
//			if (isIncrease) {
//
//				// Update the Current and total Balances of the selected Item
//				// Income Account.
//				updateCurrentAndTotalBalancesOfGridAccountOrItemAccount(stat,
//						newLineTotal, incomeAccountId,
//						AccounterConstants.SYMBOL_PLUS);
//
//			} else {
//
//				// Update the Current and total Balances of the selected Item
//				// Income Account.
//				updateCurrentAndTotalBalancesOfGridAccountOrItemAccount(stat,
//						newLineTotal, incomeAccountId,
//						AccounterConstants.SYMBOL_MINUS);
//
//			}
//
//			break;
//
//		case TransactionItem.TYPE_ACCOUNT:
//
//			ResultSet rs2 = stat.executeQuery(new StringBuilder().append(
//					"SELECT A.IS_INCREASE FROM ACCOUNT A WHERE A.ID=").append(
//					newAccountId).toString());
//			if (rs2.next()) {
//				isIncrease = rs2.getBoolean(1);
//			}
//
//			if (isIncrease) {
//
//				// Update the Current and total Balances of the selected Grid
//				// Account.
//				updateCurrentAndTotalBalancesOfGridAccountOrItemAccount(stat,
//						newLineTotal, newAccountId,
//						AccounterConstants.SYMBOL_PLUS);
//
//			} else {
//
//				// Update the Current and total Balances of the selected Grid
//				// Account.
//				updateCurrentAndTotalBalancesOfGridAccountOrItemAccount(stat,
//						newLineTotal, newAccountId,
//						AccounterConstants.SYMBOL_MINUS);
//
//			}
//
//			break;
//
//		// This case is for the purpose of WriteChecks & Customer Credit Memo
//		// only
//
//		case TransactionItem.TYPE_SALESTAX:
//
//			ResultSet rs3 = stat
//					.executeQuery(new StringBuilder()
//							.append(
//									"SELECT A.IS_INCREASE FROM ACCOUNT A WHERE A.ID=(SELECT A.ID FROM ACCOUNT A JOIN TAXAGENCY TA ON A.ID = TA.ACCOUNT_ID JOIN TAXCODE TC ON TC.TAXAGENCY_ID = TA.ID WHERE TC.ID =")
//							.append(newTaxCodeId).append(")").toString());
//			if (rs3.next()) {
//				isIncrease = rs3.getBoolean(1);
//			}
//
//			if (isIncrease) {
//
//				// Updating the Current and Total Balances of corresponding
//				// TaxCode's Tax Agency Account.
//				updateCurrentAndTotalBalancesOfCorrespondingTaxAgencyAccount(
//						stat, newLineTotal, newTaxCodeId,
//						AccounterConstants.SYMBOL_PLUS);
//
//			} else {
//				// Updating the Current and Total Balances of corresponding
//				// TaxCode's Tax Agency Account.
//				updateCurrentAndTotalBalancesOfCorrespondingTaxAgencyAccount(
//						stat, newLineTotal, newTaxCodeId,
//						AccounterConstants.SYMBOL_MINUS);
//			}
//
//			// Need to Update the TaxAgencies Opening Balances to Increase
//			updateCorrespondingTaxAgencyBalance(stat, newLineTotal,
//					newTaxCodeId, AccounterConstants.SYMBOL_PLUS);
//
//			break;
//
//		}
//
//	}
//
//	@Override
//	public void init(Connection arg0, String arg1, String arg2, String arg3,
//			boolean arg4, int arg5) throws SQLException {
//		// TODO Auto-generated method stub
//
//	}
//
//}
