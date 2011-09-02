package com.vimukti.accounter.web.client;

import java.util.ArrayList;

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
import com.vimukti.accounter.web.client.core.reports.SalesByLocationDetails;
import com.vimukti.accounter.web.client.core.reports.SalesByLocationSummary;
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

	public ArrayList<SalesByCustomerDetail> getSalesByCustomerSummary(
			final ClientFinanceDate startDate, final ClientFinanceDate endDate);

	// public ArrayList<AccountBalance> getAccountBalances();

	public ArrayList<TrialBalance> getTrialBalance(ClientFinanceDate startDate,
			ClientFinanceDate endDate);

	public ArrayList<AgedDebtors> getAgedDebtors(ClientFinanceDate startDate,
			ClientFinanceDate endDate);

	public ArrayList<AgedDebtors> getAgedCreditors(ClientFinanceDate startDate,
			ClientFinanceDate endDate);

	public ArrayList<SalesByCustomerDetail> getSalesByCustomerDetailReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public ArrayList<SalesByCustomerDetail> getSalesByItemDetail(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public ArrayList<SalesByCustomerDetail> getSalesByItemSummary(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public ArrayList<TransactionHistory> getCustomerTransactionHistory(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public ArrayList<DepositDetail> getDepositDetail(
			final ClientFinanceDate startDate, final ClientFinanceDate endDate);

	public ArrayList<SalesByCustomerDetail> getPurchasesByVendorDetail(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public ArrayList<SalesByCustomerDetail> getPurchasesByVendorSummary(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public ArrayList<SalesByCustomerDetail> getPurchasesByItemDetail(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public ArrayList<SalesByCustomerDetail> getPurchasesByItemSummary(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public ArrayList<TransactionHistory> getVendorTransactionHistory(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public ArrayList<AmountsDueToVendor> getAmountsDueToVendor(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public ArrayList<MostProfitableCustomers> getMostProfitableCustomers(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public ArrayList<TransactionDetailByTaxItem> getTransactionDetailByTaxItem(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	ArrayList<ClientTransaction> getRegister(long accountId);

	ArrayList<AccountRegister> getAccountRegister(ClientFinanceDate startDate,
			ClientFinanceDate endDate, long accountId);

	public ArrayList<ClientCustomer> getTransactionHistoryCustomers(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public ArrayList<ClientVendor> getTransactionHistoryVendors(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public ArrayList<ClientItem> getSalesReportItems(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public ArrayList<ClientItem> getPurchaseReportItems(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public ArrayList<TransactionDetailByAccount> getTransactionDetailByAccount(
			final ClientFinanceDate startDate, final ClientFinanceDate endDate);

	public ArrayList<SalesTaxLiability> getSalesTaxLiabilityReport(
			final ClientFinanceDate startDate, final ClientFinanceDate endDate);

	public ArrayList<SalesByCustomerDetail> getSalesByCustomerDetailReport(
			String customerName, ClientFinanceDate startDate,
			ClientFinanceDate endDate);

	public ArrayList<SalesByCustomerDetail> getSalesByItemDetail(
			String itemName, ClientFinanceDate startDate,
			ClientFinanceDate endDate);

	public ArrayList<SalesByCustomerDetail> getPurchasesByVendorDetail(
			String vendorName, ClientFinanceDate startDate,
			ClientFinanceDate endDate);

	public ArrayList<SalesByCustomerDetail> getPurchasesByItemDetail(
			String itemName, ClientFinanceDate startDate,
			ClientFinanceDate endDate);

	public ArrayList<TransactionDetailByAccount> getTransactionDetailByAccount(
			String accountName, final ClientFinanceDate startDate,
			final ClientFinanceDate endDate);

	public ArrayList<ClientFinanceDate> getMinimumAndMaximumTransactionDate();

	public ArrayList<TransactionDetailByTaxItem> getTransactionDetailByTaxItem(
			String taxItemname, ClientFinanceDate startDate,
			ClientFinanceDate endDate);

	public ArrayList<TrialBalance> getBalanceSheetReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public ArrayList<TrialBalance> getProfitAndLossReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public ArrayList<SalesByLocationDetails> getSalesByLocationDetailsReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public ArrayList<TrialBalance> getCashFlowReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public ArrayList<OpenAndClosedOrders> getPurchaseOpenOrderReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public ArrayList<OpenAndClosedOrders> getPurchaseCompletedOrderReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public ArrayList<OpenAndClosedOrders> getPurchaseCancelledOrderReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public ArrayList<OpenAndClosedOrders> getPurchaseOrderReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public ArrayList<OpenAndClosedOrders> getPurchaseClosedOrderReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public ArrayList<OpenAndClosedOrders> getSalesOpenOrderReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public ArrayList<OpenAndClosedOrders> getSalesCompletedOrderReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public ArrayList<OpenAndClosedOrders> getSalesOrderReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public ArrayList<OpenAndClosedOrders> getSalesCancelledOrderReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public ArrayList<OpenAndClosedOrders> getSalesClosedOrderReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public ArrayList<VATDetail> getPriorVATReturnVATDetailReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	ArrayList<VATDetail> getPriorVATReturnReport(long vatAgancy,
			ClientFinanceDate endDate);

	ArrayList<VATSummary> getPriorReturnVATSummary(long vatAgency,
			ClientFinanceDate endDate);

	ArrayList<VATSummary> getVAT100Report(long vatAgncy,
			ClientFinanceDate fromDate, ClientFinanceDate toDate);

	public ArrayList<UncategorisedAmountsReport> getUncategorisedAmountsReport(
			ClientFinanceDate fromDate, ClientFinanceDate toDate);

	public ArrayList<ECSalesList> getECSalesListReport(
			ClientFinanceDate fromDate, ClientFinanceDate toDate);

	public ArrayList<ECSalesListDetail> getECSalesListDetailReport(
			String payeeName, ClientFinanceDate fromDate,
			ClientFinanceDate toDate);

	public ArrayList<ReverseChargeListDetail> getReverseChargeListDetailReport(
			String payeeName, ClientFinanceDate fromDate,
			ClientFinanceDate toDate);

	public ArrayList<ReverseChargeList> getReverseChargeListReport(
			ClientFinanceDate fromDate, ClientFinanceDate toDate);

	public ArrayList<DummyDebitor> getDebitors(ClientFinanceDate startDate,
			ClientFinanceDate endDate) throws AccounterException;

	public ArrayList<DummyDebitor> getCreditors(ClientFinanceDate startDate,
			ClientFinanceDate endDate) throws AccounterException;

	public ArrayList<AgedDebtors> getAgedDebtors(String Name,
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public ArrayList<AgedDebtors> getAgedCreditors(String Name,
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public ArrayList<VATItemSummary> getVATItemSummaryReport(
			ClientFinanceDate fromDate, ClientFinanceDate toDate);

	public ArrayList<VATItemDetail> getVATItemDetailReport(String vatItemName,
			ClientFinanceDate fromDate, ClientFinanceDate toDate);

	public ArrayList<ExpenseList> getExpenseReportByType(int status,
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	ArrayList<CheckDetailReport> getCheckDetailReport(long paymentmethod,
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	ArrayList<PayeeStatementsList> getStatements(long id, long transactionDate,
			ClientFinanceDate fromDate, ClientFinanceDate toDate, int noOfDays,
			boolean isEnabledOfZeroBalBox,
			boolean isEnabledOfLessThanZeroBalBox,
			double lessThanZeroBalanceValue,
			boolean isEnabledOfNoAccountActivity,
			boolean isEnabledOfInactiveCustomer);

	ArrayList<PayeeStatementsList> getCustomerStatement(long customer,
			long fromDate, long toDate) throws AccounterException;

	public ArrayList<SalesByLocationSummary> getSalesByLocationSummaryReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	public ArrayList<SalesByLocationDetails> getSalesByLocationDetailsForLocation(
			String locationName, ClientFinanceDate startDate,
			ClientFinanceDate endDate);
}