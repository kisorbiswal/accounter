/**
 * 
 */
package com.vimukti.accounter.services;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Map;

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
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.FiscalYear;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.JournalEntry;
import com.vimukti.accounter.core.MakeDeposit;
import com.vimukti.accounter.core.PaySalesTaxEntries;
import com.vimukti.accounter.core.PayVATEntries;
import com.vimukti.accounter.core.ReceivePayment;
import com.vimukti.accounter.core.ReceiveVATEntries;
import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.core.TransactionMakeDeposit;
import com.vimukti.accounter.core.TransferFund;
import com.vimukti.accounter.core.VATReturn;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.core.WriteCheck;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientTransactionMakeDeposit;
import com.vimukti.accounter.web.client.core.ClientUserInfo;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.IAccounterCore;
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
import com.vimukti.accounter.web.client.core.Lists.OpenAndClosedOrders;
import com.vimukti.accounter.web.client.core.Lists.OverDueInvoicesList;
import com.vimukti.accounter.web.client.core.Lists.PayBillTransactionList;
import com.vimukti.accounter.web.client.core.Lists.PayeeList;
import com.vimukti.accounter.web.client.core.Lists.PayeeStatementsList;
import com.vimukti.accounter.web.client.core.Lists.PaymentsList;
import com.vimukti.accounter.web.client.core.Lists.PurchaseOrdersAndItemReceiptsList;
import com.vimukti.accounter.web.client.core.Lists.PurchaseOrdersList;
import com.vimukti.accounter.web.client.core.Lists.ReceivePaymentTransactionList;
import com.vimukti.accounter.web.client.core.Lists.ReceivePaymentsList;
import com.vimukti.accounter.web.client.core.Lists.SalesOrdersList;
import com.vimukti.accounter.web.client.core.Lists.SellingOrDisposingFixedAssetList;
import com.vimukti.accounter.web.client.core.Lists.TempFixedAsset;
import com.vimukti.accounter.web.client.core.reports.AccountBalance;
import com.vimukti.accounter.web.client.core.reports.AccountRegister;
import com.vimukti.accounter.web.client.core.reports.AgedDebtors;
import com.vimukti.accounter.web.client.core.reports.AmountsDueToVendor;
import com.vimukti.accounter.web.client.core.reports.DepositDetail;
import com.vimukti.accounter.web.client.core.reports.ECSalesList;
import com.vimukti.accounter.web.client.core.reports.ECSalesListDetail;
import com.vimukti.accounter.web.client.core.reports.ExpenseList;
import com.vimukti.accounter.web.client.core.reports.MostProfitableCustomers;
import com.vimukti.accounter.web.client.core.reports.ReverseChargeList;
import com.vimukti.accounter.web.client.core.reports.ReverseChargeListDetail;
import com.vimukti.accounter.web.client.core.reports.SalesByCustomerDetail;
import com.vimukti.accounter.web.client.core.reports.SalesTaxLiability;
import com.vimukti.accounter.web.client.core.reports.TransactionDetailByAccount;
import com.vimukti.accounter.web.client.core.reports.TransactionDetailByTaxItem;
import com.vimukti.accounter.web.client.core.reports.TransactionHistory;
import com.vimukti.accounter.web.client.core.reports.TrialBalance;
import com.vimukti.accounter.web.client.core.reports.UncategorisedAmountsReport;
import com.vimukti.accounter.web.client.core.reports.VATDetail;
import com.vimukti.accounter.web.client.core.reports.VATItemDetail;
import com.vimukti.accounter.web.client.core.reports.VATItemSummary;
import com.vimukti.accounter.web.client.core.reports.VATSummary;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.reports.CheckDetailReport;

/**
 * @author Fernandez
 * 
 */
public interface IFinanceDAOService {

	// public <T extends IAccounterCore> String createObject(T t)
	// throws DAOException;
	//
	// public <T extends IAccounterCore> Boolean updateObject(T t)
	// throws DAOException;
	//
	// public Boolean deleteObject(AccounterCoreType type, String id)
	// throws DAOException;

	public <T extends IAccounterCore> T getObjectById(AccounterCoreType type,
			long id) throws DAOException, AccounterException;

	public <T extends IAccounterCore> T getObjectByName(AccounterCoreType type,
			String name) throws DAOException, AccounterException;

	public <T extends IAccounterCore> ArrayList<T> getObjects(
			AccounterCoreType type) throws DAOException, AccounterException;

	// public Boolean updateCompanyPreferences(ClientCompanyPreferences
	// preferences)
	// throws DAOException;
	//
	// public Boolean updateCompany(ClientCompany clientCompany)
	// throws DAOException;

	/**
	 * This method is to check whether an Object is deletable or not.
	 * 
	 * @param <T>
	 * @param clazz
	 * @param id
	 * @return true if the object related to the given string is deletable,
	 *         otherwise return false.
	 * @throws DAOException
	 */
	// public <T extends IAccounterServerCore> Boolean canDelete(
	// AccounterCoreType clazz, String id) throws DAOException;

	public void alterFiscalYear(final FiscalYear fiscalYear)
			throws DAOException;

	/**
	 * For merging the from one customer to another customer
	 * 
	 * @param fromID
	 * @param toID
	 */
	public void mergeCustomer(ClientCustomer fromClientCustomer,
			ClientCustomer toClientCustomer) throws DAOException;

	public void mergeVendor(ClientVendor from, ClientVendor to)
			throws DAOException;

	public void mergeAcoount(long fromID, long toID) throws DAOException;

	public void mergeItem(ClientItem fromClientItem, ClientItem toClientItem)
			throws DAOException;

	/**
	 * Company Home page widgets Related Dao Methods
	 */

	/*
	 * ==========================================================================
	 * ======
	 */
	// IAccounterGUIDAOService methods
	/*
	 * ==========================================================================
	 * =======
	 */

	// public ProfitAndLoss getProfitAndLossReport()
	// throws DAOException;
	public ArrayList<OverDueInvoicesList> getOverDueInvoices()
			throws DAOException;

	public ArrayList<EnterBill> getBillsOwed() throws DAOException;

	public ArrayList<Estimate> getLatestQuotes() throws DAOException;

	// public ArrayList<ExpensesThisFiscalYear> getExpensesThisFiscalYear(
	// Company company) throws DAOException;

	public ArrayList<CreditCardCharge> getCreditCardChargesThisMonth(
			final long date) throws DAOException;

	public ArrayList<Customer> getLatestCustomers() throws DAOException;

	public ArrayList<Vendor> getLatestVendors() throws DAOException;

	public ArrayList<Item> getLatestItems() throws DAOException;

	public ArrayList<PaymentsList> getLatestPayments() throws DAOException;

	public ArrayList<CashSales> getLatestCashSales() throws DAOException;

	public ArrayList<CustomerRefund> getLatestCustomerRefunds()
			throws DAOException;

	public ArrayList<BillsList> getLatestBills() throws DAOException;

	public ArrayList<CashPurchase> getLatestCashPurchases() throws DAOException;

	public ArrayList<WriteCheck> getLatestChecks() throws DAOException;

	public ArrayList<MakeDeposit> getLatestDeposits() throws DAOException;

	public ArrayList<TransferFund> getLatestFundsTransfer() throws DAOException;

	public ArrayList<InvoicesList> getLatestInvoices() throws DAOException;

	public ArrayList<ReceivePayment> getLatestReceivePayments()
			throws DAOException;

	public ArrayList<PaymentsList> getLatestVendorPayments()
			throws DAOException;

	public ArrayList<Item> getLatestSalesItems() throws DAOException;

	public ArrayList<Item> getLatestPurchaseItems() throws DAOException;

	/**
	 * Other Dao Methods required for GUI
	 */

	// To auto generate the next Transaction number for all types of
	// Transactions
	public String getNextTransactionNumber(int transactionType)
			throws DAOException;

	public Long getNextIssuePaymentCheckNumber(long account2)
			throws DAOException;

	// To auto generate the next Voucher number for all vouchers.
	public String getNextVoucherNumber() throws DAOException;

	// To auto generate the next Check number.
	public Long getNextCheckNumber(long account) throws DAOException;

	// To auto generate the next Nominal Code for a given account type with in
	// the range.
	public Long getNextNominalCode(int accountType) throws DAOException;

	public String getNextFixedAssetNumber() throws DAOException;

	// To check whether an Account is a Tax Agency Account or not
	public boolean isTaxAgencyAccount(long account) throws DAOException;

	// To check whether an Account is a Sales Tax Payable Account or not
	public boolean isSalesTaxPayableAccount(long accountId) throws DAOException;

	// To check whether an Account is a Sales Tax Payable Account or not
	public boolean isSalesTaxPayableAccountByName(String accountName)
			throws DAOException;

	// To get all the Estimates/Quotes in a company
	public ArrayList<Estimate> getEstimates() throws DAOException;

	// To get the Estimates/Quotes of a particular customer in the company
	public ArrayList<Estimate> getEstimates(long customer) throws DAOException;

	// To check whether an Invoice or Vendor Bill can be Voidable and Editable
	// or not
	public boolean canVoidOrEdit(long invoiceOrVendorBillId)
			throws DAOException;

	// To display the Item combo box of Transaction Item lines in Creating
	// Creating Invoice,Cash Sale, Customer Credit Memo, Customer Refund
	public ArrayList<Item> getSalesItems() throws DAOException;

	// To display the Item combo box of Transaction Item lines in Creating Enter
	// Bill,Cash Purchase, Vendor Credit Memo Transactions
	public ArrayList<Item> getPurchaseItems() throws DAOException;

	// To get the Credits and Payments of a particular Customer in a company
	public ArrayList<CreditsAndPayments> getCustomerCreditsAndPayments(
			long customer) throws DAOException;

	// To get the Credits and Payments of a particular Vendor in a company
	public ArrayList<CreditsAndPayments> getVendorCreditsAndPayments(long vendor)
			throws DAOException;

	// To get Make Deposit by passing the Transaction Make Deposit
	public TransactionMakeDeposit getTransactionMakeDeposit(
			long transactionMakeDepositId) throws DAOException;

	// To get all the Invoices and CustomerRefunds of a particular customer
	// which are not paid and display as the Transaction ReceivePayments in
	// ReceivePayment
	public ArrayList<ReceivePaymentTransactionList> getTransactionReceivePayments(
			long customerId, long paymentDate) throws DAOException,
			ParseException;

	// To get all the Vendor Bills and MakeDeposit(New Vendor Entries) which are
	// not paid and display as the Transaction PayBills in PayBill
	public ArrayList<PayBillTransactionList> getTransactionPayBills()
			throws DAOException;

	// To get all the Vendor Bills and MakeDeposit(New Vendor Entries) of a
	// particular Vendor which are not paid and display as the Transaction
	// PayBills in PayBill
	public ArrayList<PayBillTransactionList> getTransactionPayBills(
			long vendorId) throws DAOException;

	// To get all the Write Checks in a company
	public ArrayList<IssuePaymentTransactionsList> getChecks()
			throws DAOException;

	// To get the Write Checks related to a particular Account in a company
	public ArrayList<IssuePaymentTransactionsList> getChecks(long account)
			throws DAOException;

	// To get a particular Journal Entry
	public JournalEntry getJournalEntry(long journalEntryId)
			throws DAOException;

	// To get all the Journal Entries in a company
	public ArrayList<JournalEntry> getJournalEntries() throws DAOException;

	// To get all the Entries of a particular journal entry
	public ArrayList<Entry> getEntries(long journalEntryId) throws DAOException;

	// to get the Account Register of a particular account
	// public AccountRegister getAccountRegister(String accountId)
	// throws DAOException;

	// To get all Customer Refunds , Vendor Payments,Cash Purchases, Credit Card
	// Charge and all Write Checks
	public ArrayList<PaymentsList> getPaymentsList() throws DAOException;

	// To get all Invoices, Customer Credit Memo, Cash Sales and Write Checks ->
	// for Customer
	public ArrayList<InvoicesList> getInvoiceList() throws DAOException;

	// To get all Customer Refunds and Write Checks -> for Customer
	public ArrayList<CustomerRefundsList> getCustomerRefundsList()
			throws DAOException;

	// To get all Vendor Bills, Vendor Credit Memo, Cash Purchase and Credit
	// Card Charge and Write Check -> Vendor
	public ArrayList<BillsList> getBillsList(boolean isExpensesList)
			throws DAOException;

	// To get all Vendor Payments, PayBills, Write Check -> Vendor and TaxAgency
	public ArrayList<ReceivePaymentsList> getReceivePaymentsList()
			throws DAOException;

	// To get all Vendor Payments, PayBills, Write Check -> Vendor and TaxAgency
	public ArrayList<PaymentsList> getVendorPaymentsList() throws DAOException;

	// To display the liabilityAccount combo box of New Tax Agency window
	public ArrayList<Account> getTaxAgencyAccounts() throws DAOException;

	public ArrayList<ClientTransactionMakeDeposit> getTransactionMakeDeposits()
			throws DAOException;

	public void test() throws Exception;

	public ArrayList<PaySalesTaxEntries> getTransactionPaySalesTaxEntriesList(
			long billsDueOnOrBefore) throws DAOException;

	public ArrayList<EstimatesAndSalesOrdersList> getEstimatesAndSalesOrdersList(
			long customerId) throws DAOException;

	public ArrayList<PurchaseOrdersAndItemReceiptsList> getPurchasesAndItemReceiptsList(
			long vendorId) throws DAOException;

	public ArrayList<SalesOrdersList> getSalesOrders(boolean orderByDate);

	public ArrayList<PurchaseOrdersList> getPurchaseOrders(boolean orderByDate);

	public ArrayList<SalesOrdersList> getSalesOrdersForCustomer(long customerID);

	public ArrayList<SalesOrdersList> getPurchaseOrdersForVendor(long vendorID);

	ArrayList<SalesOrdersList> getSalesOrdersList() throws DAOException;

	ArrayList<PurchaseOrdersList> getPurchaseOrdersList() throws DAOException;

	ArrayList<PurchaseOrdersList> getNotReceivedPurchaseOrdersList(long vendorId)
			throws DAOException;

	ArrayList<FixedAssetList> getFixedAssets(int status) throws DAOException;

	ArrayList<SellingOrDisposingFixedAssetList> getSellingOrDisposingFixedAssets()
			throws DAOException;

	public void runDepreciation(long depreciationFrom, long depreciationTo,
			FixedAssetLinkedAccountMap linkedAccounts) throws DAOException;

	public DepreciableFixedAssetsList getDepreciableFixedAssets(
			long depreciationFrom, long depreciationTo) throws DAOException;

	public ClientFinanceDate getDepreciationLastDate() throws DAOException;

	public void rollBackDepreciation(long rollBackDepreciationTo)
			throws AccounterException;

	public double getCalculatedDepreciatedAmount(int depreciationMethod,
			double depreciationRate, double purchasePrice,
			long depreciationFrom, long depreciationTo) throws DAOException;

	public double getCalculatedRollBackDepreciationAmount(
			long rollBackDepreciationTo) throws DAOException;

	public double getCalculatedRollBackDepreciationAmount(long fixedAssetID,
			long rollBackDepreciationTo) throws DAOException;

	public FixedAssetSellOrDisposeReviewJournal getReviewJournal(
			TempFixedAsset fixedAsset) throws DAOException;

	// public void changeDepreciationStartDateTo(long newStartDate)
	// throws DAOException;

	public ArrayList<ClientFinanceDate> getFinancialYearStartDates()
			throws DAOException;

	public ArrayList<ClientFinanceDate> getAllDepreciationFromDates()
			throws DAOException;

	// public void changeFiscalYearsStartDateTo(long newStartDate)
	// throws DAOException;

	/*
	 * ==========================================================================
	 * ======================
	 */
	// IAccounterReportsDAOService methods
	/*
	 * ==========================================================================
	 * ======================
	 */

	public ArrayList<AccountBalance> getAccountBalances() throws DAOException;

	public ArrayList<TrialBalance> getTrialBalance(FinanceDate startDate,
			FinanceDate endDate) throws DAOException;

	public ArrayList<AgedDebtors> getAgedDebtors(FinanceDate startDate,
			FinanceDate endDate) throws DAOException;

	public ArrayList<AgedDebtors> getAgedDebtors(FinanceDate startDate,
			FinanceDate endDate, int intervalDays, int throughDaysPassOut)
			throws DAOException;

	public ArrayList<AgedDebtors> getAgedCreditors(FinanceDate startDate,
			FinanceDate endDate) throws DAOException;

	public ArrayList<SalesByCustomerDetail> getSalesByCustomerDetailReport(
			FinanceDate startDate, FinanceDate endDate) throws DAOException;

	public ArrayList<SalesByCustomerDetail> getSalesByCustomerSummary(
			FinanceDate startDate, FinanceDate endDate) throws DAOException;

	public ArrayList<SalesByCustomerDetail> getSalesByItemDetail(
			FinanceDate startDate, FinanceDate endDate) throws DAOException;

	public ArrayList<SalesByCustomerDetail> getSalesByItemSummary(
			FinanceDate startDate, FinanceDate endDate) throws DAOException;

	public ArrayList<TransactionHistory> getCustomerTransactionHistory(
			FinanceDate startDate, FinanceDate endDate)
			throws AccounterException;

	public ArrayList<SalesByCustomerDetail> getPurchasesByVendorDetail(
			FinanceDate startDate, FinanceDate endDate) throws DAOException;

	public ArrayList<SalesByCustomerDetail> getPurchasesByVendorSummary(
			FinanceDate startDate, FinanceDate endDate) throws DAOException;

	public ArrayList<SalesByCustomerDetail> getPurchasesByItemDetail(
			FinanceDate startDate, FinanceDate endDate) throws DAOException;

	public ArrayList<SalesByCustomerDetail> getPurchasesByItemSummary(
			FinanceDate startDate, FinanceDate endDate) throws DAOException;

	public ArrayList<TransactionHistory> getVendorTransactionHistory(
			FinanceDate startDate, FinanceDate endDate)
			throws AccounterException;

	public ArrayList<AmountsDueToVendor> getAmountsDueToVendor(
			FinanceDate startDate, FinanceDate endDate) throws DAOException;

	public ArrayList<MostProfitableCustomers> getMostProfitableCustomers(
			FinanceDate startDate, FinanceDate endDate) throws DAOException;

	public ArrayList<MostProfitableCustomers> getProfitabilityByCustomerDetail(
			long customer, FinanceDate startDate, FinanceDate endDate)
			throws DAOException;

	public ArrayList<TransactionDetailByTaxItem> getTransactionDetailByTaxItem(
			FinanceDate startDate, FinanceDate endDate) throws DAOException;

	public ArrayList<Transaction> getRegister(Account account)
			throws DAOException;

	public ArrayList<AccountRegister> getAccountRegister(FinanceDate startDate,
			FinanceDate endDate, long accountId) throws DAOException;

	public ArrayList<TransactionDetailByAccount> getTransactionDetailByAccount(
			final FinanceDate startDate, final FinanceDate endDate)
			throws DAOException;

	public ArrayList<SalesTaxLiability> getSalesTaxLiabilityReport(
			final FinanceDate startDate, final FinanceDate endDate)
			throws AccounterException;

	public ArrayList<Customer> getTransactionHistoryCustomers(
			FinanceDate startDate, FinanceDate endDate) throws DAOException;

	public ArrayList<Vendor> getTransactionHistoryVendors(
			FinanceDate startDate, FinanceDate endDate) throws DAOException;

	public ArrayList<Item> getSalesReportItems(FinanceDate startDate,
			FinanceDate endDate) throws DAOException;

	public ArrayList<Item> getPurchaseReportItems(FinanceDate startDate,
			FinanceDate endDate) throws DAOException;

	public ClientFinanceDate[] getMinimumAndMaximumTransactionDate()
			throws AccounterException;

	public ArrayList<TrialBalance> getBalanceSheetReport(FinanceDate startDate,
			FinanceDate endDate) throws DAOException;

	public ArrayList<TrialBalance> getProfitAndLossReport(
			FinanceDate startDate, FinanceDate endDate) throws DAOException;

	public ArrayList<TrialBalance> getCashFlowReport(FinanceDate startDate,
			FinanceDate endDate) throws AccounterException;

	public ArrayList<SalesByCustomerDetail> getSalesByCustomerDetailReport(
			String customerName, FinanceDate startDate, FinanceDate endDate)
			throws DAOException;

	public ArrayList<SalesByCustomerDetail> getSalesByItemDetail(
			String itemName, FinanceDate startDate, FinanceDate endDate)
			throws DAOException;

	public ArrayList<SalesByCustomerDetail> getPurchasesByVendorDetail(
			String vendorName, FinanceDate startDate, FinanceDate endDate)
			throws DAOException;

	public ArrayList<SalesByCustomerDetail> getPurchasesByItemDetail(
			String itemName, FinanceDate startDate, FinanceDate endDate)
			throws DAOException;

	public ArrayList<TransactionDetailByAccount> getTransactionDetailByAccount(
			String accountName, final FinanceDate startDate,
			final FinanceDate endDate) throws DAOException;

	public ArrayList<TransactionDetailByTaxItem> getTransactionDetailByTaxItem(
			final String taxItemName, final FinanceDate startDate,
			final FinanceDate endDate) throws DAOException;

	public ArrayList<OpenAndClosedOrders> getOpenSalesOrders(
			FinanceDate startDate, FinanceDate endDate) throws DAOException;

	public ArrayList<OpenAndClosedOrders> getClosedSalesOrders(
			FinanceDate startDate, FinanceDate endDate) throws DAOException;

	public ArrayList<OpenAndClosedOrders> getCompletedSalesOrders(
			FinanceDate startDate, FinanceDate endDate) throws DAOException;

	public ArrayList<OpenAndClosedOrders> getPurchaseOrders(
			FinanceDate startDate, FinanceDate endDate) throws DAOException;

	public ArrayList<OpenAndClosedOrders> getSalesOrders(FinanceDate startDate,
			FinanceDate endDate) throws DAOException;

	public ArrayList<OpenAndClosedOrders> getCanceledSalesOrders(
			FinanceDate startDate, FinanceDate endDate) throws DAOException;

	public ArrayList<OpenAndClosedOrders> getOpenPurchaseOrders(
			FinanceDate startDate, FinanceDate endDate) throws DAOException;

	public ArrayList<OpenAndClosedOrders> getClosedPurchaseOrders(
			FinanceDate startDate, FinanceDate endDate) throws DAOException;

	public ArrayList<OpenAndClosedOrders> getCompletedPurchaseOrders(
			FinanceDate startDate, FinanceDate endDate) throws DAOException;

	public ArrayList<OpenAndClosedOrders> getCanceledPurchaseOrders(
			FinanceDate startDate, FinanceDate endDate) throws DAOException;

	// For UK

	public Map<String, Double> getVATReturnBoxes(FinanceDate startDate,
			FinanceDate endDate) throws DAOException;

	public VATReturn getVATReturnDetails(TAXAgency vatAgency,
			FinanceDate fromDate,

			FinanceDate toDate) throws DAOException, AccounterException;

	public ArrayList<VATSummary> getPriorReturnVATSummary(TAXAgency vatAgency,
			FinanceDate endDate) throws DAOException, ParseException;

	public ArrayList<VATDetail> getPriorVATReturnVATDetailReport(
			TAXAgency vatAgency, FinanceDate endDate) throws DAOException,
			ParseException;

	public ArrayList<VATDetail> getVATDetailReport(FinanceDate startDate,
			FinanceDate endDate) throws DAOException, ParseException;

	public ArrayList<VATSummary> getVAT100Report(TAXAgency vatAgency,
			FinanceDate fromDate, FinanceDate toDate) throws DAOException,
			ParseException;

	public ArrayList<UncategorisedAmountsReport> getUncategorisedAmountsReport(
			FinanceDate fromDate, FinanceDate toDate) throws DAOException,
			ParseException;

	public ArrayList<VATItemDetail> getVATItemDetailReport(
			FinanceDate fromDate, FinanceDate toDate) throws DAOException,
			ParseException;

	public ArrayList<VATItemSummary> getVATItemSummaryReport(
			FinanceDate fromDate, FinanceDate toDate) throws DAOException,
			ParseException;

	public ArrayList<PayVATEntries> getPayVATEntries();

	public ArrayList<ReceiveVATEntries> getReceiveVATEntries();

	public ArrayList<ECSalesListDetail> getECSalesListDetailReport(
			String payeeName, FinanceDate fromDate, FinanceDate toDate)
			throws DAOException, ParseException;

	public KeyFinancialIndicators getKeyFinancialIndicators()
			throws DAOException;

	public ArrayList<ECSalesList> getECSalesListReport(FinanceDate fromDate,
			FinanceDate toDate) throws DAOException, ParseException;

	public ArrayList<ReverseChargeListDetail> getReverseChargeListDetailReport(
			String payeeName, FinanceDate fromDate, FinanceDate toDate)
			throws DAOException, ParseException;

	public ArrayList<ReverseChargeList> getReverseChargeListReport(
			FinanceDate fromDate, FinanceDate toDate) throws DAOException,
			ParseException;

	public void createTaxes(int[] vatReturnType) throws DAOException;

	public ArrayList<PayeeList> getPayeeList(int transactionCategory)
			throws DAOException;

	public ArrayList<ExpenseList> getExpenseReportByType(int status,
			FinanceDate startDate, FinanceDate endDate) throws DAOException;

	/*
	 * 
	 * ==========================================================================
	 * ==================
	 */
	/*
	 * ==========================================================================
	 * ==================
	 */

	public ArrayList<VATItemDetail> getVATItemDetailReport(String vatItemId,
			FinanceDate fromDate, FinanceDate toDate) throws DAOException,
			ParseException;

	public String getNextVendorNumber() throws DAOException;

	public String getNextCustomerNumber() throws DAOException;

	public ArrayList<DepositDetail> getDepositDetail(FinanceDate startDate,
			FinanceDate endDate);

	public ArrayList<CheckDetailReport> getCheckDetailReport(
			long paymentmethod, FinanceDate startDate, FinanceDate endDate)
			throws DAOException;

	public ArrayList<PayeeStatementsList> getPayeeStatementsList(long id,
			long transactionDate, FinanceDate fromDate, FinanceDate toDate,
			int noOfDays, boolean isEnabledOfZeroBalBox,
			boolean isEnabledOfLessthanZeroBalBox,
			double lessThanZeroBalanceValue,
			boolean isEnabledOfNoAccountActivity,
			boolean isEnabledOfInactiveCustomer) throws DAOException;

	public ArrayList<Double> getGraphPointsforAccount(int chartType,
			long accountNo) throws DAOException;

	public ArrayList<BillsList> getEmployeeExpensesByStatus(String userName,
			int status) throws DAOException;

	public boolean changeMyPassword(String emailId, String oldPassword,
			String newPassword) throws DAOException;

	public ArrayList<ClientUserInfo> getAllUsers() throws AccounterException;

	public ArrayList<PayeeStatementsList> getCustomerStatement(long customer,
			long fromDate, long toDate);
}