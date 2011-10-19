package com.vimukti.accounter.web.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
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
import com.vimukti.accounter.web.client.core.ClientPaySalesTaxEntries;
import com.vimukti.accounter.web.client.core.ClientPayVATEntries;
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
import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * 
 * @author Fernandez
 * 
 */
public interface IAccounterHomeViewService extends RemoteService {

	public ArrayList<OverDueInvoicesList> getOverDueInvoices();

	public ArrayList<ClientEnterBill> getBillsOwed();

	public ArrayList<ClientEstimate> getLatestQuotes();

	public ArrayList<BillsList> getBillsAndItemReceiptList(
			boolean isExpensesList);

	public ArrayList<PayBillTransactionList> getTransactionPayBills();

	public ArrayList<PayBillTransactionList> getTransactionPayBills(
			long vendorId);

	public ArrayList<PaymentsList> getVendorPaymentsList();

	public ArrayList<PaymentsList> getPaymentsList();

	public ArrayList<ClientCreditsAndPayments> getVendorCreditsAndPayments(
			long vendorId);

	public ArrayList<IssuePaymentTransactionsList> getChecks();

	public ArrayList<IssuePaymentTransactionsList> getChecks(long accountId);

	public ArrayList<ClientCreditCardCharge> getCreditCardChargesThisMonth(
			long date);

	public ArrayList<ClientCustomer> getLatestCustomers();

	public ArrayList<ClientVendor> getLatestVendors();

	public ArrayList<ClientItem> getLatestItems();

	public ArrayList<PaymentsList> getLatestPayments();

	public ArrayList<ClientCashSales> getLatestCashSales();

	public ArrayList<ClientCustomerRefund> getLatestCustomerRefunds();

	public ArrayList<BillsList> getLatestBills();

	public ArrayList<ClientCashPurchase> getLatestCashPurchases();

	public ArrayList<ClientWriteCheck> getLatestChecks();

	public ArrayList<ClientMakeDeposit> getLatestDeposits();

	public ArrayList<ClientTransferFund> getLatestFundsTransfer();

	/**
	 * Other Utility GET Methods
	 */

	public String getNextTransactionNumber(int transactionType);

	// To auto generate the next Check number.
	public Long getNextCheckNumber(long accountId);

	// To auto generate the next Issue Payement Check number.
	public String getNextIssuepaymentCheckNumber(long accountId);

	// To check whether an Account is a Tax Agency Account or not
	public boolean isTaxAgencyAccount(long accountId);

	// To check whether an Account is a Sales Tax Payable Account or not
	public boolean isSalesTaxPayableAccount(long accountId);

	// To check whether an Account is a Sales Tax Payable Account or not
	public boolean isSalesTaxPayableAccountByName(String accountName);

	// To get all the Estimates/Quotes in a company
	public ArrayList<ClientEstimate> getEstimates();

	// To get the Estimates/Quotes of a particular customer in the company
	public ArrayList<ClientEstimate> getEstimates(long customerId);

	// To check whether an Invoice or Vendor Bill can be Voidable and Editable
	// or not
	public boolean canVoidOrEdit(long invoiceOrVendorBillId);

	// To display the Item combo box of Transaction Item lines in Creating
	// Creating Invoice,Cash Sale, Customer Credit Memo, Customer Refund
	public ArrayList<ClientItem> getSalesItems();

	// To display the Item combo box of Transaction Item lines in Creating Enter
	// Bill,Cash Purchase, Vendor Credit Memo Transactions
	public ArrayList<ClientItem> getPurchaseItems();

	// To get the Credits and Payments of a particular Customer in a company
	public ArrayList<ClientCreditsAndPayments> getCustomerCreditsAndPayments(
			long customerId);

	// To get all the Invoices and CustomerRefunds of a particular customer
	// which are not paid and display as the Transaction ReceivePayments in
	// ReceivePayment
	public ArrayList<ReceivePaymentTransactionList> getTransactionReceivePayments(
			long customerId, long paymentDate) throws AccounterException;

	// To get a particular Journal Entry
	public ClientJournalEntry getJournalEntry(long journalEntryId);

	// To get all the Journal Entries in a company
	public ArrayList<ClientJournalEntry> getJournalEntries();

	// To get all the Entries of a particular journal entry
	public ArrayList<ClientEntry> getEntries(long journalEntryId);

	// to get the Account Register of a particular account
	// public AccountRegister getAccountRegister(String accountId)
	// throws DAOException;

	// To get all Invoices, Customer Credit Memo, Cash Sales and Write Checks ->
	// for Customer
	public ArrayList<InvoicesList> getInvoiceList();

	// To get all Customer Refunds and Write Checks -> for Customer
	public ArrayList<CustomerRefundsList> getCustomerRefundsList();

	// To display the liabilityAccount combo box of New Tax Agency window
	public ArrayList<ClientAccount> getTaxAgencyAccounts();

	// To display Received payments in list View.
	public ArrayList<ReceivePaymentsList> getReceivePaymentsList();

	public ArrayList<ClientItem> getLatestPurchaseItems();

	public ArrayList<PaymentsList> getLatestVendorPayments();

	public ArrayList<ClientReceivePayment> getLatestReceivePayments();

	public ClientTransactionMakeDeposit getTransactionMakeDeposit(
			long transactionMakeDepositId);

	public List<ClientTransactionMakeDeposit> getTransactionMakeDeposits();

	public ArrayList<SalesOrdersList> getSalesOrders()
			throws AccounterException;

	public ArrayList<PurchaseOrdersList> getPurchaseOrders()
			throws AccounterException;

	/*
	 * public ArrayList<SalesOrdersList> getSalesOrdersForCustomer(long
	 * customerID) throws AccounterException;
	 * 
	 * public ArrayList<SalesOrdersList> getPurchaseOrdersForVendor(long
	 * vendorID) throws AccounterException;
	 */

	public ArrayList<PurchaseOrdersList> getNotReceivedPurchaseOrdersList(
			long vendorID) throws AccounterException;

	/* public ClientPurchaseOrder getPurchaseOrderById(long transactionId); */

	public ArrayList<PurchaseOrdersAndItemReceiptsList> getPurchasesAndItemReceiptsList(
			long vendorId) throws AccounterException;

	public ArrayList<EstimatesAndSalesOrdersList> getEstimatesAndSalesOrdersList(
			long customerId) throws AccounterException;

	public DepreciableFixedAssetsList getDepreciableFixedAssets(
			long depreciationFrom, long depreciationTo)
			throws AccounterException;

	public ClientFinanceDate getDepreciationLastDate()
			throws AccounterException;

	public void rollBackDepreciation(long rollBackDepreciationTo)
			throws AccounterException;

	public ArrayList<ClientFinanceDate> getFinancialYearStartDates()
			throws AccounterException;

	public ArrayList<ClientFinanceDate> getAllDepreciationFromDates()
			throws AccounterException;

	public void changeDepreciationStartDateTo(long newStartDate)
			throws AccounterException;

	public ClientVATReturn getTAXReturn(ClientTAXAgency taxAgency,
			ClientFinanceDate fromDate, ClientFinanceDate toDate)
			throws AccounterException;

	public FixedAssetSellOrDisposeReviewJournal getReviewJournal(
			TempFixedAsset fixedAsset) throws AccounterException;

	// public boolean createTaxes(int[] vatReturnType);

	public double getAccumulatedDepreciationAmount(int depreciationMethod,
			double depreciationRate, double purchasePrice,
			long depreciationfrom, long depreciationtoDate)
			throws AccounterException;

	public Long getNextNominalCode(int accountType);

	public String getNextFixedAssetNumber();

	public boolean changeFiscalYearsStartDateTo(long newStartDate);

	public void runDepreciation(long depreciationFrom, long depreciationTo,
			FixedAssetLinkedAccountMap linkedAccounts);

	public ArrayList<ClientPaySalesTaxEntries> getPaySalesTaxEntries(
			long transactionDate);

	public ArrayList<ClientPayVATEntries> getPayVATEntries()
			throws AccounterException;

	public ArrayList<ClientReceiveVATEntries> getReceiveVATEntries()
			throws AccounterException;

	public ArrayList<PayeeList> getPayeeList(int transactionCategory);

	public ArrayList<InvoicesList> getInvoiceList(long fromDate, long toDate);

	public ArrayList<ClientFinanceDate> getMinimumAndMaximumTransactionDate();

	public String getCustomerNumber();

	public String getVendorNumber();

	public ArrayList<Double> getGraphPointsforAccount(int chartType,
			long accountNo);

	boolean changePassWord(String emailID, String oldPassword,
			String newPassword);

	public ArrayList<BillsList> getEmployeeExpensesByStatus(String userName,
			int status);

	public ArrayList<ClientUserInfo> getAllUsers() throws AccounterException;

	ArrayList<ClientRecurringTransaction> getRecurringsList()
			throws AccounterException;

	// For merging

	public void mergeCustomer(ClientCustomer clientCustomer,
			ClientCustomer clientCustomer2) throws AccounterException;

	void mergeVendor(ClientVendor fromClientVendor, ClientVendor toClientVendor)
			throws AccounterException;

	void mergeItem(ClientItem froClientItem, ClientItem toClientItem)
			throws AccounterException;

	public PaginationList<ClientActivity> getUsersActivityLog(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			int startIndex, int length) throws AccounterException;

	void mergeAccount(ClientAccount fromClientAccount,
			ClientAccount toClientAccount) throws AccounterException;

	// for sending pdf in email

	public void sendPdfInMail(long objectID, int type, long brandingThemeId,
			String mimeType, String subject, String content,
			String senderEmail, String recipientEmail, String ccEmail)
			throws Exception, IOException, AccounterException;

	public ArrayList<ClientBudget> getBudgetList();

	// for TDS

	public ArrayList<ClientTDSInfo> getPayBillsByTDS()
			throws AccounterException;

}