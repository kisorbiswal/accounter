package com.vimukti.accounter.web.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
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
import com.vimukti.accounter.web.client.ui.reports.CheckDetailReport;

public interface IAccounterReportServiceAsync {

	public void getSalesByCustomerSummary(final long startDate,
			final long endDate,
			AsyncCallback<List<SalesByCustomerDetail>> callBackResult);

	public void getTrialBalance(long startDate, long endDate,
			AsyncCallback<List<TrialBalance>> callBackResult);

	public void getAgedDebtors(long startDate, long endDate,
			AsyncCallback<List<AgedDebtors>> callBackResult);

	public void getAgedCreditors(long startDate, long endDate,
			AsyncCallback<List<AgedDebtors>> callBackResult);

	public void getSalesByCustomerDetailReport(long startDate, long endDate,
			AsyncCallback<List<SalesByCustomerDetail>> callBackResult);

	public void getSalesByItemDetail(long startDate, long endDate,
			AsyncCallback<List<SalesByCustomerDetail>> callBackResult);

	public void getSalesByItemSummary(long startDate, long endDate,
			AsyncCallback<List<SalesByCustomerDetail>> callBackResult);

	public void getCustomerTransactionHistory(long startDate, long endDate,
			AsyncCallback<List<TransactionHistory>> callBackResult);

	public void getPurchasesByVendorDetail(long startDate, long endDate,
			AsyncCallback<List<SalesByCustomerDetail>> callBackResult);

	public void getPurchasesByVendorSummary(long startDate, long endDate,
			AsyncCallback<List<SalesByCustomerDetail>> callBackResult);

	public void getPurchasesByItemDetail(long startDate, long endDate,
			AsyncCallback<List<SalesByCustomerDetail>> callBackResult);

	public void getPurchasesByItemSummary(long startDate, long endDate,
			AsyncCallback<List<SalesByCustomerDetail>> callBackResult);

	public void getVendorTransactionHistory(long startDate, long endDate,
			AsyncCallback<List<TransactionHistory>> callBackResult);

	public void getAmountsDueToVendor(long startDate, long endDate,
			AsyncCallback<List<AmountsDueToVendor>> callBackResult);

	public void getMostProfitableCustomers(long startDate, long endDate,
			AsyncCallback<List<MostProfitableCustomers>> callBackResult);

	public void getTransactionDetailByTaxItem(long startDate, long endDate,
			AsyncCallback<List<TransactionDetailByTaxItem>> callBackResult);

	public void getRegister(long accountId,
			AsyncCallback<List<ClientTransaction>> callBackResult);

	public void getAccountRegister(long startDate, long endDate,
			long accountId,
			AsyncCallback<List<AccountRegister>> callBackResult);

	public void getTransactionHistoryCustomers(long startDate, long endDate,
			AsyncCallback<List<ClientCustomer>> callBackResult);

	public void getTransactionHistoryVendors(long startDate, long endDate,
			AsyncCallback<List<ClientVendor>> callBackResult);

	public void getSalesReportItems(long startDate, long endDate,
			AsyncCallback<List<ClientItem>> callBackResult);

	public void getPurchaseReportItems(long startDate, long endDate,
			AsyncCallback<List<ClientItem>> callBackResult);

	public void getTransactionDetailByAccount(final long startDate,
			final long endDate,
			AsyncCallback<List<TransactionDetailByAccount>> callBackResult);

	public void getSalesTaxLiabilityReport(final long startDate,
			final long endDate, AsyncCallback<List<SalesTaxLiability>> callBack);

	public void getSalesByCustomerDetailReport(String customerName,
			long startDate, long endDate,
			AsyncCallback<List<SalesByCustomerDetail>> callBack);

	public void getSalesByItemDetail(String itemName, long startDate,
			long endDate, AsyncCallback<List<SalesByCustomerDetail>> callBack);

	public void getPurchasesByVendorDetail(String vendorName, long startDate,
			long endDate, AsyncCallback<List<SalesByCustomerDetail>> callBack);

	public void getPurchasesByItemDetail(String itemName, long startDate,
			long endDate, AsyncCallback<List<SalesByCustomerDetail>> callBack);

	public void getTransactionDetailByAccount(String accountName,
			final long startDate, final long endDate,
			AsyncCallback<List<TransactionDetailByAccount>> callBack);

	public void getMinimumAndMaximumTransactionDate(
			AsyncCallback<List<ClientFinanceDate>> callBack);

	public void getTransactionDetailByTaxItem(String taxItemname,
			long startDate, long endDate,
			AsyncCallback<List<TransactionDetailByTaxItem>> callBackResult);

	public void getBalanceSheetReport(long startDate, long endDate,
			AsyncCallback<List<TrialBalance>> callBack);

	public void getProfitAndLossReport(long startDate, long endDate,
			AsyncCallback<List<TrialBalance>> callBack);

	public void getCashFlowReport(long startDate, long endDate,
			AsyncCallback<List<TrialBalance>> callBack);

	public void getPurchaseOpenOrderReport(long startDate, long endDate,
			AsyncCallback<List<OpenAndClosedOrders>> callBack);

	public void getPurchaseCompletedOrderReport(long startDate, long endDate,
			AsyncCallback<List<OpenAndClosedOrders>> callBack);

	public void getPurchaseCancelledOrderReport(long startDate, long endDate,
			AsyncCallback<List<OpenAndClosedOrders>> callBack);

	public void getPurchaseOrderReport(long startDate, long endDate,
			AsyncCallback<List<OpenAndClosedOrders>> callBack);

	public void getPurchaseClosedOrderReport(long startDate, long endDate,
			AsyncCallback<List<OpenAndClosedOrders>> callBack);

	public void getSalesOpenOrderReport(long startDate, long endDate,
			AsyncCallback<List<OpenAndClosedOrders>> callBack);

	public void getSalesCompletedOrderReport(long startDate, long endDate,
			AsyncCallback<List<OpenAndClosedOrders>> callBack);

	public void getSalesOrderReport(long startDate, long endDate,
			AsyncCallback<List<OpenAndClosedOrders>> callBack);

	public void getSalesCancelledOrderReport(long startDate, long endDate,
			AsyncCallback<List<OpenAndClosedOrders>> callBack);

	public void getSalesClosedOrderReport(long startDate, long endDate,
			AsyncCallback<List<OpenAndClosedOrders>> callBack);

	void getPriorVATReturnVATDetailReport(long startDate, long endDate,
			AsyncCallback<List<VATDetail>> callback);

	void getPriorVATReturnReport(long vatAgancy, long endDate,
			AsyncCallback<List<VATDetail>> callback);

	public void getPriorReturnVATSummary(long vatAgency, long endDate,
			AsyncCallback<List<VATSummary>> callback);

	public void getVAT100Report(String vatAgncy, long fromDate, long toDate,
			AsyncCallback<List<VATSummary>> callback);

	public void getUncategorisedAmountsReport(long fromDate, long toDate,
			AsyncCallback<List<UncategorisedAmountsReport>> callback);

	public void getECSalesListReport(long fromDate, long toDate,
			AsyncCallback<List<ECSalesList>> callback);

	public void getECSalesListDetailReport(String payeeName, long fromDate,
			long toDate, AsyncCallback<List<ECSalesListDetail>> callback);

	public void getReverseChargeListDetailReport(String payeeName,
			long fromDate, long toDate,
			AsyncCallback<List<ReverseChargeListDetail>> callback);

	public void getReverseChargeListReport(long fromDate, long toDate,
			AsyncCallback<List<ReverseChargeList>> callback);

	public void getDebitors(long startDate, long endDate,
			AsyncCallback<List<DummyDebitor>> callback);

	public void getCreditors(long startDate, long endDate,
			AsyncCallback<List<DummyDebitor>> callback);

	public void getAgedDebtors(String Name, long startDate, long endDate,
			AsyncCallback<List<AgedDebtors>> callback);

	public void getAgedCreditors(String debitorName, long fromDate,
			long toDate, AsyncCallback<List<AgedDebtors>> callback);

	public void getVATItemSummaryReport(long fromDate, long toDate,
			AsyncCallback<List<VATItemSummary>> callback);

	public void getVATItemDetailReport(String vatItemName, long fromDate,
			long toDate, AsyncCallback<List<VATItemDetail>> callback);

	public void getExpenseReportByType(int status, long startDate,
			long endDate, AsyncCallback<List<ExpenseList>> callback);

	public void getDepositDetail(final long startDate, final long endDate,
			AsyncCallback<List<DepositDetail>> callBackResult);

	public void getCheckDetailReport(String paymentmethod,
			final long startDate, final long endDate,
			AsyncCallback<List<CheckDetailReport>> callBackResult);

	public void getStatements(String id, long transactionDate, long fromDate,
			long toDate, int noOfDays, boolean isEnabledOfZeroBalBox,
			boolean isEnabledOfLessThanZeroBalBox,
			double lessThanZeroBalanceValue,
			boolean isEnabledOfNoAccountActivity,
			boolean isEnabledOfInactiveCustomer,
			AsyncCallback<List<PayeeStatementsList>> callBack);
}