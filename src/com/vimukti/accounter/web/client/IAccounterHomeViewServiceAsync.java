/**
 * 
 */
package com.vimukti.accounter.web.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientAccount;
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
import com.vimukti.accounter.web.client.core.ClientFinanceLogger;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientJournalEntry;
import com.vimukti.accounter.web.client.core.ClientMakeDeposit;
import com.vimukti.accounter.web.client.core.ClientPaySalesTaxEntries;
import com.vimukti.accounter.web.client.core.ClientPayVATEntries;
import com.vimukti.accounter.web.client.core.ClientPurchaseOrder;
import com.vimukti.accounter.web.client.core.ClientReceivePayment;
import com.vimukti.accounter.web.client.core.ClientReceiveVATEntries;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ClientTransactionMakeDeposit;
import com.vimukti.accounter.web.client.core.ClientTransferFund;
import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.core.ClientVATReturn;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ClientWriteCheck;
import com.vimukti.accounter.web.client.core.Lists.BillsList;
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
			AsyncCallback<List<OverDueInvoicesList>> callBack);

	public void getBillsOwed(AsyncCallback<List<ClientEnterBill>> callBack);

	public void getLatestQuotes(AsyncCallback<List<ClientEstimate>> callBack);

	public void getBillsAndItemReceiptList(boolean isExpensesList,
			AsyncCallback<List<BillsList>> callBack);

	public void getTransactionPayBills(
			AsyncCallback<List<PayBillTransactionList>> callback);

	public void getTransactionPayBills(String vendorId,
			AsyncCallback<List<PayBillTransactionList>> callback);

	public void getVendorPaymentsList(AsyncCallback<List<PaymentsList>> callBack);

	public void getPaymentsList(AsyncCallback<List<PaymentsList>> callBack);

	public void getVendorCreditsAndPayments(String vendorId,
			AsyncCallback<List<ClientCreditsAndPayments>> callBack);

	public void getChecks(
			AsyncCallback<List<IssuePaymentTransactionsList>> callBack);

	public void getChecks(String accountId,
			AsyncCallback<List<IssuePaymentTransactionsList>> callback);

	public void getCreditCardChargesThisMonth(long date,
			AsyncCallback<List<ClientCreditCardCharge>> callback);

	public void getLatestCustomers(AsyncCallback<List<ClientCustomer>> callBack);

	public void getLatestVendors(AsyncCallback<List<ClientVendor>> callBack);

	public void getLatestItems(AsyncCallback<List<ClientItem>> callBack);

	public void getLatestPayments(AsyncCallback<List<PaymentsList>> callBack);

	public void getLatestCashSales(AsyncCallback<List<ClientCashSales>> callBack);

	public void getLatestCustomerRefunds(
			AsyncCallback<List<ClientCustomerRefund>> callBack);

	public void getLatestBills(AsyncCallback<List<BillsList>> callBack);

	public void getLatestCashPurchases(
			AsyncCallback<List<ClientCashPurchase>> callBack);

	public void getLatestChecks(AsyncCallback<List<ClientWriteCheck>> callBack);

	public void getLatestDeposits(
			AsyncCallback<List<ClientMakeDeposit>> callBack);

	public void getLatestFundsTransfer(
			AsyncCallback<List<ClientTransferFund>> callBack);

	/**
	 * Other Utility GET Methods
	 */

	public void getNextTransactionNumber(int transactionType,
			AsyncCallback<String> callback);

	public void getNextVoucherNumber(AsyncCallback<String> callback);

	// To auto generate the next Check number.
	public void getNextCheckNumber(long accountId,
			AsyncCallback<Long> callback);

	public void getNextIssuepaymentCheckNumber(String accountId,
			AsyncCallback<Long> callback);

	// To check whether an Account is a Tax Agency Account or not
	public void isTaxAgencyAccount(String accountId,
			AsyncCallback<Boolean> callback);

	// To check whether an Account is a Sales Tax Payable Account or not
	public void isSalesTaxPayableAccount(String accountId,
			AsyncCallback<Boolean> callback);

	// To check whether an Account is a Sales Tax Payable Account or not
	public void isSalesTaxPayableAccountByName(String accountName,
			AsyncCallback<Boolean> callback);

	// To get all the Estimates/Quotes in a company
	public void getEstimates(AsyncCallback<List<ClientEstimate>> callback);

	// To get the Estimates/Quotes of a particular customer in the company
	public void getEstimates(String customerId,
			AsyncCallback<List<ClientEstimate>> callback);

	// To check whether an Invoice or Vendor Bill can be Voidable and Editable
	// or not
	public void canVoidOrEdit(String invoiceOrVendorBillId,
			AsyncCallback<Boolean> callback);

	// To display the Item combo box of Transaction Item lines in Creating
	// Creating Invoice,Cash Sale, Customer Credit Memo, Customer Refund
	public void getSalesItems(AsyncCallback<List<ClientItem>> callback);

	// To display the Item combo box of Transaction Item lines in Creating Enter
	// Bill,Cash Purchase, Vendor Credit Memo Transactions
	public void getPurchaseItems(AsyncCallback<List<ClientItem>> callback);

	// To get the Credits and Payments of a particular Customer in a company
	public void getCustomerCreditsAndPayments(String customerId,
			AsyncCallback<List<ClientCreditsAndPayments>> callback);

	// To get all the Invoices and CustomerRefunds of a particular customer
	// which are not paid and display as the Transaction ReceivePayments in
	// ReceivePayment
	public void getTransactionReceivePayments(String customerId,
			long paymentDate,
			AsyncCallback<List<ReceivePaymentTransactionList>> callback);

	// To get a particular Journal Entry
	public void getJournalEntry(String journalEntryId,
			AsyncCallback<ClientJournalEntry> callback);

	// To get all the Journal Entries in a company
	public void getJournalEntries(
			AsyncCallback<List<ClientJournalEntry>> callback);

	// To get all the Entries of a particular journal entry
	public void getEntries(String journalEntryId,
			AsyncCallback<List<ClientEntry>> callback);

	// to get the Account Register of a particular account
	// public AccountRegister getAccountRegister(String accountId)
	// throws DAOException;

	// To get all Invoices, Customer Credit Memo, Cash Sales and Write Checks ->
	// for Customer
	public void getInvoiceList(AsyncCallback<List<InvoicesList>> callback);

	// To get all Customer Refunds and Write Checks -> for Customer
	public void getCustomerRefundsList(
			AsyncCallback<List<CustomerRefundsList>> callback);

	// To display the liabilityAccount combo box of New Tax Agency window
	public void getTaxAgencyAccounts(AsyncCallback<List<ClientAccount>> callback);

	public void getReceivePaymentsList(
			AsyncCallback<List<ReceivePaymentsList>> callback);

	public void getLatestPurchaseItems(AsyncCallback<List<ClientItem>> callback);

	public void getLatestVendorPayments(
			AsyncCallback<List<PaymentsList>> callback);

	public void getLatestReceivePayments(
			AsyncCallback<List<ClientReceivePayment>> callback);

	public void getTransactionMakeDeposit(String transactionMakeDepositId,
			AsyncCallback<ClientTransactionMakeDeposit> callback);

	public void getTransactionMakeDeposits(
			AsyncCallback<List<ClientTransactionMakeDeposit>> callback);

	public void getSalesOrders(AsyncCallback<List<SalesOrdersList>> callback);

	public void getPurchaseOrders(
			AsyncCallback<List<PurchaseOrdersList>> callback);

	public void getSalesOrdersForCustomer(String customerID,
			AsyncCallback<List<SalesOrdersList>> callback);

	public void getPurchaseOrdersForVendor(String vendorID,
			AsyncCallback<List<SalesOrdersList>> callback);

	public void getNotReceivedPurchaseOrdersList(String vendorID,
			AsyncCallback<List<PurchaseOrdersList>> callback);

	public void getPurchaseOrderById(String transactionId,
			AsyncCallback<ClientPurchaseOrder> callback);

	public void getPurchasesAndItemReceiptsList(String vendorID,
			AsyncCallback<List<PurchaseOrdersAndItemReceiptsList>> callback);

	public void getEstimatesAndSalesOrdersList(String customerID,
			AsyncCallback<List<EstimatesAndSalesOrdersList>> callback);

	public void getDepreciableFixedAssets(long depreciationFrom,
			long depreciationTo,
			AsyncCallback<DepreciableFixedAssetsList> callback);

	public void getDepreciationLastDate(
			AsyncCallback<ClientFinanceDate> callback);

	@SuppressWarnings("unchecked")
	public void rollBackDepreciation(long rollBackDepreciationTo,
			AsyncCallback callback);

	public void getFinancialYearStartDates(
			AsyncCallback<List<ClientFinanceDate>> callback);

	public void getAllDepreciationFromDates(
			AsyncCallback<List<ClientFinanceDate>> callback);

	@SuppressWarnings("unchecked")
	public void changeDepreciationStartDateTo(long newStartDate,
			AsyncCallback callback);

	public void getTAXReturn(ClientTAXAgency taxAgency, long fromDate,
			long toDate, AsyncCallback<ClientVATReturn> callback);

	void getReviewJournal(TempFixedAsset fixedAsset,
			AsyncCallback<FixedAssetSellOrDisposeReviewJournal> callback);

	void getAccumulatedDepreciationAmount(int depreciationMethod,
			double depreciationRate, double purchasePrice,
			long depreciationfrom, long depreciationtoDate,
			AsyncCallback<Double> callback);

	public void getNextNominalCode(int accountType, AsyncCallback<Long> callback);

	public void createTaxes(int[] vatReturnType, AsyncCallback<Boolean> callback);

	public void getNextFixedAssetNumber(AsyncCallback<String> callback);

	public void changeFiscalYearsStartDateTo(long newStartDate,
			AsyncCallback<Boolean> callback);

	@SuppressWarnings("unchecked")
	public void runDepreciation(long depreciationFrom, long depreciationTo,
			FixedAssetLinkedAccountMap linkedAccounts, AsyncCallback callBack);

	public void getPaySalesTaxEntries(long transactionDate,
			AsyncCallback<List<ClientPaySalesTaxEntries>> callBack);

	public void getPayVATEntries(
			AsyncCallback<List<ClientPayVATEntries>> callBack);

	public void getLog(long id, boolean isNext,
			AsyncCallback<List<ClientFinanceLogger>> callBack);

	public void getLog(String date, long id, boolean isNext,
			AsyncCallback<List<ClientFinanceLogger>> callBack);

	public void getPayeeList(int transactionCategory,
			AsyncCallback<List<PayeeList>> callBack);

	public void getInvoiceList(long fromDate, long toDate,
			AsyncCallback<List<InvoicesList>> callback);

	public void getMinimumAndMaximumTransactionDate(
			AsyncCallback<List<ClientFinanceDate>> callBack);

	public void getCustomerNumber(AsyncCallback<String> callback);

	void getReceiveVATEntries(
			AsyncCallback<List<ClientReceiveVATEntries>> callback);

	public void getGraphPointsforAccount(int chartType, long accountNo,
			AsyncCallback<List<Double>> callBack);
	
	public void getEmployeeExpensesByStatus(String userName,int status,
			AsyncCallback<List<BillsList>> callBack);
	
	public void changePassWord(String emailID, String oldPassword, String newPassword,
			AsyncCallback<Boolean> callback);
	
	void getAllUsers(
			AsyncCallback<List<ClientUser>> callback);
}