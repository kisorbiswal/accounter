package com.vimukti.accounter.web.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
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
import com.vimukti.accounter.web.client.core.Lists.TempFixedAsset;
import com.vimukti.accounter.workspace.tool.AccounterException;

/**
 * 
 * @author Fernandez
 * 
 */
public interface IAccounterHomeViewService extends RemoteService {

	public List<OverDueInvoicesList> getOverDueInvoices();

	public List<ClientEnterBill> getBillsOwed();

	public List<ClientEstimate> getLatestQuotes();

	public List<BillsList> getBillsAndItemReceiptList(boolean isExpensesList);

	public List<PayBillTransactionList> getTransactionPayBills();

	public List<PayBillTransactionList> getTransactionPayBills(long vendorId);

	public List<PaymentsList> getVendorPaymentsList();

	public List<PaymentsList> getPaymentsList();

	public List<ClientCreditsAndPayments> getVendorCreditsAndPayments(
			long vendorId);

	public List<IssuePaymentTransactionsList> getChecks();

	public List<IssuePaymentTransactionsList> getChecks(long accountId);

	public List<ClientCreditCardCharge> getCreditCardChargesThisMonth(long date);

	public List<ClientCustomer> getLatestCustomers();

	public List<ClientVendor> getLatestVendors();

	public List<ClientItem> getLatestItems();

	public List<PaymentsList> getLatestPayments();

	public List<ClientCashSales> getLatestCashSales();

	public List<ClientCustomerRefund> getLatestCustomerRefunds();

	public List<BillsList> getLatestBills();

	public List<ClientCashPurchase> getLatestCashPurchases();

	public List<ClientWriteCheck> getLatestChecks();

	public List<ClientMakeDeposit> getLatestDeposits();

	public List<ClientTransferFund> getLatestFundsTransfer();

	/**
	 * Other Utility GET Methods
	 */

	public String getNextTransactionNumber(int transactionType);

	public String getNextVoucherNumber();

	// To auto generate the next Check number.
	public Long getNextCheckNumber(long accountId);

	// To auto generate the next Issue Payement Check number.
	public Long getNextIssuepaymentCheckNumber(long accountId);

	// To check whether an Account is a Tax Agency Account or not
	public boolean isTaxAgencyAccount(long accountId);

	// To check whether an Account is a Sales Tax Payable Account or not
	public boolean isSalesTaxPayableAccount(long accountId);

	// To check whether an Account is a Sales Tax Payable Account or not
	public boolean isSalesTaxPayableAccountByName(String accountName);

	// To get all the Estimates/Quotes in a company
	public List<ClientEstimate> getEstimates();

	// To get the Estimates/Quotes of a particular customer in the company
	public List<ClientEstimate> getEstimates(long customerId);

	// To check whether an Invoice or Vendor Bill can be Voidable and Editable
	// or not
	public boolean canVoidOrEdit(long invoiceOrVendorBillId);

	// To display the Item combo box of Transaction Item lines in Creating
	// Creating Invoice,Cash Sale, Customer Credit Memo, Customer Refund
	public List<ClientItem> getSalesItems();

	// To display the Item combo box of Transaction Item lines in Creating Enter
	// Bill,Cash Purchase, Vendor Credit Memo Transactions
	public List<ClientItem> getPurchaseItems();

	// To get the Credits and Payments of a particular Customer in a company
	public List<ClientCreditsAndPayments> getCustomerCreditsAndPayments(
			long customerId);

	// To get all the Invoices and CustomerRefunds of a particular customer
	// which are not paid and display as the Transaction ReceivePayments in
	// ReceivePayment
	public List<ReceivePaymentTransactionList> getTransactionReceivePayments(
			long customerId, long paymentDate);

	// To get a particular Journal Entry
	public ClientJournalEntry getJournalEntry(long journalEntryId);

	// To get all the Journal Entries in a company
	public List<ClientJournalEntry> getJournalEntries();

	// To get all the Entries of a particular journal entry
	public List<ClientEntry> getEntries(long journalEntryId);

	// to get the Account Register of a particular account
	// public AccountRegister getAccountRegister(String accountId)
	// throws DAOException;

	// To get all Invoices, Customer Credit Memo, Cash Sales and Write Checks ->
	// for Customer
	public List<InvoicesList> getInvoiceList();

	// To get all Customer Refunds and Write Checks -> for Customer
	public List<CustomerRefundsList> getCustomerRefundsList();

	// To display the liabilityAccount combo box of New Tax Agency window
	public List<ClientAccount> getTaxAgencyAccounts();

	// To display Received payments in list View.
	public List<ReceivePaymentsList> getReceivePaymentsList();

	public List<ClientItem> getLatestPurchaseItems();

	public List<PaymentsList> getLatestVendorPayments();

	public List<ClientReceivePayment> getLatestReceivePayments();

	public ClientTransactionMakeDeposit getTransactionMakeDeposit(
			long transactionMakeDepositId);

	public List<ClientTransactionMakeDeposit> getTransactionMakeDeposits();

	/*
	 * public List<SalesOrdersList> getSalesOrders() throws AccounterException;
	 * 
	 * public List<PurchaseOrdersList> getPurchaseOrders() throws
	 * AccounterException;
	 * 
	 * public List<SalesOrdersList> getSalesOrdersForCustomer(long customerID)
	 * throws AccounterException;
	 * 
	 * public List<SalesOrdersList> getPurchaseOrdersForVendor(long vendorID)
	 * throws AccounterException;
	 */

	public List<PurchaseOrdersList> getNotReceivedPurchaseOrdersList(
			long vendorID) throws AccounterException;

	/* public ClientPurchaseOrder getPurchaseOrderById(long transactionId); */

	public List<PurchaseOrdersAndItemReceiptsList> getPurchasesAndItemReceiptsList(
			long vendorId) throws AccounterException;

	public List<EstimatesAndSalesOrdersList> getEstimatesAndSalesOrdersList(
			long customerId) throws AccounterException;

	public DepreciableFixedAssetsList getDepreciableFixedAssets(
			long depreciationFrom, long depreciationTo)
			throws AccounterException;

	public ClientFinanceDate getDepreciationLastDate()
			throws AccounterException;

	public void rollBackDepreciation(long rollBackDepreciationTo)
			throws AccounterException;

	public List<ClientFinanceDate> getFinancialYearStartDates()
			throws AccounterException;

	public List<ClientFinanceDate> getAllDepreciationFromDates()
			throws AccounterException;

	public void changeDepreciationStartDateTo(long newStartDate)
			throws AccounterException;

	public ClientVATReturn getTAXReturn(ClientTAXAgency taxAgency,
			long fromDate, long toDate) throws AccounterException;

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

	public List<ClientPaySalesTaxEntries> getPaySalesTaxEntries(
			long transactionDate);

	public List<ClientPayVATEntries> getPayVATEntries()
			throws AccounterException;

	public List<ClientReceiveVATEntries> getReceiveVATEntries()
			throws AccounterException;

	public List<ClientFinanceLogger> getLog(long id, boolean isNext)
			throws AccounterException;

	public List<ClientFinanceLogger> getLog(String date, long id, boolean isNext)
			throws AccounterException;

	public List<PayeeList> getPayeeList(int transactionCategory);

	public List<InvoicesList> getInvoiceList(long fromDate, long toDate);

	public List<ClientFinanceDate> getMinimumAndMaximumTransactionDate();

	public String getCustomerNumber();

	public List<Double> getGraphPointsforAccount(int chartType, long accountNo);

	boolean changePassWord(String emailID, String oldPassword,
			String newPassword);

	public List<BillsList> getEmployeeExpensesByStatus(String userName,
			int status);

	public List<ClientUser> getAllUsers() throws AccounterException;
}