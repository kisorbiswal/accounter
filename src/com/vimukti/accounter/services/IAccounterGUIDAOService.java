package com.vimukti.accounter.services;

import java.util.Date;
import java.util.List;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.CashPurchase;
import com.vimukti.accounter.core.CashSales;
import com.vimukti.accounter.core.CreditCardCharge;
import com.vimukti.accounter.core.CreditsAndPayments;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.CustomerRefund;
import com.vimukti.accounter.core.EnterBill;
import com.vimukti.accounter.core.Entry;
import com.vimukti.accounter.core.Estimate;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.JournalEntry;
import com.vimukti.accounter.core.MakeDeposit;
import com.vimukti.accounter.core.PaySalesTaxEntries;
import com.vimukti.accounter.core.ReceivePayment;
import com.vimukti.accounter.core.TransactionMakeDeposit;
import com.vimukti.accounter.core.TransferFund;
import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.core.VATReturn;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.core.WriteCheck;
import com.vimukti.accounter.web.client.core.Lists.BillsList;
import com.vimukti.accounter.web.client.core.Lists.CustomerRefundsList;
import com.vimukti.accounter.web.client.core.Lists.DepreciableFixedAssetsList;
import com.vimukti.accounter.web.client.core.Lists.EstimatesAndSalesOrdersList;
import com.vimukti.accounter.web.client.core.Lists.FixedAssetLinkedAccountMap;
import com.vimukti.accounter.web.client.core.Lists.FixedAssetList;
import com.vimukti.accounter.web.client.core.Lists.FixedAssetSellOrDisposeReviewJournal;
import com.vimukti.accounter.web.client.core.Lists.InvoicesList;
import com.vimukti.accounter.web.client.core.Lists.IssuePaymentTransactionsList;
import com.vimukti.accounter.web.client.core.Lists.KeyFinancialIndicators;
import com.vimukti.accounter.web.client.core.Lists.MakeDepositTransactionsList;
import com.vimukti.accounter.web.client.core.Lists.OverDueInvoicesList;
import com.vimukti.accounter.web.client.core.Lists.PayBillTransactionList;
import com.vimukti.accounter.web.client.core.Lists.PaymentsList;
import com.vimukti.accounter.web.client.core.Lists.PurchaseOrdersAndItemReceiptsList;
import com.vimukti.accounter.web.client.core.Lists.PurchaseOrdersList;
import com.vimukti.accounter.web.client.core.Lists.ReceivePaymentTransactionList;
import com.vimukti.accounter.web.client.core.Lists.ReceivePaymentsList;
import com.vimukti.accounter.web.client.core.Lists.SalesOrdersList;
import com.vimukti.accounter.web.client.core.Lists.SellingOrDisposingFixedAssetList;
import com.vimukti.accounter.web.client.core.Lists.TempFixedAsset;

public interface IAccounterGUIDAOService {

	/**
	 * Company Home page widgets Related Dao Methods
	 */


	// public ProfitAndLoss getProfitAndLossReport()
	// throws DAOException;
	public List<OverDueInvoicesList> getOverDueInvoices() throws DAOException;

	public List<EnterBill> getBillsOwed() throws DAOException;

	public List<Estimate> getLatestQuotes() throws DAOException;

	// public List<ExpensesThisFiscalYear> getExpensesThisFiscalYear(
	// Company company) throws DAOException;

	public List<CreditCardCharge> getCreditCardChargesThisMonth(final Date date)
			throws DAOException;

	public List<Customer> getLatestCustomers() throws DAOException;

	public List<Vendor> getLatestVendors() throws DAOException;

	public List<Item> getLatestItems() throws DAOException;

	public List<PaymentsList> getLatestPayments() throws DAOException;

	public List<CashSales> getLatestCashSales() throws DAOException;

	public List<CustomerRefund> getLatestCustomerRefunds() throws DAOException;

	public List<BillsList> getLatestBills() throws DAOException;

	public List<CashPurchase> getLatestCashPurchases() throws DAOException;

	public List<WriteCheck> getLatestChecks() throws DAOException;

	public List<MakeDeposit> getLatestDeposits() throws DAOException;

	public List<TransferFund> getLatestFundsTransfer() throws DAOException;

	public List<InvoicesList> getLatestInvoices() throws DAOException;

	public List<ReceivePayment> getLatestReceivePayments() throws DAOException;

	public List<PaymentsList> getLatestVendorPayments() throws DAOException;

	public List<Item> getLatestSalesItems() throws DAOException;

	public List<Item> getLatestPurchaseItems() throws DAOException;

	/**
	 * Other Dao Methods required for GUI
	 */

	// To auto generate the next Transaction number for all types of
	// Transactions
	public Long getNextTransactionNumber(int transactionType)
			throws DAOException;

	// To auto generate the next Voucher number for all vouchers.
	public Long getNextVoucherNumber() throws DAOException;

	// To auto generate the next Check number.
	public Long getNextCheckNumber(String account) throws DAOException;

	public Long getNextNominalCode(int accountType) throws DAOException; 
	
	// To check whether an Account is a Tax Agency Account or not
	public boolean isTaxAgencyAccount(String account) throws DAOException;

	// To check whether an Account is a Sales Tax Payable Account or not
	public boolean isSalesTaxPayableAccount(String accountId)
			throws DAOException;

	// To check whether an Account is a Sales Tax Payable Account or not
	public boolean isSalesTaxPayableAccountByName(String accountName)
			throws DAOException;

	// To get all the Estimates/Quotes in a company
	public List<Estimate> getEstimates() throws DAOException;

	// To get the Estimates/Quotes of a particular customer in the company
	public List<Estimate> getEstimates(String customer) throws DAOException;

	// To check whether an Invoice or Vendor Bill can be Voidable and Editable
	// or not
	public boolean canVoidOrEdit(String invoiceOrVendorBillId)
			throws DAOException;

	// To display the Item combo box of Transaction Item lines in Creating
	// Creating Invoice,Cash Sale, Customer Credit Memo, Customer Refund
	public List<Item> getSalesItems() throws DAOException;

	// To display the Item combo box of Transaction Item lines in Creating Enter
	// Bill,Cash Purchase, Vendor Credit Memo Transactions
	public List<Item> getPurchaseItems() throws DAOException;

	// To get the Credits and Payments of a particular Customer in a company
	public List<CreditsAndPayments> getCustomerCreditsAndPayments(
			String customer) throws DAOException;

	// To get the Credits and Payments of a particular Vendor in a company
	public List<CreditsAndPayments> getVendorCreditsAndPayments(String vendor)
			throws DAOException;

	// To get Make Deposit by passing the Transaction Make Deposit
	public TransactionMakeDeposit getTransactionMakeDeposit(
			String transactionMakeDepositId) throws DAOException;

	// To get all the Invoices and CustomerRefunds of a particular customer
	// which are not paid and display as the Transaction ReceivePayments in
	// ReceivePayment
	public List<ReceivePaymentTransactionList> getTransactionReceivePayments(
			String customerId) throws DAOException;

	// To get all the Vendor Bills and MakeDeposit(New Vendor Entries) which are
	// not paid and display as the Transaction PayBills in PayBill
	public List<PayBillTransactionList> getTransactionPayBills()
			throws DAOException;

	// To get all the Vendor Bills and MakeDeposit(New Vendor Entries) of a
	// particular Vendor which are not paid and display as the Transaction
	// PayBills in PayBill
	public List<PayBillTransactionList> getTransactionPayBills(String vendorId)
			throws DAOException;

	// To get all the Write Checks in a company
	public List<IssuePaymentTransactionsList> getChecks() throws DAOException;

	// To get the Write Checks related to a particular Account in a company
	public List<IssuePaymentTransactionsList> getChecks(String account)
			throws DAOException;

	// To get a particular Journal Entry
	public JournalEntry getJournalEntry(String journalEntryId)
			throws DAOException;

	// To get all the Journal Entries in a company
	public List<JournalEntry> getJournalEntries() throws DAOException;

	// To get all the Entries of a particular journal entry
	public List<Entry> getEntries(String journalEntryId) throws DAOException;

	// to get the Account Register of a particular account
	// public AccountRegister getAccountRegister(String accountId)
	// throws DAOException;

	// To get all Customer Refunds , Vendor Payments,Cash Purchases, Credit Card
	// Charge and all Write Checks
	public List<PaymentsList> getPaymentsList() throws DAOException;

	// To get all Invoices, Customer Credit Memo, Cash Sales and Write Checks ->
	// for Customer
	public List<InvoicesList> getInvoiceList() throws DAOException;

	// To get all Customer Refunds and Write Checks -> for Customer
	public List<CustomerRefundsList> getCustomerRefundsList()
			throws DAOException;

	// To get all Vendor Bills, Vendor Credit Memo, Cash Purchase and Credit
	// Card Charge and Write Check -> Vendor
	public List<BillsList> getBillsList() throws DAOException;

	// To get all Vendor Payments, PayBills, Write Check -> Vendor and TaxAgency
	public List<ReceivePaymentsList> getReceivePaymentsList()
			throws DAOException;

	// To get all Vendor Payments, PayBills, Write Check -> Vendor and TaxAgency
	public List<PaymentsList> getVendorPaymentsList() throws DAOException;

	// To display the liabilityAccount combo box of New Tax Agency window
	public List<Account> getTaxAgencyAccounts() throws DAOException;

	public List<MakeDepositTransactionsList> getTransactionMakeDeposits()
			throws DAOException;

	public void test() throws Exception;

	public List<PaySalesTaxEntries> getTransactionPaySalesTaxEntriesList(
			Date transactionDate) throws DAOException;

	public List<EstimatesAndSalesOrdersList> getEstimatesAndSalesOrdersList(String customerId) throws DAOException;
	
	public List<PurchaseOrdersAndItemReceiptsList> getPurchasesAndItemReceiptsList(String vendorId) throws DAOException;

	List<SalesOrdersList> getSalesOrdersList(String customerId)
			throws DAOException;

	List<PurchaseOrdersList> getPurchaseOrdersList(String vendorId)
			throws DAOException;

	List<PurchaseOrdersList> getNotReceivedPurchaseOrdersList(String vendorId)
			throws DAOException;
	
	public List<DepreciableFixedAssetsList> getDepreciationFixedAssets(
			Date startDate, Date toDate) throws DAOException;

	VATReturn getVATReturn(TAXAgency vatAgency, Date fromDate, Date toDate)
			throws DAOException;
	
	List<FixedAssetList> getFixedAssets(int status) throws DAOException;

	List<SellingOrDisposingFixedAssetList> getSellingOrDisposingFixedAssets()
			throws DAOException;
	
	public void runDepreciation(Date depreciationFrom, Date depreciationTo,FixedAssetLinkedAccountMap linkedAccounts) throws DAOException; 
	
	public DepreciableFixedAssetsList getDepreciableFixedAssets(
			Date depreciationFrom, Date depreciationTo) throws DAOException;
	
	public Date getDepreciationLastDate() throws DAOException;
	
	public void rollBackDepreciation(Date rollBackDepreciationTo) throws DAOException;
	
	public double getCalculatedDepreciatedAmount(int depreciationMethod,
			double depreciationRate, double purchasePrice,
			Date depreciationFrom, Date depreciationTo) throws DAOException;
	
	public double getCalculatedRollBackDepreciationAmount(String fixedAssetID,
			Date rollBackDepreciationTo) throws DAOException;
	
	public FixedAssetSellOrDisposeReviewJournal getReviewJournal(
			TempFixedAsset fixedAsset) throws DAOException;
	
	public void changeDepreciationStartDateTo(Date newStartDate) throws DAOException;
	
	public List<Date> getFinancialYearStartDates() throws DAOException;
	
	public List<Date> getAllDepreciationFromDates() throws DAOException; 
	
	public void changeFiscalYearsStartDateTo(Date newStartDate)
	throws DAOException;
	
	public KeyFinancialIndicators getKeyFinancialIndicators()
	throws DAOException;
	
}
