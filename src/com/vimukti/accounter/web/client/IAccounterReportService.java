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
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.reports.CheckDetailReport;

public interface IAccounterReportService extends RemoteService {

	public List<SalesByCustomerDetail> getSalesByCustomerSummary(
			final ClientFinanceDate startDate, final ClientFinanceDate endDate);

	// public List<AccountBalance> getAccountBalances();

	public List<TrialBalance> getTrialBalance(ClientFinanceDate startDate,
			ClientFinanceDate endDate);

	public List<AgedDebtors> getAgedDebtors(ClientFinanceDate startDate,
			ClientFinanceDate endDate);

	public List<AgedDebtors> getAgedCreditors(ClientFinanceDate startDate,
			ClientFinanceDate endDate);

	public List<SalesByCustomerDetail> getSalesByCustomerDetailReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public List<SalesByCustomerDetail> getSalesByItemDetail(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public List<SalesByCustomerDetail> getSalesByItemSummary(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public List<TransactionHistory> getCustomerTransactionHistory(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public List<DepositDetail> getDepositDetail(
			final ClientFinanceDate startDate, final ClientFinanceDate endDate);

	public List<SalesByCustomerDetail> getPurchasesByVendorDetail(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public List<SalesByCustomerDetail> getPurchasesByVendorSummary(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public List<SalesByCustomerDetail> getPurchasesByItemDetail(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public List<SalesByCustomerDetail> getPurchasesByItemSummary(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public List<TransactionHistory> getVendorTransactionHistory(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public List<AmountsDueToVendor> getAmountsDueToVendor(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public List<MostProfitableCustomers> getMostProfitableCustomers(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public List<TransactionDetailByTaxItem> getTransactionDetailByTaxItem(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	List<ClientTransaction> getRegister(long accountId);

	List<AccountRegister> getAccountRegister(ClientFinanceDate startDate,
			ClientFinanceDate endDate, long accountId);

	public List<ClientCustomer> getTransactionHistoryCustomers(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public List<ClientVendor> getTransactionHistoryVendors(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public List<ClientItem> getSalesReportItems(ClientFinanceDate startDate,
			ClientFinanceDate endDate);

	public List<ClientItem> getPurchaseReportItems(ClientFinanceDate startDate,
			ClientFinanceDate endDate);

	public List<TransactionDetailByAccount> getTransactionDetailByAccount(
			final ClientFinanceDate startDate, final ClientFinanceDate endDate);

	public List<SalesTaxLiability> getSalesTaxLiabilityReport(
			final ClientFinanceDate startDate, final ClientFinanceDate endDate);

	public List<SalesByCustomerDetail> getSalesByCustomerDetailReport(
			String customerName, ClientFinanceDate startDate,
			ClientFinanceDate endDate);

	public List<SalesByCustomerDetail> getSalesByItemDetail(String itemName,
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public List<SalesByCustomerDetail> getPurchasesByVendorDetail(
			String vendorName, ClientFinanceDate startDate,
			ClientFinanceDate endDate);

	public List<SalesByCustomerDetail> getPurchasesByItemDetail(
			String itemName, ClientFinanceDate startDate,
			ClientFinanceDate endDate);

	public List<TransactionDetailByAccount> getTransactionDetailByAccount(
			String accountName, final ClientFinanceDate startDate,
			final ClientFinanceDate endDate);

	public List<ClientFinanceDate> getMinimumAndMaximumTransactionDate();

	public List<TransactionDetailByTaxItem> getTransactionDetailByTaxItem(
			String taxItemname, ClientFinanceDate startDate,
			ClientFinanceDate endDate);

	public List<TrialBalance> getBalanceSheetReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public List<TrialBalance> getProfitAndLossReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public List<TrialBalance> getCashFlowReport(ClientFinanceDate startDate,
			ClientFinanceDate endDate);

	public List<OpenAndClosedOrders> getPurchaseOpenOrderReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public List<OpenAndClosedOrders> getPurchaseCompletedOrderReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public List<OpenAndClosedOrders> getPurchaseCancelledOrderReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public List<OpenAndClosedOrders> getPurchaseOrderReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public List<OpenAndClosedOrders> getPurchaseClosedOrderReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public List<OpenAndClosedOrders> getSalesOpenOrderReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public List<OpenAndClosedOrders> getSalesCompletedOrderReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public List<OpenAndClosedOrders> getSalesOrderReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public List<OpenAndClosedOrders> getSalesCancelledOrderReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public List<OpenAndClosedOrders> getSalesClosedOrderReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public List<VATDetail> getPriorVATReturnVATDetailReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	List<VATDetail> getPriorVATReturnReport(long vatAgancy,
			ClientFinanceDate endDate);

	List<VATSummary> getPriorReturnVATSummary(long vatAgency,
			ClientFinanceDate endDate);

	List<VATSummary> getVAT100Report(long vatAgncy, ClientFinanceDate fromDate,
			ClientFinanceDate toDate);

	public List<UncategorisedAmountsReport> getUncategorisedAmountsReport(
			ClientFinanceDate fromDate, ClientFinanceDate toDate);

	public List<ECSalesList> getECSalesListReport(ClientFinanceDate fromDate,
			ClientFinanceDate toDate);

	public List<ECSalesListDetail> getECSalesListDetailReport(String payeeName,
			ClientFinanceDate fromDate, ClientFinanceDate toDate);

	public List<ReverseChargeListDetail> getReverseChargeListDetailReport(
			String payeeName, ClientFinanceDate fromDate,
			ClientFinanceDate toDate);

	public List<ReverseChargeList> getReverseChargeListReport(
			ClientFinanceDate fromDate, ClientFinanceDate toDate);

	public List<DummyDebitor> getDebitors(ClientFinanceDate startDate,
			ClientFinanceDate endDate) throws AccounterException;

	public List<DummyDebitor> getCreditors(ClientFinanceDate startDate,
			ClientFinanceDate endDate) throws AccounterException;

	public List<AgedDebtors> getAgedDebtors(String Name,
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public List<AgedDebtors> getAgedCreditors(String Name,
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public List<VATItemSummary> getVATItemSummaryReport(
			ClientFinanceDate fromDate, ClientFinanceDate toDate);

	public List<VATItemDetail> getVATItemDetailReport(String vatItemName,
			ClientFinanceDate fromDate, ClientFinanceDate toDate);

	public List<ExpenseList> getExpenseReportByType(int status,
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	List<CheckDetailReport> getCheckDetailReport(long paymentmethod,
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	List<PayeeStatementsList> getStatements(long id, long transactionDate,
			ClientFinanceDate fromDate, ClientFinanceDate toDate, int noOfDays,
			boolean isEnabledOfZeroBalBox,
			boolean isEnabledOfLessThanZeroBalBox,
			double lessThanZeroBalanceValue,
			boolean isEnabledOfNoAccountActivity,
			boolean isEnabledOfInactiveCustomer);
}