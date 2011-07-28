package com.vimukti.accounter.services;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.support.TransactionTemplate;

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
import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.core.TransactionMakeDeposit;
import com.vimukti.accounter.core.TransferFund;
import com.vimukti.accounter.core.Utility;
import com.vimukti.accounter.core.VATReturn;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.core.WriteCheck;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
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

public class AccounterGUIDAOService extends HibernateDaoSupport implements
		IAccounterGUIDAOService {

	TransactionTemplate transactionTemplate;
	IAccounterDAOService accounterDao;

	public void setTransactionTemplate(TransactionTemplate template) {
		this.transactionTemplate = template;
	}

	public void setAccounterDao(IAccounterDAOService accounterDao) {
		this.accounterDao = accounterDao;
	}

	private long getLongIdForGivenid(AccounterCoreType entity, String account) {

		Session session = Utility.getCurrentSession();
		Query query = session.getNamedQuery(
				"get.entity.from." + entity.getServerClassFullyQualifiedName())
				.setString(0, account);
		List<?> l = query.list();
		if (l != null && !l.isEmpty() && l.get(0) != null) {
			return (Long) l.get(0);
		} else
			return 0;

	}

	@Override
	public boolean canVoidOrEdit(String invoiceOrVendorBillId)
			throws DAOException {
		return false;
	}

	@Override
	public void changeDepreciationStartDateTo(Date newStartDate)
			throws DAOException {

	}

	@Override
	public void changeFiscalYearsStartDateTo(Date newStartDate)
			throws DAOException {

	}

	@Override
	public List<Date> getAllDepreciationFromDates() throws DAOException {

		return null;
	}

	@Override
	public List<BillsList> getBillsList() throws DAOException {

		return null;
	}

	@Override
	public List<EnterBill> getBillsOwed() throws DAOException {

		return null;
	}

	@Override
	public double getCalculatedDepreciatedAmount(int depreciationMethod,
			double depreciationRate, double purchasePrice,
			Date depreciationFrom, Date depreciationTo) throws DAOException {

		return 0;
	}

	@Override
	public double getCalculatedRollBackDepreciationAmount(String fixedAssetID,
			Date rollBackDepreciationTo) throws DAOException {

		return 0;
	}

	@Override
	public List<IssuePaymentTransactionsList> getChecks() throws DAOException {

		return null;
	}

	@Override
	public List<IssuePaymentTransactionsList> getChecks(String account)
			throws DAOException {

		return null;
	}

	@Override
	public List<CreditCardCharge> getCreditCardChargesThisMonth(Date date)
			throws DAOException {

		return null;
	}

	@Override
	public List<CreditsAndPayments> getCustomerCreditsAndPayments(
			String customer) throws DAOException {

		return null;
	}

	@Override
	public List<CustomerRefundsList> getCustomerRefundsList()
			throws DAOException {

		return null;
	}

	@Override
	public DepreciableFixedAssetsList getDepreciableFixedAssets(
			Date depreciationFrom, Date depreciationTo) throws DAOException {

		return null;
	}

	@Override
	public List<DepreciableFixedAssetsList> getDepreciationFixedAssets(
			Date startDate, Date toDate) throws DAOException {

		return null;
	}

	@Override
	public Date getDepreciationLastDate() throws DAOException {

		return null;
	}

	@Override
	public List<Entry> getEntries(String journalEntryId) throws DAOException {

		return null;
	}

	@Override
	public List<Estimate> getEstimates() throws DAOException {

		return null;
	}

	@Override
	public List<Estimate> getEstimates(String customer) throws DAOException {

		return null;
	}

	@Override
	public List<EstimatesAndSalesOrdersList> getEstimatesAndSalesOrdersList(
			String customerId) throws DAOException {

		return null;
	}

	@Override
	public List<Date> getFinancialYearStartDates() throws DAOException {

		return null;
	}

	@Override
	public List<FixedAssetList> getFixedAssets(int status) throws DAOException {

		return null;
	}

	@Override
	public List<InvoicesList> getInvoiceList() throws DAOException {

		return null;
	}

	@Override
	public List<JournalEntry> getJournalEntries() throws DAOException {

		return null;
	}

	@Override
	public JournalEntry getJournalEntry(String journalEntryId)
			throws DAOException {

		return null;
	}

	@Override
	public KeyFinancialIndicators getKeyFinancialIndicators()
			throws DAOException {

		return null;
	}

	@Override
	public List<BillsList> getLatestBills() throws DAOException {

		return null;
	}

	@Override
	public List<CashPurchase> getLatestCashPurchases() throws DAOException {

		return null;
	}

	@Override
	public List<CashSales> getLatestCashSales() throws DAOException {

		return null;
	}

	@Override
	public List<WriteCheck> getLatestChecks() throws DAOException {

		return null;
	}

	@Override
	public List<CustomerRefund> getLatestCustomerRefunds() throws DAOException {

		return null;
	}

	@Override
	public List<Customer> getLatestCustomers() throws DAOException {

		return null;
	}

	@Override
	public List<MakeDeposit> getLatestDeposits() throws DAOException {

		return null;
	}

	@Override
	public List<TransferFund> getLatestFundsTransfer() throws DAOException {

		return null;
	}

	@Override
	public List<InvoicesList> getLatestInvoices() throws DAOException {

		return null;
	}

	@Override
	public List<Item> getLatestItems() throws DAOException {

		return null;
	}

	@Override
	public List<PaymentsList> getLatestPayments() throws DAOException {

		return null;
	}

	@Override
	public List<Item> getLatestPurchaseItems() throws DAOException {

		return null;
	}

	@Override
	public List<Estimate> getLatestQuotes() throws DAOException {

		return null;
	}

	@Override
	public List<ReceivePayment> getLatestReceivePayments() throws DAOException {

		return null;
	}

	@Override
	public List<Item> getLatestSalesItems() throws DAOException {

		return null;
	}

	@Override
	public List<PaymentsList> getLatestVendorPayments() throws DAOException {

		return null;
	}

	@Override
	public List<Vendor> getLatestVendors() throws DAOException {

		return null;
	}

	@Override
	public Long getNextCheckNumber(String account) throws DAOException {

		return null;
	}

	@Override
	public Long getNextNominalCode(int accountType) throws DAOException {

		return null;
	}

	@Override
	public Long getNextTransactionNumber(int transactionType)
			throws DAOException {

		return null;
	}

	@Override
	public Long getNextVoucherNumber() throws DAOException {

		return null;
	}

	@Override
	public List<PurchaseOrdersList> getNotReceivedPurchaseOrdersList(
			String vendorId) throws DAOException {

		return null;
	}

	@Override
	public List<OverDueInvoicesList> getOverDueInvoices() throws DAOException {

		return null;
	}

	@Override
	public List<PaymentsList> getPaymentsList() throws DAOException {

		return null;
	}

	@Override
	public List<Item> getPurchaseItems() throws DAOException {

		return null;
	}

	@Override
	public List<PurchaseOrdersList> getPurchaseOrdersList(String vendorId)
			throws DAOException {

		return null;
	}

	@Override
	public List<PurchaseOrdersAndItemReceiptsList> getPurchasesAndItemReceiptsList(
			String vendorId) throws DAOException {

		return null;
	}

	@Override
	public List<ReceivePaymentsList> getReceivePaymentsList()
			throws DAOException {

		return null;
	}

	@Override
	public FixedAssetSellOrDisposeReviewJournal getReviewJournal(
			TempFixedAsset fixedAsset) throws DAOException {

		return null;
	}

	@Override
	public List<Item> getSalesItems() throws DAOException {

		return null;
	}

	@Override
	public List<SalesOrdersList> getSalesOrdersList(String customerId)
			throws DAOException {

		return null;
	}

	@Override
	public List<SellingOrDisposingFixedAssetList> getSellingOrDisposingFixedAssets()
			throws DAOException {

		return null;
	}

	@Override
	public List<Account> getTaxAgencyAccounts() throws DAOException {

		return null;
	}

	@Override
	public TransactionMakeDeposit getTransactionMakeDeposit(
			String transactionMakeDepositId) throws DAOException {

		return null;
	}

	@Override
	public List<MakeDepositTransactionsList> getTransactionMakeDeposits()
			throws DAOException {

		return null;
	}

	@Override
	public List<PayBillTransactionList> getTransactionPayBills()
			throws DAOException {

		return null;
	}

	@Override
	public List<PayBillTransactionList> getTransactionPayBills(String vendorId)
			throws DAOException {

		return null;
	}

	@Override
	public List<PaySalesTaxEntries> getTransactionPaySalesTaxEntriesList(
			Date transactionDate) throws DAOException {

		return null;
	}

	@Override
	public List<ReceivePaymentTransactionList> getTransactionReceivePayments(
			String customerId) throws DAOException {

		return null;
	}

	@Override
	public VATReturn getVATReturn(TAXAgency vatAgency, Date fromDate,
			Date toDate) throws DAOException {

		return null;
	}

	@Override
	public List<CreditsAndPayments> getVendorCreditsAndPayments(String vendor)
			throws DAOException {

		return null;
	}

	@Override
	public List<PaymentsList> getVendorPaymentsList() throws DAOException {

		return null;
	}

	@Override
	public boolean isSalesTaxPayableAccount(String accountId)
			throws DAOException {

		return false;
	}

	@Override
	public boolean isSalesTaxPayableAccountByName(String accountName)
			throws DAOException {

		return false;
	}

	@Override
	public boolean isTaxAgencyAccount(String account) throws DAOException {

		return false;
	}

	@Override
	public void rollBackDepreciation(Date rollBackDepreciationTo)
			throws DAOException {

	}

	@Override
	public void runDepreciation(Date depreciationFrom, Date depreciationTo,
			FixedAssetLinkedAccountMap linkedAccounts) throws DAOException {

	}

	@Override
	public void test() throws Exception {

	}

}
