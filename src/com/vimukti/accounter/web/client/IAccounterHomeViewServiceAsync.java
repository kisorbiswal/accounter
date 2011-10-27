/**
 * 
 */
package com.vimukti.accounter.web.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientActivity;
import com.vimukti.accounter.web.client.core.ClientBudget;
import com.vimukti.accounter.web.client.core.ClientCashPurchase;
import com.vimukti.accounter.web.client.core.ClientCashSales;
import com.vimukti.accounter.web.client.core.ClientCreditCardCharge;
import com.vimukti.accounter.web.client.core.ClientCreditsAndPayments;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientCustomerRefund;
import com.vimukti.accounter.web.client.core.ClientEnterBill;
import com.vimukti.accounter.web.client.core.ClientEntry;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientJournalEntry;
import com.vimukti.accounter.web.client.core.ClientMakeDeposit;
import com.vimukti.accounter.web.client.core.ClientPayTAXEntries;
import com.vimukti.accounter.web.client.core.ClientReceivePayment;
import com.vimukti.accounter.web.client.core.ClientReceiveVATEntries;
import com.vimukti.accounter.web.client.core.ClientRecurringTransaction;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ClientTransactionMakeDeposit;
import com.vimukti.accounter.web.client.core.ClientTransferFund;
import com.vimukti.accounter.web.client.core.ClientUserInfo;
import com.vimukti.accounter.web.client.core.ClientVATReturn;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ClientWriteCheck;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.core.Lists.BillsList;
import com.vimukti.accounter.web.client.core.Lists.ClientTDSInfo;
import com.vimukti.accounter.web.client.core.Lists.CustomerRefundsList;
import com.vimukti.accounter.web.client.core.Lists.DepreciableFixedAssetsList;
import com.vimukti.accounter.web.client.core.Lists.EstimatesAndSalesOrdersList;
import com.vimukti.accounter.web.client.core.Lists.FixedAssetLinkedAccountMap;
import com.vimukti.accounter.web.client.core.Lists.FixedAssetSellOrDisposeReviewJournal;
import com.vimukti.accounter.web.client.core.Lists.InvoicesList;
import com.vimukti.accounter.web.client.core.Lists.IssuePaymentTransactionsList;
import com.vimukti.accounter.web.client.core.Lists.OverDueInvoicesList;
import com.vimukti.accounter.web.client.core.Lists.PayBillTransactionList;
import com.vimukti.accounter.web.client.core.Lists.PayeeList;
import com.vimukti.accounter.web.client.core.Lists.PaymentsList;
import com.vimukti.accounter.web.client.core.Lists.PurchaseOrdersAndItemReceiptsList;
import com.vimukti.accounter.web.client.core.Lists.PurchaseOrdersList;
import com.vimukti.accounter.web.client.core.Lists.ReceivePaymentTransactionList;
import com.vimukti.accounter.web.client.core.Lists.ReceivePaymentsList;
import com.vimukti.accounter.web.client.core.Lists.SalesOrdersList;
import com.vimukti.accounter.web.client.core.Lists.TempFixedAsset;

/**
 * @author Fernandez
 * 
 */
public interface IAccounterHomeViewServiceAsync {

	public void getOverDueInvoices(
			AsyncCallback<ArrayList<OverDueInvoicesList>> callBack);

	public void getBillsOwed(AsyncCallback<ArrayList<ClientEnterBill>> callBack);

	public void getLatestQuotes(
			AsyncCallback<ArrayList<ClientEstimate>> callBack);

	public void getBillsAndItemReceiptList(boolean isExpensesList,
			AsyncCallback<ArrayList<BillsList>> callBack);

	public void getTransactionPayBills(
			AsyncCallback<ArrayList<PayBillTransactionList>> callback);

	public void getTransactionPayBills(long vendorId,
			AsyncCallback<ArrayList<PayBillTransactionList>> callback);

	public void getVendorPaymentsList(
			AsyncCallback<ArrayList<PaymentsList>> callBack);

	public void getPaymentsList(AsyncCallback<ArrayList<PaymentsList>> callBack);

	public void getRecurringsList(
			AsyncCallback<ArrayList<ClientRecurringTransaction>> callBack);

	public void getVendorCreditsAndPayments(long vendorId,
			AsyncCallback<ArrayList<ClientCreditsAndPayments>> callBack);

	public void getChecks(
			AsyncCallback<ArrayList<IssuePaymentTransactionsList>> callBack);

	public void getChecks(long accountId,
			AsyncCallback<ArrayList<IssuePaymentTransactionsList>> callback);

	public void getCreditCardChargesThisMonth(long date,
			AsyncCallback<ArrayList<ClientCreditCardCharge>> callback);

	public void getLatestCustomers(
			AsyncCallback<ArrayList<ClientCustomer>> callBack);

	public void getLatestVendors(AsyncCallback<ArrayList<ClientVendor>> callBack);

	public void getLatestItems(AsyncCallback<ArrayList<ClientItem>> callBack);

	public void getLatestPayments(
			AsyncCallback<ArrayList<PaymentsList>> callBack);

	public void getLatestCashSales(
			AsyncCallback<ArrayList<ClientCashSales>> callBack);

	public void getLatestCustomerRefunds(
			AsyncCallback<ArrayList<ClientCustomerRefund>> callBack);

	public void getLatestBills(AsyncCallback<ArrayList<BillsList>> callBack);

	public void getLatestCashPurchases(
			AsyncCallback<ArrayList<ClientCashPurchase>> callBack);

	public void getLatestChecks(
			AsyncCallback<ArrayList<ClientWriteCheck>> callBack);

	public void getLatestDeposits(
			AsyncCallback<ArrayList<ClientMakeDeposit>> callBack);

	public void getLatestFundsTransfer(
			AsyncCallback<ArrayList<ClientTransferFund>> callBack);

	/**
	 * Other Utility GET Methods
	 */

	public void getNextTransactionNumber(int transactionType,
			AsyncCallback<String> callback);

	// To auto generate the next Check number.
	public void getNextCheckNumber(long accountId, AsyncCallback<Long> callback);

	public void getNextIssuepaymentCheckNumber(long accountId,
			AsyncCallback<String> callback);

	// To check whether an Account is a Tax Agency Account or not
	public void isTaxAgencyAccount(long accountId,
			AsyncCallback<Boolean> callback);

	// To check whether an Account is a Sales Tax Payable Account or not
	public void isSalesTaxPayableAccount(long accountId,
			AsyncCallback<Boolean> callback);

	// To check whether an Account is a Sales Tax Payable Account or not
	public void isSalesTaxPayableAccountByName(String accountName,
			AsyncCallback<Boolean> callback);

	// To get all the Estimates/Quotes in a company
	public void getEstimates(int type,
			AsyncCallback<ArrayList<ClientEstimate>> callback);

	// To get the Estimates/Quotes of a particular customer in the company
	public void getEstimates(long customerId,
			AsyncCallback<ArrayList<ClientEstimate>> callback);

	// To check whether an Invoice or Vendor Bill can be Voidable and Editable
	// or not
	public void canVoidOrEdit(long invoiceOrVendorBillId,
			AsyncCallback<Boolean> callback);

	// To display the Item combo box of Transaction Item lines in Creating
	// Creating Invoice,Cash Sale, Customer Credit Memo, Customer Refund
	public void getSalesItems(AsyncCallback<ArrayList<ClientItem>> callback);

	// To display the Item combo box of Transaction Item lines in Creating Enter
	// Bill,Cash Purchase, Vendor Credit Memo Transactions
	public void getPurchaseItems(AsyncCallback<ArrayList<ClientItem>> callback);

	// To get the Credits and Payments of a particular Customer in a company
	public void getCustomerCreditsAndPayments(long customerId,
			AsyncCallback<ArrayList<ClientCreditsAndPayments>> callback);

	// To get all the Invoices and CustomerRefunds of a particular customer
	// which are not paid and display as the Transaction ReceivePayments in
	// ReceivePayment
	public void getTransactionReceivePayments(long customerId,
			long paymentDate,
			AsyncCallback<ArrayList<ReceivePaymentTransactionList>> callback);

	// To get a particular Journal Entry
	public void getJournalEntry(long journalEntryId,
			AsyncCallback<ClientJournalEntry> callback);

	// To get all the Journal Entries in a company
	public void getJournalEntries(
			AsyncCallback<ArrayList<ClientJournalEntry>> callback);

	// To get all the Entries of a particular journal entry
	public void getEntries(long journalEntryId,
			AsyncCallback<ArrayList<ClientEntry>> callback);

	// to get the Account Register of a particular account
	// public AccountRegister getAccountRegister(String accountId)
	// throws DAOException;

	// To get all Invoices, Customer Credit Memo, Cash Sales and Write Checks ->
	// for Customer
	public void getInvoiceList(AsyncCallback<ArrayList<InvoicesList>> callback);

	// To get all Customer Refunds and Write Checks -> for Customer
	public void getCustomerRefundsList(
			AsyncCallback<ArrayList<CustomerRefundsList>> callback);

	// To display the liabilityAccount combo box of New Tax Agency window
	public void getTaxAgencyAccounts(
			AsyncCallback<ArrayList<ClientAccount>> callback);

	public void getReceivePaymentsList(
			AsyncCallback<ArrayList<ReceivePaymentsList>> callback);

	public void getLatestPurchaseItems(
			AsyncCallback<ArrayList<ClientItem>> callback);

	public void getLatestVendorPayments(
			AsyncCallback<ArrayList<PaymentsList>> callback);

	public void getLatestReceivePayments(
			AsyncCallback<ArrayList<ClientReceivePayment>> callback);

	public void getTransactionMakeDeposit(long transactionMakeDepositId,
			AsyncCallback<ClientTransactionMakeDeposit> callback);

	public void getTransactionMakeDeposits(
			AsyncCallback<List<ClientTransactionMakeDeposit>> callback);

	public void getSalesOrders(
			AsyncCallback<ArrayList<SalesOrdersList>> callback);

	public void getPurchaseOrders(
			AsyncCallback<ArrayList<PurchaseOrdersList>> callback);

	/*
	 * public void getSalesOrdersForCustomer(long customerID,
	 * AsyncCallback<ArrayList<SalesOrdersList>> callback);
	 */

	/*
	 * public void getPurchaseOrdersForVendor(long vendorID,
	 * AsyncCallback<ArrayList<SalesOrdersList>> callback);
	 */

	public void getNotReceivedPurchaseOrdersList(long vendorID,
			AsyncCallback<ArrayList<PurchaseOrdersList>> callback);

	/*
	 * public void getPurchaseOrderById(long transactionId,
	 * AsyncCallback<ClientPurchaseOrder> callback);
	 */

	public void getPurchasesAndItemReceiptsList(long vendorID,
			AsyncCallback<ArrayList<PurchaseOrdersAndItemReceiptsList>> callback);

	public void getEstimatesAndSalesOrdersList(long customerID,
			AsyncCallback<ArrayList<EstimatesAndSalesOrdersList>> callback);

	public void getDepreciableFixedAssets(long depreciationFrom,
			long depreciationTo,
			AsyncCallback<DepreciableFixedAssetsList> callback);

	public void getDepreciationLastDate(
			AsyncCallback<ClientFinanceDate> callback);

	public void rollBackDepreciation(long rollBackDepreciationTo,
			AsyncCallback callback);

	public void getFinancialYearStartDates(
			AsyncCallback<ArrayList<ClientFinanceDate>> callback);

	public void getAllDepreciationFromDates(
			AsyncCallback<ArrayList<ClientFinanceDate>> callback);

	public void changeDepreciationStartDateTo(long newStartDate,
			AsyncCallback callback);

	public void getTAXReturn(ClientTAXAgency taxAgency,
			ClientFinanceDate fromDate, ClientFinanceDate toDate,
			AsyncCallback<ClientVATReturn> callback);

	void getReviewJournal(TempFixedAsset fixedAsset,
			AsyncCallback<FixedAssetSellOrDisposeReviewJournal> callback);

	void getAccumulatedDepreciationAmount(int depreciationMethod,
			double depreciationRate, double purchasePrice,
			long depreciationfrom, long depreciationtoDate,
			AsyncCallback<Double> callback);

	public void getNextNominalCode(int accountType, AsyncCallback<Long> callback);

	// public void createTaxes(int[] vatReturnType, AsyncCallback<Boolean>
	// callback);

	public void getNextFixedAssetNumber(AsyncCallback<String> callback);

	public void changeFiscalYearsStartDateTo(long newStartDate,
			AsyncCallback<Boolean> callback);

	public void runDepreciation(long depreciationFrom, long depreciationTo,
			FixedAssetLinkedAccountMap linkedAccounts, AsyncCallback callBack);

	// public void getPaySalesTaxEntries(long transactionDate,
	// AsyncCallback<ArrayList<ClientFileTAXEntry>> callBack);

	public void getPayVATEntries(
			AsyncCallback<ArrayList<ClientPayTAXEntries>> callBack);

	public void getPayeeList(int transactionCategory,
			AsyncCallback<ArrayList<PayeeList>> callBack);

	public void getInvoiceList(long fromDate, long toDate,
			AsyncCallback<ArrayList<InvoicesList>> callback);

	public void getMinimumAndMaximumTransactionDate(
			AsyncCallback<ArrayList<ClientFinanceDate>> callBack);

	public void getCustomerNumber(AsyncCallback<String> callback);

	public void getVendorNumber(AsyncCallback<String> callback);

	void getReceiveVATEntries(
			AsyncCallback<ArrayList<ClientReceiveVATEntries>> callback);

	public void getGraphPointsforAccount(int chartType, long accountNo,
			AsyncCallback<ArrayList<Double>> callBack);

	public void getEmployeeExpensesByStatus(String userName, int status,
			AsyncCallback<ArrayList<BillsList>> callBack);

	public void changePassWord(String emailID, String oldPassword,
			String newPassword, AsyncCallback<Boolean> callback);

	void getAllUsers(AsyncCallback<ArrayList<ClientUserInfo>> callback);

	public void getUsersActivityLog(ClientFinanceDate startDate,
			ClientFinanceDate endDate, int startIndex, int length,
			AsyncCallback<PaginationList<ClientActivity>> callback);

	void mergeCustomer(ClientCustomer clientCustomer,
			ClientCustomer clientCustomer1, AsyncCallback<Void> callback);

	void mergeVendor(ClientVendor fromClientVendor,
			ClientVendor toClientVendor, AsyncCallback<Void> callback);

	void mergeItem(ClientItem froClientItem, ClientItem toClientItem,
			AsyncCallback<Void> callback);

	void mergeAccount(ClientAccount fromClientAccount,
			ClientAccount toClientAccount, AsyncCallback<Void> callback);

	// this method is used to send Pdf as an attachment in email
	public void sendPdfInMail(long objectID, int type, long brandingThemeId,
			String mimeType, String subject, String content,
			String senderEmail, String recipientEmail, String ccEmail,
			AsyncCallback<Void> callback);

	public void getBudgetList(AsyncCallback<ArrayList<ClientBudget>> callBack);

	// For tds
	public void getPayBillsByTDS(
			AsyncCallback<ArrayList<ClientTDSInfo>> callback);

}