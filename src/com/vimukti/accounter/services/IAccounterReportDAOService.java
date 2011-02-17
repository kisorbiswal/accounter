package com.vimukti.accounter.services;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.core.VATReturn;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.web.client.core.reports.AccountBalance;
import com.vimukti.accounter.web.client.core.reports.AccountRegister;
import com.vimukti.accounter.web.client.core.reports.AgedDebtors;
import com.vimukti.accounter.web.client.core.reports.AmountsDueToVendor;
import com.vimukti.accounter.web.client.core.reports.DepositDetail;
import com.vimukti.accounter.web.client.core.reports.MostProfitableCustomers;
import com.vimukti.accounter.web.client.core.reports.SalesByCustomerDetail;
import com.vimukti.accounter.web.client.core.reports.SalesTaxLiability;
import com.vimukti.accounter.web.client.core.reports.TransactionDetailByAccount;
import com.vimukti.accounter.web.client.core.reports.TransactionDetailByTaxItem;
import com.vimukti.accounter.web.client.core.reports.TransactionHistory;
import com.vimukti.accounter.web.client.core.reports.TrialBalance;
import com.vimukti.accounter.web.client.core.reports.VATDetailReport;
import com.vimukti.accounter.web.client.core.reports.VATItemDetail;
import com.vimukti.accounter.web.client.core.reports.VATSummary;

public interface IAccounterReportDAOService {

	public List<AccountBalance> getAccountBalances() throws DAOException;

	public List<TrialBalance> getTrialBalance(String startDate, String endDate)
			throws DAOException;

	public List<AgedDebtors> getAgedDebtors(String startDate, String endDate)
			throws DAOException;

	public List<AgedDebtors> getAgedCreditors(String startDate, String endDate)
			throws DAOException;

	public List<SalesByCustomerDetail> getSalesByCustomerDetailReport(
			String startDate, String endDate) throws DAOException;

	public List<SalesByCustomerDetail> getSalesByCustomerSummary(
			String startDate, String endDate) throws DAOException;

	public List<SalesByCustomerDetail> getSalesByItemDetail(String startDate,
			String endDate) throws DAOException;

	public List<SalesByCustomerDetail> getSalesByItemSummary(String startDate,
			String endDate) throws DAOException;

	public List<TransactionHistory> getCustomerTransactionHistory(
			String startDate, String endDate) throws DAOException;

	public List<DepositDetail> getDepositDetail(String startDate, String endDate)
			throws DAOException;

	public List<SalesByCustomerDetail> getPurchasesByVendorDetail(
			String startDate, String endDate) throws DAOException;

	public List<SalesByCustomerDetail> getPurchasesByVendorSummary(
			String startDate, String endDate) throws DAOException;

	public List<SalesByCustomerDetail> getPurchasesByItemDetail(
			String startDate, String endDate) throws DAOException;

	public List<SalesByCustomerDetail> getPurchasesByItemSummary(
			String startDate, String endDate) throws DAOException;

	public List<TransactionHistory> getVendorTransactionHistory(
			String startDate, String endDate) throws DAOException;

	public List<AmountsDueToVendor> getAmountsDueToVendor(String startDate,
			String endDate) throws DAOException;

	public List<MostProfitableCustomers> getMostProfitableCustomers(
			String startDate, String endDate) throws DAOException;

	public List<MostProfitableCustomers> getProfitabilityByCustomerDetail(
			String customer, String startDate, String endDate)
			throws DAOException;

	public List<TransactionDetailByTaxItem> getTransactionDetailByTaxItem(
			String startDate, String endDate) throws DAOException;

	public List<Transaction> getRegister(Account account) throws DAOException;

	public List<AccountRegister> getAccountRegister(String startDate,
			String endDate, String accountId) throws DAOException;

	public List<TransactionDetailByAccount> getTransactionDetailByAccount(
			final String startDate, final String endDate) throws DAOException;

	public List<SalesTaxLiability> getSalesTaxLiabilityReport(
			final String startDate, final String endDate) throws DAOException;

	public List<Customer> getTransactionHistoryCustomers(String startDate,
			String endDate) throws DAOException;

	public List<Vendor> getTransactionHistoryVendors(String startDate,
			String endDate) throws DAOException;

	public List<Item> getSalesReportItems(String startDate, String endDate)
			throws DAOException;

	public List<Item> getPurchaseReportItems(String startDate, String endDate)
			throws DAOException;

	public Date[] getMinimumAndMaximumTransactionDate() throws DAOException;

	public List<TrialBalance> getBalanceSheetReport(String startDate,
			String endDate) throws DAOException;

	public List<TrialBalance> getProfitAndLossReport(String startDate,
			String endDate) throws DAOException;

	public List<TrialBalance> getCashFlowReport(String startDate, String endDate)
			throws DAOException;

	public List<SalesByCustomerDetail> getSalesByCustomerDetailReport(
			String customerName, String startDate, String endDate)
			throws DAOException;

	public List<SalesByCustomerDetail> getSalesByItemDetail(String itemName,
			String startDate, String endDate) throws DAOException;

	public List<SalesByCustomerDetail> getPurchasesByVendorDetail(
			String vendorName, String startDate, String endDate)
			throws DAOException;

	public List<SalesByCustomerDetail> getPurchasesByItemDetail(
			String itemName, String startDate, String endDate)
			throws DAOException;

	public List<TransactionDetailByAccount> getTransactionDetailByAccount(
			String accountName, final String startDate, final String endDate)
			throws DAOException;

	public List<TransactionDetailByTaxItem> getTransactionDetailByTaxItem(
			final String taxItemName, final String startDate,
			final String endDate) throws DAOException;

	// For UK

	public Map<String, Double> getVATReturnBoxes(String startDate,
			String endDate) throws DAOException;

	public VATSummary getPriorReturnVATSummary(TAXAgency vatAgency, Date endDate)
			throws DAOException;

	public VATDetailReport getPriorVATReturnVATDetailReport(
			TAXAgency vatAgency, Date endDate) throws DAOException;

	public VATReturn getVATReturnDetails(TAXAgency vatAgency, Date fromDate,
			Date toDate) throws DAOException;

	public List<VATItemDetail> getVATItemDetailReport(String fromDate,
			String toDate) throws DAOException;

	public void createTaxes(int... vatReturnType) throws DAOException;

	public Long getNextNominalCode(int accountType) throws DAOException;
}
