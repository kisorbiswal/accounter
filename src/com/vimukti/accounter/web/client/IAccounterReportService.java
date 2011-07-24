package com.vimukti.accounter.web.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.Lists.DummyDebitor;
import com.vimukti.accounter.web.client.core.Lists.OpenAndClosedOrders;
import com.vimukti.accounter.web.client.core.Lists.PayeeStatementsList;
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
import com.vimukti.accounter.web.client.data.InvalidSessionException;
import com.vimukti.accounter.web.client.ui.reports.CheckDetailReport;

public interface IAccounterReportService extends RemoteService {

	public List<SalesByCustomerDetail> getSalesByCustomerSummary(
			final long startDate, final long endDate);

	// public List<AccountBalance> getAccountBalances();

	public List<TrialBalance> getTrialBalance(long startDate, long endDate);

	public List<AgedDebtors> getAgedDebtors(long startDate, long endDate);

	public List<AgedDebtors> getAgedCreditors(long startDate, long endDate);

	public List<SalesByCustomerDetail> getSalesByCustomerDetailReport(
			long startDate, long endDate);

	public List<SalesByCustomerDetail> getSalesByItemDetail(long startDate,
			long endDate);

	public List<SalesByCustomerDetail> getSalesByItemSummary(long startDate,
			long endDate);

	public List<TransactionHistory> getCustomerTransactionHistory(
			long startDate, long endDate);

	public List<DepositDetail> getDepositDetail(final long startDate,final long endDate);

	public List<SalesByCustomerDetail> getPurchasesByVendorDetail(
			long startDate, long endDate);

	public List<SalesByCustomerDetail> getPurchasesByVendorSummary(
			long startDate, long endDate);

	public List<SalesByCustomerDetail> getPurchasesByItemDetail(long startDate,
			long endDate);

	public List<SalesByCustomerDetail> getPurchasesByItemSummary(
			long startDate, long endDate);

	public List<TransactionHistory> getVendorTransactionHistory(long startDate,
			long endDate);

	public List<AmountsDueToVendor> getAmountsDueToVendor(long startDate,
			long endDate);

	public List<MostProfitableCustomers> getMostProfitableCustomers(
			long startDate, long endDate);

	public List<TransactionDetailByTaxItem> getTransactionDetailByTaxItem(
			long startDate, long endDate);

	List<ClientTransaction> getRegister(long accountId);

	List<AccountRegister> getAccountRegister(long startDate, long endDate,
			long accountId);

	public List<ClientCustomer> getTransactionHistoryCustomers(long startDate,
			long endDate);

	public List<ClientVendor> getTransactionHistoryVendors(long startDate,
			long endDate);

	public List<ClientItem> getSalesReportItems(long startDate, long endDate);

	public List<ClientItem> getPurchaseReportItems(long startDate, long endDate);

	public List<TransactionDetailByAccount> getTransactionDetailByAccount(
			final long startDate, final long endDate);

	public List<SalesTaxLiability> getSalesTaxLiabilityReport(
			final long startDate, final long endDate);

	public List<SalesByCustomerDetail> getSalesByCustomerDetailReport(
			String customerName, long startDate, long endDate);

	public List<SalesByCustomerDetail> getSalesByItemDetail(String itemName,
			long startDate, long endDate);

	public List<SalesByCustomerDetail> getPurchasesByVendorDetail(
			String vendorName, long startDate, long endDate);

	public List<SalesByCustomerDetail> getPurchasesByItemDetail(
			String itemName, long startDate, long endDate);

	public List<TransactionDetailByAccount> getTransactionDetailByAccount(
			String accountName, final long startDate, final long endDate);

	public List<ClientFinanceDate> getMinimumAndMaximumTransactionDate();

	public List<TransactionDetailByTaxItem> getTransactionDetailByTaxItem(
			String taxItemname, long startDate, long endDate);

	public List<TrialBalance> getBalanceSheetReport(long startDate, long endDate);

	public List<TrialBalance> getProfitAndLossReport(long startDate,
			long endDate);

	public List<TrialBalance> getCashFlowReport(long startDate, long endDate);

	public List<OpenAndClosedOrders> getPurchaseOpenOrderReport(long startDate,
			long endDate);

	public List<OpenAndClosedOrders> getPurchaseCompletedOrderReport(
			long startDate, long endDate);

	public List<OpenAndClosedOrders> getPurchaseCancelledOrderReport(
			long startDate, long endDate);

	public List<OpenAndClosedOrders> getPurchaseOrderReport(long startDate,
			long endDate);

	public List<OpenAndClosedOrders> getPurchaseClosedOrderReport(
			long startDate, long endDate);

	public List<OpenAndClosedOrders> getSalesOpenOrderReport(long startDate,
			long endDate);

	public List<OpenAndClosedOrders> getSalesCompletedOrderReport(
			long startDate, long endDate);

	public List<OpenAndClosedOrders> getSalesOrderReport(long startDate,
			long endDate);

	public List<OpenAndClosedOrders> getSalesCancelledOrderReport(
			long startDate, long endDate);

	public List<OpenAndClosedOrders> getSalesClosedOrderReport(long startDate,
			long endDate);

	public List<VATDetail> getPriorVATReturnVATDetailReport(long startDate,
			long endDate);

	List<VATDetail> getPriorVATReturnReport(long vatAgancy, long endDate);

	List<VATSummary> getPriorReturnVATSummary(long vatAgency, long endDate);

	List<VATSummary> getVAT100Report(long vatAgncy, long fromDate, long toDate);

	public List<UncategorisedAmountsReport> getUncategorisedAmountsReport(
			long fromDate, long toDate);

	public List<ECSalesList> getECSalesListReport(long fromDate, long toDate);

	public List<ECSalesListDetail> getECSalesListDetailReport(String payeeName,
			long fromDate, long toDate);

	public List<ReverseChargeListDetail> getReverseChargeListDetailReport(
			String payeeName, long fromDate, long toDate);

	public List<ReverseChargeList> getReverseChargeListReport(long fromDate,
			long toDate);

	public List<DummyDebitor> getDebitors(long startDate, long endDate)
			throws InvalidSessionException;

	public List<DummyDebitor> getCreditors(long startDate, long endDate)
			throws InvalidSessionException;

	public List<AgedDebtors> getAgedDebtors(String Name, long startDate,
			long endDate);

	public List<AgedDebtors> getAgedCreditors(String Name, long startDate,
			long endDate);

	public List<VATItemSummary> getVATItemSummaryReport(long fromDate,
			long toDate);

	public List<VATItemDetail> getVATItemDetailReport(String vatItemName,
			long fromDate, long toDate);

	public List<ExpenseList> getExpenseReportByType(int status, long startDate,
			long endDate);

	List<CheckDetailReport> getCheckDetailReport(long paymentmethod,
			long startDate, long endDate);
	List<PayeeStatementsList> getStatements(long id, long transactionDate,
			long fromDate, long toDate, int noOfDays,
			boolean isEnabledOfZeroBalBox,
			boolean isEnabledOfLessThanZeroBalBox,
			double lessThanZeroBalanceValue,
			boolean isEnabledOfNoAccountActivity,
			boolean isEnabledOfInactiveCustomer);
}