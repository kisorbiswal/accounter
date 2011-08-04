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

	public void getSalesByCustomerSummary(final ClientFinanceDate startDate,
			final ClientFinanceDate endDate,
			AsyncCallback<List<SalesByCustomerDetail>> callBackResult);

	public void getTrialBalance(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<List<TrialBalance>> callBackResult);

	public void getAgedDebtors(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<List<AgedDebtors>> callBackResult);

	public void getAgedCreditors(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<List<AgedDebtors>> callBackResult);

	public void getSalesByCustomerDetailReport(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<List<SalesByCustomerDetail>> callBackResult);

	public void getSalesByItemDetail(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<List<SalesByCustomerDetail>> callBackResult);

	public void getSalesByItemSummary(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<List<SalesByCustomerDetail>> callBackResult);

	public void getCustomerTransactionHistory(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<List<TransactionHistory>> callBackResult);

	public void getPurchasesByVendorDetail(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<List<SalesByCustomerDetail>> callBackResult);

	public void getPurchasesByVendorSummary(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<List<SalesByCustomerDetail>> callBackResult);

	public void getPurchasesByItemDetail(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<List<SalesByCustomerDetail>> callBackResult);

	public void getPurchasesByItemSummary(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<List<SalesByCustomerDetail>> callBackResult);

	public void getVendorTransactionHistory(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<List<TransactionHistory>> callBackResult);

	public void getAmountsDueToVendor(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<List<AmountsDueToVendor>> callBackResult);

	public void getMostProfitableCustomers(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<List<MostProfitableCustomers>> callBackResult);

	public void getTransactionDetailByTaxItem(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<List<TransactionDetailByTaxItem>> callBackResult);

	public void getRegister(long accountId,
			AsyncCallback<List<ClientTransaction>> callBackResult);

	public void getAccountRegister(ClientFinanceDate startDate,
			ClientFinanceDate endDate, long accountId,
			AsyncCallback<List<AccountRegister>> callBackResult);

	public void getTransactionHistoryCustomers(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<List<ClientCustomer>> callBackResult);

	public void getTransactionHistoryVendors(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<List<ClientVendor>> callBackResult);

	public void getSalesReportItems(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<List<ClientItem>> callBackResult);

	public void getPurchaseReportItems(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<List<ClientItem>> callBackResult);

	public void getTransactionDetailByAccount(
			final ClientFinanceDate startDate, final ClientFinanceDate endDate,
			AsyncCallback<List<TransactionDetailByAccount>> callBackResult);

	public void getSalesTaxLiabilityReport(final ClientFinanceDate startDate,
			final ClientFinanceDate endDate,
			AsyncCallback<List<SalesTaxLiability>> callBack);

	public void getSalesByCustomerDetailReport(String customerName,
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			AsyncCallback<List<SalesByCustomerDetail>> callBack);

	public void getSalesByItemDetail(String itemName,
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			AsyncCallback<List<SalesByCustomerDetail>> callBack);

	public void getPurchasesByVendorDetail(String vendorName,
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			AsyncCallback<List<SalesByCustomerDetail>> callBack);

	public void getPurchasesByItemDetail(String itemName,
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			AsyncCallback<List<SalesByCustomerDetail>> callBack);

	public void getTransactionDetailByAccount(String accountName,
			final ClientFinanceDate startDate, final ClientFinanceDate endDate,
			AsyncCallback<List<TransactionDetailByAccount>> callBack);

	public void getMinimumAndMaximumTransactionDate(
			AsyncCallback<List<ClientFinanceDate>> callBack);

	public void getTransactionDetailByTaxItem(String taxItemname,
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			AsyncCallback<List<TransactionDetailByTaxItem>> callBackResult);

	public void getBalanceSheetReport(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<List<TrialBalance>> callBack);

	public void getProfitAndLossReport(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<List<TrialBalance>> callBack);

	public void getCashFlowReport(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<List<TrialBalance>> callBack);

	public void getPurchaseOpenOrderReport(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<List<OpenAndClosedOrders>> callBack);

	public void getPurchaseCompletedOrderReport(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<List<OpenAndClosedOrders>> callBack);

	public void getPurchaseCancelledOrderReport(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<List<OpenAndClosedOrders>> callBack);

	public void getPurchaseOrderReport(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<List<OpenAndClosedOrders>> callBack);

	public void getPurchaseClosedOrderReport(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<List<OpenAndClosedOrders>> callBack);

	public void getSalesOpenOrderReport(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<List<OpenAndClosedOrders>> callBack);

	public void getSalesCompletedOrderReport(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<List<OpenAndClosedOrders>> callBack);

	public void getSalesOrderReport(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<List<OpenAndClosedOrders>> callBack);

	public void getSalesCancelledOrderReport(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<List<OpenAndClosedOrders>> callBack);

	public void getSalesClosedOrderReport(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<List<OpenAndClosedOrders>> callBack);

	void getPriorVATReturnVATDetailReport(ClientFinanceDate startDate,
			ClientFinanceDate endDate, AsyncCallback<List<VATDetail>> callback);

	void getPriorVATReturnReport(long vatAgancy, ClientFinanceDate endDate,
			AsyncCallback<List<VATDetail>> callback);

	public void getPriorReturnVATSummary(long vatAgency,
			ClientFinanceDate endDate, AsyncCallback<List<VATSummary>> callback);

	public void getVAT100Report(long vatAgncy, ClientFinanceDate fromDate,
			ClientFinanceDate toDate, AsyncCallback<List<VATSummary>> callback);

	public void getUncategorisedAmountsReport(ClientFinanceDate fromDate,
			ClientFinanceDate toDate,
			AsyncCallback<List<UncategorisedAmountsReport>> callback);

	public void getECSalesListReport(ClientFinanceDate fromDate,
			ClientFinanceDate toDate, AsyncCallback<List<ECSalesList>> callback);

	public void getECSalesListDetailReport(String payeeName,
			ClientFinanceDate fromDate, ClientFinanceDate toDate,
			AsyncCallback<List<ECSalesListDetail>> callback);

	public void getReverseChargeListDetailReport(String payeeName,
			ClientFinanceDate fromDate, ClientFinanceDate toDate,
			AsyncCallback<List<ReverseChargeListDetail>> callback);

	public void getReverseChargeListReport(ClientFinanceDate fromDate,
			ClientFinanceDate toDate,
			AsyncCallback<List<ReverseChargeList>> callback);

	public void getDebitors(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<List<DummyDebitor>> callback);

	public void getCreditors(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<List<DummyDebitor>> callback);

	public void getAgedDebtors(String Name, ClientFinanceDate startDate,
			ClientFinanceDate endDate, AsyncCallback<List<AgedDebtors>> callback);

	public void getAgedCreditors(String debitorName,
			ClientFinanceDate fromDate, ClientFinanceDate toDate,
			AsyncCallback<List<AgedDebtors>> callback);

	public void getVATItemSummaryReport(ClientFinanceDate fromDate,
			ClientFinanceDate toDate,
			AsyncCallback<List<VATItemSummary>> callback);

	public void getVATItemDetailReport(String vatItemName,
			ClientFinanceDate fromDate, ClientFinanceDate toDate,
			AsyncCallback<List<VATItemDetail>> callback);

	public void getExpenseReportByType(int status, ClientFinanceDate startDate,
			ClientFinanceDate endDate, AsyncCallback<List<ExpenseList>> callback);

	public void getDepositDetail(final ClientFinanceDate startDate,
			final ClientFinanceDate endDate,
			AsyncCallback<List<DepositDetail>> callBackResult);

	public void getCheckDetailReport(long paymentmethod,
			final ClientFinanceDate startDate, final ClientFinanceDate endDate,
			AsyncCallback<List<CheckDetailReport>> callBackResult);

	public void getStatements(long id, long transactionDate,
			ClientFinanceDate fromDate, ClientFinanceDate toDate, int noOfDays,
			boolean isEnabledOfZeroBalBox,
			boolean isEnabledOfLessThanZeroBalBox,
			double lessThanZeroBalanceValue,
			boolean isEnabledOfNoAccountActivity,
			boolean isEnabledOfInactiveCustomer,
			AsyncCallback<List<PayeeStatementsList>> callBack);
	
	void getCustomerStatement(long customer, long fromDate, long toDate,
			AsyncCallback<List<PayeeStatementsList>> callBack);
}